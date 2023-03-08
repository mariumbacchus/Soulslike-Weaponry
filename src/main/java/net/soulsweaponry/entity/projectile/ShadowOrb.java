package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entity.mobs.ChaosMonarch;
import net.soulsweaponry.items.ChaosSet;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.PacketRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.ParticleNetworking;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class ShadowOrb extends AbstractFireballEntity implements GeoEntity {

    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    
    public ShadowOrb(EntityType<? extends ShadowOrb> entityType, World world) {
        super(entityType, world);
    }

    public ShadowOrb(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ) {
        super(EntityRegistry.SHADOW_ORB, owner, velocityX, velocityY, velocityZ, world);
    }

    public ShadowOrb(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(EntityRegistry.SHADOW_ORB, x, y, z, velocityX, velocityY, velocityZ, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (entity != null && entity instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) entity;
            target.damage(CustomDamageSource.shadowOrb(this, this.getOwner()), 5f);
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));
            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.DECAY, 200, 0));
        }
        super.onEntityHit(entityHitResult);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3d vec3d = this.getVelocity();
        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;
        this.world.addParticle(ParticleTypes.ENTITY_EFFECT, d + random.nextDouble() - .5D, e + random.nextDouble() - .5D, f + random.nextDouble() - .5D, 0.0, 0.0, 0.0);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            ParticleNetworking.sendServerParticlePacket((ServerWorld) this.world, PacketRegistry.DARK_EXPLOSION_ID, this.getBlockPos(), 10);
            this.discard();
        }
    }

    @Override
    protected boolean canHit(Entity entity) {
        if (entity instanceof ChaosMonarch) {
            return false;
        }
        if (entity != null && entity instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) entity;
            for (ItemStack stack : target.getArmorItems()) {
                if (stack.getItem() instanceof ChaosSet) {
                    return false;
                }
            }
        }
        return super.canHit(entity);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}
