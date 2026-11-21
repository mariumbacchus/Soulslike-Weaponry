package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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
    private static final TrackedData<Boolean> BOOST_DARKNESS = DataTracker.registerData(DarknessOrb.class, TrackedDataHandlerRegistry.BOOLEAN);

    public DarknessOrb(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setAreaParticle(ParticleTypes.LARGE_SMOKE);
        this.setAreaParticleCount((byte) 1);
        this.setBoostDarkness(false);
    }

    public DarknessOrb(World world, LivingEntity owner, int maxAge, boolean boostDarkness) {
        super(EntityRegistry.DARKNESS_ORB, world, owner, maxAge);
        this.setAreaParticle(ParticleTypes.LARGE_SMOKE);
        this.setAreaParticleCount((byte) 1);
        this.setBoostDarkness(boostDarkness);
    }

    public DarknessOrb(World world, LivingEntity owner, int maxAge) {
        this(world, owner, maxAge, false);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(BOOST_DARKNESS, false);
    }

    public void setBoostDarkness(boolean bl) {
        this.dataTracker.set(BOOST_DARKNESS, bl);
    }

    public boolean getBoostDarkness() {
        return this.dataTracker.get(BOOST_DARKNESS);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            if (this.age % 10 != 0) {
                return;
            }
            for (int i = 0; i < this.getAreaParticleCount(); ++i) {
                this.getWorld().addParticle(this.getAreaParticle(), this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void applyDamageEffects(boolean wasHit, LivingEntity target) {
        if (this.getBoostDarkness() && wasHit && target.hasStatusEffect(StatusEffects.DARKNESS)) {
            target.removeStatusEffect(StatusEffects.DARKNESS);
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0));
        } else {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 40, 0));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}
