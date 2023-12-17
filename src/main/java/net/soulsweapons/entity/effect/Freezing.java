package net.soulsweapons.entity.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Freezing extends MobEffect {

    public Freezing() {
        super(MobEffectCategory.NEUTRAL, 0xa915d6);
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
        if (!entity.level.isClientSide) {
            //ParticleNetworking.specificServerParticlePacket((ServerWorld) entity.world, PacketRegistry.SNOW_PARTICLES_ID, entity.getBlockPos(), (double)entity.getWidth(), (float)entity.getHeight()); TODO implement particle fix
        }
        if (entity.isDeadOrDying()) {
//            if (entity instanceof AnimatedDeathInterface animated) { //TODO add LeviathanAxe and IAnimatedDeath
//                if (animated.getDeathTicks() < 2) {
//                    LeviathanAxe.iceExplosion(entity.getWorld(), entity.getBlockPos(), entity.getAttacker(), amplifier);
//                }
//            }
//            else if (entity.deathTime < 2) {
//                LeviathanAxe.iceExplosion(entity.getWorld(), entity.getBlockPos(), entity.getAttacker(), amplifier);
//            }
        }
    }
}
