package net.soulsweaponry.entity.projectile;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.command.argument.ParticleEffectArgumentType;
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
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ParticleRegistry;
import org.slf4j.Logger;

/**
 * Parent class containing common variables & fields that may be used by the child, such as
 * particles, stack, bounding box size, max age, etc.
 */
public abstract class ModPersistentProjectile extends PersistentProjectileEntity {

    // NB: Variables that are used client side (i.e. particle types, particle count, etc.) NEED to be data tracked! maxAge is used server side only so that's fine
    private static final Logger LOGGER = LogUtils.getLogger();
    private int maxAge;
    private static final TrackedData<Float> WIDTH = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> DESPAWN_PARTICLE_COUNT = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TRAIL_PARTICLE_COUNT = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> AREA_PARTICLE_COUNT = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> DESPAWN_PARTICLE_EXPANSION = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<ParticleEffect> DESPAWN_PARTICLE = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.PARTICLE);
    private static final TrackedData<ParticleEffect> TRAIL_PARTICLE = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.PARTICLE);
    private static final TrackedData<ParticleEffect> AREA_PARTICLE = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.PARTICLE);
    private static final TrackedData<ItemStack> STACK = DataTracker.registerData(ModPersistentProjectile.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public ModPersistentProjectile(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
        super(type, owner, world);
        this.setBoundingBoxWidth(type.getDimensions().width);
        this.setBoundingBoxHeight(type.getDimensions().height);
    }

    public ModPersistentProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setBoundingBoxWidth(entityType.getDimensions().width);
        this.setBoundingBoxHeight(entityType.getDimensions().height);
    }

    public ModPersistentProjectile(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
        this.setBoundingBoxWidth(type.getDimensions().width);
        this.setBoundingBoxHeight(type.getDimensions().height);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(WIDTH, 1.85f);
        this.dataTracker.startTracking(HEIGHT, 1.85f);
        this.dataTracker.startTracking(DESPAWN_PARTICLE_COUNT, 75);
        this.dataTracker.startTracking(TRAIL_PARTICLE_COUNT, 4);
        this.dataTracker.startTracking(AREA_PARTICLE_COUNT, 0);
        this.dataTracker.startTracking(DESPAWN_PARTICLE_EXPANSION, 0.125f);
        this.dataTracker.startTracking(DESPAWN_PARTICLE, ParticleTypes.SOUL_FIRE_FLAME);
        this.dataTracker.startTracking(TRAIL_PARTICLE, ParticleTypes.GLOW);
        this.dataTracker.startTracking(AREA_PARTICLE, ParticleRegistry.NIGHTFALL_PARTICLE);
        this.dataTracker.startTracking(STACK, ItemStack.EMPTY);
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
        if (nbt.contains("Stack", NbtElement.COMPOUND_TYPE)) {
            this.setItemStack(ItemStack.fromNbt(nbt.getCompound("Stack")));
        }
        if (nbt.contains("BoundingBoxWidth")) {
            this.setBoundingBoxWidth(nbt.getFloat("BoundingBoxWidth"));
        }
        if (nbt.contains("BoundingBoxHeight")) {
            this.setBoundingBoxHeight(nbt.getFloat("BoundingBoxHeight"));
        }
        if (nbt.contains("MaxAge")) {
            this.maxAge = nbt.getInt("MaxAge");
        }
        if (nbt.contains("AreaParticle", 8)) {
            try {
                this.setAreaParticle(ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("AreaParticle")), Registries.PARTICLE_TYPE.getReadOnlyWrapper()));
            } catch (CommandSyntaxException var5) {
                LOGGER.warn("Couldn't load custom particle {}", nbt.getString("AreaParticle"), var5);
            }
        }
        if (nbt.contains("TrailParticle", 8)) {
            try {
                this.setTrailParticle(ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("TrailParticle")), Registries.PARTICLE_TYPE.getReadOnlyWrapper()));
            } catch (CommandSyntaxException var5) {
                LOGGER.warn("Couldn't load custom particle {}", nbt.getString("TrailParticle"), var5);
            }
        }
        if (nbt.contains("DespawnParticle", 8)) {
            try {
                this.setDespawnParticle(ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("DespawnParticle")), Registries.PARTICLE_TYPE.getReadOnlyWrapper()));
            } catch (CommandSyntaxException var5) {
                LOGGER.warn("Couldn't load custom particle {}", nbt.getString("DespawnParticle"), var5);
            }
        }
        if (nbt.contains("AreaParticleCount")) {
            this.setAreaParticleCount(nbt.getInt("AreaParticleCount"));
        }
        if (nbt.contains("TrailParticleCount")) {
            this.setTrailParticleCount(nbt.getInt("TrailParticleCount"));
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
        if (this.asItemStack() != null) {
            nbt.put("Stack", this.asItemStack().writeNbt(new NbtCompound()));
        }
        nbt.putFloat("BoundingBoxWidth", this.getBoundingBoxWidth());
        nbt.putFloat("BoundingBoxHeight", this.getBoundingBoxHeight());
        nbt.putInt("MaxAge", this.maxAge);
        nbt.putString("AreaParticle", this.getAreaParticle().asString());
        nbt.putString("TrailParticle", this.getTrailParticle().asString());
        nbt.putString("DespawnParticle", this.getDespawnParticle().asString());
        nbt.putInt("AreaParticleCount", this.getAreaParticleCount());
        nbt.putInt("TrailParticleCount", this.getTrailParticleCount());
        nbt.putInt("DespawnParticleCount", this.getDespawnParticleCount());
        nbt.putFloat("DespawnParticleExpansion", this.getDespawnParticleExpansion());
    }

    @Override
    protected ItemStack asItemStack() {
        return this.dataTracker.get(STACK);
    }

    public void setItemStack(ItemStack stackShotFrom) {
        this.dataTracker.set(STACK, stackShotFrom == null ? ItemStack.EMPTY : stackShotFrom.copy());
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

    public int getAreaParticleCount() {
        return this.dataTracker.get(AREA_PARTICLE_COUNT);
    }

    public void setAreaParticleCount(int areaParticleCount) {
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

    public int getTrailParticleCount() {
        return this.dataTracker.get(TRAIL_PARTICLE_COUNT);
    }

    public void setTrailParticleCount(int trailParticleCount) {
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
}
