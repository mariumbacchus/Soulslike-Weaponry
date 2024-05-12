package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class PostureBreak extends StatusEffect {

    public PostureBreak() {
        super(StatusEffectCategory.HARMFUL, 0x1c0000);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int k = 10 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 3));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20, 9));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 20, 9));
    }
}