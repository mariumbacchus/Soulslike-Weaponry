package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;

public class VeilOfFire extends StatusEffect {

    public VeilOfFire() {
        super(StatusEffectCategory.NEUTRAL, 0xfa8500);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 30 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        for (LivingEntity target : entity.getWorld().getNonSpectatingEntities(LivingEntity.class, entity.getBoundingBox().expand(1.5D))) {
            target.damage(DamageSource.IN_FIRE, 2f + amplifier);
            target.setOnFireFor(2 + amplifier);
        }
        if (entity.getWorld().isClient) {
            for (int i = 0; i < 50; i++) {
                entity.getWorld().addParticle(ParticleTypes.FLAME, entity.getParticleX(1D), entity.getBodyY(0.5) + entity.getRandom().nextDouble() * 2 - 1D, entity.getParticleZ(1D), 0, 0, 0);
            }
        }
        if (entity instanceof PlayerEntity player) {
            if (!player.isCreative()) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 60, 0));
                entity.setOnFireFor(3);
            }
        } else {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 60, 0));
            entity.setOnFireFor(3);
        }
    }
}
