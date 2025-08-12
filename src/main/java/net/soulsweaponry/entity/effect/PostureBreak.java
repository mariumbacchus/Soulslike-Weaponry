package net.soulsweaponry.entity.effect;

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
        if (entity.getWorld().isClient) {
            return;
        }
        int duration = entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK) ? entity.getStatusEffect(EffectRegistry.POSTURE_BREAK).getDuration() : 60;
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 3));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, 9));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, 9));
    }
}
