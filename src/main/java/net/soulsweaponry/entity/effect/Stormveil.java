package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.Random;

public class Stormveil extends StatusEffect {

    private static final Random RAND = new Random();

    public Stormveil() {
        super(StatusEffectCategory.BENEFICIAL, 0x82f3ff);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % RAND.nextInt(15, 40) == 0;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient) {
            for (int i = 0; i < 40; i++) {
                entity.getWorld().addParticle(ParticleRegistry.SOUL_SPARK.get(), entity.getParticleX(1D), entity.getBodyY(0.5) + entity.getRandom().nextDouble() * 2 - 1D, entity.getParticleZ(1D), 0, 0, 0);
            }
        }
        entity.playSound(SoundRegistry.STORMVEIL_AMBIENT.get(), 1f, 1f);
    }
}