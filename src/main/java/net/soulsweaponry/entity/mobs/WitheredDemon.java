package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.soulsweaponry.config.BossConfig;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.armor.Hallowheart;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.IAnimatedDeath;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Random;

public class WitheredDemon extends HostileEntity implements GeoEntity, IAnimatedDeath {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
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
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SWING_ARM, false);
        builder.add(DEATH, false);
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
                if (stack.getItem() instanceof Hallowheart) {
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
        .add(EntityAttributes.GENERIC_MAX_HEALTH, BossConfig.withered_demon_health)
        .add(EntityAttributes.GENERIC_ARMOR, BossConfig.withered_demon_armor)
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
        DamageSource damageSource = this.getDamageSources().mobAttack(this);
        boolean bl = target.damage(damageSource, g);
        if (bl) {
            double d = target instanceof LivingEntity livingEntity ? livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) : 0.0;
            double e = Math.max(0.0, 1.0 - d);
            target.setVelocity(target.getVelocity().add(0.0, 0.4F * e, 0.0));
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                EnchantmentHelper.onTargetDamaged(serverWorld, target, damageSource);
            }
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
                    if (stack.getItem() instanceof Hallowheart) {
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
            double attackDistance = this.getSquaredMaxAttackDistance(target);
            double squaredDistance = this.mob.squaredDistanceTo(target);
            if (squaredDistance <= attackDistance && this.getCooldown() <= 0) {
                this.mob.setSwingArm(true);
            }

            if (this.mob.getSwingArm()) {
                this.attackStatus++;
                if (attackStatus == 10 && squaredDistance <= attackDistance) {
                    this.mob.tryAttack(target);
                }
                if (attackStatus >= 30) {
                    this.mob.setSwingArm(false);
                    this.attackStatus = 0;
                    this.resetCooldown();
                }
            }
        }

        //TODO move this into parent class when it is made
        protected double getSquaredMaxAttackDistance(LivingEntity target) {
            float reach = this.mob.getWidth() * 2.0F;
            return reach * reach + target.getWidth();
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

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundRegistry.DEMON_IDLE_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.DEMON_DAMAGE_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.DEMON_DEATH_EVENT;
    }

    protected SoundEvent getStepSound() {
        return SoundRegistry.DEMON_WALK_EVENT;
    }

    @Override
    public boolean hasInvertedHealingAndHarm() {
        return true;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
}
