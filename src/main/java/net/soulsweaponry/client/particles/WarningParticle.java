package net.soulsweaponry.client.particles;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class WarningParticle extends SpriteBillboardParticle {

    public WarningParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        this.scale = 0.5f;
        this.maxAge = 20;
        this.gravityStrength = 0f;
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);

        // default color, can be changed later
        this.setColor(0.2f, 0.9f, 1.0f); // aqua-ish
        this.setAlpha(0.9f);
    }

    @Override
    public void tick() {
        super.tick();
        // slight upward movement
        this.velocityY += 0.004;
        this.velocityX *= 0.9;
        this.velocityY *= 0.9;
        this.velocityZ *= 0.9;

        // fade out
        if (this.age > this.maxAge - 5) {
            this.setAlpha((this.maxAge - this.age) / 5f);
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
}
