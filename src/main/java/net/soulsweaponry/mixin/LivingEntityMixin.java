package net.soulsweaponry.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.ParticleNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.soulsweaponry.util.CustomDamageSource;

import static net.soulsweaponry.items.UmbralTrespassItem.SHOULD_DAMAGE_RIDING;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin<T> {

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Inject(method = "applyEnchantmentsToDamage", at = @At("TAIL"))
    protected void applyEnchantmentsToDamage(DamageSource source, float amount, CallbackInfoReturnable<T> infoReturnable) {
        LivingEntity entity = ((LivingEntity)(Object)this);
        if (entity.hasStatusEffect(EffectRegistry.DECAY) && !entity.getEquippedStack(EquipmentSlot.HEAD).isOf(ItemRegistry.CHAOS_CROWN) && !entity.getEquippedStack(EquipmentSlot.HEAD).isOf(ItemRegistry.CHAOS_HELMET)) {
            int amplifier = entity.getStatusEffect(EffectRegistry.DECAY).getAmplifier();
            float amountAdded = amount * ((amplifier + 1)*.2f);
            amount += amountAdded;
        }
        if (source.isMagic() && entity.hasStatusEffect(EffectRegistry.MAGIC_RESISTANCE) && !source.isOutOfWorld()) {
            int amplifier = entity.getStatusEffect(EffectRegistry.MAGIC_RESISTANCE).getAmplifier();
            float amountReduced = amount * ((amplifier + 1)*.2f);
            amount -= amountReduced;
        }
        if (entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK) && !source.isProjectile() && source.getAttacker() != null && source.getAttacker() instanceof LivingEntity) {
            int amplifier = entity.getStatusEffect(EffectRegistry.POSTURE_BREAK).getAmplifier();
            float baseAdded = entity instanceof PlayerEntity ? 3f : 8f;
            float totalAdded = baseAdded * (amplifier + 1);
            amount += totalAdded;
            entity.world.playSound(null, entity.getBlockPos(), SoundRegistry.CRIT_HIT_EVENT, SoundCategory.HOSTILE, .5f, 1f);
            entity.removeStatusEffect(EffectRegistry.POSTURE_BREAK);
        }
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    public void interceptHeal(float amount, CallbackInfo info) {
        if (((LivingEntity)(Object)this).hasStatusEffect(EffectRegistry.DISABLE_HEAL)) {
            info.cancel();
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void interceptDamage(DamageSource source, float amount, CallbackInfoReturnable<T> info) {
        if (source == CustomDamageSource.TRUE_MAGIC) {
            LivingEntity entity = (LivingEntity)(Object)this;
            ((LivingEntityInvoker)entity).invokeApplyDamage(source, amount);
        }
        if (source.isFromFalling() && ((LivingEntity)(Object)this).hasStatusEffect(EffectRegistry.CALCULATED_FALL)) {
            ((LivingEntity)(Object)this).removeStatusEffect(EffectRegistry.CALCULATED_FALL);
            //Removes, then re-adds for half a second so that "dream_on" advancement may trigger
            ((LivingEntity)(Object)this).addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL, 10, 0));
            info.cancel();
        }
    }

    @Inject(method = "onDismounted", at = @At("HEAD"))
    public void interceptDismount(Entity entity, CallbackInfo info) {
        if (entity instanceof LivingEntity && ((LivingEntity)(Object)this) instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity)(Object)this);
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
                        ParticleNetworking.specificServerParticlePacket((ServerWorld) player.world, PacketRegistry.UMBRAL_TRESPASS_ID, player.getBlockPos(), player.getEyeY());
                    }
                }
            } catch (Exception e) {}
        }
    }
}
