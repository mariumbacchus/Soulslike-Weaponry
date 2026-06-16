package net.soulsweaponry.entity.mobs.boss;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.AccursedLordGoal;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class AccursedLordBoss extends BossEntity<AccursedLordBoss.States> implements GeoEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private int spawnTicks;
    public ArrayList<BlockPos> lavaPos = new ArrayList<>();

    public AccursedLordBoss(EntityType<? extends AccursedLordBoss> entityType, World world) {
        super(entityType, world, BossBar.Color.RED, States.class);
    }

    @Override
    public boolean isFireImmune() {
        return EntityConfig.decaying_king_is_fire_immune;
    }

    @Override
    public boolean hasInvertedHealingAndHarm() {
        return EntityConfig.decaying_king_has_inverted_heal_and_harm;
    }

    private PlayState attackAnimations(AnimationState<?> state) {
        if (this.isDead()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.death"));
        } else {
            switch (this.getState()) {
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
                case SPAWN -> state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.spawn"));
                default -> state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.model.idle"));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public int getTicksUntilDeath() {
        return 150;
    }

    @Override
    public int getXp() {
        return (int) EntityConfig.decaying_king_xp;
    }

    @Override
    public boolean isSpawning() {
        return this.isState(States.SPAWN);
    }

    @Override
    public SoundEvent getBossMusic() {
        return null;
    }

    @Override
    public boolean hasBossMusic() {
        return false;
    }

    @Override
    public List<ParticleEffect> getDeathParticles() {
        return List.of(ParticleTypes.LARGE_SMOKE, ParticleTypes.FLAME);
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
            .add(EntityAttributes.GENERIC_MAX_HEALTH, EntityConfig.decaying_king_health)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0D)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
            .add(EntityAttributes.GENERIC_ARMOR, EntityConfig.decaying_king_armor)
            .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0D);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (this.isSpawning()) {
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
            if (this.spawnTicks == 111) {
                this.getWorld().playSound(null, this.getBlockPos(), SoundRegistry.DAWNBREAKER_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                Box chunkBox = new Box(this.getBlockPos()).expand(5);
                List<Entity> nearbyEntities = this.getWorld().getOtherEntities(this, chunkBox);
                for (Entity nearbyEntity : nearbyEntities) {
                    if (nearbyEntity instanceof LivingEntity closestTarget) {
                        double x = closestTarget.getX() - (this.getX());
                        double z = closestTarget.getZ() - this.getZ();
                        closestTarget.takeKnockback(10F, -x, -z);
                        closestTarget.damage(this.getWorld().getDamageSources().mobAttack(this), 50f * EntityConfig.decaying_king_damage_modifier);
                    }
                }
                if (!this.getWorld().isClient) {
                    ParticleHandler.particleSphere(this.getWorld(), 1000, this.getX(), this.getY() + 1f, this.getZ(), ParticleTypes.FLAME, 1f);
                    ParticleHandler.particleOutburstMap(this.getWorld(), 200, this.getX(), this.getY() + .1f, this.getZ(), ParticleEvents.DAWNBREAKER_MAP, 1f);
                }
            }
            if (this.spawnTicks >= 125) {
                this.setIdle();
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

    @Override
    public void onDeath(DamageSource source) {
        this.removePlacedLava();
        super.onDeath(source);
    }

    @Override
    public String[] getBlacklistedStatusEffects() {
        return EntityConfig.decaying_king_status_effect_blacklist;
    }

    @Override
    public boolean disablesShield() {
        return EntityConfig.decaying_king_disables_shields;
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
        return SoundRegistry.DEMON_BOSS_IDLE_EVENT;
    }
  
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundRegistry.DEMON_BOSS_HURT_EVENT;
    }
  
    protected SoundEvent getDeathSound() {
        return SoundRegistry.DEMON_BOSS_DEATH_EVENT;
    }

    public enum States {
        IDLE, SWORDSLAM, FIREBALLS, PULL, HEATWAVE, SPIN, WITHERBALLS, HAND_SLAM, SPAWN
    }
}
