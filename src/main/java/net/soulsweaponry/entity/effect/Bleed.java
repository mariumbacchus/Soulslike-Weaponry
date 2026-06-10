package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.soulsweaponry.config.EntityStatsConfig;
import net.soulsweaponry.entitydata.BleedData;

public class Bleed extends StatusEffect {

    public Bleed() {
        super(StatusEffectCategory.HARMFUL, 0xba0c00);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 15 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        }
        return true;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        BleedData.addBleed(entity, (int) (EntityStatsConfig.bleed_effect_base_increase + (amplifier + 1) * EntityStatsConfig.bleed_effect_increase_per_amp));
        return true;
    }
}
