package net.soulsweaponry.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.soulsweaponry.items.BladeDanceItem;
import net.soulsweaponry.registry.EffectRegistry;

public class BladeDance extends StatusEffect {

    public BladeDance() {
        super(StatusEffectCategory.BENEFICIAL, 0xa8ffff);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        amplifier++;
        BladeDanceItem.updateBladeDanceItem(entity.getMainHandStack(), amplifier);
        if (entity.getMainHandStack().getItem() instanceof BladeDanceItem item) {
            if (item.getMaxStacks() <= amplifier && !entity.hasStatusEffect(EffectRegistry.COOLDOWN)) {
                item.applyMaxStacksEffects(entity, entity.getMainHandStack());
                entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.COOLDOWN, item.getMaxStacksCooldown(), 0));
            }
        }
    }

    @Override
    public void onEntityRemoval(LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
        super.onEntityRemoval(entity, amplifier, reason);
        BladeDanceItem.updateBladeDanceItem(entity.getMainHandStack(), 0);
    }

    @Override
    public void onEntityDamage(LivingEntity entity, int amplifier, DamageSource source, float amount) {
        super.onEntityDamage(entity, amplifier, source, amount);//TODO test
        // Remove stacks of Blade Dance when taking damage
        int amp = entity.getStatusEffect(EffectRegistry.BLADE_DANCE).getAmplifier();
        int duration = entity.getStatusEffect(EffectRegistry.BLADE_DANCE).getDuration();
        entity.removeStatusEffect(EffectRegistry.BLADE_DANCE);
        amp--;
        if (amp >= 0) {
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLADE_DANCE, duration, amp));
        }
    }
}
