package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class Bloodthirsty extends StatusEffect {

    public Bloodthirsty() {
        super(StatusEffectCategory.NEUTRAL, 0x630109);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % Math.max(40 - amplifier * 10, 20) == 0;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.damage(entity.getWorld().getDamageSources().wither(), 1f);
    }
}