package net.soulsweaponry.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockState;
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
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundCategory;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.entitydata.UmbralTrespassData;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.detonateground.IDetonateGround;
import net.soulsweaponry.items.abilities.posthit.UltraHeavy;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;
import net.soulsweaponry.util.WeaponUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void interceptFallDamage(float fallDistance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> info) {
        if (IDetonateGround.triggerCalculateFall(((PlayerEntity)(Object)this), fallDistance, source)) {
            info.setReturnValue(false);
            info.cancel();
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
        int parryTicks = ParryData.getParryTicks(player);
        int parryFrames = ParryData.getParryFrames(player);
        if (parryTicks >= 1 && parryTicks <= parryFrames && !source.isIn(DamageTypeTags.BYPASSES_SHIELD)) {
            player.getWorld().sendEntityStatus(player, EntityStatuses.BLOCK_WITH_SHIELD);
            if (source.isIn(DamageTypeTags.IS_PROJECTILE) && source.getSource() instanceof ProjectileEntity) {
                info.setReturnValue(false);
                return;
            }
            if (source.getAttacker() instanceof LivingEntity attacker) {
                if (!attacker.hasStatusEffect(EffectRegistry.POSTURE_BREAK.get())) {
                    attacker.getWorld().playSound(null, attacker.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT.get(), SoundCategory.PLAYERS, .5f, 1f);
                }
                attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK.get(), 60, 0));
                attacker.takeKnockback(0.4f,  player.getX() - attacker.getX(), player.getZ() - attacker.getZ());
                info.setReturnValue(false);
            }
        }
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
        if (WeaponUtil.isFightModLoaded()) {
            return;
        }
        PlayerEntity player = ((PlayerEntity) (Object)this);

        ItemStack stack = player.getInventory().getMainHandStack();
        boolean mainHeavy = IHasAbilities.getAbility(stack, UltraHeavy.class).isPresent();

        ItemStack offStack = player.getInventory().offHand.get(0);
        boolean offHeavy = IHasAbilities.getAbility(offStack, UltraHeavy.class).isPresent();
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

    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityGroup;)F",
                    ordinal = 0
            )
    )
    private float abilities$modifyAttackDamage(float original, Entity target) {
        PlayerEntity attacker = (PlayerEntity)(Object)this;
        ItemStack stack = attacker.getMainHandStack();
        if (!(stack.getItem() instanceof IHasAbilities has) || has.getAbilities().isEmpty()) {
            return original;
        }
        DamageSource source = attacker.getDamageSources().playerAttack(attacker);
        float bonus = has.getBonusAttackDamage(target, original, source);
        return original + bonus;
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void soulsweapons$applyUpgradeMiningSpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        ItemStack stack = player.getMainHandStack();
        float bonus = NbtHelper.getFloat(stack, NbtIds.UPGRADE_MINING_EFFICIENCY, 0f);
        if (bonus > 0f) {
            cir.setReturnValue(cir.getReturnValue() + bonus);
        }
    }
}