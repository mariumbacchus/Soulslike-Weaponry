package net.soulsweaponry.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.soulsweaponry.registry.EffectRegistry;

public class PostureBreak extends StatusEffect {

    public PostureBreak() {
        super(StatusEffectCategory.HARMFUL, 0x1c0000);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        int duration = entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK) ? entity.getStatusEffect(EffectRegistry.POSTURE_BREAK).getDuration() : 60;
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 3));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, 9));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, 9));
    }

    @Override
    public void onEntityRemoval(LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
        super.onEntityRemoval(entity, amplifier, reason);
        if (entity.hasStatusEffect(StatusEffects.SLOWNESS)) entity.removeStatusEffect(StatusEffects.SLOWNESS);
        if (entity.hasStatusEffect(StatusEffects.WEAKNESS)) entity.removeStatusEffect(StatusEffects.WEAKNESS);
        if (entity.hasStatusEffect(StatusEffects.MINING_FATIGUE)) entity.removeStatusEffect(StatusEffects.MINING_FATIGUE);
    }
}
