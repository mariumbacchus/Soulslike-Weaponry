package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.ai.goal.NightProwlerGoal;
import net.soulsweaponry.entity.logic.BlackflameSnakeLogic;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.CustomDeathHandler;
import net.soulsweaponry.util.ParticleHandler;
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

import java.util.Optional;
import java.util.UUID;

public class NightProwler extends BossEntity implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public int deathTicks;
    public int phaseTwoTicks;
    public int spawnTicks;
    public int phaseTwoMaxTransitionTicks = 120;
    public int maxSpawnTicks = 50;
    public int darknessRiseTicks;
    private int[] aliveSummons = new int[0];
    @Nullable private BlackflameSnakeLogic blackflameSnakeLogic = null;
    public static final int ATTACKS_LENGTH = Attacks.values().length;
    private static final TrackedData<Integer> ATTACKS = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> INITIATING_PHASE_2 = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_PHASE_2 = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_FLYING = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Optional<UUID>> PARTNER_UUID = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Integer> REMAINING_ANI_TICKS = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<BlockPos> TARGET_POS = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Boolean> CHASE_TARGET = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> WAIT_ANIMATION = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SPAWN_PARTICLES_STATE = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> DARKNESS_RISE = DataTracker.registerData(NightProwler.class, TrackedDataHandlerRegistry.BOOLEAN);


    public NightProwler(EntityType<? extends NightProwler> entityType, World world) {
        super(entityType, world, Color.PURPLE);
        this.drops.add(WeaponRegistry.SOUL_REAPER);
        this.drops.add(WeaponRegistry.FORLORN_SCYTHE);
        this.drops.add(ItemRegistry.LORD_SOUL_NIGHT_PROWLER);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new NightProwlerGoal(this, 0.75D, true));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true, entity -> !this.isPartner(entity)));
        this.targetSelector.add(5, (new RevengeGoal(this)).setGroupRevenge());
    }

    public void setAttackAnimation(NightProwler.Attacks attack) {
        for (int i = 0; i < ATTACKS_LENGTH; i++) {
            if (NightProwler.Attacks.values()[i].equals(attack)) {
                this.dataTracker.set(ATTACKS, i);
            }
        }
    }

    public NightProwler.Attacks getAttackAnimation() {
        return NightProwler.Attacks.values()[this.dataTracker.get(ATTACKS)];
    }

    public boolean isInitiatingPhaseTwo() {
        return this.dataTracker.get(INITIATING_PHASE_2);
    }

    public void setInitiatePhaseTwo(boolean bl) {
        this.dataTracker.set(INITIATING_PHASE_2, bl);
    }

    private <E extends IAnimatable> PlayState attacks(AnimationEvent<E> event) {
        if (this.isDead()) return PlayState.STOP;
        if (this.isInitiatingPhaseTwo()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("start_phase_2", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        } else if (this.getAttackAnimation().equals(NightProwler.Attacks.SPAWN)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("spawn_1", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else {
            if (!this.isPhaseTwo()) {
                switch (this.getAttackAnimation()) {
                    case TRINITY -> event.getController().setAnimation(new AnimationBuilder().addAnimation("trinity_1", ILoopType.EDefaultLoopTypes.LOOP));
                    case REAPING_SLASH -> event.getController().setAnimation(new AnimationBuilder().addAnimation("reaping_slash_1", ILoopType.EDefaultLoopTypes.LOOP));
                    case NIGHTS_EMBRACE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("nights_embrace_1", ILoopType.EDefaultLoopTypes.LOOP));
                    case RIPPLE_FANG -> event.getController().setAnimation(new AnimationBuilder().addAnimation("ripple_fang_1", ILoopType.EDefaultLoopTypes.LOOP));
                    case BLADES_REACH -> event.getController().setAnimation(new AnimationBuilder().addAnimation("blades_reach_1", ILoopType.EDefaultLoopTypes.LOOP));
                    case SOUL_REAPER -> event.getController().setAnimation(new AnimationBuilder().addAnimation("soul_reaper_1", ILoopType.EDefaultLoopTypes.LOOP));
                    case DIMINISHING_LIGHT -> event.getController().setAnimation(new AnimationBuilder().addAnimation("diminishing_light_1", ILoopType.EDefaultLoopTypes.LOOP));
                    case DARKNESS_RISE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("darkness_rise_1", ILoopType.EDefaultLoopTypes.LOOP));
                    case ENGULF -> event.getController().setAnimation(new AnimationBuilder().addAnimation("engulf_1", ILoopType.EDefaultLoopTypes.LOOP));
                    case BLACKFLAME_SNAKE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("blackflame_snake_1", ILoopType.EDefaultLoopTypes.LOOP));
                    case DEATHBRINGERS_GRASP -> event.getController().setAnimation(new AnimationBuilder().addAnimation("deaths_grasp_1", ILoopType.EDefaultLoopTypes.LOOP));
                    default -> event.getController().setAnimation(new AnimationBuilder().addAnimation("empty_1", ILoopType.EDefaultLoopTypes.LOOP));
                }
            } else {
                switch (this.getAttackAnimation()) {
                    case TRINITY -> event.getController().setAnimation(new AnimationBuilder().addAnimation("trinity_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case NIGHTS_EMBRACE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("nights_embrace_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case RIPPLE_FANG -> event.getController().setAnimation(new AnimationBuilder().addAnimation("ripple_fang_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case BLADES_REACH -> event.getController().setAnimation(new AnimationBuilder().addAnimation("blades_reach_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case SOUL_REAPER -> event.getController().setAnimation(new AnimationBuilder().addAnimation("soul_reaper_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case DIMINISHING_LIGHT -> event.getController().setAnimation(new AnimationBuilder().addAnimation("diminishing_light_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case DARKNESS_RISE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("darkness_rise_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case ECLIPSE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("eclipse_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case ENGULF -> event.getController().setAnimation(new AnimationBuilder().addAnimation("engulf_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case BLACKFLAME_SNAKE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("blackflame_snake_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case LUNAR_DISPLACEMENT -> event.getController().setAnimation(new AnimationBuilder().addAnimation("lunar_displacement_2", ILoopType.EDefaultLoopTypes.LOOP));
                    case DEATHBRINGERS_GRASP -> event.getController().setAnimation(new AnimationBuilder().addAnimation("deaths_grasp_2", ILoopType.EDefaultLoopTypes.LOOP));
                    default -> event.getController().setAnimation(new AnimationBuilder().addAnimation("empty_2", ILoopType.EDefaultLoopTypes.LOOP));
                }
            }
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState idle(AnimationEvent<E> event) {
        if (this.isInitiatingPhaseTwo()) {
            return PlayState.STOP;
        }
        if (this.isDead() || this.getAttackAnimation().equals(Attacks.DEATH) || this.getDeathTicks() > 0) {
            if (this.isPhaseTwo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("death_2", ILoopType.EDefaultLoopTypes.LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("death_1", ILoopType.EDefaultLoopTypes.LOOP));
            }
        } else {
            if (!this.getAttackAnimation().equals(Attacks.IDLE)) {
                return PlayState.STOP;
            } else {
                if (this.isPhaseTwo()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("wings_2", ILoopType.EDefaultLoopTypes.LOOP));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("wings_1", ILoopType.EDefaultLoopTypes.LOOP));
                }
            }
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState cape(AnimationEvent<E> event) {
        if (!this.isInitiatingPhaseTwo() && this.isPhaseTwo()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("cape_2", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState flying(AnimationEvent<E> event) {
        if (!this.isFlying()) {
            return PlayState.STOP;
        } else {
            if (this.isPhaseTwo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("flying_2", ILoopType.EDefaultLoopTypes.LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("flying_1", ILoopType.EDefaultLoopTypes.LOOP));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACKS, 0);
        this.dataTracker.startTracking(INITIATING_PHASE_2, false);
        this.dataTracker.startTracking(IS_PHASE_2, false);
        this.dataTracker.startTracking(PARTNER_UUID, Optional.empty());
        this.dataTracker.startTracking(REMAINING_ANI_TICKS, 0);
        this.dataTracker.startTracking(IS_FLYING, false);
        this.dataTracker.startTracking(TARGET_POS, new BlockPos(0, 0, 0));
        this.dataTracker.startTracking(CHASE_TARGET, true);
        this.dataTracker.startTracking(WAIT_ANIMATION, false);
        this.dataTracker.startTracking(SPAWN_PARTICLES_STATE, 0);
        this.dataTracker.startTracking(DARKNESS_RISE, false);
    }

    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks == this.getTicksUntilDeath() && !this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(this.getWorld(), this.getPos(), SoundRegistry.DAWNBREAKER_EVENT, ParticleTypes.LARGE_SMOKE, ParticleTypes.SOUL_FIRE_FLAME);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Nullable
    public DayStalker getPartner(ServerWorld world) {
        return (DayStalker) world.getEntity(this.getPartnerUuid());
    }

    public UUID getPartnerUuid() {
        return this.dataTracker.get(PARTNER_UUID).orElse(null);
    }

    public void setPartnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(PARTNER_UUID, Optional.ofNullable(uuid));
    }

    public boolean isPartner(LivingEntity living) {
        return this.getPartnerUuid() != null && living.getUuid() != null && this.getPartnerUuid().equals(living.getUuid());
    }

    public int[] getAliveSummonsList() {
        return this.aliveSummons;
    }

    public void setAliveSummons(int[] aliveSummonsList) {
        this.aliveSummons = aliveSummonsList;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.getPartnerUuid() != null) {
            nbt.putUuid("partner_uuid", this.getPartnerUuid());
        }
        nbt.putBoolean("phase_two", this.isPhaseTwo());
        nbt.putInt("remaining_ani_ticks", this.getRemainingAniTicks());
        nbt.putBoolean("is_flying", this.isFlying());
        nbt.putBoolean("chase_target", this.shouldChaseTarget());
        nbt.putIntArray("summons", this.getAliveSummonsList());
        nbt.putInt("darknessTicks", this.getDarknessRiseTicks());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        UUID uUID = null;
        if (nbt.containsUuid("partner_uuid")) {
            uUID = nbt.getUuid("partner_uuid");
        }
        if (uUID != null) {
            try {
                this.setPartnerUuid(uUID);
            } catch (Throwable ignored) {}
        }
        if (nbt.contains("phase_two")) {
            this.setPhaseTwo(nbt.getBoolean("phase_two"));
        }
        if (nbt.contains("remaining_ani_ticks")) {
            this.setRemainingAniTicks(nbt.getInt("remaining_ani_ticks"));
        }
        if (nbt.contains("is_flying")) {
            this.setFlying(nbt.getBoolean("is_flying"));
        }
        if (nbt.contains("chase_target")) {
            this.setChaseTarget(nbt.getBoolean("chase_target"));
        }
        if (nbt.contains("summons")) {
            this.setAliveSummons(nbt.getIntArray("summons"));
        }
        if (nbt.contains("darknessTicks")) {
            this.setDarknessRiseTicks(nbt.getInt("darknessTicks"));
        }
    }

    public boolean isEmpowered() {
        return (!this.getWorld().isClient && this.getWorld().isNight()) || this.isPhaseTwo();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isPhaseTwo() ? SoundRegistry.HARD_BOSS_DEATH_LONG : SoundRegistry.HARD_BOSS_DEATH_SHORT;
    }

    @Override
    public int getTicksUntilDeath() {
        return this.isPhaseTwo() ? 140 : 80;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    @Override
    public void setDeath() {
        this.setAttackAnimation(Attacks.DEATH);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean isUndead() {
        return false;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.DEFAULT;
    }

    @Override
    public boolean disablesShield() {
        return true;
    }

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 120D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, ConfigConstructor.night_prowler_health)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 10D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.8D);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "attacks", 0, this::attacks));
        data.addAnimationController(new AnimationController<>(this, "idle", 0, this::idle));
        data.addAnimationController(new AnimationController<>(this, "cape", 0, this::cape));
        data.addAnimationController(new AnimationController<>(this, "flying", 0, this::flying));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public enum Attacks {
        IDLE, DEATH, SPAWN, TRINITY, REAPING_SLASH, NIGHTS_EMBRACE, RIPPLE_FANG, BLADES_REACH, SOUL_REAPER,
        DIMINISHING_LIGHT, DARKNESS_RISE, ECLIPSE, ENGULF, BLACKFLAME_SNAKE, LUNAR_DISPLACEMENT, DEATHBRINGERS_GRASP
    }

    @Override
    public int getXp() {
        return ConfigConstructor.night_prowler_xp;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (!this.getWorld().isClient) {
            LivingEntity partner = this.getPartner((ServerWorld) this.getWorld());
            if (!this.isPhaseTwo() && (partner == null || partner.isDead())) {
                this.setInitiatePhaseTwo(true);
                this.setFlying(false);
            }
        }
        if (this.isInitiatingPhaseTwo()) {
            this.phaseTwoTicks++;
            int maxHealTicks = this.phaseTwoMaxTransitionTicks - 40;
            float healPerTick = this.getMaxHealth() / maxHealTicks;
            this.heal(healPerTick);
            if (this.phaseTwoTicks == 78) {
                this.getWorld().playSound(null, this.getBlockPos(), SoundRegistry.PARTNER_DIES, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.phaseTwoTicks == 81) {
                this.getWorld().playSound(null, this.getBlockPos(), SoundRegistry.DAWNBREAKER_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                if (!getWorld().isClient) {
                    ParticleHandler.particleSphereList(this.getWorld(), 1000, this.getX(), this.getY(), this.getZ(), 1f, ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.LARGE_SMOKE);
                }
                NightProwlerGoal placeHolder = new NightProwlerGoal(this, 1D, true);
                placeHolder.aoe(this.getBoundingBox().expand(4D), 50f, 4f, true);
            }
            if (this.phaseTwoTicks >= phaseTwoMaxTransitionTicks) {
                this.setPhaseTwo(true);
                this.setInitiatePhaseTwo(false);
            }
        }
        if (this.getAttackAnimation().equals(NightProwler.Attacks.SPAWN)) {
            this.spawnTicks++;
            if (this.spawnTicks >= this.maxSpawnTicks) {
                this.setAttackAnimation(NightProwler.Attacks.IDLE);
            }
        }
        this.setRemainingAniTicks(Math.max(this.getRemainingAniTicks() - 1, 0));
        if (this.getRemainingAniTicks() <= 0 && this.shouldWaitAnimation()) {
            this.setWaitAnimation(false);
            this.setAttackAnimation(NightProwler.Attacks.IDLE);
            this.getNavigation().stop();
        }
        if (this.getDarknessRise()) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 40, 1));
            this.darknessRiseTicks++;
            float r = 4.5f;
            Box box = new Box(this.getPos().add(r, 1, r), this.getPos().add(-r, -1, -r));
            for (Entity entity : this.getWorld().getOtherEntities(this, box)) {
                if (this.darknessRiseTicks % 4 == 0 && entity instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(EffectRegistry.DECAY, 60, 0));
                    living.damage(DamageSource.MAGIC, 1f);
                }
            }
            if (this.getDarknessRiseTicks() >= (this.isPhaseTwo() ? 200 : 120)) {
                this.setDarknessRise(false);
                this.darknessRiseTicks = 0;
            }
        }
        if (this.getBlackflameSnakeLogic() != null) {
            this.getBlackflameSnakeLogic().tick(this.getWorld());
            if (this.getBlackflameSnakeLogic().isFinished()) {
                this.setBlackflameSnakeLogic(null);
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInitiatingPhaseTwo()) {
            return false;
        }
        if (source.isFromFalling()) {
            return false;
        }
        if (this.isEmpowered() && this.getAttackAnimation().equals(Attacks.IDLE) && !this.isFlying()
                && this.random.nextDouble() < ConfigConstructor.night_prowler_teleport_chance * (source.isProjectile() ? 1.5f : 1)
                && source.getAttacker() instanceof LivingEntity attacker) {
            if (this.squaredDistanceTo(attacker) > 250D) {
                double x = attacker.getX() + this.random.nextInt(12) - 6;
                double y = attacker.getY();
                double z = attacker.getZ() + this.random.nextInt(12) - 6;
                if (this.teleportTo(x, y, z)) {
                    return false;
                }
            } else {
                if (this.teleportAway()) {
                    return false;
                }
            }
        }
        if (this.isEmpowered() && source.isProjectile() &&
                this.getHealth() < this.getMaxHealth() * ConfigConstructor.night_prowler_projectile_heal_below_percent_health) {
            this.playSound(SoundEvents.BLOCK_BEACON_POWER_SELECT, 1f, 1f);
            this.heal(ConfigConstructor.night_prowler_projectile_heal_amount);
            return false;
        }
        if (this.getAttackAnimation().equals(Attacks.ECLIPSE)) {
            amount = amount * 0.75f;
        }
        if (source.isExplosive()) {
            amount = amount * 0.25f;
        }
        return super.damage(source, amount);
    }

    public boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > this.getWorld().getBottomY() && !this.getWorld().getBlockState(mutable).getMaterial().blocksMovement()) {
            mutable.move(Direction.DOWN);
        }
        BlockState blockState = this.getWorld().getBlockState(mutable);
        boolean bl = blockState.getMaterial().blocksMovement();
        boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
        if (!bl || bl2) {
            return false;
        }
        Vec3d vec3d = this.getPos();
        boolean bl3 = this.teleport(x, y, z, true);
        if (bl3) {
            this.getWorld().emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(this));
            if (!this.isSilent()) {
                this.getWorld().playSound(null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f);
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            }
        }
        return bl3;
    }

    public boolean teleportAway() {
        if (this.getWorld().isClient() || !this.isAlive()) {
            return false;
        }
        double d = this.getX() + (this.getRandom().nextDouble() - 0.5) * 32;
        double e = this.getY() + (double)(this.getRandom().nextInt(16) - 4);
        double f = this.getZ() + (this.getRandom().nextDouble() - 0.5) * 32;
        return this.teleportTo(d, e, f);
    }

    public void setRemainingAniTicks(int ticks) {
        this.dataTracker.set(REMAINING_ANI_TICKS, ticks);
    }

    public int getRemainingAniTicks() {
        return this.dataTracker.get(REMAINING_ANI_TICKS);
    }

    public void setPhaseTwo(boolean bl) {
        this.dataTracker.set(IS_PHASE_2, bl);
    }

    public boolean isPhaseTwo() {
        return this.dataTracker.get(IS_PHASE_2);
    }

    public void setFlying(boolean bl) {
        this.dataTracker.set(IS_FLYING, bl);
    }

    public boolean isFlying() {
        return this.dataTracker.get(IS_FLYING);
    }

    public void setTargetPos(BlockPos pos) {
        this.dataTracker.set(TARGET_POS, pos);
    }

    public BlockPos getTargetPos() {
        return this.dataTracker.get(TARGET_POS);
    }

    public void setChaseTarget(boolean bl) {
        this.dataTracker.set(CHASE_TARGET, bl);
    }

    public boolean shouldChaseTarget() {
        return this.dataTracker.get(CHASE_TARGET);
    }

    public void setWaitAnimation(boolean bl) {
        this.dataTracker.set(WAIT_ANIMATION, bl);
    }

    public boolean shouldWaitAnimation() {
        return this.dataTracker.get(WAIT_ANIMATION);
    }

    /**
     * Sets particle state.
     * <p>- 0 is idle or false</p>
     * <p>- 1 is TRINITY</p>
     * <p>- 2 is SOUL_REAPER/ENGULF slam attack</p>
     * <p>- 3 is SOUL_REAPER X-slash attack</p>
     * <p>- 4 is ECLIPSE giant circle</p>
     * @param type Set the particle state
     */
    public void setParticleState(int type) {
        this.dataTracker.set(SPAWN_PARTICLES_STATE, type);
    }

    public int getParticleState() {
        return this.dataTracker.get(SPAWN_PARTICLES_STATE);
    }

    public void setDarknessRise(boolean bl) {
        this.dataTracker.set(DARKNESS_RISE, bl);
    }

    public boolean getDarknessRise() {
        return this.dataTracker.get(DARKNESS_RISE);
    }

    public void setDarknessRiseTicks(int ticks) {
        this.darknessRiseTicks = ticks;
    }

    public int getDarknessRiseTicks() {
        return this.darknessRiseTicks;
    }

    public void setBlackflameSnakeLogic(@Nullable BlackflameSnakeLogic object) {
        this.blackflameSnakeLogic = object;
    }

    @Nullable
    public BlackflameSnakeLogic getBlackflameSnakeLogic() {
        return this.blackflameSnakeLogic;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getWorld().isClient) {
            if (this.getHealth() < this.getMaxHealth() * ConfigConstructor.night_prowler_projectile_heal_below_percent_health) {
                for (int i = 0; i < 2; i++) {
                    this.getWorld().addParticle(ParticleRegistry.DAZZLING_PARTICLE, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
                }
            }
            switch (this.getParticleState()) {
                case 1 -> {
                    if (this.getTargetPos() != null) {
                        for (int i = 0; i < 500; ++i) {
                            Random random = this.getRandom();
                            Vec3d pos = Vec3d.ofCenter(this.getTargetPos());
                            double d = random.nextGaussian() * 0.05D;
                            double e = random.nextGaussian() * 0.05D;
                            double newX = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d);
                            double newZ = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e);
                            double newY = (random.nextDouble() - 0.5D + random.nextDouble() * 0.5D) / 2;
                            this.getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(), newX, newY, newZ);
                            this.getWorld().addParticle(ParticleRegistry.NIGHTFALL_PARTICLE, pos.getX(), pos.getY(), pos.getZ(), newX, newY, newZ);
                            this.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY(), pos.getZ(), newX, newY, newZ);
                        }
                    }
                }
                case 2 -> {
                    if (this.getTargetPos() != null) {
                        for (int i = 0; i < 250; ++i) {
                            Random random = this.getRandom();
                            Vec3d pos = Vec3d.ofCenter(this.getTargetPos());
                            double d = random.nextGaussian() * 0.05D;
                            double e = random.nextGaussian() * 0.05D;
                            double newX = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d) / 2;
                            double newZ = (random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e) / 2;
                            double newY = (random.nextDouble() - 0.5D + random.nextDouble() * 0.5D) / 4;
                            this.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY(), pos.getZ(), newX, newY, newZ);
                            this.getWorld().addParticle(ParticleTypes.CLOUD, pos.getX(), pos.getY(), pos.getZ(), newX, newY, newZ);
                            this.getWorld().addParticle(ParticleRegistry.DARK_STAR, pos.getX(), pos.getY(), pos.getZ(), newX, newY, newZ);
                        }
                    }
                }
                case 3 -> {
                    for (int t = -90; t < 90; t++) {
                        double rad = Math.toRadians(t);
                        int out = 4;
                        float yaw = (float) Math.toRadians(this.getYaw() + 90);
                        double x1 = out * Math.cos(rad);
                        double y1 = out * Math.sin(rad);
                        double z1 = out * Math.sin(rad);
                        Vec3d vec1 = new Vec3d(x1, y1, z1).rotateY(-yaw).add(this.getEyePos().add(0, -1, 0));

                        double x2 = out * Math.cos(rad);
                        double y2 = out * Math.sin(rad);
                        double z2 = -out * Math.sin(rad);
                        Vec3d vec2 = new Vec3d(x2, y2, z2).rotateY(-yaw).add(this.getEyePos().add(0, -1, 0));

                        int div = 75;
                        this.getWorld().addParticle(ParticleRegistry.DARK_STAR, vec1.x, vec1.y, vec1.z,
                                this.random.nextGaussian()/div, this.random.nextGaussian()/div, this.random.nextGaussian()/div);
                        this.getWorld().addParticle(ParticleRegistry.DAZZLING_PARTICLE, vec2.x, vec2.y, vec2.z,
                                this.random.nextGaussian()/div, this.random.nextGaussian()/div, this.random.nextGaussian()/div);
                    }
                }
                case 4 -> {
                    double phi = Math.PI * (3. - Math.sqrt(5.));
                    int points = 600;
                    int div = 60;
                    float sizeMod = 15f;
                    for (int i = 0; i < points; i++) {
                        double y = 1 - ((double) i /(points - 1)) * 2;
                        double radius = Math.sqrt(1 - y*y);
                        double theta = phi * i;
                        double x = Math.cos(theta) * radius;
                        double z = Math.sin(theta) * radius;
                        this.getWorld().addParticle(ParticleRegistry.DARK_STAR, true, x * sizeMod + this.getX(), y * sizeMod + this.getY(), z * sizeMod + this.getZ(),
                                this.random.nextGaussian()/div, this.random.nextGaussian()/div, this.random.nextGaussian()/div);
                    }
                }
                default -> {}
            }
            if (this.getDarknessRise()) {
                int div = 30;
                for (int i = 0; i < 12; i++) {
                    float r = i / 2f;
                    for (int theta = 0; theta < 360; theta++) {
                        if (theta % 8 == 0) {
                            double x0 = this.getX();
                            double z0 = this.getZ();
                            double x = x0 + r * Math.cos(theta * Math.PI / 180);
                            double z = z0 + r * Math.sin(theta * Math.PI / 180);
                            this.getWorld().addParticle(ParticleRegistry.DARK_STAR, x, this.getY() + 0.1f, z,
                                    this.random.nextGaussian()/div, this.random.nextGaussian()/div, this.random.nextGaussian()/div);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hasNoGravity() {
        return this.isFlying();
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        if (!this.isFlying()) {
            super.fall(heightDifference, onGround, state, landedPosition);
        }
    }

    @Override
    public boolean isClimbing() {
        return !this.isFlying() && super.isClimbing();
    }

    @Override
    protected boolean shouldDropLoot() {
        return this.isPhaseTwo();
    }
}