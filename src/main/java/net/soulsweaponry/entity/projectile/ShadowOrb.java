package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entity.mobs.ChaosMonarch;
import net.soulsweaponry.entity.mobs.NightShade;
import net.soulsweaponry.items.ChaosSet;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.ParticleEvents;
import net.soulsweaponry.util.ParticleHandler;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class ShadowOrb extends AbstractFireballEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private final StatusEffect[] effects;
    
    public ShadowOrb(EntityType<? extends ShadowOrb> entityType, World world) {
        super(entityType, world);
        this.effects = new StatusEffect[] {StatusEffects.WITHER, EffectRegistry.DECAY};
    }

    public ShadowOrb(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ, StatusEffect[] effects) {
        super(EntityRegistry.SHADOW_ORB, owner, velocityX, velocityY, velocityZ, world);
        this.effects = effects;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity target && this.getOwner() instanceof LivingEntity) {
            target.damage(CustomDamageSource.create(this.getWorld(), CustomDamageSource.SHADOW_ORB, this, this.getOwner()), 5f);
            for (StatusEffect effect : this.effects) {
                target.addStatusEffect(new StatusEffectInstance(effect, 150, 0));
            }
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
        this.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, d + random.nextDouble() - .5D, e + random.nextDouble() - .5D, f + random.nextDouble() - .5D, 0.0, 0.0, 0.0);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            ParticleHandler.particleSphereList(this.getWorld(), 10, this.getX(), this.getY(), this.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 1f);
            this.discard();
        }
    }

    @Override
    protected boolean canHit(Entity entity) {
        if (entity instanceof ChaosMonarch || entity instanceof NightShade) {
            return false;
        }
        if (entity instanceof LivingEntity target) {
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
