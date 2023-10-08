package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.UUID;

/**
 * NOTE: Geckolib can't seem to render animations uniquely/distinctively from each other unless the entity extends
 * MobEntity or LivingEntity. If this class only extends Entity, it will render each entity in minecraft with
 * conflicting animations (trying to play different animations for each entity). <p></p>
 * Why this is the case? No clue and well, the only thing I care about is whether this works or not, so I've got no
 * reason to look for an answer or fix either.
 */
public class NightsEdge extends PathAwareEntity implements Ownable, GeoEntity {
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUuid;
    private int warmup;
    public int maxTicks = 15;
    private int ticksLeft = maxTicks;
    private float damage = 15f;
    private boolean startedAttack;
    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private static final TrackedData<Boolean> EMERGE = DataTracker.registerData(NightsEdge.class, TrackedDataHandlerRegistry.BOOLEAN);

    public NightsEdge(EntityType<? extends NightsEdge> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    public void setWarmup(int warmup) {
        this.warmup = warmup;
    }

    public int getWarmup() {
        return this.warmup;
    }

    /**
     * Filler attributes that must exist due to this being a MobEntity class.
     * @return Filler attributes
     */
    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 120D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 10D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.8D);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            this.setWarmup(this.getWarmup() - 1);
            if (this.getWarmup() < 0) {
                this.setEmerge(true);
                if (this.getWarmup() == -7) {
                    List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.2D));
                    for (LivingEntity livingEntity : list) {
                        this.damage(livingEntity);
                    }
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

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    private void damage(LivingEntity target) {
        LivingEntity livingEntity = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && target != livingEntity) {
            if (livingEntity == null) {
                target.damage(this.getDamageSources().magic(), this.getDamage());
            } else {
                if (!livingEntity.isTeammate(target)) {
                    target.damage(this.getDamageSources().indirectMagic(this, livingEntity), this.getDamage());
                }
            }
        }
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
        if (status == EntityStatuses.PLAY_ATTACK_SOUND) {
            if (!this.isSilent()) {
                this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_EVOKER_FANGS_ATTACK, this.getSoundCategory(), 1.0f, this.random.nextFloat() * 0.2f + 0.85f, false);
            }
        }
    }

    private float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(EMERGE, false);
    }

    private void setEmerge(boolean bl) {
        this.dataTracker.set(EMERGE, bl);
    }

    private boolean getEmerge() {
        return this.dataTracker.get(EMERGE);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Warmup")) {
            this.warmup = nbt.getInt("Warmup");
        }
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
        if (nbt.contains("Damage")) {
            this.damage = nbt.getFloat("Damage");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Warmup", this.warmup);
        nbt.putFloat("Damage", this.damage);
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }

    @Override
    @Nullable
    public LivingEntity getOwner() {
        Entity entity;
        if (this.owner == null && this.ownerUuid != null && this.getWorld() instanceof ServerWorld && (entity = ((ServerWorld)this.getWorld()).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (LivingEntity)entity;
        }
        return this.owner;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    private PlayState idle(AnimationState<?> state) {
        if (this.getEmerge()) {
            state.getController().setAnimation(RawAnimation.begin().then("emerge", Animation.LoopType.HOLD_ON_LAST_FRAME));
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
