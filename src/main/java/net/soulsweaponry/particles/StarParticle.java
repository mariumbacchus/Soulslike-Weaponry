package net.soulsweaponry.particles;

import net.fabricmc.api.Environment;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class StarParticle extends SpriteBillboardParticle {
    static final Random RANDOM = new Random();
    private final SpriteProvider spriteProvider;

    StarParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, h, i);
        this.velocityMultiplier = 0.96F;
        this.field_28787 = true;
        this.spriteProvider = spriteProvider;
        this.scale *= 0.75F;
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getBrightness(float tint) {
        float f = ((float)this.age + tint) / (float)this.maxAge;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightness(tint);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static class LifeStealParticle implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public LifeStealParticle(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            StarParticle particle = new StarParticle(clientWorld, d, e, f, 0.0D, 0.0D, 0.0D, this.spriteProvider);
            particle.setColor(0.64F, 0.012F, 0.067F);
            particle.setVelocity(g * 0.25D, h * 0.25D, i * 0.25D);
            
            particle.setMaxAge(clientWorld.random.nextInt(20) + 5);
            return particle;
        }
    }
}
