package net.soulsweaponry.entity.mobs;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.AnimatedDeathInterface;
import net.soulsweaponry.util.ParticleNetworking;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;

public class RimeSpectre extends Remnant implements GeoEntity, AnimatedDeathInterface {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    public int deathTicks;
    protected static final TrackedData<Boolean> CHARGING = DataTracker.registerData(RimeSpectre.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> ATTACKING = DataTracker.registerData(RimeSpectre.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> ATTACK_PARTICLE = DataTracker.registerData(RimeSpectre.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<BlockPos> POS = DataTracker.registerData(RimeSpectre.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public RimeSpectre(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new SpectreMoveControl(this);
        this.setTamed(false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new RimeSpectreGoal(this));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, 5, false, false, entity -> !this.isTamed()
                || !(this.getOwner() instanceof PlayerEntity)));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, MobEntity.class, 5, false, false,
                entity -> this.isTamed() && entity instanceof Monster && !this.isTeammate(entity)));
        this.targetSelector.add(5, new RevengeGoal(this).setGroupRevenge());
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.equals(DamageSource.FREEZE)) return false;
        if (source.isMagic()) {
            return super.damage(source, amount);
        }
        return super.damage(source, amount * 0.1f);
    }

    @Override
    public int getSoulAmount() {
        return 5;
    }

    @Override
    public void initEquip() {}

    public static DefaultAttributeContainer.Builder createSpectreAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10D);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACKING, false);
        this.dataTracker.startTracking(CHARGING, false);
        this.dataTracker.startTracking(ATTACK_PARTICLE, false);
        this.dataTracker.startTracking(POS, new BlockPos(0, 0, 0));
    }

    class SpectreMoveControl extends MoveControl {
        public SpectreMoveControl(RimeSpectre owner) {
            super(owner);
        }

        public void tick() {
            if (this.state == State.MOVE_TO) {
                Vec3d vec3d = new Vec3d(this.targetX - RimeSpectre.this.getX(), this.targetY - RimeSpectre.this.getY(), this.targetZ - RimeSpectre.this.getZ());
                double d = vec3d.length();
                if (d < RimeSpectre.this.getBoundingBox().getAverageSideLength()) {
                    this.state = State.WAIT;
                    RimeSpectre.this.setVelocity(RimeSpectre.this.getVelocity().multiply(0.5D));
                } else {
                    RimeSpectre.this.setVelocity(RimeSpectre.this.getVelocity().add(vec3d.multiply(this.speed * 0.05D / d)));
                    if (RimeSpectre.this.getTarget() == null) {
                        Vec3d vec3d2 = RimeSpectre.this.getVelocity();
                        RimeSpectre.this.setYaw(-((float) MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776F);
                    } else {
                        double e = RimeSpectre.this.getTarget().getX() - RimeSpectre.this.getX();
                        double f = RimeSpectre.this.getTarget().getZ() - RimeSpectre.this.getZ();
                        RimeSpectre.this.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
                    }
                    RimeSpectre.this.bodyYaw = RimeSpectre.this.getYaw();
                }

            }
        }
    }

    private PlayState predicate(AnimationState<?> state) {
        if (this.isDead()) {
            state.getController().setAnimation(RawAnimation.begin().then("death", Animation.LoopType.HOLD_ON_LAST_FRAME));
        } else if (this.isSitting()) {
            state.getController().setAnimation(RawAnimation.begin().then("sit", Animation.LoopType.LOOP));
        } else if (this.getShooting()) {
            state.getController().setAnimation(RawAnimation.begin().then("attack", Animation.LoopType.PLAY_ONCE));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private PlayState idle(AnimationState<?> state) {
        state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "idle", 0, this::idle));
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    public boolean getCharging() {
        return this.dataTracker.get(CHARGING);
    }

    public void setCharging(boolean charging) {
        this.dataTracker.set(CHARGING, charging);
    }

    public boolean getShooting() {
        return this.dataTracker.get(ATTACKING);
    }

    public void setShooting(boolean bl) {
        this.dataTracker.set(ATTACKING, bl);
    }

    public boolean getShootingParticle() {
        return this.dataTracker.get(ATTACK_PARTICLE);
    }

    public void setShootingParticle(boolean bl) {
        this.dataTracker.set(ATTACK_PARTICLE, bl);
    }

    public BlockPos getShootPos() {
        return this.dataTracker.get(POS);
    }

    public void setShootPos(BlockPos pos) {
        this.dataTracker.set(POS, pos);
    }

    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
        for (Entity entity : this.world.getOtherEntities(this, this.getBoundingBox().expand(6D))) {
            if (entity instanceof LivingEntity target && !this.isOwner(target) && !this.isTeammate(target)) {
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, 20, 0));
            }
        }
        if (this.hasStatusEffect(EffectRegistry.FREEZING)) this.removeStatusEffect(EffectRegistry.FREEZING);
    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movement);
        this.checkBlockCollision();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.world.addParticle(ParticleTypes.SNOWFLAKE, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
        if (this.getShootingParticle()) {
            BlockPos pos = this.getShootPos();
            double distanceToEntity = this.squaredDistanceTo(pos.getX(), pos.getY() + 0.5f, pos.getZ());
            for (int i = 0; i < 20; i++) {
                double e = pos.getX() - (this.getX());
                double f = pos.getY() + 0.5f - this.getBodyY(1.0D);
                double g = pos.getZ() - this.getZ();
                double h = Math.sqrt(Math.sqrt(distanceToEntity)) * 0.5D;
                if (this.world.isClient) {
                    world.addParticle(ParticleTypes.SNOWFLAKE, this.getX(), this.getEyeY(), this.getZ(), (e + this.getRandom().nextGaussian() * h)/4f, (f + this.getRandom().nextGaussian())/4f, (g + this.getRandom().nextGaussian() * h)/4f);
                }
            }
        }
    }

    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks >= this.getTicksUntilDeath() && !this.world.isClient()) {
            this.world.sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            if (!world.isClient) {
                ParticleNetworking.sendServerParticlePacket((ServerWorld) world, PacketRegistry.ICE_PARTICLES_ID, this.getBlockPos(), 600);
            }
            this.world.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 1f, 1f);
            this.world.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 1f, .5f);
            this.remove(RemovalReason.KILLED);
        }
    }

    static class RimeSpectreGoal extends Goal {

        private final RimeSpectre mob;
        private int attackStatus;
        private int cooldown;

        public RimeSpectreGoal(RimeSpectre mob) {
            this.setControls(EnumSet.of(Control.MOVE));
            this.mob = mob;
        }

        public boolean canStart() {
            LivingEntity target = this.mob.getTarget();
            return !this.mob.isSitting() && target != null && target.isAlive() && this.mob.canTarget(target) && this.mob.getRandom().nextInt(7) == 0;
        }

        public boolean shouldContinue() {
            return !this.mob.isSitting() && this.mob.getCharging() && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) return;
            Vec3d vec3d = livingEntity.getEyePos();
            this.mob.getMoveControl().moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            this.mob.setCharging(true);
        }

        public void stop() {
            this.mob.setCharging(false);
            this.mob.setShooting(false);
            this.mob.setShootingParticle(false);
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        private void moveRandomSpot(Vec3d vec3d) {
            this.mob.getMoveControl().moveTo(vec3d.x + this.mob.getRandom().nextInt(8) - 4, vec3d.y + this.mob.getRandom().nextInt(3), vec3d.z + this.mob.getRandom().nextInt(8) - 4, 1D);
        }

        public void tick() {
            this.cooldown = Math.max(this.cooldown - 1, 0);
            LivingEntity target = this.mob.getTarget();
            if (target != null && target.getBlockPos() != null) {
                Vec3d vec3d = target.getEyePos();
                if (this.cooldown > 0) {
                    this.moveRandomSpot(vec3d);
                }
                if (this.cooldown <= 0 && !this.mob.isInsideWall()) {
                    this.mob.setShooting(true);
                    this.frostBeam(target);
                }
            }
        }

        private void frostBeam(LivingEntity target) {
            attackStatus++;
            this.mob.setShootPos(target.getBlockPos());
            this.mob.getLookControl().lookAt(target);
            this.mob.getNavigation().stop();
            if (attackStatus >= 15) {
                this.mob.setShootingParticle(true);
                if (attackStatus % 2 == 0) {
                    Box box = new Box(target.getBlockPos(), this.mob.getBlockPos()).expand(3D);
                    for (Entity entity : this.mob.world.getOtherEntities(this.mob, box)) {
                        if (entity instanceof LivingEntity living && !this.mob.isOwner(living) && !this.mob.isTeammate(living)) {
                            living.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, 40, 2));
                            living.damage(DamageSource.mob(this.mob), 2f);
                        }
                    }
                }
            }
            if (attackStatus >= 40) {
                this.attackStatus = 0;
                this.cooldown = 40;
                this.mob.setShootingParticle(false);
                this.mob.setShooting(false);
            }
        }
    }

    @Override
    public int getTicksUntilDeath() {
        return 15;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    @Override
    public void setDeath() {}

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean isUndead() {
        return false;
    }
}