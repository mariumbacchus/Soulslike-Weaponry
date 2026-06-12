package net.soulsweaponry.entity.mobs.boss;

import net.minecraft.entity.Entity;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.collision.RotatableHitboxDebugRegistry;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.ReturningKnightHitboxes;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ReturningKnight extends BossEntity<ReturningKnight.States> implements GeoEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private int spawnTicks;
    public int deathTicks;
    private final List<UUID> healers = new ArrayList<>();
    private final RotatableHitbox debugObliterateMaceHitbox = ReturningKnightHitboxes.createObliterateMaceHitboxPlaceholder();

    private static final TrackedData<BlockPos> OBLITERATE_TARGET = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Long> ATTACK_START_WORLD_TIME = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.LONG);
    
    public ReturningKnight(EntityType<? extends ReturningKnight> entityType, World world) {
        super(entityType, world, BossBar.Color.BLUE, ReturningKnight.States.class);//TODO returning knight is completely broken now lol but i gotta rework it anyway sooo
    }

    private PlayState predicate(AnimationState<?> state) {
        switch (this.getState()) {
            case DEATH -> state.getController().setAnimation(RawAnimation.begin().thenPlay("death"));
            case SPAWN -> state.getController().setAnimation(RawAnimation.begin().thenPlay("spawn"));
            case UNBREAKABLE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("unbreakable"));
            case SUMMON -> state.getController().setAnimation(RawAnimation.begin().thenPlay("summon_warriors"));
            case OBLITERATE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("obliterate"));
            case BLIND -> state.getController().setAnimation(RawAnimation.begin().thenPlay("blinding_reflection"));
            case RUPTURE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("rupture"));
            case MACE_OF_SPADES -> state.getController().setAnimation(RawAnimation.begin().thenPlay("mace_of_spades"));
            default -> {
                if (this.isAttacking()) {
                    state.getController().setAnimation(RawAnimation.begin().thenPlay("walk"));
                } else {
                    state.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
                }
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(OBLITERATE_TARGET, BlockPos.ORIGIN);
        builder.add(ATTACK_START_WORLD_TIME, -1L);
    }

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50D)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, EntityConfig.returning_knight_health)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
        .add(EntityAttributes.GENERIC_ARMOR, EntityConfig.returning_knight_armor);
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
        this.setState(States.DEATH);
    }

    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks >= this.getTicksUntilDeath() && !this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(this.getWorld(), this.getPos(), SoundRegistry.DAWNBREAKER_EVENT, ParticleTypes.LARGE_SMOKE, ParticleRegistry.NIGHTFALL_PARTICLE);
            this.remove(RemovalReason.KILLED);
        }
    }

    public void setSpawning() {
        this.setState(States.SPAWN);
    }

    @Override
    public boolean isSpawning() {
        return this.isState(States.SPAWN);
    }

    public boolean getDeath() {
        return this.isState(States.DEATH);
    }

    public void setObliterateTarget(BlockPos pos) {
        this.dataTracker.set(OBLITERATE_TARGET, pos);
    }

    public BlockPos getObliterateTarget() {
        return this.dataTracker.get(OBLITERATE_TARGET);
    }

    /**
     * Set to track world age server side in the goal class when the attack starts so the client can predict attack status from the goal class.
     */
    public void setAttackStartWorldTime(long time) {
        this.dataTracker.set(ATTACK_START_WORLD_TIME, time);
    }

    public long getAttackStartWorldTime() {
        return this.dataTracker.get(ATTACK_START_WORLD_TIME);
    }

    public int getSyncedAttackStatusTick() {
        long startTime = this.getAttackStartWorldTime();
        if (startTime < 0L) {
            return 0;
        }
        // Goal class runs every other tick (10 ticks per second instead of 20) so gotta divide by 2 to not be too fast!
        // Also add 4 ticks since its behind those ticks when starting to track
        return (int) ((this.getWorld().getTime() + 4 - startTime) / 2L);
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
                getWorld().addParticle(ParticleTypes.SOUL, pos.getX(), pos.getY(), pos.getZ(), newX/2, newY/2, newZ/2);
                getWorld().addParticle(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY(), pos.getZ(), newX/2, newY/2, newZ/2);
            }
            
            if (this.spawnTicks % 10 == 0) {
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.spawnTicks >= 80) {
                this.setState(States.IDLE);
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
                this.getWorld().addParticle(ParticleTypes.WAX_OFF, this.getX(), this.getY() + 5.5f, this.getZ(), newX*25, newY*18, newZ*25);
            }
        }

        // Debug hitbox during obliterate attack
        if (this.getWorld().isClient() && this.isState(States.OBLITERATE)) {
            BlockPos targetPos = this.getObliterateTarget();
            int attackTick = this.getSyncedAttackStatusTick();
            if (!targetPos.equals(BlockPos.ORIGIN) && ReturningKnightHitboxes.isObliterateDebugTick(attackTick)) {
                ReturningKnightHitboxes.updateObliterateMaceHitbox(this.debugObliterateMaceHitbox, this, targetPos, attackTick);
                RotatableHitboxDebugRegistry.put(
                        this.getWorld(),
                        "returning_knight_obliterate_server_predicted_" + this.getUuidAsString(),
                        this.debugObliterateMaceHitbox
                );
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
           return false;
        } else {
            Entity entity = source.getSource();
            if (entity instanceof ProjectileEntity projectile && !this.isProjectileWhitelisted(projectile)) {
                return false;
            }
            return super.damage(source, amount);
        }
    }

    @Override
    public String[] getWhitelistedProjectiles() {
        return EntityConfig.returning_knight_projectile_immunity_whitelist;
    }

    @Override
    public String[] getBlacklistedStatusEffects() {
        return EntityConfig.returning_knight_status_effect_blacklist;
    }

    @Override
    public boolean disablesShield() {
        return EntityConfig.returning_knight_disables_shields;
    }

    @Override
    public boolean hasInvertedHealingAndHarm() {
        return EntityConfig.returning_knight_has_inverted_heal_and_harm;
    }

    @Override
    public boolean isFireImmune() {
        return EntityConfig.returning_knight_is_fire_immune;
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
        return (int) EntityConfig.returning_knight_xp;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        //Reflect all projectiles
        //Box chunkBox = new Box(this.getX() - 4, this.getEyeY() - 2, this.getZ() - 4, this.getX() + 4, this.getEyeY() + 2, this.getZ() + 4);
        Box chunkBox = this.getBoundingBox().expand(3);
        List<Entity> nearbyEntities = this.getWorld().getOtherEntities(this, chunkBox);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof PersistentProjectileEntity projectile && !this.isProjectileWhitelisted(projectile)) {
                projectile.setVelocity(-projectile.getVelocity().getX(), -projectile.getVelocity().getY(), -projectile.getVelocity().getZ());
            }
        }
        this.breakSurroundingBlocks();
    }

    public boolean hasHealersAlive() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            // Use iterator for safely removing of the uuids of entities that don't exist anymore
            Iterator<UUID> iterator = this.healers.iterator();
            while (iterator.hasNext()) {
                UUID uuid = iterator.next();
                Entity entity = serverWorld.getEntity(uuid);
                if (entity == null || !entity.isAlive()) {
                    iterator.remove();
                }
            }
            return !this.healers.isEmpty();
        }
        return false;
    }

    public void addHealer(UUID uuid) {
        this.healers.add(uuid);
    }

    @Override
    public SoundEvent getBossMusic() {
        return null;
    }

    @Override
    public boolean hasBossMusic() {
        return false;
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

    public enum States {
        IDLE, SPAWN, DEATH, OBLITERATE, BLIND, SUMMON, RUPTURE, UNBREAKABLE, MACE_OF_SPADES
    }
}
