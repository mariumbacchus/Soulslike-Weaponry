package net.soulsweaponry.mixin;

import org.spongepowered.asm.mixin.Mixin;
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
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDamageSource;

@Mixin(LivingEntity.class)
public class LivingEntityMixin<T> {
    
    @Inject(method = "modifyAppliedDamage", at = @At("TAIL"))
    protected float modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<T> infoReturnable) {
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
        return amount;
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    public void interceptHeal(float amount, CallbackInfo info) {
        if (((LivingEntity)(Object)this).hasStatusEffect(EffectRegistry.DISABLE_HEAL)) {
            info.cancel();
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public boolean interceptDamage(DamageSource source, float amount, CallbackInfoReturnable<T> info) {
        if (source == CustomDamageSource.TRUE_MAGIC) {
            LivingEntity entity = (LivingEntity)(Object)this;
            ((LivingEntityInvoker)entity).invokeApplyDamage(source, amount);
            return true;
        }
        if (source.isFromFalling() && ((LivingEntity)(Object)this).hasStatusEffect(EffectRegistry.CALCULATED_FALL)) {
            ((LivingEntity)(Object)this).removeStatusEffect(EffectRegistry.CALCULATED_FALL);
            //Removes, then re-adds for half a second so that "dream_on" advancement may trigger
            ((LivingEntity)(Object)this).addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL, 10, 0));
            info.cancel();
            return false;
        }
        return info.getReturnValueZ();
    }
}
