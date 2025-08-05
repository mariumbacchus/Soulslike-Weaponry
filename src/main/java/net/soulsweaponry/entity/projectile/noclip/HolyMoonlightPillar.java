package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.*;
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
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;

public class HolyMoonlightPillar extends DamagingWarmupEntity implements GeoEntity {

    private float knockUp = ConfigConstructor.holy_moonlight_ability_knockup;
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public HolyMoonlightPillar(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
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
        super.onTrigger();
        if (this.getParticleAmountMod() > 0) {
            Map<ParticleEffect, Vec3d> map = Map.of(ParticleTypes.SOUL_FIRE_FLAME, this.getParticleVec(), ParticleTypes.LARGE_SMOKE, this.getParticleVec());
            ParticleHandler.particleOutburstMap(this.getWorld(), Math.min(20 * (int) this.getParticleAmountMod(), 100), this.getX(), this.getY(), this.getZ(), map, 0.5f);
        }
    }

    private float getKnockup() {
        return this.knockUp;
    }

    public void setKnockUp(float knockUp) {
        this.knockUp = knockUp;
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
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("Knockup", this.knockUp);
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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "idle", 0, this::idle));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
