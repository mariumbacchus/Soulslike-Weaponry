package net.soulsweaponry.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
}
