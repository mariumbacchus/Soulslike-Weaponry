package net.soulsweaponry.client.particles.factory;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.soulsweaponry.client.particles.WarningParticle;

public class WarningParticleFactory implements ParticleFactory<SimpleParticleType> {

    private final SpriteProvider spriteProvider;

    public WarningParticleFactory(SpriteProvider spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        return new WarningParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);
    }
}
