package net.soulsweaponry.entity.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.soulsweaponry.items.LeviathanAxe;
import net.soulsweaponry.util.IAnimatedDeath;

public class Freezing extends MobEffect {

    public Freezing() {
        super(MobEffectCategory.NEUTRAL, 9238001);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        int ticks = entity.getTicksFrozen();
        //if (entity instanceof FrostGiant || entity instanceof RimeSpectre) return; TODO add entities
        entity.setIsInPowderSnow(true);
        entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze(), ticks + amplifier));
        //NOTE: This is only called client side when affecting players, not regular mobs
        if (!entity.getLevel().isClientSide) {
            ((ServerLevel) entity.getLevel()).sendParticles(ParticleTypes.SNOWFLAKE, entity.getRandomX(1D), entity.getY(0.5) + entity.getRandom().nextDouble() * 2 - 1D, entity.getRandomZ(1D), 1, 0, 0, 0, 0);
        }
        if (entity.isDeadOrDying()) {
            if (entity instanceof IAnimatedDeath animated) {
                if (animated.getDeathTicks() < 2) {
                    LeviathanAxe.iceExplosion(entity.getLevel(), entity.blockPosition(), entity.getLastHurtByMob(), amplifier);
                }
            }
            else if (entity.deathTime < 2) {
                LeviathanAxe.iceExplosion(entity.getLevel(), entity.blockPosition(), entity.getLastHurtByMob(), amplifier);
            }
        }
    }
}
