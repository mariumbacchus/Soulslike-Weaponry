package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entity.mobs.DayStalker;
import net.soulsweaponry.entity.mobs.NightProwler;
import net.soulsweaponry.entity.mobs.WarmthEntity;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;

public class AirCombustion extends DamagingWarmupEntity {

    private boolean isEmpowered;
    private static final TrackedData<Float> RADIUS = DataTracker.registerData(AirCombustion.class, TrackedDataHandlerRegistry.FLOAT);

    public AirCombustion(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public AirCombustion(World world, LivingEntity livingEntity, float damage, float radius, int warmup) {
        super(EntityRegistry.AIR_COMBUSTION, world);
        this.setOwner(livingEntity);
        this.setDamage(damage);
        this.setRadius(radius);
        this.setWarmup(warmup);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            ParticleHandler.particleOutburst(this.getWorld(), 20, this.getX(), this.getBodyY(0.5f), this.getZ(), ParticleTypes.FLAME, new Vec3d(15 ,15 ,15), 1f);
        }
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, this.getSoundCategory(), 1f, 1f, true);
        this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, this.getSoundCategory(), 1f, 1f, true);
    }

    @Override
    public void handleSoundStatus(byte status) {
        this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, this.getSoundCategory(), 1f, 1f, true);
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
        ParticleEvents.airCombustionEvent(this.getWorld(), this.getX(), this.getBodyY(0.5f), this.getZ());
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
        if (nbt.contains("Radius")) {
            this.setRadius(nbt.getFloat("Radius"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("isEmpowered", this.isEmpowered);
        nbt.putFloat("Radius", this.getRadius());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(RADIUS, 1.0F);
    }

    public void setRadius(float radius) {
        if (!this.getWorld().isClient) {
            this.getDataTracker().set(RADIUS, MathHelper.clamp(radius, 0.0F, 32.0F));
        }
    }

    @Override
    public void calculateDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.calculateDimensions();
        this.setPosition(d, e, f);
    }

    public float getRadius() {
        return this.getDataTracker().get(RADIUS);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (RADIUS.equals(data)) {
            this.calculateDimensions();
        }
        super.onTrackedDataSet(data);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(this.getRadius(), this.getRadius());
    }
}
