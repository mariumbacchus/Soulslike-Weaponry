package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Map;

public class FlamePillar extends DamagingWarmupEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);

    public FlamePillar(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void handleSoundStatus(byte status) {
        this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundRegistry.DAY_STALKER_CHAOS_STORM.get(), this.getSoundCategory(), 1f, 1f, false);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
    }

    @Override
    public void applyDamageEffects(boolean wasHit, LivingEntity target) {
        if (wasHit) {
            target.addVelocity(0, 0.5f, 0);
        }
    }

    @Override
    public void onTrigger() {
        if (this.getWorld().getBlockState(this.getBlockPos()).isAir()) {
            this.getWorld().setBlockState(this.getBlockPos(), Blocks.FIRE.getDefaultState());
        }
        if (this.getParticleAmountMod() > 0) {
            Map<ParticleEffect, Vec3d> map = Map.of(ParticleTypes.WAX_ON, this.getParticleVec(), ParticleTypes.FLAME, this.getParticleVec(), ParticleRegistry.SUN_PARTICLE.get(), this.getParticleVec());
            ParticleHandler.particleOutburstMap(this.getWorld(), Math.min(30 * (int) this.getParticleAmountMod(), 100), this.getX(), this.getY(), this.getZ(), map, 0.4f);
        }
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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 0, this::idle));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}