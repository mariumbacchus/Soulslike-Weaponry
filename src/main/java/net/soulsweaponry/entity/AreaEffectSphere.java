package net.soulsweaponry.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EntityRegistry;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AreaEffectSphere extends Entity implements Ownable {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final TrackedData<Float> RADIUS = DataTracker.registerData(AreaEffectSphere.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> PARTICLE_COUNT_MODIFIER = DataTracker.registerData(AreaEffectSphere.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> WAITING = DataTracker.registerData(AreaEffectSphere.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<ParticleEffect> PARTICLE_ID = DataTracker.registerData(AreaEffectSphere.class, TrackedDataHandlerRegistry.PARTICLE);
    private final List<StatusEffectInstance> effects;
    private final Map<Entity, Integer> affectedEntities;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusGrowth;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUuid;
    private boolean affectOwner = true;

    public AreaEffectSphere(EntityType<?> type, World world) {
        super(type, world);
        this.effects = Lists.newArrayList();
        this.affectedEntities = Maps.newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.noClip = true;
    }

    public AreaEffectSphere(World world, double x, double y, double z) {
        this(EntityRegistry.AREA_EFFECT_SPHERE, world);
        this.setPosition(x, y, z);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(RADIUS, 3f);
        builder.add(PARTICLE_COUNT_MODIFIER, 6.7f);
        builder.add(WAITING, false);
        builder.add(PARTICLE_ID, EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, -1));
    }

    public void setRadius(float radius) {
        if (!this.getWorld().isClient) {
            this.getDataTracker().set(RADIUS, MathHelper.clamp(radius, 0.0F, 32.0F));
        }
    }

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

    public void addEffect(StatusEffectInstance effect) {
        this.effects.add(effect);
    }

    public ParticleEffect getParticleType() {
        return this.getDataTracker().get(PARTICLE_ID);
    }

    public void setParticleType(ParticleEffect particle) {
        this.getDataTracker().set(PARTICLE_ID, particle);
    }

    protected void setWaiting(boolean waiting) {
        this.getDataTracker().set(WAITING, waiting);
    }

    public boolean isWaiting() {
        return this.getDataTracker().get(WAITING);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getParticleAmountModifier() {
        return this.getDataTracker().get(PARTICLE_COUNT_MODIFIER);
    }

    /**
     * Normally at 6.7. The total particle amount scales with radius, so this modifies the amount of them.
     */
    public void setParticleAmountModifier(float modifier) {
        this.getDataTracker().set(PARTICLE_COUNT_MODIFIER, modifier);
    }

    @Override
    public void tick() {
        super.tick();
        boolean waiting = this.isWaiting();
        float radius = this.getRadius();
        if (this.getWorld().isClient) {
            this.tickClient(waiting, radius);
            return;
        }
        this.tickServer(waiting, radius);
    }

    private void tickClient(boolean waiting, float radius) {
        if (waiting && this.random.nextBoolean()) {
            return;
        }
        int points = MathHelper.floor(radius * this.getParticleAmountModifier());
        randomParticleBox(this.getWorld(), this.getX(), this.getY() + this.getHeight() / 2f, this.getZ(), points, radius * 1.25f, this.getParticleType(), this.random);
    }

    private void tickServer(boolean wasWaiting, float radius) {
        if (this.age >= this.waitTime + this.duration) {
            this.discard();
            return;
        }
        boolean shouldWait = this.age < this.waitTime;
        if (wasWaiting != shouldWait) {
            this.setWaiting(shouldWait);
        }
        if (shouldWait) {
            return;
        }
        radius = this.applyRadiusGrowth(radius);
        if (this.isRemoved()) {
            return;
        }
        if (this.age % 5 != 0) {
            return;
        }
        this.applyEffectsToEntities(radius);
    }

    private float applyRadiusGrowth(float radius) {
        if (this.radiusGrowth == 0.0F) {
            return radius;
        }
        radius += this.radiusGrowth;
        if (radius < 0.5F) {
            this.discard();
            return radius;
        }
        this.setRadius(radius);
        return radius;
    }

    private void applyEffectsToEntities(float radius) {
        this.affectedEntities.entrySet().removeIf(entry -> this.age >= entry.getValue());
        List<StatusEffectInstance> effectsToApply = Lists.newArrayList();
        effectsToApply.addAll(this.effects);
        if (effectsToApply.isEmpty()) {
            this.affectedEntities.clear();
            return;
        }
        List<LivingEntity> entities = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox());
        for (LivingEntity entity : entities) {
            if (!this.canAffect(entity, radius)) {
                continue;
            }
            this.affectEntity(entity, effectsToApply);
            radius = this.applyRadiusOnUse(radius);
            if (this.isRemoved()) {
                return;
            }
            this.applyDurationOnUse();
            if (this.isRemoved()) {
                return;
            }
        }
    }

    private boolean canAffect(LivingEntity entity, float radius) {
        boolean ownerOrTeammate = (this.getOwner() != null && entity.isTeammate(this.getOwner())) || entity.equals(this.getOwner());
        if (this.affectedEntities.containsKey(entity) || !entity.isAffectedBySplashPotions() || (!this.shouldAffectOwner() && ownerOrTeammate)) {
            return false;
        }
        double dx = entity.getX() - this.getX();
        double dz = entity.getZ() - this.getZ();
        double distanceSquared = dx * dx + dz * dz;
        return distanceSquared <= radius * radius;
    }

    private void affectEntity(LivingEntity entity, List<StatusEffectInstance> effectsToApply) {
        this.affectedEntities.put(entity, this.age + this.reapplicationDelay);
        for (StatusEffectInstance effect : effectsToApply) {
            if (effect.getEffectType().value().isInstant()) {
                effect.getEffectType().value().applyInstantEffect(this, this.getOwner(), entity, effect.getAmplifier(), 0.5);
            } else {
                entity.addStatusEffect(new StatusEffectInstance(effect), this);
            }
        }
    }

    private float applyRadiusOnUse(float radius) {
        if (this.radiusOnUse == 0.0F) {
            return radius;
        }
        radius += this.radiusOnUse;
        if (radius < 0.5F) {
            this.discard();
            return radius;
        }
        this.setRadius(radius);
        return radius;
    }

    private void applyDurationOnUse() {
        if (this.durationOnUse == 0) {
            return;
        }
        this.duration += this.durationOnUse;
        if (this.duration <= 0) {
            this.discard();
        }
    }

    public static void randomParticleBox(World world, double x, double y, double z, double points, float sizeModifier, ParticleEffect particle, Random random) {
        double phi = Math.PI * (3. - Math.sqrt(5.));
        for (int i = 0; i < points; i++) {
            double velocityY = 1 - (i/(points - 1)) * 2;
            double radius = Math.sqrt(1 - velocityY*velocityY);
            double theta = phi * i;
            double velocityX = Math.cos(theta) * radius;
            double velocityZ = Math.sin(theta) * radius;
            world.addParticle(particle, true, x + velocityX*sizeModifier + random.nextGaussian(),
                    y + velocityY*sizeModifier + random.nextGaussian(), z + velocityZ*sizeModifier + random.nextGaussian(),
                    0, 0, 0);
        }
    }

    public float getRadiusOnUse() {
        return this.radiusOnUse;
    }

    public void setRadiusOnUse(float radiusOnUse) {
        this.radiusOnUse = radiusOnUse;
    }

    public float getRadiusGrowth() {
        return this.radiusGrowth;
    }

    public void setRadiusGrowth(float radiusGrowth) {
        this.radiusGrowth = radiusGrowth;
    }

    public int getDurationOnUse() {
        return this.durationOnUse;
    }

    public void setDurationOnUse(int durationOnUse) {
        this.durationOnUse = durationOnUse;
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUuid != null && this.getWorld() instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.getWorld()).getEntity(this.ownerUuid);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }
        return this.owner;
    }

    public void setAffectOwner(boolean affectOwner) {
        this.affectOwner = affectOwner;
    }

    public boolean shouldAffectOwner() {
        return this.affectOwner;
    }

    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.age = nbt.getInt("Age");
        this.duration = nbt.getInt("Duration");
        this.waitTime = nbt.getInt("WaitTime");
        this.reapplicationDelay = nbt.getInt("ReapplicationDelay");
        this.durationOnUse = nbt.getInt("DurationOnUse");
        this.radiusOnUse = nbt.getFloat("RadiusOnUse");
        this.radiusGrowth = nbt.getFloat("RadiusPerTick");
        this.setRadius(nbt.getFloat("Radius"));
        this.setParticleAmountModifier(nbt.getFloat("ParticleAmountMod"));
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
        RegistryOps<NbtElement> registryOps = this.getRegistryManager().getOps(NbtOps.INSTANCE);
        if (nbt.contains("Particle", NbtElement.COMPOUND_TYPE)) {
            ParticleTypes.TYPE_CODEC
                    .parse(registryOps, nbt.get("Particle"))
                    .resultOrPartial(string -> LOGGER.warn("Failed to parse area effect cloud particle options: '{}'", string))
                    .ifPresent(this::setParticleType);
        }
        if (nbt.contains("Effects", 9)) {
            NbtList nbtList = nbt.getList("Effects", 10);
            this.effects.clear();

            for(int i = 0; i < nbtList.size(); ++i) {
                StatusEffectInstance statusEffectInstance = StatusEffectInstance.fromNbt(nbtList.getCompound(i));
                if (statusEffectInstance != null) {
                    this.addEffect(statusEffectInstance);
                }
            }
        }
        this.setAffectOwner(nbt.getBoolean("AffectOwner"));
    }

    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Age", this.age);
        nbt.putInt("Duration", this.duration);
        nbt.putInt("WaitTime", this.waitTime);
        nbt.putInt("ReapplicationDelay", this.reapplicationDelay);
        nbt.putInt("DurationOnUse", this.durationOnUse);
        nbt.putFloat("RadiusOnUse", this.radiusOnUse);
        nbt.putFloat("RadiusPerTick", this.radiusGrowth);
        nbt.putFloat("Radius", this.getRadius());
        nbt.putFloat("ParticleAmountMod", this.getParticleAmountModifier());
        RegistryOps<NbtElement> registryOps = this.getRegistryManager().getOps(NbtOps.INSTANCE);
        nbt.put("Particle", ParticleTypes.TYPE_CODEC.encodeStart(registryOps, this.getParticleType()).getOrThrow());
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
        if (!this.effects.isEmpty()) {
            NbtList nbtList = new NbtList();
            for (StatusEffectInstance statusEffectInstance : this.effects) {
                nbtList.add(statusEffectInstance.writeNbt());
            }
            nbt.put("Effects", nbtList);
        }
        nbt.putBoolean("AffectOwner", this.shouldAffectOwner());
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (RADIUS.equals(data)) {
            this.calculateDimensions();
        }
        super.onTrackedDataSet(data);
    }

    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(this.getRadius() * 2.0F, this.getRadius() * 2.0F);
    }
}
