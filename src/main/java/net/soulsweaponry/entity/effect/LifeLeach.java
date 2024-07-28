package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.ModTags;

public class LifeLeach extends StatusEffect {

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
            for (StatusEffectInstance instance : target.getStatusEffects()) {
                StatusEffect effect = instance.getEffectType();
                if (Registries.STATUS_EFFECT.getEntry(effect).isIn(ModTags.Effects.DAMAGE_OVER_TIME)) {
                    entity.heal(1);
                    break;
                }
            }
        }
    }
}
