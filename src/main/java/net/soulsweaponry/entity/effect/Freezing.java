package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.soulsweaponry.entity.mobs.FrostGiant;
import net.soulsweaponry.entity.mobs.RimeSpectre;
import net.soulsweaponry.items.LeviathanAxe;
import net.soulsweaponry.util.IAnimatedDeath;
import net.soulsweaponry.particles.ParticleHandler;

public class Freezing extends StatusEffect {

    public Freezing() {
        super(StatusEffectCategory.HARMFUL, 9238001);
    }
    
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        int ticks = entity.getFrozenTicks();
        if (entity instanceof FrostGiant || entity instanceof RimeSpectre) return;
        entity.setInPowderSnow(true);
        entity.setFrozenTicks(Math.min(entity.getMinFreezeDamageTicks(), ticks + amplifier));
        if (!entity.getWorld().isClient) {
            ParticleHandler.singleParticle(entity.getWorld(), ParticleTypes.SNOWFLAKE, entity.getParticleX(0.5D), entity.getRandomBodyY(), entity.getParticleZ(0.5D), 0, 0, 0);
        }
        if (entity.isDead()) {
            if (entity instanceof IAnimatedDeath animated) {
                if (animated.getDeathTicks() < 2) {
                    LeviathanAxe.iceExplosion(entity.getWorld(), entity.getBlockPos(), entity.getAttacker(), amplifier);
                }
            }
            else if (entity.deathTime < 2) {
                LeviathanAxe.iceExplosion(entity.getWorld(), entity.getBlockPos(), entity.getAttacker(), amplifier);
            }
        }
    }
}
