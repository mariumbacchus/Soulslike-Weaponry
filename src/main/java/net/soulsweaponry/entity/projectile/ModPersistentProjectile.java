package net.soulsweaponry.entity.projectile;

import com.mojang.logging.LogUtils;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryOps;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ParticleRegistry;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * Parent class containing common variables & fields that may be used by the child, such as
 * particles, bounding box size, max age, etc.
 */
public abstract class ModPersistentProjectile extends PersistentProjectileEntity {

    // NB: Variables that are used client side (i.e. particle types, particle count, etc.) NEED to be data tracked! maxAge is used server side only so that's fine
    private static final Logger LOGGER = LogUtils.getLogger();
    private int maxAge;
    private boolean allowArrowSticking;
    private static final TrackedData<Float> WIDTH = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> DESPAWN_PARTICLE_COUNT = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Byte> TRAIL_PARTICLE_COUNT = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> AREA_PARTICLE_COUNT = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Float> DESPAWN_PARTICLE_EXPANSION = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<ParticleEffect> DESPAWN_PARTICLE = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.PARTICLE);
    private static final TrackedData<ParticleEffect> TRAIL_PARTICLE = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.PARTICLE);
    private static final TrackedData<ParticleEffect> AREA_PARTICLE = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.PARTICLE);

    public ModPersistentProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setBoundingBoxWidth(entityType.getDimensions().width());
        this.setBoundingBoxHeight(entityType.getDimensions().height());
    }

    public ModPersistentProjectile(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack projectileStack, @Nullable ItemStack weapon) {
        super(type, owner, world, projectileStack, weapon);
        this.setBoundingBoxWidth(type.getDimensions().width());
        this.setBoundingBoxHeight(type.getDimensions().height());
    }

    public ModPersistentProjectile(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack projectileStack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, projectileStack, weapon);
        this.setBoundingBoxWidth(type.getDimensions().width());
        this.setBoundingBoxHeight(type.getDimensions().height());
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(WIDTH, 1.85f);
        builder.add(HEIGHT, 1.85f);
        builder.add(DESPAWN_PARTICLE_COUNT, 75);
        builder.add(TRAIL_PARTICLE_COUNT, (byte) 4);
        builder.add(AREA_PARTICLE_COUNT, (byte) 0);
        builder.add(DESPAWN_PARTICLE_EXPANSION, 0.125f);
        builder.add(DESPAWN_PARTICLE, ParticleTypes.SOUL_FIRE_FLAME);
        builder.add(TRAIL_PARTICLE, ParticleTypes.GLOW);
        builder.add(AREA_PARTICLE, ParticleRegistry.NIGHTFALL_PARTICLE);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (WIDTH.equals(data) || HEIGHT.equals(data)) {
            this.calculateDimensions();
        }
        super.onTrackedDataSet(data);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(this.getBoundingBoxWidth(), this.getBoundingBoxHeight());
    }

    @Override
    public void calculateDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.calculateDimensions();
        this.setPosition(d, e, f);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("BoundingBoxWidth")) {
            this.setBoundingBoxWidth(nbt.getFloat("BoundingBoxWidth"));
        }
        if (nbt.contains("BoundingBoxHeight")) {
            this.setBoundingBoxHeight(nbt.getFloat("BoundingBoxHeight"));
        }
        if (nbt.contains("MaxAge")) {
            this.maxAge = nbt.getInt("MaxAge");
        }

        RegistryOps<NbtElement> registryOps = this.getRegistryManager().getOps(NbtOps.INSTANCE);
        if (nbt.contains("AreaParticle", NbtElement.COMPOUND_TYPE)) {
            ParticleTypes.TYPE_CODEC
                    .parse(registryOps, nbt.get("AreaParticle"))
                    .resultOrPartial(string -> LOGGER.warn("Failed to parse soulsweapons projectile AreaParticle options: '{}'", string))
                    .ifPresent(this::setAreaParticle);
        }
        if (nbt.contains("TrailParticle", NbtElement.COMPOUND_TYPE)) {
            ParticleTypes.TYPE_CODEC
                    .parse(registryOps, nbt.get("TrailParticle"))
                    .resultOrPartial(string -> LOGGER.warn("Failed to parse soulsweapons projectile TrailParticle options: '{}'", string))
                    .ifPresent(this::setTrailParticle);
        }
        if (nbt.contains("DespawnParticle", NbtElement.COMPOUND_TYPE)) {
            ParticleTypes.TYPE_CODEC
                    .parse(registryOps, nbt.get("DespawnParticle"))
                    .resultOrPartial(string -> LOGGER.warn("Failed to parse soulsweapons projectile DespawnParticle options: '{}'", string))
                    .ifPresent(this::setDespawnParticle);
        }
        if (nbt.contains("AreaParticleCount")) {
            this.setAreaParticleCount(nbt.getByte("AreaParticleCount"));
        }
        if (nbt.contains("TrailParticleCount")) {
            this.setTrailParticleCount(nbt.getByte("TrailParticleCount"));
        }
        if (nbt.contains("DespawnParticleCount")) {
            this.setDespawnParticleCount(nbt.getInt("DespawnParticleCount"));
        }
        if (nbt.contains("DespawnParticleExpansion")) {
            this.setDespawnParticleExpansion(nbt.getFloat("DespawnParticleExpansion"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("BoundingBoxWidth", this.getBoundingBoxWidth());
        nbt.putFloat("BoundingBoxHeight", this.getBoundingBoxHeight());
        nbt.putInt("MaxAge", this.maxAge);
        RegistryOps<NbtElement> registryOps = this.getRegistryManager().getOps(NbtOps.INSTANCE);
        nbt.put("AreaParticle", ParticleTypes.TYPE_CODEC.encodeStart(registryOps, this.getAreaParticle()).getOrThrow());
        nbt.put("TrailParticle", ParticleTypes.TYPE_CODEC.encodeStart(registryOps, this.getTrailParticle()).getOrThrow());
        nbt.put("DespawnParticle", ParticleTypes.TYPE_CODEC.encodeStart(registryOps, this.getDespawnParticle()).getOrThrow());
        nbt.putByte("AreaParticleCount", this.getAreaParticleCount());
        nbt.putByte("TrailParticleCount", this.getTrailParticleCount());
        nbt.putInt("DespawnParticleCount", this.getDespawnParticleCount());
        nbt.putFloat("DespawnParticleExpansion", this.getDespawnParticleExpansion());
    }

    public void setRadius(float radius) {
        this.dataTracker.set(WIDTH, radius);
        this.dataTracker.set(HEIGHT, radius);
    }

    public float getBoundingBoxWidth() {
        return this.dataTracker.get(WIDTH);
    }

    public float getBoundingBoxHeight() {
        return this.dataTracker.get(HEIGHT);
    }

    public void setBoundingBoxWidth(float width) {
        this.dataTracker.set(WIDTH, width);
    }

    public void setBoundingBoxHeight(float height) {
        this.dataTracker.set(HEIGHT, height);
    }

    public float getRadius() {
        return Math.max(this.getBoundingBoxWidth(), this.getBoundingBoxHeight());
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public byte getAreaParticleCount() {
        return this.dataTracker.get(AREA_PARTICLE_COUNT);
    }

    /**
     * @param areaParticleCount max 127 particles
     */
    public void setAreaParticleCount(byte areaParticleCount) {
        this.dataTracker.set(AREA_PARTICLE_COUNT, areaParticleCount);
    }

    /**
     * Particle to be spawned around the entity continuously
     */
    public ParticleEffect getAreaParticle() {
        return this.dataTracker.get(AREA_PARTICLE);
    }

    /**
     * Particle to be spawned around the entity continuously
     */
    public void setAreaParticle(ParticleEffect areaParticle) {
        this.dataTracker.set(AREA_PARTICLE, areaParticle);
    }

    public byte getTrailParticleCount() {
        return this.dataTracker.get(TRAIL_PARTICLE_COUNT);
    }

    /**
     * @param trailParticleCount max 127 particles
     */
    public void setTrailParticleCount(byte trailParticleCount) {
        this.dataTracker.set(TRAIL_PARTICLE_COUNT, trailParticleCount);
    }

    /**
     * Particle to be spawned behind the entity as it flies
     */
    public ParticleEffect getTrailParticle() {
        return this.dataTracker.get(TRAIL_PARTICLE);
    }

    /**
     * Particle to be spawned behind the entity as it flies
     */
    public void setTrailParticle(ParticleEffect trailParticle) {
        this.dataTracker.set(TRAIL_PARTICLE, trailParticle);
    }

    public int getDespawnParticleCount() {
        return this.dataTracker.get(DESPAWN_PARTICLE_COUNT);
    }

    public void setDespawnParticleCount(int despawnParticleCount) {
        this.dataTracker.set(DESPAWN_PARTICLE_COUNT, despawnParticleCount);
    }

    /**
     * Particle to be spawned as the entity despawns
     */
    public ParticleEffect getDespawnParticle() {
        return this.dataTracker.get(DESPAWN_PARTICLE);
    }

    /**
     * Particle to be spawned as the entity despawns
     */
    public void setDespawnParticle(ParticleEffect despawnParticle) {
        this.dataTracker.set(DESPAWN_PARTICLE, despawnParticle);
    }

    /**
     * Modifier that can be used to increase the expansion of the particle spread as the entity despawns, like expand the particle explosion that may appear
     */
    public float getDespawnParticleExpansion() {
        return this.dataTracker.get(DESPAWN_PARTICLE_EXPANSION);
    }

    /**
     * Modifier that can be used to increase the expansion of the particle spread as the entity despawns, like expand the particle explosion that may appear
     */
    public void setDespawnParticleExpansion(float despawnParticleExpansion) {
        this.dataTracker.set(DESPAWN_PARTICLE_EXPANSION, despawnParticleExpansion);
    }

    /**
     * @return Whether arrows should be visually stuck to the player when this projectile lands
     */
    public boolean shouldAllowArrowSticking() {
        return this.allowArrowSticking;
    }

    public void setAllowArrowSticking(boolean allowArrowSticking) {
        this.allowArrowSticking = allowArrowSticking;
    }
}
