package net.soulsweaponry.entity.mobs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.soulsweaponry.config.BossConfig;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.ai.goal.MoonknightGoal;
import net.soulsweaponry.networking.PacketHelper;
import net.soulsweaponry.networking.S2C.packets.StopBossMusicS2C;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.keyframe.event.ParticleKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class Moonknight extends BossEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    public int deathTicks;
    private int spawnTicks;
    private int phaseTransitionTicks;
    private final int phaseTransitionMaxTicks = 120;
    private int blockBreakingCooldown;
    private final List<EntityType<?>> absorbedProjectileTypes = new ArrayList<>();
    private final List<Float> absorbedProjectileDamage = new ArrayList<>();
    public float prevBeamHeight;
    @Environment(EnvType.CLIENT)
    public float renderBeamHeight;

    private static final TrackedData<Boolean> SPAWNING = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> INITIATE_PHASE_2 = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> PHASE_2 = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CAN_BEAM = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_SWORD_CHARGING = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ATTACK = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<BlockPos> BEAM_LOCATION = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Float> BEAM_HEIGHT = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> INITIATED_PHASE_2 = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);

    public Moonknight(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world, Color.WHITE);
    }

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
        .add(EntityAttributes.FOLLOW_RANGE, 50D)
        .add(EntityAttributes.MAX_HEALTH, BossConfig.fallen_icon_health)
        .add(EntityAttributes.MOVEMENT_SPEED, 0.15D)
        .add(EntityAttributes.ATTACK_DAMAGE, 15.0D)
        .add(EntityAttributes.KNOCKBACK_RESISTANCE, 10.0D)
        .add(EntityAttributes.ARMOR, BossConfig.fallen_icon_armor);
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return fallDistance > 6 ? super.computeFallDamage(fallDistance, damageMultiplier) : 0;
    }

    public void setSpawning(boolean bl) {
        this.dataTracker.set(SPAWNING, bl);
    }

    @Override
    public boolean isSpawning() {
        return this.dataTracker.get(SPAWNING);
    }

    public void initiatePhaseTwo(boolean bl) {
        this.dataTracker.set(INITIATE_PHASE_2, bl);
    }

    public boolean isInitiatingPhaseTwo() {
        return this.dataTracker.get(INITIATE_PHASE_2);
    }

    public void setPhaseTwo(boolean bl) {
        this.dataTracker.set(PHASE_2, bl);
    }

    public boolean isPhaseTwo() {
        return this.dataTracker.get(PHASE_2);
    }

    public void setCanBeam(boolean bl) {
        this.dataTracker.set(CAN_BEAM, bl);
    }

    public boolean getCanBeam() {
        return this.dataTracker.get(CAN_BEAM);
    }

    public void setBeamLocation(BlockPos pos) {
        this.dataTracker.set(BEAM_LOCATION, pos);
    }
    
    public BlockPos getBeamLocation() {
        return this.dataTracker.get(BEAM_LOCATION);
    }

    public void setBeamHeight(float fl) {
        this.dataTracker.set(BEAM_HEIGHT, fl);
    }

    public float getBeamHeight() {
        return this.dataTracker.get(BEAM_HEIGHT);
    }

    public void setChargingSword(boolean bl) {
        this.dataTracker.set(IS_SWORD_CHARGING, bl);
    }

    public boolean isSwordCharging() {
        return this.dataTracker.get(IS_SWORD_CHARGING);
    }

    public void setInitiatedPhaseTwo(boolean bl) {
        this.dataTracker.set(INITIATED_PHASE_2, bl);
    }

    public boolean initiatedPhaseTwo() {
        return this.dataTracker.get(INITIATED_PHASE_2);
    }

    public List<EntityType<?>> getAbsorbedProjectileTypes() {
        return absorbedProjectileTypes;
    }

    public List<Float> getAbsorbedProjectileDamage() {
        return absorbedProjectileDamage;
    }

    public void clearAbsorbedProjectiles() {
        this.absorbedProjectileTypes.clear();
        this.absorbedProjectileDamage.clear();
    }

    public void setPhaseOneAttack(MoonknightPhaseOne phaseOneAttack) {
        for (int i = 0; i < MoonknightPhaseOne.values().length; i++) {
            if (MoonknightPhaseOne.values()[i].equals(phaseOneAttack)) {
                this.dataTracker.set(ATTACK, i);
                return;
            }
        }
    }

    public MoonknightPhaseOne getPhaseOneAttack() {
        return MoonknightPhaseOne.values()[this.dataTracker.get(ATTACK)];
    }

    public void setPhaseTwoAttack(MoonknightPhaseTwo phaseTwoAttack) {
        for (int i = 0; i < MoonknightPhaseTwo.values().length; i++) {
            if (MoonknightPhaseTwo.values()[i].equals(phaseTwoAttack)) {
                this.dataTracker.set(ATTACK, i);
                return;
            }
        }
    }

    public MoonknightPhaseTwo getPhaseTwoAttack() {
        return MoonknightPhaseTwo.values()[this.dataTracker.get(ATTACK)];
    }

    @Override
    public boolean damage(ServerWorld serverWorld, DamageSource source, float amount) {
        if (this.blockBreakingCooldown <= 0) {
            this.blockBreakingCooldown = 20;
        }
        if (this.isInitiatingPhaseTwo()) {
            return false;
        }
        if (!this.isPhaseTwo() && this.getHealth() - amount < 1f) {
            PacketHelper.sendToAllPlayersS2C(serverWorld, this.getBlockPos(), new StopBossMusicS2C(this.getBossMusic().id()));
            this.setPlayingMusic(false);
            this.clearStatusEffects();
            this.initiatePhaseTwo(true);
            getWorld().playSound(null, this.getBlockPos(), SoundRegistry.KNIGHT_DEATH_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            return false;
        }
        if (this.isInvulnerableTo(serverWorld, source)) {
           return false;
        } else {
            Entity entity = source.getSource();
            if (entity instanceof ProjectileEntity projectile && !this.isProjectileWhitelisted(projectile) && entity.getBlockPos() != null) {
                ParticleHandler.particleSphereList(this.getWorld(), 10, entity.getX(), entity.getY(), entity.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                if (projectile.getOwner() != null && projectile.getOwner().equals(this)) {
                    projectile.discard();
                    return false;
                }
                // Add three of the same to spice the attack up
                for (int i = 0; i < 3; i++) {
                    this.absorbedProjectileTypes.add(projectile.getType());
                    this.absorbedProjectileDamage.add(amount);
                }
                return false;
            }
            return super.damage(serverWorld, source, amount);
        }
    }

    @Override
    public String[] getWhitelistedProjectiles() {
        return BossConfig.fallen_icon_projectile_immunity_whitelist;
    }

    @Override
    public String[] getBlacklistedStatusEffects() {
        return BossConfig.fallen_icon_status_effect_blacklist;
    }

    @Override
    public int getXp() {
        return (int) BossConfig.fallen_icon_xp;
    }

    @Override
    public void tick() {
        this.prevBeamHeight = this.getBeamHeight();
        super.tick();
    }

    @Override
    protected void mobTick(ServerWorld serverWorld) {
        super.mobTick(serverWorld);
        if (this.isInitiatingPhaseTwo()) {
            this.phaseTransitionTicks++;
            this.tryToPlayBossMusic();
            if (this.phaseTransitionTicks >= 40) {
                int maxHealTicks = this.phaseTransitionMaxTicks - 40;
                float healPerTick = this.getMaxHealth() / maxHealTicks;
                this.heal(healPerTick);
                if (this.phaseTransitionTicks <= 92) {
                    if (!this.getWorld().isClient) {
                        ParticleHandler.particleOutburstMap(this.getWorld(), 30, this.getX(), this.getY(), this.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
                    }
                }
            }
            if (this.phaseTransitionTicks == 89) {
                ParticleEvents.deathExplosionEvent(this.getWorld(), this.getPos(), SoundRegistry.DAWNBREAKER_EVENT, ParticleRegistry.NIGHTFALL_PARTICLE, ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.LARGE_SMOKE);
            }
            if (this.phaseTransitionTicks == 96) {
                this.setInitiatedPhaseTwo(true);
            }
            if (this.phaseTransitionTicks >= this.phaseTransitionMaxTicks) {
                this.setPhaseTwo(true);
                this.initiatePhaseTwo(false);
                this.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                this.setCustomName(Text.translatable("entity.soulsweapons.moonknight_phase_2"));
                this.bossBar.setColor(Color.BLUE);
            }
        }
        
        if (ConfigConstructor.can_bosses_break_blocks) {
            int j;
            int i;
            int k;
            if (this.blockBreakingCooldown > 0) {
                --this.blockBreakingCooldown;
                if (this.blockBreakingCooldown == 0 && serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    i = MathHelper.floor(this.getY());
                    j = MathHelper.floor(this.getX());
                    k = MathHelper.floor(this.getZ());
                    for (int l = -3; l <= 3; ++l) {
                        for (int m = -3; m <= 3; ++m) {
                            for (int n = 0; n <= 8; ++n) {
                                if (!(this.getWorld().getBlockState(new BlockPos(j + l, i + n, k + m)).getBlock() instanceof BlockWithEntity)) {
                                    this.getWorld().breakBlock(new BlockPos(j + l, i + n, k + m), true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public SoundEvent getBossMusic() {
        return SoundRegistry.FALLEN_ICON_SONG;
    }

    @Override
    public boolean hasBossMusic() {
        return true;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.isSpawning()) {
            this.spawnTicks++;
            this.summonParticles();
            if (this.spawnTicks % 10 == 0) {
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.spawnTicks >= 80) {
                this.setSpawning(false);
                this.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
            }
        }
        if (this.getWorld().isClient && !this.isDead() && this.isPhaseTwo() && this.getPhaseTwoAttack().equals(MoonknightPhaseTwo.CORE_BEAM) && this.getCanBeam() && !this.isPosNullish(this.getBeamLocation())) {
            Vec3d start = this.getPos().add(0, 6f, 0);
            Vec3d end = this.getBeamLocation().toCenterPos().add(0, this.renderBeamHeight, 0);
            Vec3d between = new Vec3d(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
            int numberOfParticles = 20;
            double stepSize = 1.0 / numberOfParticles;
            for (double progress = 0; progress < 1.0; progress += stepSize) {
                Vec3d particlePos = start.add(between.multiply(progress));
                //Looks like wind blows particles away
                this.getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, particlePos.getX(), particlePos.getY(), particlePos.getZ(), this.random.nextDouble() - .05f, this.random.nextDouble() - .05f, this.random.nextDouble() - .05f);
            }
            double d = this.random.nextGaussian() * 0.05D;
            double q = this.random.nextGaussian() * 0.05D;
            for(int i = 0; i < 2; ++i) {
                double newX = this.random.nextDouble() - 0.5D + this.random.nextGaussian() * 0.15D + d;
                double newZ = this.random.nextDouble() - 0.5D + this.random.nextGaussian() * 0.15D + q;
                double newY = this.random.nextDouble() - 0.5D + this.random.nextDouble() * 0.5D;
                this.getWorld().addParticle(ParticleTypes.WAX_OFF, this.getX(), this.getY() + 5.5f, this.getZ(), newX*25, newY*18, newZ*25);
            }
        }
        if (this.isPhaseTwo() && !this.isDead() && this.isSwordCharging()) {
            if (this.getPhaseTwoAttack().equals(MoonknightPhaseTwo.IDLE)) this.setChargingSword(false);
        }
    }

    private boolean isPosNullish(BlockPos pos) {
        return pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0;
    }

    private void summonParticles() {
        if (getWorld().isClient) {
            for(int i = 0; i < 50; ++i) {
                Random random = this.getRandom();
                double d = random.nextGaussian() * 0.05D;
                double e = random.nextGaussian() * 0.05D;
                double newX = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d;
                double newZ = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e;
                double newY = random.nextDouble() - 0.5D + random.nextDouble() * 0.5D;
                getWorld().addParticle(ParticleTypes.SOUL, this.getX(), this.getY(), this.getZ(), newX/2, newY/2, newZ/2);
                getWorld().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), newX/2, newY/2, newZ/2);
            }
        }
    }

    @Override
    public int getTicksUntilDeath() {
        return 100;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    @Override
    public void setDeath() {
    }

    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks == 40 && this.getBlockPos() != null) this.getWorld().playSound(null, this.getBlockPos(), SoundRegistry.KNIGHT_DEATH_LAUGH_EVENT, SoundCategory.HOSTILE , 1f, 1f);
        if (this.deathTicks >= this.getTicksUntilDeath() && !this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            ParticleEvents.deathExplosionEvent(this.getWorld(), this.getPos(), SoundRegistry.DAWNBREAKER_EVENT, ParticleRegistry.NIGHTFALL_PARTICLE, ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.LARGE_SMOKE);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public boolean isFireImmune() {
        return BossConfig.fallen_icon_is_fire_immune;
    }

    @Override
    public boolean hasInvertedHealingAndHarm() {
        return BossConfig.fallen_icon_has_inverted_heal_and_harm;
    }

    @Override
    public boolean disablesShield() {
        return BossConfig.fallen_icon_disables_shields;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<Moonknight> main = new AnimationController<>(this, "main", 0, this::mainAnimations);
        AnimationController<Moonknight> cape = new AnimationController<>(this, "cape", 0, this::cape);
        AnimationController<Moonknight> phase = new AnimationController<>(this, "phase", 0, this::phase);
        AnimationController<Moonknight> heart = new AnimationController<>(this, "heart", 0, this::heart);
        controllers.add(main);
        controllers.add(cape);
        controllers.add(phase);
        controllers.add(heart);
        main.setParticleKeyframeHandler(this::particleListener);
    }

    private void particleListener(ParticleKeyframeEvent<Moonknight> moonknightParticleKeyframeEvent) {
        this.setChargingSword(!this.isSwordCharging());
    }

    @Override
	protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MoonknightGoal(this));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 12.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(5, (new RevengeGoal(this)).setGroupRevenge());
		super.initGoals();
	}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
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

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SPAWNING, false);
        builder.add(PHASE_2, false);
        builder.add(INITIATE_PHASE_2, false);
        builder.add(CAN_BEAM, false);
        builder.add(IS_SWORD_CHARGING, false);
        builder.add(ATTACK, 0);
        builder.add(BEAM_LOCATION, BlockPos.ORIGIN);
        builder.add(BEAM_HEIGHT, 0f);
        builder.add(INITIATED_PHASE_2, false);
    }

    private PlayState heart(AnimationState<?> state) {
        state.getController().setAnimation(RawAnimation.begin().thenPlay("idle_heart"));
        return PlayState.CONTINUE;
    }

    private PlayState phase(AnimationState<?> state) {
        if (this.isInitiatingPhaseTwo()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
        } else if (this.isPhaseTwo()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("phase_2"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("phase_1"));
        }
        return PlayState.CONTINUE;
    }

    private PlayState cape(AnimationState<?> state) {
        state.getController().setAnimation(RawAnimation.begin().thenPlay("idle_cape"));
        return PlayState.CONTINUE;
    }

    private PlayState mainAnimations(AnimationState<?> state) {
        if (this.isDead()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("death_phase_2"));
        } else if (this.isSpawning()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("spawn_phase_1"));
        } else if (this.isInitiatingPhaseTwo()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("initiate_phase_2"));
        } else {
            if (this.isPhaseTwo()) {
                switch (this.getPhaseTwoAttack()) {
                    case BLINDING_LIGHT ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("blinding_light"));
                    case CORE_BEAM ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("core_beam_phase_2"));
                    case IDLE -> {
                        if (this.isAttacking()) {
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("walk"));
                        } else {
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
                        }
                    }
                    case MOONFALL ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("obliterate_phase_2"));
                    case MOONVEIL ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("moon_explosion_phase_2"));
                    case SWORD_OF_LIGHT ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("sword_of_light_phase_2"));
                    case THRUST -> state.getController().setAnimation(RawAnimation.begin().thenPlay("thrust_phase_2"));
                    case RUPTURE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("rupture_phase_2"));
                    case HEAVY_SWING -> state.getController().setAnimation(RawAnimation.begin().thenPlay("heavy_swing_phase_2"));
                    case UNBREAKABLE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("unbreakable"));
                }
            } else {
                switch (this.getPhaseOneAttack()) {
                    case BLINDING_LIGHT ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("blinding_light"));
                    case IDLE -> {
                        if (this.isAttacking()) {
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("walk"));
                        } else {
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
                        }
                    }
                    case MACE_OF_SPADES ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("mace_of_spades_phase_1"));
                    case OBLITERATE ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("obliterate_phase_1"));
                    case RUPTURE ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("rupture_phase_1"));
                    case SUMMON ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("summon_warriors_phase_1"));
                    case UNBREAKABLE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("unbreakable"));
                }
            }
        }
        return PlayState.CONTINUE;
    }

    public enum MoonknightPhaseOne {
        IDLE,
        MACE_OF_SPADES,
        OBLITERATE,
        SUMMON,
        RUPTURE,
        BLINDING_LIGHT,
        UNBREAKABLE
    }

    public enum MoonknightPhaseTwo {
        IDLE,
        SWORD_OF_LIGHT,
        MOONFALL,
        MOONVEIL,
        THRUST,
        BLINDING_LIGHT,
        CORE_BEAM,
        RUPTURE,
        HEAVY_SWING,
        UNBREAKABLE
    }
    /* 
     * NB!!! So there was a bug in the Goal class where while using a certain attack, all attacks would stop.
     * That was because the index of the attack in phase 2 enum and the phase one spawn enum were the same 
     * (it was SWORD_OF_LIGHT vs SPAWN). So even though they were two different constants, it would
     * still think that when it attacked, it was spawning, and since the goal stops ticking if it is spawning (to
     * prevent the boss from attacking early), the boss would simply freeze forever. 
     * It could have something to do with the fact that the boss passes integers it generates based on the enum value
     * array index, haven't bothered looking into it. All I know is that in my desperate attempt to make the code better
     * and more readable, it got worse and just overall chaotic. At least I have learned my lesson.
     * Will I ever re-do the whole thing again one day? Maybe.
     */
}
