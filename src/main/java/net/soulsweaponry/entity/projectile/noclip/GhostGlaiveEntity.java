package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;

public class GhostGlaiveEntity extends DamagingNoClipEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private int postureLoss = (int) ConfigConstructor.glaive_of_hodir_projectile_posture_loss;

    public GhostGlaiveEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setAreaParticle(ParticleRegistry.SUN_PARTICLE);
        this.setAreaParticleCount((byte) 20);
    }

    public GhostGlaiveEntity(World world, LivingEntity owner, int maxAge) {
        super(EntityRegistry.GHOST_GLAIVE_TYPE, world, owner, maxAge);
        this.setAreaParticle(ParticleRegistry.SUN_PARTICLE);
        this.setAreaParticleCount((byte) 20);
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
        PostureData.addPostureLoss(target, this.postureLoss);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    public void setPostureLoss(int postureLoss) {
        this.postureLoss = postureLoss;
    }

    public int getPostureLoss() {
        return this.postureLoss;
    }
}