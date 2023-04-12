package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;

public class LifeLeach extends StatusEffect {
    public LifeLeach() {
        super(
            StatusEffectCategory.BENEFICIAL,
            0x187a02
        );
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int k = 40 >> amplifier;
         if (k > 0) {
            return duration % k == 0;
         } else {
            return true;
         }
    }

    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        LivingEntity target = entity.getAttacking();
        if (target != null && target.hasStatusEffect(StatusEffects.WITHER)) {
            entity.heal(1 + target.getStatusEffect(StatusEffects.WITHER).getAmplifier());
        }
    }
}
