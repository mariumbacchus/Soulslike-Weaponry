package net.soulsweaponry.entity.mobs;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.ai.goal.DraugrBossGoal;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class DraugrBoss extends BossEntity implements GeoEntity {

    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    public int deathTicks;
    private int spawnTicks;
    private boolean shouldDisableShield = false;

    public DraugrBoss(EntityType<? extends DraugrBoss> entityType, World world) {
        super(entityType, world, BossBar.Color.WHITE);
        this.setDrops(WeaponRegistry.DRAUGR);
    }

    private static final TrackedData<Boolean> SHIELD_UP = DataTracker.registerData(DraugrBoss.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SHIELD_DOWN = DataTracker.registerData(DraugrBoss.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SHIELD_STANCE = DataTracker.registerData(DraugrBoss.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SHIELD_BASH = DataTracker.registerData(DraugrBoss.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> COUNTER = DataTracker.registerData(DraugrBoss.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> POSTURE_BREAK = DataTracker.registerData(DraugrBoss.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> DEATH = DataTracker.registerData(DraugrBoss.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SPAWN = DataTracker.registerData(DraugrBoss.class, TrackedDataHandlerRegistry.BOOLEAN);

    private PlayState attackAnimations(AnimationState event) {
        boolean shielding = event.getController().getCurrentAnimation() != null && event.getController().getCurrentAnimation().animation().name().equals("start_block");
        boolean stop_shielding = event.getController().getCurrentAnimation() != null && event.getController().getCurrentAnimation().animation().name().equals("stop_block");
        if (this.getDeath()) {
            event.getController().setAnimation(RawAnimation.begin().then("death", Animation.LoopType.HOLD_ON_LAST_FRAME));
        } else if (this.getSpawning()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("spawn"));
        } else if (this.getPostureBreak()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("posture_break"));
            this.setShieldDown(false);
            this.setShieldStance(false);
        } else if (this.getShieldUp() && !this.getShieldStance()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("start_block"));
            if (event.getController().hasAnimationFinished()) this.setShieldStance(true);
        } else if (this.getShieldStance() && this.getShieldDown()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("stop_block"));
            if (event.getController().hasAnimationFinished()) {
                this.setShieldDown(false);
                this.setShieldStance(false);
            }
        } else if (this.getShieldBash() && !this.getCounter()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("shield_bash"));
        } else if (this.getCounter() && !this.getShieldBash()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("counter"));
        } else if (this.isAttacking() && !shielding && !stop_shielding/* && event.getController().getAnimationState().equals(AnimationState.Stopped) */) {
            if (this.getShieldStance()) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("block_stance"));
            } else {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("walk"));
            }
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
        }
        return PlayState.CONTINUE;
    }

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 60D)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, ConfigConstructor.old_champions_remains_health)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, ConfigConstructor.old_champions_remains_generic_damage)
        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
        .add(EntityAttributes.GENERIC_ARMOR, 6.0D);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SHIELD_UP, Boolean.FALSE);
        this.dataTracker.startTracking(SHIELD_DOWN, Boolean.FALSE);
        this.dataTracker.startTracking(SHIELD_STANCE, Boolean.FALSE);
        this.dataTracker.startTracking(SHIELD_BASH, Boolean.FALSE);
        this.dataTracker.startTracking(COUNTER, Boolean.FALSE);
        this.dataTracker.startTracking(POSTURE_BREAK, Boolean.FALSE);
        this.dataTracker.startTracking(DEATH, Boolean.FALSE);
        this.dataTracker.startTracking(SPAWN, Boolean.FALSE);
    }


    @Override
	protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new DraugrBossGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(5, (new RevengeGoal(this, new Class[0])).setGroupRevenge());
		super.initGoals();
	}

    public void setSpawning(boolean bl) {
        this.dataTracker.set(SPAWN, bl);
    }

    public boolean getSpawning() {
        return this.dataTracker.get(SPAWN);
    }

    public void setShieldUp(boolean bl) {
        this.dataTracker.set(SHIELD_UP, bl);
    }

    public boolean getShieldUp() {
        return this.dataTracker.get(SHIELD_UP);
    }

    public void setShieldDown(boolean bl) {
        this.dataTracker.set(SHIELD_DOWN, bl);
    }

    public boolean getShieldDown() {
        return this.dataTracker.get(SHIELD_DOWN);
    }

    public void setShieldStance(boolean bl) {
        this.dataTracker.set(SHIELD_STANCE, bl);
    }

    public boolean getShieldStance() {
        return this.dataTracker.get(SHIELD_STANCE);
    }

    public void setShieldBash(boolean bl) {
        this.dataTracker.set(SHIELD_BASH, bl);
    }

    public boolean getShieldBash() {
        return this.dataTracker.get(SHIELD_BASH);
    }

    public void setCounter(boolean bl) {
        this.dataTracker.set(COUNTER, bl);
    }

    public boolean getCounter() {
        return this.dataTracker.get(COUNTER);
    }

    public void setPostureBreak(boolean bl) {
        this.dataTracker.set(POSTURE_BREAK, bl);
    }

    public boolean getPostureBreak() {
        return this.dataTracker.get(POSTURE_BREAK);
    }

    public void setDeath(boolean bl) {
        this.dataTracker.set(DEATH, bl);
    }

    public boolean getDeath() {
        return this.dataTracker.get(DEATH);
    }

    public void tickMovement() {
        super.tickMovement();
        
        if (this.getSpawning()) {
            this.spawnTicks++;
            
            for(int i = 0; i < 30; ++i) {
                Random random = this.getRandom();
                BlockPos pos = this.getBlockPos();
                double d = random.nextGaussian() * 0.05D;
                double e = random.nextGaussian() * 0.05D;
                double newX = random.nextDouble() - 1D * 0.5D + random.nextGaussian() * 0.15D + d;
                double newZ = random.nextDouble() - 1D * 0.5D + random.nextGaussian() * 0.15D + e;
                double newY = random.nextDouble() - 1D * 0.5D + random.nextDouble() * 0.5D;
                world.addParticle(ParticleTypes.SOUL, pos.getX(), pos.getY(), pos.getZ(), newX/2, newY/8, newZ/2);
                world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY(), pos.getZ(), newX/2, newY/8, newZ/2);
            }
            
            if (this.spawnTicks % 10 == 0 && this.spawnTicks < 40) {
                this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.spawnTicks == 48 || this.spawnTicks == 55) {
                this.world.playSound(null, this.getBlockPos(), SoundRegistry.SWORD_HIT_SHIELD_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                for (Entity entity : this.world.getOtherEntities(this, getBoundingBox().expand(10f))) {
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 0));
                        ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 0));
                    }
                }
            }
            if (this.spawnTicks >= 70) {
                this.setSpawning(false);
            }
        }

        if (this.getHealth() <= this.getMaxHealth() / 2.0F) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 3));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10, 1));
            for(int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        } else if (this.world.isSkyVisible(this.getBlockPos())) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 10, 1));
            for(int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.SNOWFLAKE, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean isUndead() {
        return true;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public int getTicksUntilDeath() {
        return 20;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        this.setDeath(true);
        CustomDeathHandler.deathExplosionEvent(world, this.getBlockPos(), true, SoundRegistry.NIGHTFALL_SPAWN_EVENT);
        NightShade entity = new NightShade(EntityRegistry.NIGHT_SHADE, world);
        entity.setPos(this.getX(), this.getY() + .1F, this.getZ());
        entity.setVelocity(0, .1f, 0);
        entity.setSpawn(true);
        world.spawnEntity(entity);
    }

    @Override
    public void setDeath() {
    }

    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks >= this.getTicksUntilDeath() && !this.world.isClient()) {
            this.world.sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public boolean disablesShield() {
        return this.shouldDisableShield;
    }

    public void updateDisableShield(boolean bl) {
        this.shouldDisableShield = bl;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller", 0, this::attackAnimations));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
    }
  
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITHER_SKELETON_HURT;
    }
  
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
    }
  
    SoundEvent getStepSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_STEP;
    }
}
