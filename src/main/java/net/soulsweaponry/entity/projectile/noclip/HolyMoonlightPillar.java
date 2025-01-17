package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Map;

public class HolyMoonlightPillar extends DamagingWarmupEntity implements GeoEntity {

    private float knockUp = ConfigConstructor.holy_moonlight_ability_knockup;
    private static final TrackedData<Float> RADIUS = DataTracker.registerData(HolyMoonlightPillar.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> PARTICLE_MOD = DataTracker.registerData(HolyMoonlightPillar.class, TrackedDataHandlerRegistry.FLOAT);
    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);

    public HolyMoonlightPillar(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(PARTICLE_MOD, 1f);
        this.dataTracker.startTracking(RADIUS, 1.85f);
    }

    @Override
    public void handleSoundStatus(byte status) {
        this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundRegistry.MOONLIGHT_BIG_EVENT, this.getSoundCategory(), 1f, 1f, false);
    }

    @Override
    public void applyDamageEffects(boolean wasHit, LivingEntity target) {
        if (wasHit) {
            target.addVelocity(0, this.getKnockup(), 0);
        }
    }

    @Override
    public void onTrigger() {
        this.setEmerge(true);
        float spread = 1.2f;
        float height = 0.34f;
        Vec3d vec = new Vec3d(spread, height, spread);
        Map<ParticleEffect, Vec3d> map = Map.of(ParticleTypes.SOUL_FIRE_FLAME, vec, ParticleTypes.LARGE_SMOKE, vec);
        ParticleHandler.particleOutburstMap(this.getWorld(), Math.min(20 * (int) this.getParticleMod(), 100), this.getX(), this.getY(), this.getZ(), map, 0.5f);
    }

    private float getKnockup() {
        return this.knockUp;
    }

    public void setKnockUp(float knockUp) {
        this.knockUp = knockUp;
    }

    public void setRadius(float radius) {
        this.dataTracker.set(RADIUS, radius);
    }

    public float getRadius() {
        return this.dataTracker.get(RADIUS);
    }

    public void setParticleMod(float particleMod) {
        this.dataTracker.set(PARTICLE_MOD, particleMod);
    }

    public float getParticleMod() {
        return this.dataTracker.get(PARTICLE_MOD);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Knockup")) {
            this.knockUp = nbt.getFloat("Knockup");
        }
        if (nbt.contains("Radius")) {
            this.setRadius(nbt.getFloat("Radius"));
        }
        if (nbt.contains("ParticleModifier")) {
            this.setParticleMod(nbt.getFloat("ParticleModifier"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("Knockup", this.knockUp);
        nbt.putFloat("Radius", this.getRadius());
        nbt.putFloat("ParticleModifier", this.getParticleMod());
    }

    private PlayState idle(AnimationState<?> state) {
        if (this.getEmerge()) {
            state.getController().setAnimation(RawAnimation.begin().then("emerge3", Animation.LoopType.HOLD_ON_LAST_FRAME));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(this.getRadius(), this.getRadius());
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (RADIUS.equals(data)) {
            this.calculateDimensions();
        }
        super.onTrackedDataSet(data);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "idle", 0, this::idle));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
