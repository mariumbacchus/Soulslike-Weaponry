package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EntityRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DarknessOrb extends DamagingNoClipEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public DarknessOrb(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setAreaParticle(ParticleTypes.LARGE_SMOKE);
        this.setAreaParticleCount((byte) 1);
    }

    public DarknessOrb(World world, LivingEntity owner, int maxAge) {
        super(EntityRegistry.DARKNESS_ORB, world, owner, maxAge);
        this.setAreaParticle(ParticleTypes.LARGE_SMOKE);
        this.setAreaParticleCount((byte) 1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            for (int i = 0; i < this.getAreaParticleCount(); ++i) {
                this.getWorld().addParticle(this.getAreaParticle(), this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void applyDamageEffects(boolean wasHit, LivingEntity target) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 40, 0));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}
