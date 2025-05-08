package net.soulsweaponry.client.particles.factory;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.soulsweaponry.client.particles.FadingParticle;

@OnlyIn(Dist.CLIENT)
public class SoulSparkFactory implements ParticleFactory<DefaultParticleType> {

    private final SpriteProvider spriteProvider;

    public SoulSparkFactory(SpriteProvider spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        FadingParticle particle = new FadingParticle(clientWorld, d, e, f, 0.0, 0.0, 0.0, this.spriteProvider);
        particle.setColor(1.0F, 0.9F, 1.0F);
        particle.setVelocity(g * 0.25, h * 0.25, i * 0.25);
        particle.setMaxAge(clientWorld.random.nextInt(6) + 2);
        return particle;
    }
}
