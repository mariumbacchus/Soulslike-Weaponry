package net.soulsweapons.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

//Not in current use, might be used later. If so, modify to fit behavior of FlameParticle.
public class FadingParticle extends TextureSheetParticle {

    protected FadingParticle(ClientLevel pLevel, double x, double y, double z, SpriteSet spriteSet, double velX, double velY, double velZ) {
        super(pLevel, x, y, z, velY, velY, velZ);

        this.friction = 0.8f;
        this.xd = velX;
        this.yd = velY;
        this.zd = velZ;
        this.quadSize *= 0.85f;
        this.lifetime = 20;
        this.setSpriteFromAge(spriteSet);
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        this.fade();
    }

    private void fade() {
        this.alpha = (-(1 / (float) lifetime) * age + 1);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet set) {
            this.spriteSet = set;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel level, double x, double y, double z, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new FadingParticle(level, x, y, z, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
