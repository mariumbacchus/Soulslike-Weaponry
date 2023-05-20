package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.soulsweaponry.entity.mobs.FrostGiant;
import net.soulsweaponry.entity.mobs.RimeSpectre;
import net.soulsweaponry.items.LeviathanAxe;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.util.AnimatedDeathInterface;
import net.soulsweaponry.util.ParticleNetworking;

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
        if (!entity.world.isClient) {
            ParticleNetworking.specificServerParticlePacket((ServerWorld) entity.world, PacketRegistry.SNOW_PARTICLES_ID, entity.getBlockPos(), (double)entity.getWidth(), (float)entity.getHeight());
        }
        if (entity.isDead()) {
            if (entity instanceof AnimatedDeathInterface animated) {
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
