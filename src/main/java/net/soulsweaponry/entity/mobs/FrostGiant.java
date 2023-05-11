package net.soulsweaponry.entity.mobs;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.soulsweaponry.items.LeviathanAxe;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.AnimatedDeathInterface;
import net.soulsweaponry.util.ParticleNetworking;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class FrostGiant extends Remnant implements IAnimatable, AnimatedDeathInterface {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public int deathTicks;
    private static final TrackedData<Boolean> SMASH = DataTracker.registerData(FrostGiant.class, TrackedDataHandlerRegistry.BOOLEAN);

    public FrostGiant(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new FrostGiantGoal(this));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, 5, false, false, entity -> !this.isTamed()));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, MobEntity.class, 5, false, false,
                entity -> this.isTamed() && entity instanceof Monster && !(entity instanceof CreeperEntity) && !this.isTeammate(entity)));
        this.targetSelector.add(5, new RevengeGoal(this).setGroupRevenge());
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isDead()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death"));
        } else if (this.isSmashing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack"));
        } else {
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk"));
            } else if (this.isSitting()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("sitting_idle"));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        float x = amount;
        if (this.isFireDamage(source)) {
            x *= 2;
        }
        return super.damage(source, x);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        LeviathanAxe.iceExplosion(this.world, this.getBlockPos(), null, 1);
        super.onDeath(damageSource);
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

    @Override
    public int getTicksUntilDeath() {
        return 30;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    @Override
    public void setDeath() {}

    private boolean isFireDamage(DamageSource source) {
        return source.isFire();
    }

    public boolean isSmashing() {
        return this.dataTracker.get(SMASH);
    }

    public void setSmash(boolean bl) {
        this.dataTracker.set(SMASH, bl);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(SMASH, false);
        super.initDataTracker();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    public static DefaultAttributeContainer.Builder createGiantAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 14.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 3.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 8.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
    }

    @Override
    public void tick() {
        super.tick();
        System.out.println(this.getOwner());
        if (this.hasStatusEffect(EffectRegistry.FREEZING)) this.removeStatusEffect(EffectRegistry.FREEZING);
    }

    @Override
    public int getSoulAmount() {
        return 5;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_HUSK_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_DROWNED_DEATH;
    }

    @Override
    public void initEquip() {}

    static class FrostGiantGoal extends MeleeAttackGoal{
        private final FrostGiant mob;
        private int attackStatus;

        public FrostGiantGoal(FrostGiant mob) {
            super(mob, 1D, false);
            this.mob = mob;
        }

        @Override
        public boolean canStart() {
            if (this.mob.isTamed() && this.mob.isSitting()) return false;
            return super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            if (this.mob.isTamed() && this.mob.isSitting()) return false;
            return super.shouldContinue();
        }

        @Override
        public void stop() {
            this.mob.setSmash(false);
            this.attackStatus = 0;
            super.stop();
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                double distanceToEntity = this.mob.squaredDistanceTo(target);
                double d = this.getSquaredMaxAttackDistance(target);
                if (distanceToEntity <= d && this.getCooldown() <= 0 && !this.mob.isSmashing()) {
                    this.mob.setSmash(true);
                }
                if (this.mob.isSmashing()) {
                    this.attackStatus++;
                    this.mob.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 20));
                    if (this.attackStatus == 32) {
                        for (Entity entity : this.mob.world.getOtherEntities(this.mob, this.mob.getBoundingBox().expand(3.5D))) {
                            if (entity instanceof LivingEntity living && !this.isOwner(living)) {
                                if (this.mob.tryAttack(living)) living.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, 60, 0));
                            }
                        }
                        if (!this.mob.world.isClient) {
                            ParticleNetworking.sendServerParticlePacket((ServerWorld) mob.world, PacketRegistry.ICE_SMASH_ID, this.mob.getBlockPos(), 100);
                        }
                        this.mob.world.playSound(null, this.mob.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 1f, 1f);
                        this.mob.world.playSound(null, this.mob.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 1f, .5f);
                        this.mob.world.playSound(null, this.mob.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
                    }
                    if (attackStatus >= 51) {
                        this.mob.setSmash(false);
                        this.attackStatus = 0;
                        this.resetCooldown();
                    }
                }
            }
        }

        private boolean isOwner(LivingEntity entity) {
            return this.mob.getOwner() != null && this.mob.isOwner(entity);
        }

        @Override
        protected void attack(LivingEntity target, double squaredDistance) {}
    }
}