package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;

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

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        LivingEntity target = entity.getAttacking();
        if (entity.getWorld().isClient) {
            for (int i = 0; i < 30; i++) {
                entity.getWorld().addParticle(ParticleRegistry.DARK_STAR, entity.getParticleX(1D), entity.getBodyY(0.5) + entity.getRandom().nextDouble() * 2 - 1D, entity.getParticleZ(1D), 0, 0, 0);
            }
        }
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
