package net.soulsweaponry.client.particles;

import net.minecraft.client.particle.AscendingParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class CyanTintedParticle extends AscendingParticle {

    public CyanTintedParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.1F, 0.1F, 0.1F, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 0.3F, 8, -0.1F, true);
        this.setColor(0.0F, 0.61f, 0.61f);
    }
}