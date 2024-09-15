package net.soulsweaponry.entity.mobs;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.ai.goal.AccursedLordGoal;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.ArrayList;
import java.util.List;

public class AccursedLordBoss extends BossEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    public int deathTicks;
    private int spawnTicks;
    private static final TrackedData<Integer> ATTACKS = DataTracker.registerData(AccursedLordBoss.class, TrackedDataHandlerRegistry.INTEGER);
    public ArrayList<BlockPos> lavaPos = new ArrayList<>();

    public AccursedLordBoss(EntityType<? extends AccursedLordBoss> entityType, World world) {
        super(entityType, world, BossBar.Color.RED);
    }

    @Override
    public boolean isFireImmune() {
        return ConfigConstructor.decaying_king_is_fire_immune;
    }

    private PlayState attackAnimations(AnimationState<?> state) {
        switch (this.getAttackAnimation()) {
            case FIREBALLS, WITHERBALLS ->
                    state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.shootFireMouth"));
            case HAND_SLAM ->
                    state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.groundSlamHand"));
            case HEATWAVE ->
                    state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.explosion"));
            case PULL -> state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.pull"));
            case SPIN -> state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.spin"));
            case SWORDSLAM ->
                    state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.swordSlam"));
            case DEATH -> state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.death"));
            case SPAWN -> state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.spawn"));
            default -> state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public int getTicksUntilDeath() {
        return 150;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    @Override
    public int getXp() {
        return ConfigConstructor.decaying_king_xp;
    }

    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks >= this.getTicksUntilDeath() && !this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(this.getWorld(), this.getPos(), SoundRegistry.DAWNBREAKER_EVENT.get(), ParticleTypes.LARGE_SMOKE, ParticleTypes.FLAME);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new AccursedLordGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, ChaosMonarch.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, WitherSkeletonEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, WitherEntity.class, true));
        this.targetSelector.add(5, (new RevengeGoal(this)).setGroupRevenge());
        super.initGoals();
    }

    public static DefaultAttributeContainer.Builder createDemonAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 60D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, ConfigConstructor.decaying_king_health)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 5.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0D);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACKS, 9);
    }

    /**
     * Set attack with enum as parameter
     */
    public void setAttackAnimation(AccursedLordAnimations attack) {
        for (int i = 0; i < AccursedLordAnimations.values().length; i++) {
            if (AccursedLordAnimations.values()[i].equals(attack)) {
                this.dataTracker.set(ATTACKS, i);
            }
        }
    }

    /**
     * Set attack with an integer to easily set the data tracker value
     */
    public void setAttackAnimation(int random) {
        this.dataTracker.set(ATTACKS, random);
    }

    public void tickMovement() {
        super.tickMovement();
        if (this.getAttackAnimation().equals(AccursedLordAnimations.SPAWN)) {
            this.spawnTicks++;

            for(int i = 0; i < 50; ++i) {
                Random random = this.getRandom();
                BlockPos pos = this.getBlockPos();
                double d = random.nextGaussian() * 0.05D;
                double e = random.nextGaussian() * 0.05D;
                double newX = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d;
                double newZ = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e;
                double newY = random.nextDouble() - 0.5D + random.nextDouble() * 0.5D;
                getWorld().addParticle(ParticleTypes.FLAME, pos.getX(), pos.getY(), pos.getZ(), newX/2, newY/6, newZ/2);
                getWorld().addParticle(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY(), pos.getZ(), newX/2, newY/6, newZ/2);
            }

            if (this.spawnTicks % 10 == 0 && this.spawnTicks < 70) {
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.spawnTicks > 110 && this.spawnTicks <= 112) {
                this.getWorld().playSound(null, this.getBlockPos(), SoundRegistry.DAWNBREAKER_EVENT.get(), SoundCategory.HOSTILE, 1f, 1f);
                Box chunkBox = new Box(this.getBlockPos()).expand(5);
                List<Entity> nearbyEntities = this.getWorld().getOtherEntities(this, chunkBox);
                for (Entity nearbyEntity : nearbyEntities) {
                    if (nearbyEntity instanceof LivingEntity closestTarget) {
                        double x = closestTarget.getX() - (this.getX());
                        double z = closestTarget.getZ() - this.getZ();
                        closestTarget.takeKnockback(10F, -x, -z);
                        closestTarget.damage(this.getWorld().getDamageSources().mobAttack(this), 50f * ConfigConstructor.decaying_king_damage_modifier);
                    }
                }
                if (!this.getWorld().isClient) {
                    ParticleHandler.particleSphere(this.getWorld(), 1000, this.getX(), this.getY() + 1f, this.getZ(), ParticleTypes.FLAME, 1f);
                    ParticleHandler.particleOutburstMap(this.getWorld(), 200, this.getX(), this.getY() + .1f, this.getZ(), ParticleEvents.DAWNBREAKER_MAP, 1f);
                }
            }
            if (this.spawnTicks >= 125) {
                this.setAttackAnimation(AccursedLordAnimations.IDLE);
            }
        }
    }

    public void removePlacedLava() {
        for (BlockPos pos : this.lavaPos) {
            if (this.getWorld().getBlockState(pos).isOf(Blocks.LAVA)) {
                this.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
        this.lavaPos.clear();
    }

    public AccursedLordAnimations getAttackAnimation() {
        return AccursedLordAnimations.values()[this.dataTracker.get(ATTACKS)];
    }

    @Override
    public void setDeath() {
        this.setAttackAnimation(AccursedLordAnimations.DEATH);
        this.removePlacedLava();
    }

    @Override
    public boolean isUndead() {
        return ConfigConstructor.decaying_king_is_undead;
    }

    @Override
    public String getGroupId() {
        return ConfigConstructor.decaying_king_group_type;
    }

    @Override
    public boolean disablesShield() {
        return ConfigConstructor.decaying_king_disables_shields;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::attackAnimations));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    protected SoundEvent getAmbientSound() {
        return SoundRegistry.DEMON_BOSS_IDLE_EVENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.DEMON_BOSS_HURT_EVENT.get();
    }

    protected SoundEvent getDeathSound() {
        return SoundRegistry.DEMON_BOSS_DEATH_EVENT.get();
    }

    public enum AccursedLordAnimations {
        SWORDSLAM, FIREBALLS, PULL, HEATWAVE, SPIN, WITHERBALLS, HAND_SLAM, SPAWN, DEATH, IDLE
    }
}