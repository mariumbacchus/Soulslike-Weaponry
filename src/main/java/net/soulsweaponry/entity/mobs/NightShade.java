package net.soulsweaponry.entity.mobs;

import java.util.EnumSet;

import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
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
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
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

public class NightShade extends BossEntity implements GeoEntity {
    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private int spawnTicks;
    public int deathTicks;

    protected static final TrackedData<Boolean> CHARGING = DataTracker.registerData(NightShade.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> DEATH = DataTracker.registerData(NightShade.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> SPAWN = DataTracker.registerData(NightShade.class, TrackedDataHandlerRegistry.BOOLEAN);

    public NightShade(EntityType<? extends NightShade> entityType, World world) {
        super(entityType, world, BossBar.Color.BLUE);
        this.moveControl = new ShadeMoveControl(this);
        this.setDrops(ItemRegistry.LORD_SOUL_DARK);
        this.setDrops(ItemRegistry.ESSENCE_OF_EVENTIDE);
    }

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 65D)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, ConfigConstructor.frenzied_shade_health)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, ConfigConstructor.frenzied_shade_generic_damage);
    }

    @Override
	protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(4, new ChargeTargetGoal());
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(5, (new RevengeGoal(this, new Class[0])).setGroupRevenge());
	}

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CHARGING, Boolean.FALSE);
        this.dataTracker.startTracking(DEATH, Boolean.FALSE);
        this.dataTracker.startTracking(SPAWN, Boolean.FALSE);
    }

    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movement);
        this.checkBlockCollision();
    }

    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
    }
    
    public void tickMovement() {
        super.tickMovement();
        if (this.getSpawn()) {
            this.spawnTicks++;
            if (spawnTicks >= 40) {
                this.setSpawn(false);
            }
        }
        for(int i = 0; i < 3; ++i) {
            this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void setDeath() {
        this.setDeath(true);
    }

    @Override
    public int getTicksUntilDeath() {
        return 60;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks % 30 == 0) {
            this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.HOSTILE, 1f, 1f);
        }
        if (this.deathTicks >= this.getTicksUntilDeath() && !this.world.isClient()) {
            this.world.sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(world, this.getBlockPos(), true, SoundRegistry.DAWNBREAKER_EVENT);
            this.remove(RemovalReason.KILLED);
        }
    }

    public boolean getCharging() {
        return this.dataTracker.get(CHARGING);
    }
  
    public void setCharging(boolean charging) {
        this.dataTracker.set(CHARGING, charging);
    }

    public boolean getSpawn() {
        return this.dataTracker.get(SPAWN);
    }
  
    public void setSpawn(boolean bl) {
        this.dataTracker.set(SPAWN, bl);
    }
    public boolean getDeath() {
        return this.dataTracker.get(DEATH);
    }
  
    public void setDeath(boolean bl) {
        this.dataTracker.set(DEATH, bl);
    }

    class ShadeMoveControl extends MoveControl {
        public ShadeMoveControl(NightShade owner) {
           super(owner);
        }
  
        public void tick() {
           if (this.state == State.MOVE_TO) {
              Vec3d vec3d = new Vec3d(this.targetX - NightShade.this.getX(), this.targetY - NightShade.this.getY(), this.targetZ - NightShade.this.getZ());
              double d = vec3d.length();
              if (d < NightShade.this.getBoundingBox().getAverageSideLength()) {
                 this.state = State.WAIT;
                 NightShade.this.setVelocity(NightShade.this.getVelocity().multiply(0.5D));
              } else {
                 NightShade.this.setVelocity(NightShade.this.getVelocity().add(vec3d.multiply(this.speed * 0.05D / d)));
                 if (NightShade.this.getTarget() == null) {
                    Vec3d vec3d2 = NightShade.this.getVelocity();
                    NightShade.this.setYaw(-((float)MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776F);
                    NightShade.this.bodyYaw = NightShade.this.getYaw();
                 } else {
                    double e = NightShade.this.getTarget().getX() - NightShade.this.getX();
                    double f = NightShade.this.getTarget().getZ() - NightShade.this.getZ();
                    NightShade.this.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
                    NightShade.this.bodyYaw = NightShade.this.getYaw();
                 }
              }
  
           }
        }
    }

    class ChargeTargetGoal extends Goal {
        private int attackCooldown;

        public ChargeTargetGoal() {
           this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }
  
        public boolean canStart() {
            LivingEntity target = NightShade.this.getTarget();
            return target != null && target.isAlive() && NightShade.this.canTarget(target) && NightShade.this.random.nextInt(7) == 0 && !NightShade.this.getSpawn();
        }
  
        public boolean shouldContinue() {
            return NightShade.this.getCharging() && NightShade.this.getTarget() != null && NightShade.this.getTarget().isAlive();
        }
  
        public void start() {
            LivingEntity livingEntity = NightShade.this.getTarget();
            Vec3d vec3d = livingEntity.getEyePos();
            NightShade.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            NightShade.this.setCharging(true);
        }
  
        public void stop() {
            NightShade.this.setCharging(false);
        }
  
        public void tick() {
            attackCooldown--;
            LivingEntity livingEntity = NightShade.this.getTarget();
            double distanceToEntity = NightShade.this.squaredDistanceTo(livingEntity);
            Vec3d vec3d = livingEntity.getEyePos();
            if (distanceToEntity < 8D && attackCooldown < 0) {
                NightShade.this.tryAttack(livingEntity);
                this.attackCooldown = 6;
                NightShade.this.setCharging(false);
            }
            if (this.attackCooldown > 0) {
                NightShade.this.moveControl.moveTo(vec3d.x + NightShade.this.random.nextInt(60) - 30, vec3d.y + NightShade.this.random.nextInt(20) - 10, vec3d.z + NightShade.this.random.nextInt(60) - 30, 1.5D);
            } else {
                NightShade.this.setCharging(true);
                NightShade.this.moveControl.moveTo(vec3d.x, vec3d.y - 2f, vec3d.z, 1.0D);
            }
        }
    }

    private PlayState predicate(AnimationState state) {
        if (this.getSpawn()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("spawn"));
        } else if (this.getDeath()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("death"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
        }
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public boolean isFireImmune() {
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
    public boolean disablesShield() {
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    protected SoundEvent getAmbientSound() {
        return SoundRegistry.NIGHT_SHADE_IDLE_EVENT;
    }
  
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.NIGHT_SHADE_DAMAGE_EVENT;
    }
  
    protected SoundEvent getDeathSound() {
        return SoundRegistry.NIGHT_SHADE_DEATH_EVENT;
    }
}
