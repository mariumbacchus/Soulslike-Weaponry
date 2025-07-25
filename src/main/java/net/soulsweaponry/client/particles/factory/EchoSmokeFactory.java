package net.soulsweaponry.client.particles.factory;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.soulsweaponry.client.particles.CyanTintedParticle;

public class EchoSmokeFactory implements ParticleFactory<SimpleParticleType> {

    private final SpriteProvider spriteProvider;

    public EchoSmokeFactory(SpriteProvider spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    public Particle createParticle(SimpleParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        return new CyanTintedParticle(clientWorld, d, e, f, g, h, i, 1.0F, this.spriteProvider);
    }
}
