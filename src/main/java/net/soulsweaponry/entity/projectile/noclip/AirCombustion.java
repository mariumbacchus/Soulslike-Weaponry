package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entity.mobs.boss.DayStalker;
import net.soulsweaponry.entity.mobs.boss.NightProwler;
import net.soulsweaponry.entity.mobs.WarmthEntity;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;

public class AirCombustion extends DamagingWarmupEntity {

    private boolean isEmpowered;
    private boolean playedSpawnSound = false;
    public static final TrackedData<Boolean> SPAWN_WARN_PARTICLES = DataTracker.registerData(AirCombustion.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<List<ParticleEffect>> PARTICLES = DataTracker.registerData(AirCombustion.class, TrackedDataHandlerRegistry.PARTICLE_LIST);
    public static final TrackedData<Integer> EXPLOSION_PARTICLE_AMOUNT = DataTracker.registerData(AirCombustion.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Float> VOLUME = DataTracker.registerData(AirCombustion.class, TrackedDataHandlerRegistry.FLOAT);

    public AirCombustion(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public AirCombustion(World world, LivingEntity livingEntity, float damage, float radius, int warmup, float volume) {
        super(EntityRegistry.AIR_COMBUSTION, world);
        this.setOwner(livingEntity);
        this.setDamage(damage);
        this.setRadius(radius);
        this.setWarmup(warmup);
        this.setVolume(volume);
    }

    public AirCombustion(World world, LivingEntity livingEntity, float damage, float radius, int warmup) {
        this(world, livingEntity, damage, radius, warmup, 1f);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SPAWN_WARN_PARTICLES, true);
        builder.add(PARTICLES, List.of(ParticleTypes.LARGE_SMOKE, ParticleTypes.FLAME, ParticleTypes.SMALL_FLAME));
        builder.add(EXPLOSION_PARTICLE_AMOUNT, 150);
        builder.add(VOLUME, 1f);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient){
            if (!this.playedSpawnSound) {
                this.playedSpawnSound = true;
                this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, this.getSoundCategory(), this.getVolume(), 1f, true);
                this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, this.getSoundCategory(), this.getVolume(), 1f, true);
            }
            if (this.shouldSpawnWarnParticles()) {
                ParticleHandler.particleOutburst(this.getWorld(), 20, this.getX(), this.getBodyY(0.5f), this.getZ(), ParticleTypes.FLAME, new Vec3d(15 ,15 ,15), 1f);
            }
        }
    }

    @Override
    public void handleSoundStatus(byte status) {
        this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundRegistry.DEEP_EXPLOSION, this.getSoundCategory(), this.getVolume(), 1f, true);
    }

    @Override
    public void applyDamageEffects(boolean wasHit, LivingEntity target) {
        if (target instanceof WarmthEntity) {
            return;
        }
        if (this.getWorld() instanceof ServerWorld serverWorld && this.getOwner() instanceof DayStalker stalker) {
            NightProwler prowler = stalker.getPartner(serverWorld);
            if (target.equals(prowler)) {
                return;
            }
        }
        DamageSource source = this.getOwner() instanceof LivingEntity living ? this.getWorld().getDamageSources().mobAttack(living) : this.getWorld().getDamageSources().mobProjectile(this, null);
        target.damage(source, (float) this.getDamage());
        if (this.isEmpowered()) {
            target.setOnFireFor(4);
        }
    }

    @Override
    public void onTrigger() {
        super.onTrigger();
        ParticleEvents.airCombustionEvent(this.getWorld(), this.getX(), this.getBodyY(0.5f), this.getZ(), this.getExplosionParticles(), this.getExplosionParticleAmount());
    }

    public boolean isEmpowered() {
        return isEmpowered;
    }

    public void setEmpowered(boolean empowered) {
        isEmpowered = empowered;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("isEmpowered")) {
            this.setEmpowered(nbt.getBoolean("isEmpowered"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("isEmpowered", this.isEmpowered);
    }

    public void setSpawnWarnParticles(boolean bl) {
        this.dataTracker.set(SPAWN_WARN_PARTICLES, bl);
    }

    public boolean shouldSpawnWarnParticles() {
        return this.dataTracker.get(SPAWN_WARN_PARTICLES);
    }

    public List<ParticleEffect> getExplosionParticles() {
        return this.dataTracker.get(PARTICLES);
    }

    public void setExplosionParticles(ParticleEffect... particles) {
        this.dataTracker.set(PARTICLES, List.of(particles));
    }

    public int getExplosionParticleAmount() {
        return this.dataTracker.get(EXPLOSION_PARTICLE_AMOUNT);
    }

    public void setExplosionParticleAmount(int amount) {
        this.dataTracker.set(EXPLOSION_PARTICLE_AMOUNT, amount);
    }

    public void setVolume(float volume) {
        this.dataTracker.set(VOLUME, volume);
    }

    public float getVolume() {
        return this.dataTracker.get(VOLUME);
    }
}
