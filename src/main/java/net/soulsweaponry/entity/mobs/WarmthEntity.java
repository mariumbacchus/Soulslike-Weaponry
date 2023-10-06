package net.soulsweaponry.entity.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.soulsweaponry.registry.SoundRegistry;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.EnumSet;

public class WarmthEntity extends TameableEntity implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final TrackedData<Integer> STATES = DataTracker.registerData(WarmthEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public WarmthEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 10;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(4, new WarmthEntityGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, true, entity -> !this.isTamed()
                || !(this.getOwner() instanceof PlayerEntity)));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, MobEntity.class, true,
                entity -> this.isTamed() && entity instanceof Monster && !(entity instanceof CreeperEntity) && !this.isTeammate(entity)));
    }

    private <E extends IAnimatable> PlayState attacks(AnimationEvent<E> event) {
        switch (this.getState()) {
            case 1 -> event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.LOOP));
            case 2 -> event.getController().setAnimation(new AnimationBuilder().addAnimation("buff", ILoopType.EDefaultLoopTypes.LOOP));
            default -> event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    /**
     * Set the state for animations and attacks.
     * @param state 0 = idle, 1 == attack, 2 == buff
     */
    public void setState(int state) {
        this.dataTracker.set(STATES, state);
    }

    public int getState() {
        return this.dataTracker.get(STATES);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STATES, 0);
    }

    public static DefaultAttributeContainer.Builder createEntityAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 30D);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::attacks));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void tickMovement() {
        if (this.world.isClient) {
            this.world.addParticle(ParticleTypes.FLAME, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0, 0.0, 0.0);
        }
        super.tickMovement();
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        if (this.world.isClient) {
            this.particleExplosion();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > 1200) {
            this.kill();
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (this.world.isClient) {
            this.particleExplosion();
        }
        this.discard();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.WARMTH_DIE_EVENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLOCK_LAVA_POP;
    }

    @Override
    public boolean hurtByWater() {
        return true;
    }

    private void particleExplosion() {
        double phi = Math.PI * (3. - Math.sqrt(5.));
        float points = 100f;
        for (int i = 0; i < points; i++) {
            double velocityY = 1 - (i/(points - 1)) * 2;
            double radius = Math.sqrt(1 - velocityY*velocityY);
            double theta = phi * i;
            double velocityX = Math.cos(theta) * radius;
            double velocityZ = Math.sin(theta) * radius;
            world.addParticle(ParticleTypes.FLAME, true, this.getX(), this.getBodyY(0.5D), this.getZ(), velocityX*0.4f, velocityY*0.4f, velocityZ*0.4f);
        }
    }

    static class WarmthEntityGoal
            extends Goal {
        private final WarmthEntity entity;
        private int attackCooldown;
        private int attackStatus;

        public WarmthEntityGoal(WarmthEntity entity) {
            this.entity = entity;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.entity.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.entity.canTarget(livingEntity);
        }

        @Override
        public void stop() {
            this.reset();
        }

        private void reset() {
            this.entity.setState(0);
            this.attackCooldown = 60;
            this.attackStatus = 0;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.attackCooldown--;
            LivingEntity livingEntity = this.entity.getTarget();
            if (livingEntity == null) {
                return;
            }
            this.entity.getLookControl().lookAt(livingEntity, 10.0f, 10.0f);
            if (this.attackCooldown < 0 && this.entity.getState() == 0) {
                int rand = this.entity.random.nextInt(5);
                this.entity.setState(rand == 1 ? 2 : 1);
            }
            switch (this.entity.getState()) {
                case 1 -> this.shootFire(livingEntity);
                case 2 -> this.buff();
            }
        }

        private void shootFire(LivingEntity livingEntity) {
            this.attackStatus++;
            double e = livingEntity.getX() - this.entity.getX();
            double f = livingEntity.getBodyY(0.5) - this.entity.getBodyY(0.5);
            double g = livingEntity.getZ() - this.entity.getZ();
            if (this.attackStatus == 7 || this.attackStatus == 15 || this.attackStatus == 21) {
                this.entity.world.playSound(null, this.entity.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 1f, 1f);
                SmallFireballEntity smallFireballEntity = new SmallFireballEntity(this.entity.world, this.entity, e, f, g);
                smallFireballEntity.setPosition(smallFireballEntity.getX(), this.entity.getBodyY(0.5f), smallFireballEntity.getZ());
                this.entity.world.spawnEntity(smallFireballEntity);
            }
            if (this.attackStatus >= 28) {
                this.reset();
            }
        }

        private void buff() {
            this.attackStatus++;
            if (attackStatus == 1) this.entity.world.playSound(null, this.entity.getBlockPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.HOSTILE, 1f, 1f);
            if (attackStatus == 30) this.entity.world.playSound(null, this.entity.getBlockPos(), SoundRegistry.WARMTH_BUFF_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            if (this.attackStatus == 35) {
                boolean bl = this.entity.isTamed() && this.entity.getOwner() != null;
                for (Entity en : this.entity.world.getOtherEntities(this.entity, this.entity.getBoundingBox().expand(16D))) {
                    if (en instanceof LivingEntity living) {
                        if (bl && !(living instanceof HostileEntity)) {
                            this.addEffects(living);
                        } else {
                            if (living instanceof Monster) {
                                this.addEffects(living);
                            }
                        }
                    }
                }
                this.addEffects(this.entity);
            }
            if (this.attackStatus >= 40) {
                this.reset();
            }
        }

        private void addEffects(LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 200, 0));
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0));
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0));
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 0));
        }
    }
}