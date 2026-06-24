package net.soulsweaponry.entity.mobs.boss;

import net.minecraft.entity.Entity;
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
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.collision.RotatableHitboxDebugRegistry;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.ObliterateHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.SeismicWaveHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.MaceOfSpadesHitbox;
import net.soulsweaponry.entity.projectile.ReturningProjectile;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public class ReturningKnight extends BossEntity<ReturningKnight.States> implements GeoEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private int spawnTicks;
    private final List<UUID> healers = new ArrayList<>();
    private final RotatableHitbox debugMaceHitbox = BossHitboxHelper.createPlaceholder(new Vec3d(7, 5, 6));
    private static final Set<States> IGNORE_IDLE = Set.of(States.SPAWN, States.UNBREAKABLE, States.SUMMON, States.OBLITERATE, States.BLIND, States.RUPTURE, States.MACE_OF_SPADES_1);
    private static final Set<States> MACE_OF_SPADES_ATTACKS = Set.of(States.MACE_OF_SPADES_1, States.MACE_OF_SPADES_2, States.MACE_OF_SPADES_3, States.MACE_OF_SPADES_4_SPIN);

    private static final TrackedData<BlockPos> OBLITERATE_TARGET = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Long> ATTACK_START_WORLD_TIME = DataTracker.registerData(ReturningKnight.class, TrackedDataHandlerRegistry.LONG);

    public ReturningKnight(EntityType<? extends ReturningKnight> entityType, World world) {
        super(entityType, world, BossBar.Color.BLUE, ReturningKnight.States.class);
    }

    @Override
    public double getAnimationSpeed() {
        return EntityConfig.returning_knight_animation_speed;
    }

    private PlayState idle(AnimationState<?> state) {
        if (this.isDead() || IGNORE_IDLE.contains(this.getState())) {
            return PlayState.STOP;
        }
        state.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
        return PlayState.CONTINUE;
    }

    private PlayState predicate(AnimationState<?> state) {
        state.getController().setAnimationSpeed(this.getAnimationSpeed());
        if (this.isDead()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("death"));
        } else {
            switch (this.getState()) {
                case SPAWN -> state.getController().setAnimation(RawAnimation.begin().thenPlay("spawn"));
                case UNBREAKABLE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("unbreakable"));
                case SUMMON -> state.getController().setAnimation(RawAnimation.begin().thenPlay("summon_warriors"));
                case OBLITERATE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("obliterate"));
                case BLIND -> state.getController().setAnimation(RawAnimation.begin().thenPlay("blinding_reflection"));
                case RUPTURE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("rupture"));
                case MACE_OF_SPADES_1 -> state.getController().setAnimation(RawAnimation.begin().thenPlay("mace_of_spades_1"));
                case MACE_OF_SPADES_2 -> state.getController().setAnimation(RawAnimation.begin().thenPlay("mace_of_spades_2"));
                case MACE_OF_SPADES_3 -> state.getController().setAnimation(RawAnimation.begin().thenPlay("mace_of_spades_3"));
                case MACE_OF_SPADES_4_SPIN -> state.getController().setAnimation(RawAnimation.begin().thenPlay("mace_of_spades_4"));
                case SEISMIC_WAVE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("seismic_wave"));
                default -> {
                    if (this.isAttacking()) {
                        state.getController().setAnimation(RawAnimation.begin().thenPlay("walk"));
                    } else {
                        state.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
                    }
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
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.17D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
        .add(EntityAttributes.GENERIC_ARMOR, EntityConfig.returning_knight_armor);
    }

    @Override
	protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new ReturningKnightGoal(this, 1D, true));
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

    public void setSpawning() {
        this.setState(States.SPAWN);
    }

    @Override
    public boolean isSpawning() {
        return this.isState(States.SPAWN);
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
        return (int) (this.getWorld().getTime() + 2 - startTime);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (this.isSpawning()) {
            this.spawnTicks++;
            
            for (int i = 0; i < 50; i++) {
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
            
            if (this.spawnTicks % this.getScaledMaxTicks(10) == 0) {
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.spawnTicks >= this.getScaledMaxTicks(80)) {
                this.setState(States.IDLE);
            }
        }

        //Unbreakable particles
        if (this.getHealth() <= this.getMaxHealth() / 2.0F && !this.isDead()) {
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

        this.updateDebugMaceHitbox();
    }

    private void updateDebugMaceHitbox() {
        if (!this.getWorld().isClient()) {
            return;
        }
        BlockPos targetPos = this.getObliterateTarget();
        if (targetPos.equals(BlockPos.ORIGIN)) {
            return;
        }
        int attackTick = this.getSyncedAttackStatusTick();
        String id = "returning_knight_mace_server_predicted_" + this.getUuidAsString();
        if (this.isState(States.OBLITERATE) && ObliterateHitbox.isObliterateDebugTick(this, attackTick)) {
            ObliterateHitbox.updateObliterateMaceHitbox(this.debugMaceHitbox, this, targetPos, attackTick);
            RotatableHitboxDebugRegistry.put(this.getWorld(), id, this.debugMaceHitbox);
        } else if (MACE_OF_SPADES_ATTACKS.contains(this.getState()) && MaceOfSpadesHitbox.isDebugTick(this, attackTick)) {
            MaceOfSpadesHitbox.updateMaceHitbox(this.debugMaceHitbox, this, attackTick);
            RotatableHitboxDebugRegistry.put(this.getWorld(), id, this.debugMaceHitbox);
        } else if (this.isState(States.SEISMIC_WAVE) && SeismicWaveHitbox.isDebugTick(this, attackTick)) {
            SeismicWaveHitbox.updateMaceHitbox(this.debugMaceHitbox, this, attackTick);
            RotatableHitboxDebugRegistry.put(this.getWorld(), id, this.debugMaceHitbox);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        Entity attacker = source.getAttacker();
        Entity sourceEntity = source.getSource();
        if (sourceEntity instanceof ProjectileEntity projectile && !this.isProjectileWhitelisted(projectile)) {
            Entity copied = this.reflectProjectileCopy(projectile, attacker);
            projectile.discard();
            // Could reuse the UUID after discarding the previous but kinda unsafe and is basically a race condition
            if (copied != null) {
                UUID uuid = UUID.randomUUID();
                copied.setUuid(uuid);
                if (copied instanceof ReturningProjectile returningProjectile && returningProjectile.getOwner() instanceof PlayerEntity player) {
                    returningProjectile.saveOnPlayer(player);
                }
                this.getWorld().spawnEntity(copied);
                ParticleHandler.particleSphereList(this.getWorld(), 10, copied.getX(), copied.getY(), copied.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                this.playSound(SoundEvents.ENTITY_BREEZE_SHOOT, 1f, 1f);
            }
            return false;
        }
        return super.damage(source, amount);
    }

    private Entity reflectProjectileCopy(ProjectileEntity projectile, Entity target) {
        if (this.getWorld().isClient()) {
            return null;
        }
        Entity copiedEntity = projectile.getType().create(this.getWorld());
        if (!(copiedEntity instanceof ProjectileEntity copiedProjectile)) {
            return null;
        }
        copiedProjectile.copyFrom(projectile);
        Vec3d direction;
        if (target != null && target != this) {
            Vec3d start = projectile.getPos();
            Vec3d targetPos = target.getPos().add(0.0D, target.getHeight() * 0.6D, 0.0D);
            direction = targetPos.subtract(start).normalize();
        } else {
            direction = projectile.getVelocity().multiply(-1.0D).normalize();
        }
        double speed = Math.max(projectile.getVelocity().length(), 0.6D);
        Vec3d spawnPos = projectile.getPos().add(direction.multiply(0.75D));
        copiedProjectile.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
        copiedProjectile.setVelocity(direction.multiply(speed));
        copiedProjectile.velocityModified = true;
        copiedProjectile.setYaw((float)(MathHelper.atan2(direction.x, direction.z) * MathHelper.DEGREES_PER_RADIAN));
        copiedProjectile.setPitch((float)(-(MathHelper.atan2(direction.y, Math.sqrt(direction.x * direction.x + direction.z * direction.z)) * MathHelper.DEGREES_PER_RADIAN)));
        return copiedProjectile;
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
        controllers.add(new AnimationController<>(this, "idle_controller", 0, this::idle));
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

    @Override
    public List<ParticleEffect> getDeathParticles() {
        return List.of(ParticleTypes.LARGE_SMOKE, ParticleRegistry.NIGHTFALL_PARTICLE);
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
        IDLE, SPAWN, OBLITERATE, BLIND, SUMMON, RUPTURE, UNBREAKABLE, MACE_OF_SPADES_1, MACE_OF_SPADES_2, MACE_OF_SPADES_3, MACE_OF_SPADES_4_SPIN, SEISMIC_WAVE
    }
}
