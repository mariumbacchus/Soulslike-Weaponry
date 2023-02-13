package net.soulsweaponry.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class Retribution extends StatusEffect {

    public Retribution() {
        super(StatusEffectCategory.HARMFUL, 0xc76700);
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
}
