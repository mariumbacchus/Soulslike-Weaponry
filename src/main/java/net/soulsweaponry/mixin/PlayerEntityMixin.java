package net.soulsweaponry.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.ShieldBreaker;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.entitydata.UmbralTrespassData;
import net.soulsweaponry.items.DetonateGroundItem;
import net.soulsweaponry.items.IUltraHeavy;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void interceptFallDamage(float fallDistance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> info) {
        if (DetonateGroundItem.triggerCalculateFall(((PlayerEntity)(Object)this), fallDistance, source)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }
    
    @Inject(method = "takeShieldHit", at = @At("TAIL"))
    protected void interceptTakeShieldHit(LivingEntity attacker, CallbackInfo info) {
        if (attacker instanceof ShieldBreaker) {
            if (((ShieldBreaker)attacker).disablesShield()) {
                ((PlayerEntity)(Object)this).disableShield(true);
            }
        }
        // Disable shields with ultra heavy if config line is enabled
        if (ConfigConstructor.ultra_heavy_disables_shields) {
            PlayerEntity entity = ((PlayerEntity)(Object)this);
            if (attacker.getMainHandStack().getItem() instanceof IUltraHeavy item && item.isHeavy()) {
                entity.disableShield(true);
            }
        }
    }

   @Inject(method = "tickRiding", at = @At("HEAD"))
    public void interceptTickRiding(CallbackInfo info) {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        if (!player.getWorld().isClient && UmbralTrespassData.shouldDamageRiding(player)) {
           int cooldown = UmbralTrespassData.getAbilityCooldown(player);
           player.addStatusEffect(new StatusEffectInstance(EffectRegistry.COOLDOWN.get(), cooldown, 0));
           if (!player.hasStatusEffect(EffectRegistry.GHOSTLY.get())) {
               player.stopRiding();
           }
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attack(Entity target, CallbackInfo info) {
        // Can't attack during Umbral Trespass ability
        PlayerEntity player = ((PlayerEntity) (Object)this);
        if (!player.getWorld().isClient && player.hasStatusEffect(EffectRegistry.GHOSTLY.get())) {
            info.cancel();
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void interceptDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        if (player.hasStatusEffect(EffectRegistry.GHOSTLY.get())) {
            info.setReturnValue(false);
        }
        int frames = ParryData.getParryFrames(player);
        if (frames >= 1 && frames <= ConfigConstructor.shield_parry_frames && !source.isUnblockable()) {
            player.world.sendEntityStatus(player, EntityStatuses.BLOCK_WITH_SHIELD);
            if (source.isProjectile() && source.getSource() instanceof ProjectileEntity) {
                info.setReturnValue(false);
                return;
            }
            if (source.getAttacker() instanceof LivingEntity attacker) {
                if (!attacker.hasStatusEffect(EffectRegistry.POSTURE_BREAK.get())) {
                    attacker.world.playSound(null, attacker.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT.get(), SoundCategory.PLAYERS, .5f, 1f);
                }
                attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK.get(), 60, 0));
                attacker.takeKnockback(0.4f,  player.getX() - attacker.getX(), player.getZ() - attacker.getZ());
                info.setReturnValue(false);
            }
        }
        // Enhanced arkenplate && health < 1/3 && projectile
        if (player.getInventory().getArmorStack(2).isOf(ItemRegistry.ENHANCED_ARKENPLATE.get()) && player.getHealth() < player.getMaxHealth() * ConfigConstructor.arkenplate_mirror_trigger_percent
                && source.isProjectile() && source.getSource() instanceof ProjectileEntity projectile) {
            Vec3d playerPos = player.getPos();
            Vec3d projectilePos = projectile.getPos();
            Vec3d projectileMotion = projectile.getVelocity();
            Vec3d reflectionVector = this.calculateReflectionVector(playerPos, projectilePos, projectileMotion);
            // Reflect the projectile back
            projectile.setVelocity(reflectionVector);
            info.setReturnValue(false);
        }
        ItemStack stack = player.getInventory().getArmorStack(2);
        if (source.getAttacker() instanceof LivingEntity attacker && player.hasStatusEffect(EffectRegistry.LIFE_LEACH.get()) && !stack.isEmpty()
                && (stack.isOf(ItemRegistry.ENHANCED_WITHERED_CHEST.get()) || stack.isOf(ItemRegistry.WITHERED_CHEST.get()))) {
            double x = player.getX() - attacker.getX();
            double z = player.getZ() - attacker.getZ();
            attacker.damage(DamageSource.WITHER, 1f);
            attacker.takeKnockback(0.5f, x, z);
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, ConfigConstructor.withered_chest_apply_wither_duration, ConfigConstructor.withered_chest_apply_wither_amplifier));
            if (!player.getInventory().getArmorStack(2).isEmpty() && player.getInventory().getArmorStack(2).isOf(ItemRegistry.ENHANCED_WITHERED_CHEST.get())) {
                attacker.setOnFireFor(ConfigConstructor.withered_chest_apply_fire_seconds);
            }
            if (!player.getWorld().isClient) {
                for (int i = 0; i < 50; i++) {
                    //ParticleHandler.singleParticle(player.getWorld(), ParticleRegistry.BLACK_FLAME, player.getParticleX(1D), player.getBodyY(0.5) + player.getRandom().nextDouble() * 2 - 1D, player.getParticleZ(1D),
                           // player.getRandom().nextGaussian() / 10f, player.getRandom().nextGaussian() / 10f, player.getRandom().nextGaussian() / 10f);
                    ((ServerWorld)player.getWorld()).spawnParticles(ParticleRegistry.BLACK_FLAME.get(), player.getParticleX(1D), player.getBodyY(0.5) + player.getRandom().nextDouble() * 2 - 1D, player.getParticleZ(1D), 1, 0, 0, 0, player.getRandom().nextGaussian() / 10f);
                }
            }
            player.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1f, 1f);
        }
    }

    @Unique
    private Vec3d calculateReflectionVector(Vec3d playerPos, Vec3d projectilePos, Vec3d projectileMotion) {
        Vec3d vectorToPlayer = playerPos.subtract(projectilePos).normalize();
        return projectileMotion.subtract(vectorToPlayer.multiply(projectileMotion.dotProduct(vectorToPlayer) * 2.0D));
    }

    @Inject(method = "attack", at = @At("HEAD"))
    public void interceptAttack(Entity target, CallbackInfo info) {
        PlayerEntity player = ((PlayerEntity) (Object)this);
        if (target instanceof LivingEntity && player.hasStatusEffect(EffectRegistry.BLOODTHIRSTY.get()) && player.getMainHandStack().getItem() instanceof ToolItem) {
            float attackCooldown = player.getAttackCooldownProgress(0.5f);
            float heal = (2f + player.getStatusEffect(EffectRegistry.BLOODTHIRSTY.get()).getAmplifier()) * attackCooldown;
            player.heal(heal);
        }
    }

    // Disable off-hand if ultra heavy weapons is held and config line is enabled
    @Inject(method = "getEquippedStack", at = @At("HEAD"), cancellable = true)
    public void interceptGetEquippedStackHead(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> info) {
        if (WeaponUtil.isModLoaded("bettercombat") || WeaponUtil.isModLoaded("epicfight")) {
            return;
        }
        PlayerEntity player = ((PlayerEntity) (Object)this);
        ItemStack stack = player.getInventory().getMainHandStack();
        boolean mainHeavy = stack.getItem() instanceof IUltraHeavy item && item.isHeavy();
        ItemStack offStack = player.getInventory().offHand.get(0);
        boolean offHeavy = offStack.getItem() instanceof IUltraHeavy item && item.isHeavy();
        if (ConfigConstructor.ultra_heavy_disable_offhand_when_held) {
            // If this statement passed if offhand also was heavy, then the item would disappear when put in offhand.
            // Therefore, only disable offhand completely if main hand is heavy, while give mining fatigue if heavy
            // is in offhand and not main hand.
            if (slot == EquipmentSlot.OFFHAND && mainHeavy) {
                info.setReturnValue(ItemStack.EMPTY);
                info.cancel();
            }
            if (offHeavy && !stack.isEmpty() && !mainHeavy) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 10, 3));
            }
        }
    }
}
