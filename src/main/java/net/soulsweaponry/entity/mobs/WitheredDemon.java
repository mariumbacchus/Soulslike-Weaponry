package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.IAnimatedDeath;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;

public class WitheredDemon extends HostileEntity implements GeoEntity, IAnimatedDeath {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    public int deathTicks;

    private static final TrackedData<Boolean> SWING_ARM = DataTracker.registerData(WitheredDemon.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> DEATH = DataTracker.registerData(WitheredDemon.class, TrackedDataHandlerRegistry.BOOLEAN);

    public WitheredDemon(EntityType<? extends WitheredDemon> entityType, World world) {
        super(entityType, world);
        this.ignoreCameraFrustum = true;
        this.experiencePoints = 20;
    }

    private PlayState predicate(AnimationState<?> state) {
        if (this.getDeath()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("death"));
        } else if (this.getSwingArm()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("attack"));
        } else if (this.isAttacking()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("walk"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
        }
        
        return PlayState.CONTINUE;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SWING_ARM, Boolean.FALSE);
        this.dataTracker.startTracking(DEATH, Boolean.FALSE);
    }

    public boolean getSwingArm() {
        return this.dataTracker.get(SWING_ARM);
    }

    public void setSwingArm(boolean bl) {
        this.dataTracker.set(SWING_ARM, bl);
    }

    public boolean getDeath() {
        return this.dataTracker.get(DEATH);
    }

    public void setDeath(boolean bl) {
        this.dataTracker.set(DEATH, bl);
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    public boolean isFireImmune() {
        return true;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
    
    @Override
	protected void initGoals() {
        this.goalSelector.add(1, new DemonAttackGoal(this, 1.7D, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true, target -> {
            boolean bl = true;
            for (ItemStack stack : target.getArmorItems()) {
                if (stack.isOf(ItemRegistry.WITHERED_CHEST) || stack.isOf(ItemRegistry.ENHANCED_WITHERED_CHEST)) {
                    bl = false;
                    break;
                }
            }
            return bl;
        }));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, WitherSkeletonEntity.class, true));
        this.targetSelector.add(3, (new RevengeGoal(this)).setGroupRevenge());
		super.initGoals();
	}

    public static DefaultAttributeContainer.Builder createDemonAttributes() {
        return HostileEntity.createHostileAttributes()
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35D)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 80D)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.12D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0D)
        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
        .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0D);
    }

    @Override
    public boolean canSpawn(WorldView view) {
        BlockPos blockUnderEntity = new BlockPos(this.getBlockX(), this.getBlockY() - 1, this.getBlockZ());
        BlockPos positionEntity = new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ());
        BlockState state = this.getWorld().getBlockState(positionEntity);
        return view.doesNotIntersectEntities(this) && !getWorld().containsFluid(this.getBoundingBox())
            && state.getBlock().canMobSpawnInside(state)
            && !getWorld().getBlockState(positionEntity.down()).isOf(Blocks.NETHER_WART_BLOCK)
            && getWorld().getDifficulty() != Difficulty.PEACEFUL
            && getWorld().getBlockState(positionEntity.down()).isOf(Blocks.CRIMSON_NYLIUM)
            && this.getWorld().getBlockState(blockUnderEntity).allowsSpawning(view, blockUnderEntity, EntityRegistry.WITHERED_DEMON)
            && this.isSpawnable();
    }

    public boolean isSpawnable() {
        return ConfigConstructor.can_withered_demon_spawn;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        this.setDeath();
    }

    @Override
    public void setDeath() {
        this.setDeath(true);
    }

    @Override
    public int getTicksUntilDeath() {
        return 40;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    //Now the renderer won't recognize the variable deathTicks, so it won't turn red
    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks >= this.getTicksUntilDeath() && !this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        float f = this.getAttackDamage();
        float g = (int)f > 0 ? f / 2.0F + (float)this.random.nextInt((int)f) : f;
        boolean bl = target.damage(this.getWorld().getDamageSources().mobAttack(this), g);
        if (bl) {
           target.setVelocity(target.getVelocity().add(0.0D, 0.4000000059604645D, 0.0D));
           this.applyDamageEffects(this, target);
        }
        
        return bl;
    }

    public boolean disablesShield() {
        return true;
    }

    static class DemonAttackGoal extends MeleeAttackGoal {
        private final WitheredDemon mob;
        private int attackStatus;

        public DemonAttackGoal(WitheredDemon mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
            this.mob = mob;
        }

        private boolean canAttackTarget() {
            boolean bl = true;
            if (this.mob.getTarget() != null && this.mob.getAttacker() != this.mob.getTarget()) {
                for (ItemStack stack : this.mob.getTarget().getArmorItems()) {
                    if (stack.isOf(ItemRegistry.WITHERED_CHEST) || stack.isOf(ItemRegistry.ENHANCED_WITHERED_CHEST)) {
                        bl = false;
                        break;
                    }
                }
            }
            return bl;
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && this.canAttackTarget();
        }

        @Override
        public boolean canStart() {
            return super.canStart() && this.canAttackTarget();
        }

        @Override
        public void stop() {
            super.stop();
            this.mob.setAttacking(false);
            this.mob.setSwingArm(false);
        }

        @Override
        protected void attack(LivingEntity target) {
            if (this.mob.isInAttackRange(target) && this.getCooldown() <= 0) {
                this.mob.setSwingArm(true);
            }

            if (this.mob.getSwingArm()) {
                this.attackStatus++;
                if (attackStatus == 10 && this.mob.isInAttackRange(target)) {
                    this.mob.tryAttack(target);
                }
                if (attackStatus >= 30) {
                    this.mob.setSwingArm(false);
                    this.attackStatus = 0;
                    this.resetCooldown();
                }
            }
        }
    }

    public void tickMovement() {
        super.tickMovement();
        Random random = new Random();
        double ran = random.nextDouble();
        if (ran < 0.05D) {
            this.getWorld().addParticle(ParticleTypes.FLAME, this.getX(), this.getY() + 1.4F, this.getZ(), ran - 0.025D, ran - 0.025D, ran - 0.025D);
        }
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    protected SoundEvent getAmbientSound() {
        return SoundRegistry.DEMON_IDLE_EVENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.DEMON_DAMAGE_EVENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundRegistry.DEMON_DEATH_EVENT;
    }

    protected SoundEvent getStepSound() {
        return SoundRegistry.DEMON_WALK_EVENT;
    }

    @Override
    public boolean isUndead() {
        return true;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
}
