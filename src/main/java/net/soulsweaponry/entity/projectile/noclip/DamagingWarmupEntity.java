package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.BiConsumer;

public abstract class DamagingWarmupEntity extends NoClipWarmupEntity {

    private boolean startedAttack;
    private int ticksLeft = 20;
    private static final TrackedData<Boolean> EMERGE = DataTracker.registerData(DamagingWarmupEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> PARTICLE_MOD = DataTracker.registerData(DamagingWarmupEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> EVENT_ID = DataTracker.registerData(DamagingWarmupEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private DamagingWarmupEntityEvents.OtherAttributes otherAttributes = new DamagingWarmupEntityEvents.OtherAttributes();

    public DamagingWarmupEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.ticksLeft = this.getMaxTicks();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            this.reduceWarmup(1);
            if (this.getWarmup() < 0) {
                if (this.getWarmup() == -7) {
                    this.setEmerge(true);
                    List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.2D));
                    for (LivingEntity livingEntity : list) {
                        if (!livingEntity.isAlive() || livingEntity.isInvulnerable()) {
                            continue;
                        }
                        if (this.getOwner() != null && (livingEntity.isTeammate(this.getOwner()) || this.isOwner(livingEntity))) {
                            continue;
                        }
                        boolean wasHit;
                        if (this.getOwner() instanceof LivingEntity owner) {
                            wasHit = livingEntity.damage(this.getWorld().getDamageSources().mobProjectile(this, owner),
                                    (float) this.getDamage() + this.getBonusDamage(livingEntity));
                        } else {
                            wasHit = livingEntity.damage(this.getWorld().getDamageSources().mobProjectile(this, null),
                                    (float) this.getDamage() + this.getBonusDamage(livingEntity));
                        }
                        this.applyDamageEffects(wasHit, livingEntity);
                    }
                    this.onTrigger();
                }
                if (!this.startedAttack) {
                    this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_ATTACK_SOUND);
                    this.startedAttack = true;
                }
                if (--this.ticksLeft < 0) {
                    this.discard();
                }
            }
        }
    }

    /**
     * Is by default 20. Override this to make the entities last longer so any animations can finish.
     */
    public int getMaxTicks() {
        return 20;
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
        if (status == EntityStatuses.PLAY_ATTACK_SOUND) {
            if (!this.isSilent()) {
                this.handleSoundStatus(status);
            }
        }
    }

    public void setEmerge(boolean bl) {
        this.dataTracker.set(EMERGE, bl);
    }

    /**
     * Data-tracked boolean set to true at the same time as damaging effects are triggered.
     */
    public boolean getEmerge() {
        return this.dataTracker.get(EMERGE);
    }

    public void setParticleAmountMod(float particleMod) {
        this.dataTracker.set(PARTICLE_MOD, particleMod);
    }

    public float getParticleAmountMod() {
        return this.dataTracker.get(PARTICLE_MOD);
    }

    /**
     * Set the event id that can be used in the {@link DamagingWarmupEntityEvents#EVENTS} map to get the consumer/actual logic to the event.
     */
    public void setEventId(int eventId) {
        this.dataTracker.set(EVENT_ID, eventId);
    }

    /**
     * Get the event id that can be used in the {@link DamagingWarmupEntityEvents#EVENTS} map to get the consumer/actual logic to the event.
     */
    public int getEventId() {
        return this.dataTracker.get(EVENT_ID);
    }

    /**
     * Used in the {@link DamagingWarmupEntityEvents#EVENTS} {@link BiConsumer} to give additional values to the stuff the event does, like for example
     * providing damage and radius values to the {@link MoltenMetal} entities spawned in the {@link DamagingWarmupEntityEvents#SPAWN_MOLTEN_METAL}
     * event without relying on the original {@link DamagingWarmupEntity}
     */
    public void setOtherAttributes(DamagingWarmupEntityEvents.OtherAttributes otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

    /**
     * Used in the {@link DamagingWarmupEntityEvents#EVENTS} {@link BiConsumer} to give additional values to the stuff the event does, like for example
     * providing damage and radius values to the {@link MoltenMetal} entities spawned in the {@link DamagingWarmupEntityEvents#SPAWN_MOLTEN_METAL}
     * event without relying on the original {@link DamagingWarmupEntity}
     */
    public DamagingWarmupEntityEvents.OtherAttributes getOtherAttributes() {
        return otherAttributes;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(EMERGE, false);
        this.dataTracker.startTracking(PARTICLE_MOD, 1f);
        this.dataTracker.startTracking(EVENT_ID, -1);
    }

    /**
     * Trigger custom sounds when damaging effects are applied.
     */
    public abstract void handleSoundStatus(byte status);

    /**
     * Apply other effects such as knockup or status effects at the same time as damaging effects are triggered.
     */
    public abstract void applyDamageEffects(boolean wasHit, LivingEntity target);

    /**
     * Called on server side at the same time as damaging effects are triggered.
     */
    public void onTrigger() {
        BiConsumer<DamagingWarmupEntity, DamagingWarmupEntityEvents.OtherAttributes> consumer = DamagingWarmupEntityEvents.EVENTS.get(this.getEventId());
        if (consumer != null) {
            consumer.accept(this, this.otherAttributes);
        }
    }

    /**
     * Override this if additional damage should be added based on the target, for example if it is undead.
     */
    public float getBonusDamage(LivingEntity target) {
        return 0f;
    }

    /**
     * Can be used to spawn particles upwards initially, override this to add own values to modify particle spread.
     */
    public Vec3d getParticleVec() {
        return new Vec3d(1.2f, 0.34f, 1.2f);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ParticleModifier")) {
            this.setParticleAmountMod(nbt.getFloat("ParticleModifier"));
        }
        if (nbt.contains("EventId")) {
            this.setEventId(nbt.getInt("EventId"));
        }
        this.otherAttributes = DamagingWarmupEntityEvents.OtherAttributes.getInstanceFromNbt(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("ParticleModifier", this.getParticleAmountMod());
        nbt.putInt("EventId", this.getEventId());
        this.otherAttributes.writeCustomDataToNbt(nbt);
    }
}
