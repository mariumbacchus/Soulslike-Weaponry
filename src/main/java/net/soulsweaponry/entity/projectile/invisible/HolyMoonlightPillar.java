package net.soulsweaponry.entity.projectile.invisible;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;
import java.util.Random;

public class HolyMoonlightPillar extends InvisibleWarmupEntity {

    private float knockUp = ConfigConstructor.holy_moonlight_ability_knockup;
    private static final TrackedData<Float> RADIUS = DataTracker.registerData(HolyMoonlightPillar.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> PARTICLE_MOD = DataTracker.registerData(HolyMoonlightPillar.class, TrackedDataHandlerRegistry.FLOAT);

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
                    this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
                    this.getWorld().playSound(null, this.getBlockPos(), SoundRegistry.MOONLIGHT_SMALL_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                    this.discard();
                }
            }
        }
    }

    @Override
    public void onRemoved() {
        Random random = new Random();
        double d = random.nextGaussian() * 0.05D;
        double e = random.nextGaussian() * 0.05D;
        double f = random.nextGaussian() * 0.05D;
        for(int j = 0; j < 200 * this.getParticleMod(); ++j) {
            double newX = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d;
            double newZ = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e;
            double newY = random.nextDouble() - 0.5D + random.nextGaussian() * 0.5D + f;
            this.getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), newX / 2, newY / 0.5f, newZ / 2);
            this.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), newX / 2, newY / 0.5f, newZ / 2);
        }
        super.onRemoved();
    }

    private void damage(LivingEntity target) {
        Entity entity = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && entity instanceof LivingEntity livingEntity && target != livingEntity) {
            if (!livingEntity.isTeammate(target)) {
                target.damage(DamageSource.mob((LivingEntity) entity), (float) this.getDamage() + 2 * EnchantmentHelper.getAttackDamage(this.getStack(), target.getGroup()));
                target.addVelocity(0, this.getKnockup(), 0);
            }
        }
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