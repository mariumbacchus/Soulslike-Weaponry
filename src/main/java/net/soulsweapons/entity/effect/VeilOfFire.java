package net.soulsweapons.entity.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class VeilOfFire extends MobEffect {

    public VeilOfFire() {
        super(MobEffectCategory.NEUTRAL, 0xfa8500);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int i = 30 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        for (LivingEntity target : entity.getLevel().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1.5D))) {
            target.hurt(DamageSource.IN_FIRE, 2f + amplifier);
            target.setSecondsOnFire(2 + amplifier);
        }
        if (entity.getLevel().isClientSide) {
            for (int i = 0; i < 50; i++) {
                entity.getLevel().addParticle(ParticleTypes.FLAME, entity.getRandomX(1D), entity.getY(0.5) + entity.getRandom().nextDouble() * 2 - 1D, entity.getRandomZ(1D), 0, 0, 0);
            }
        }
        if (entity instanceof Player player) {
            if (!player.isCreative()) {
                entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0));
                entity.setSecondsOnFire(3);
            }
        } else {
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0));
            entity.setSecondsOnFire(3);
        }
    }
}
