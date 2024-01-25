package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.soulsweaponry.registry.EffectRegistry;

public class LifeLeach extends StatusEffect {

    private static final StatusEffect[] DAMAGE_OVER_TIME = {StatusEffects.WITHER, StatusEffects.POISON, EffectRegistry.BLEED};

    public LifeLeach() {
        super(StatusEffectCategory.BENEFICIAL, 0x452773);
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
        if (target != null) {
            for (StatusEffect effect : DAMAGE_OVER_TIME) {
                if (target.hasStatusEffect(effect)) {
                    entity.heal(1);
                    break;
                }
            }
        }
    }
}
