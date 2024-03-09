package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

public class ReturningKnight extends BossEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private int spawnTicks;
    public int deathTicks;
    private int blockBreakingCooldown;
    
    public ReturningKnight(EntityType<? extends ReturningKnight> entityType, World world) {
        super(entityType, world, BossBar.Color.BLUE);
    }

    private static final TrackedData<Boolean> OBLITERATE = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> BLIND = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SUMMON = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> RUPTURE = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> UNBREAKABLE = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SPAWN = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> DEATH = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> MACE_OF_SPADES = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.BOOLEAN);


    private PlayState predicate(AnimationState<?> state) {
        if (this.getDeath()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("death"));
        } else if (this.getSpawning()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("spawn"));
        } else if (this.getUnbreakable()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("unbreakable"));
        } else if (this.getSummon()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("summon_warriors"));
        } else if (this.getObliterate()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("obliterate"));
        } else if (this.getBlind()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("blinding_reflection"));
        } else if (this.getRupture()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("rupture"));
        } else if (this.getMaceOfSpades()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("mace_of_spades"));
        } else if (this.isAttacking()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("walk"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
        }
        return PlayState.CONTINUE;
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(OBLITERATE, Boolean.FALSE);
        this.dataTracker.startTracking(BLIND, Boolean.FALSE);
        this.dataTracker.startTracking(SUMMON, Boolean.FALSE);
        this.dataTracker.startTracking(RUPTURE, Boolean.FALSE);
        this.dataTracker.startTracking(UNBREAKABLE, Boolean.FALSE);
        this.dataTracker.startTracking(SPAWN, Boolean.FALSE);
        this.dataTracker.startTracking(DEATH, Boolean.FALSE);
        this.dataTracker.startTracking(MACE_OF_SPADES, Boolean.FALSE);
    }

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50D)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, ConfigConstructor.returning_knight_health)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
        .add(EntityAttributes.GENERIC_ARMOR, 8.0D);
    }

    @Override
	protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new ReturningKnightGoal(this));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 12.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(5, (new RevengeGoal(this)).setGroupRevenge());
		super.initGoals();
	}

    @Override
    public int getTicksUntilDeath() {
        return 70;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    @Override
    public void setDeath() {
        this.setDeath(true);
    }

    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks >= this.getTicksUntilDeath() && !this.method_48926().isClient()) {
            this.method_48926().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(this.method_48926(), this.getPos(), SoundRegistry.DAWNBREAKER_EVENT, ParticleTypes.LARGE_SMOKE, ParticleRegistry.NIGHTFALL_PARTICLE);
            this.remove(RemovalReason.KILLED);
        }
    }

    public void setObliterate(boolean bl) {
        this.dataTracker.set(OBLITERATE, bl);
    }

    public boolean getObliterate() {
        return this.dataTracker.get(OBLITERATE);
    }

    public void setBlind(boolean bl) {
        this.dataTracker.set(BLIND, bl);
    }

    public boolean getBlind() {
        return this.dataTracker.get(BLIND);
    }

    public void setSummon(boolean bl) {
        this.dataTracker.set(SUMMON, bl);
    }

    public boolean getSummon() {
        return this.dataTracker.get(SUMMON);
    }

    public void setRupture(boolean bl) {
        this.dataTracker.set(RUPTURE, bl);
    }

    public boolean getRupture() {
        return this.dataTracker.get(RUPTURE);
    }

    public void setMaceOfSpades(boolean bl) {
        this.dataTracker.set(MACE_OF_SPADES, bl);
    }

    public boolean getMaceOfSpades() {
        return this.dataTracker.get(MACE_OF_SPADES);
    }

    public void setUnbreakable(boolean bl) {
        this.dataTracker.set(UNBREAKABLE, bl);
    }

    public boolean getUnbreakable() {
        return this.dataTracker.get(UNBREAKABLE);
    }

    public void setSpawning(boolean bl) {
        this.dataTracker.set(SPAWN, bl);
    }

    public boolean getSpawning() {
        return this.dataTracker.get(SPAWN);
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
            
            for(int i = 0; i < 50; ++i) {
                Random random = this.getRandom();
                BlockPos pos = this.getBlockPos();
                double d = random.nextGaussian() * 0.05D;
                double e = random.nextGaussian() * 0.05D;
                double newX = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d;
                double newZ = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e;
                double newY = random.nextDouble() - 0.5D + random.nextDouble() * 0.5D;
                method_48926().addParticle(ParticleTypes.SOUL, pos.getX(), pos.getY(), pos.getZ(), newX/2, newY/2, newZ/2);
                method_48926().addParticle(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY(), pos.getZ(), newX/2, newY/2, newZ/2);
            }
            
            if (this.spawnTicks % 10 == 0) {
                this.method_48926().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.spawnTicks >= 80) {
                this.setSpawning(false);
            }
        }

        //Unbreakable particles
        if (this.getHealth() <= this.getMaxHealth() / 2.0F && !this.getDeath()) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10, 0));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 10, this.getAttackingPlayers().size() >= 3 ? 3 : 2));

            double d = this.random.nextGaussian() * 0.05D;
            double e = this.random.nextGaussian() * 0.05D;
            for(int i = 0; i < 2; ++i) {
                double newX = this.random.nextDouble() - 0.5D + this.random.nextGaussian() * 0.15D + d;
                double newZ = this.random.nextDouble() - 0.5D + this.random.nextGaussian() * 0.15D + e;
                double newY = this.random.nextDouble() - 0.5D + this.random.nextDouble() * 0.5D;
                this.method_48926().addParticle(ParticleTypes.WAX_OFF, this.getX(), this.getY() + 5.5f, this.getZ(), newX*25, newY*18, newZ*25);
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.blockBreakingCooldown <= 0) {
            this.blockBreakingCooldown = 20;
        }
        if (this.isInvulnerableTo(source)) {
           return false;
        } else {
            Entity entity = source.getSource();
            if (entity instanceof ProjectileEntity) {
                return false;
            }
            return super.damage(source, amount);
        }
    }

    @Override
    public boolean disablesShield() {
        return true;
    }

    @Override
    public boolean isUndead() {
        return true;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.DEFAULT;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    public int getXp() {
        return ConfigConstructor.returning_knight_xp;
    }

    protected void mobTick() {
        super.mobTick();
        
        //Reflect all projectiles
        //Box chunkBox = new Box(this.getX() - 4, this.getEyeY() - 2, this.getZ() - 4, this.getX() + 4, this.getEyeY() + 2, this.getZ() + 4);
        Box chunkBox = this.getBoundingBox().expand(3);
        List<Entity> nearbyEntities = this.method_48926().getOtherEntities(this, chunkBox);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof PersistentProjectileEntity projectile) {
                projectile.setVelocity(-projectile.getVelocity().getX(), -projectile.getVelocity().getY(), -projectile.getVelocity().getZ());
            }
        }

        if (ConfigConstructor.can_bosses_break_blocks) {
            int j;
            int i;
            int k;
            if (this.blockBreakingCooldown > 0) {
                --this.blockBreakingCooldown;
                if (this.blockBreakingCooldown == 0 && this.method_48926().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    i = MathHelper.floor(this.getY());
                    j = MathHelper.floor(this.getX());
                    k = MathHelper.floor(this.getZ());
                    for (int l = -3; l <= 3; ++l) {
                        for (int m = -3; m <= 3; ++m) {
                            for (int n = 0; n <= 8; ++n) {
                                if (!(this.method_48926().getBlockState(new BlockPos(j + l, i + n, k + m)).getBlock() instanceof BlockWithEntity)) {
                                    this.method_48926().breakBlock(new BlockPos(j + l, i + n, k + m), true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundRegistry.DEATH_SCREAMS_EVENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.KNIGHT_HIT_EVENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundRegistry.KNIGHT_DEATH_EVENT;
    }
}
