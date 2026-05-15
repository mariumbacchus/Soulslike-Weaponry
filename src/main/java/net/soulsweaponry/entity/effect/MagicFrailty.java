package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.soulsweaponry.registry.ParticleRegistry;

import java.util.Random;

public class MagicFrailty extends StatusEffect {

    private static final Random RAND = new Random();

    public MagicFrailty() {
        super(StatusEffectCategory.BENEFICIAL, 0x232184);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % RAND.nextInt(15, 30) == 0;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            for (int i = 0; i < 10; i++) {
                serverWorld.spawnParticles(ParticleRegistry.BLUE_FLAME, entity.getParticleX(1D), entity.getBodyY(0.5) + entity.getRandom().nextDouble() * 2 - 1D, entity.getParticleZ(1D), 1, 0, 0, 0, 0);
            }
        }
        entity.playSound(SoundEvents.BLOCK_FIRE_AMBIENT, 1f, 1f);
        return true;
    }
}
