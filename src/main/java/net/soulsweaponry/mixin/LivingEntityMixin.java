package net.soulsweaponry.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.BossEntity;
import net.soulsweaponry.events.LivingEntityTickCallback;
import net.soulsweaponry.items.DetonateGroundItem;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.ModifyDamageUtil;
import net.soulsweaponry.util.ParticleEvents;
import net.soulsweaponry.util.ParticleHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.soulsweaponry.items.UmbralTrespassItem.SHOULD_DAMAGE_RIDING;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    private final LivingEntity entity = ((LivingEntity)(Object)this);

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    /*
     * NB! Only called if the damage is bigger than 0 (decimals count)
     */
    @Inject(method = "applyEnchantmentsToDamage", at = @At("TAIL"), cancellable = true)
    protected void applyEnchantmentsToDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        LivingEntity entity = this.entity;
        float newAmount = info.getReturnValue();
        info.setReturnValue(ModifyDamageUtil.modifyDamageTaken(entity, newAmount, source));
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    public void interceptHeal(float amount, CallbackInfo info) {
        if (this.entity.hasStatusEffect(EffectRegistry.DISABLE_HEAL)) {
            info.cancel();
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    public void interceptDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (source == CustomDamageSource.TRUE_MAGIC) {
            ((LivingEntityInvoker)this.entity).invokeApplyDamage(source, amount);
        }
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void interceptFallDamage(float fallDistance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> info) {
        //Another interceptFallDamage is made for players in PlayerEntityMixin since it won't trigger if they are in creative
        //from this, but in survival it would trigger twice. This check is therefore needed to prevent the double call.
        if (!(this.entity instanceof PlayerEntity) && DetonateGroundItem.triggerCalculateFall(this.entity, fallDistance, source)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }

    @Inject(method = "onDismounted", at = @At("HEAD"))
    public void interceptDismount(Entity entity, CallbackInfo info) {
        if (entity instanceof LivingEntity && this.entity instanceof PlayerEntity player) {
            try {
                if (player.getDataTracker().get(SHOULD_DAMAGE_RIDING)) {
                    LivingEntity target = ((LivingEntity)entity);
                    float damage = 20f;
                    ItemCooldownManager cooldownManager = player.getItemCooldownManager();
                    for (Hand hand : Hand.values()) {
                        ItemStack stack = player.getStackInHand(hand);
                        if (player.getStackInHand(hand).isOf(WeaponRegistry.SHADOW_ASSASSIN_SCYTHE)) {
                            damage = ConfigConstructor.shadow_assassin_scythe_ability_damage + EnchantmentHelper.getAttackDamage(stack, target.getGroup());
                            cooldownManager.set(stack.getItem(), ConfigConstructor.shadow_assassin_scythe_ability_cooldown);
                        } else if (player.getStackInHand(hand).isOf(WeaponRegistry.DARKIN_SCYTHE_PRIME)) {
                            damage = ConfigConstructor.darkin_scythe_prime_ability_damage + EnchantmentHelper.getAttackDamage(stack, target.getGroup()) + (target.getMaxHealth() * (ConfigConstructor.darkin_scythe_prime_ability_percent_health_damage/100f));
                            float healing = damage * ConfigConstructor.darkin_scythe_prime_heal_modifier;
                            player.heal(healing);
                            cooldownManager.set(stack.getItem(), ConfigConstructor.darkin_scythe_prime_ability_cooldown);
                        }
                    }
                    target.damage(DamageSource.mob(player), damage);
                    player.getDataTracker().set(SHOULD_DAMAGE_RIDING, false);
                    if (!player.world.isClient && player.getBlockPos() != null) {
                        player.world.playSound(null, player.getBlockPos(), SoundRegistry.SLICE_TARGET_EVENT, SoundCategory.PLAYERS, 0.8f, 1f);
                        ParticleHandler.particleOutburstMap(player.getWorld(), 150, player.getX(), player.getEyeY(), player.getZ(), ParticleEvents.SOUL_FLAME_SMALL_OUTBURST_MAP, 1f);
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    @Inject(method = "getMaxHealth", at = @At("HEAD"), cancellable = true)
    protected void interceptGetMaxHealth(CallbackInfoReturnable<Float> infoReturnable) {
        if (this.entity instanceof BossEntity boss) {
            infoReturnable.setReturnValue((float) boss.getBossMaxHealth());
        }
    }

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    private void interceptTick(CallbackInfo info) {
        ActionResult result = LivingEntityTickCallback.EVENT.invoker().tick(this.entity);
        if (result == ActionResult.FAIL) {
            info.cancel();
        }
    }
}
