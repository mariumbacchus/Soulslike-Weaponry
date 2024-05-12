package net.soulsweaponry.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

//Not in current use, might be used later. If so, modify to fit behavior of FlameParticle.
public class FadingParticle extends SpriteBillboardParticle {

    public FadingParticle(ClientWorld pLevel, double x, double y, double z, SpriteProvider spriteSet, double velX, double velY, double velZ) {
        super(pLevel, x, y, z, velY, velY, velZ);

        this.gravityStrength = 0.8f;
        this.velocityX = velX;
        this.velocityY = velY;
        this.velocityZ = velZ;
        this.scale *= 0.85f;
        this.maxAge = 20;
        this.setSprite(spriteSet);
        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        this.fade();
    }

    private void fade() {
        this.alpha = (-(1 / (float) maxAge) * age + 1);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteSet;

        public Provider(SpriteProvider set) {
            this.spriteSet = set;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType pType, ClientWorld level, double x, double y, double z, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new FadingParticle(level, x, y, z, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
