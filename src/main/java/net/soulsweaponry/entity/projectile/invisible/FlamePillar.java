package net.soulsweaponry.entity.projectile.invisible;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;
import java.util.Random;

public class FlamePillar extends InvisibleWarmupEntity {

    private static final TrackedData<Float> RADIUS = DataTracker.registerData(FlamePillar.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> PARTICLE_MOD = DataTracker.registerData(FlamePillar.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> DIVERGENCE = DataTracker.registerData(FlamePillar.class, TrackedDataHandlerRegistry.FLOAT);

    public FlamePillar(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world, Items.AIR.getDefaultStack());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(PARTICLE_MOD, 1f);
        this.dataTracker.startTracking(RADIUS, 1.5f);
        this.dataTracker.startTracking(DIVERGENCE, 1f);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            this.setWarmup(this.getWarmup() - 1);
            if (this.getWarmup() < 0) {
                if (this.getWarmup() == -7) {
                    List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.2D));
                    for (LivingEntity livingEntity : list) {
                        this.damage(livingEntity);
                    }
                    this.getWorld().playSound(null, this.getBlockPos(), SoundRegistry.DAY_STALKER_CHAOS_STORM, SoundCategory.HOSTILE, 1f, 1f);
                    if (this.getWorld().getBlockState(this.getBlockPos()).isAir()) {
                        this.getWorld().setBlockState(this.getBlockPos(), Blocks.FIRE.getDefaultState());
                    }
                    this.discard();
                }
            }
        }
    }

    @Override
    public void onRemoved() {
        Random random = new Random();
        double d = random.nextGaussian() * 0.05D * this.getParticleDivergence();
        double e = random.nextGaussian() * 0.05D * this.getParticleDivergence();
        double f = random.nextGaussian() * 0.05D * this.getParticleDivergence();
        for(int j = 0; j < 200 * this.getParticleMod(); ++j) {
            double newX = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d;
            double newZ = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e;
            double newY = random.nextDouble() - 0.5D + random.nextGaussian() * 0.5D + f;
            this.getWorld().addParticle(ParticleTypes.WAX_ON, this.getX(), this.getY(), this.getZ(), newX / 0.1f, newY / 0.01f, newZ / 0.1f);
            this.getWorld().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), newX / 4, newY / 0.5f, newZ / 4);
            this.getWorld().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), newX / 4, newY / 0.5f, newZ / 4);
        }
        super.onRemoved();
    }

    private void damage(LivingEntity target) {
        Entity entity = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && entity instanceof LivingEntity livingEntity && target != livingEntity) {
            if (!livingEntity.isTeammate(target)) {
                if (target.damage(this.getDamageSources().mobAttack((LivingEntity) entity), (float) this.getDamage())) {
                    target.addVelocity(0, 0.5f, 0);
                }
            }
        }
    }

    public void setRadius(float radius) {
        if (!this.getWorld().isClient) {
            this.dataTracker.set(RADIUS, MathHelper.clamp(radius, 0.0F, 32f));
        }
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

    public void setParticleDivergence(float divergence) {
        this.dataTracker.set(DIVERGENCE, divergence);
    }

    public float getParticleDivergence() {
        return this.dataTracker.get(DIVERGENCE);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Radius")) {
            this.setRadius(nbt.getFloat("Radius"));
        }
        if (nbt.contains("ParticleModifier")) {
            this.setParticleMod(nbt.getFloat("ParticleModifier"));
        }
        if (nbt.contains("ParticleDivergence")) {
            this.setParticleDivergence(nbt.getFloat("ParticleDivergence"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("Radius", this.getRadius());
        nbt.putFloat("ParticleModifier", this.getParticleMod());
        nbt.putFloat("ParticleDivergence", this.getParticleDivergence());
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
}