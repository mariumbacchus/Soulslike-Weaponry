package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
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
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.ai.goal.MoonknightGoal;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import net.soulsweaponry.util.ParticleNetworking;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.ParticleKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;

public class Moonknight extends BossEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    public int deathTicks;
    private int spawnTicks;
    private int unbreakableTicks;
    private int phaseTransitionTicks;
    private final int phaseTransitionMaxTicks = 120;
    private int blockBreakingCooldown;

    private static final TrackedData<Boolean> SPAWNING = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> UNBREAKABLE = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> INITIATE_PHASE_2 = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> PHASE_2 = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CAN_BEAM = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_SWORD_CHARGING = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ATTACK = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<BlockPos> BEAM_LOCATION = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Float> BEAM_LENGTH = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> BEAM_HEIGHT = DataTracker.registerData(Moonknight.class, TrackedDataHandlerRegistry.FLOAT);
    
    public Moonknight(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world, Color.WHITE);
        this.setDrops(WeaponRegistry.MOONLIGHT_GREATSWORD);
        this.setDrops(ItemRegistry.LORD_SOUL_WHITE);
        this.setDrops(ItemRegistry.ESSENCE_OF_LUMINESCENCE);
        this.setDrops(ItemRegistry.ARKENSTONE);
    }

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50D)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, ConfigConstructor.fallen_icon_health)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10.0D)
        .add(EntityAttributes.GENERIC_ARMOR, 20.0D);
    }

    public void setSpawning(boolean bl) {
        this.dataTracker.set(SPAWNING, bl);
    }

    public boolean getSpawning() {
        return this.dataTracker.get(SPAWNING);
    }

    public void setUnbreakable(boolean bl) {
        this.dataTracker.set(UNBREAKABLE, bl);
    }

    public boolean getUnbreakable() {
        return this.dataTracker.get(UNBREAKABLE);
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

    public void setBeamLength(float fl) {
        this.dataTracker.set(BEAM_LENGTH, fl);
    }

    private float getBeamLength() {
        return this.dataTracker.get(BEAM_LENGTH);
    }

    public void setBeamHeight(float fl) {
        this.dataTracker.set(BEAM_HEIGHT, fl);
    }

    private float getBeamHeight() {
        return this.dataTracker.get(BEAM_HEIGHT);
    }

    public void setChargingSword(boolean bl) {
        this.dataTracker.set(IS_SWORD_CHARGING, bl);
    }

    public boolean isSwordCharging() {
        return this.dataTracker.get(IS_SWORD_CHARGING);
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
    public boolean damage(DamageSource source, float amount) {
        if (this.blockBreakingCooldown <= 0) {
            this.blockBreakingCooldown = 20;
        }
        if (this.isInitiatingPhaseTwo()) {
            return false;
        }
        if (!this.isPhaseTwo() && this.getHealth() - amount < 1f) {
            this.initiatePhaseTwo(true);
            world.playSound(null, this.getBlockPos(), SoundRegistry.KNIGHT_DEATH_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            return false;
        }
        if (this.isInvulnerableTo(source)) {
           return false;
        } else {
            Entity entity = source.getSource();
            if (entity instanceof ProjectileEntity && entity.getBlockPos() != null) {
                if (!this.world.isClient) ParticleNetworking.sendServerParticlePacket((ServerWorld) this.world, PacketRegistry.DARK_EXPLOSION_ID, entity.getBlockPos(), 10);
                return false;
            }
            return super.damage(source, amount);
        }
    }

    @Override
    public int getXp() {
        return ConfigConstructor.fallen_icon_xp;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.isInitiatingPhaseTwo()) {
            this.phaseTransitionTicks++;
            if (this.phaseTransitionTicks >= 40) {
                int maxHealTicks = this.phaseTransitionMaxTicks - 40;
                float healPerTick = this.getMaxHealth() / maxHealTicks;
                this.heal(healPerTick);
                if (this.phaseTransitionTicks <= 92) {
                    this.summonParticles();
                }
            }
            if (this.phaseTransitionTicks == 89) {
                CustomDeathHandler.deathExplosionEvent(world, this.getBlockPos(), true, SoundRegistry.DAWNBREAKER_EVENT);
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
                if (this.blockBreakingCooldown == 0 && this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    i = MathHelper.floor(this.getY());
                    j = MathHelper.floor(this.getX());
                    k = MathHelper.floor(this.getZ());
                    for (int l = -3; l <= 3; ++l) {
                        for (int m = -3; m <= 3; ++m) {
                            for (int n = 0; n <= 8; ++n) {
                                if (!(this.world.getBlockState(new BlockPos(j + l, i + n, k + m)).getBlock() instanceof BlockWithEntity)) {
                                    this.world.breakBlock(new BlockPos(j + l, i + n, k + m), true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getSpawning()) {
            this.spawnTicks++;
            this.summonParticles();
            if (this.spawnTicks % 10 == 0) {
                this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.spawnTicks >= 80) {
                this.setSpawning(false);
                this.setUnbreakable(true);
            }
        }
        if (!this.isDead() && !this.isPhaseTwo() && this.getUnbreakable()) {
            this.unbreakableTicks++;
            if (this.unbreakableTicks == 38) {
                this.world.playSound(null, this.getBlockPos(), SoundRegistry.NIGHTFALL_SHIELD_EVENT, SoundCategory.HOSTILE, .75f, 1f);
                for (Entity entity : world.getOtherEntities(this, this.getBoundingBox().expand(20))) {
                    if (entity instanceof LivingEntity living) {
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 400, 1));
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 400, 1));
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
                    }
                }
            }
            if (this.unbreakableTicks >= 75) {
                this.setUnbreakable(false);
                this.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
            }
        }
        if (this.isPhaseTwo() && this.getPhaseTwoAttack().equals(MoonknightPhaseTwo.CORE_BEAM) && this.getCanBeam() && !this.isPosNullish(this.getBeamLocation())) {
            double e = this.getBeamLocation().getX() - this.getX();
            double f = this.getBeamLocation().getY() - this.getEyeY() + this.getBeamHeight();
            double g = this.getBeamLocation().getZ() - this.getZ();
            double h = Math.sqrt(e * e + f * f + g * g);
            e /= h;
            f /= h;
            g /= h;
            double length = this.random.nextDouble();
            double spreader = 1000;
            int points = 50;
            for (int i = 0; i < 10; i++) {
                length += (this.getBeamLength()/100f) + this.random.nextDouble();
                this.world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + e * length, this.getEyeY() + f * length, this.getZ() + g * length, (double)this.random.nextBetween(-points, points)/spreader, (double)this.random.nextBetween(-points, points)/spreader, (double)this.random.nextBetween(-points, points)/spreader);
                this.world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + e * length, this.getEyeY() + f * length, this.getZ() + g * length, (double)this.random.nextBetween(-points, points)/spreader, (double)this.random.nextBetween(-points, points)/spreader, (double)this.random.nextBetween(-points, points)/spreader);
                this.world.addParticle(ParticleTypes.GLOW, this.getX() + e * length, this.getEyeY() + f * length, this.getZ() + g * length, (double)this.random.nextBetween(-points, points)/spreader, (double)this.random.nextBetween(-points, points)/spreader, (double)this.random.nextBetween(-points, points)/spreader);
                this.world.addParticle(ParticleTypes.WAX_OFF, this.getX() + e * length, this.getEyeY() + f * length, this.getZ() + g * length, (double)this.random.nextBetween(-points, points)/10, (double)this.random.nextBetween(-points, points)/10, (double)this.random.nextBetween(-points, points)/10);
                //Looks like wind blows particles away
                this.world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + e * length, this.getEyeY() + f * length, this.getZ() + g * length, this.random.nextDouble() - .05f, this.random.nextDouble() - .05f, this.random.nextDouble() - .05f);
            }
            double d = this.random.nextGaussian() * 0.05D;
            double q = this.random.nextGaussian() * 0.05D;
            for(int i = 0; i < 2; ++i) {
                double newX = this.random.nextDouble() - 0.5D + this.random.nextGaussian() * 0.15D + d;
                double newZ = this.random.nextDouble() - 0.5D + this.random.nextGaussian() * 0.15D + q;
                double newY = this.random.nextDouble() - 0.5D + this.random.nextDouble() * 0.5D;
                this.world.addParticle(ParticleTypes.WAX_OFF, this.getX(), this.getY() + 5.5f, this.getZ(), newX*25, newY*18, newZ*25);
            }
        }
        if (this.isPhaseTwo() && !this.isDead() && this.isSwordCharging()) {
            if (this.getPhaseTwoAttack().equals(MoonknightPhaseTwo.IDLE)) this.setChargingSword(false);
            for (int i = 0; i < 100; i++) {
                this.world.addParticle(ParticleRegistry.NIGHTFALL_PARTICLE, this.getParticleX(this.getWidth()), this.getRandomBodyY(), this.getParticleZ(this.getWidth()), 0.0D, 0.2D, 0.0D);
            }
        }
    }

    private boolean isPosNullish(BlockPos pos) {
        return pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0;
    }

    private void summonParticles() {
        if (world.isClient) {
            for(int i = 0; i < 50; ++i) {
                Random random = this.getRandom();
                double d = random.nextGaussian() * 0.05D;
                double e = random.nextGaussian() * 0.05D;
                double newX = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d;
                double newZ = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e;
                double newY = random.nextDouble() - 0.5D + random.nextDouble() * 0.5D;
                world.addParticle(ParticleTypes.SOUL, this.getX(), this.getY(), this.getZ(), newX/2, newY/2, newZ/2);
                world.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), newX/2, newY/2, newZ/2);
            }
        } else {
            ParticleNetworking.sendServerParticlePacket((ServerWorld) world, PacketRegistry.OBLITERATE_ID, this.getBlockPos(), 50);
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
        if (this.deathTicks == 40 && this.getBlockPos() != null) this.world.playSound(null, this.getBlockPos(), SoundRegistry.KNIGHT_DEATH_LAUGH_EVENT, SoundCategory.HOSTILE , 1f, 1f);
        if (this.deathTicks >= this.getTicksUntilDeath() && !this.world.isClient()) {
            this.world.sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(world, this.getBlockPos(), true, SoundRegistry.DAWNBREAKER_EVENT);
            this.remove(RemovalReason.KILLED);
        }
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

    @Override
    public double getBossMaxHealth() {
        return ConfigConstructor.fallen_icon_health;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<Moonknight> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controllers.add(controller);
        controller.setParticleKeyframeHandler(this::particleListener);
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

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SPAWNING, Boolean.FALSE);
        this.dataTracker.startTracking(PHASE_2, Boolean.FALSE);
        this.dataTracker.startTracking(INITIATE_PHASE_2, Boolean.FALSE);
        this.dataTracker.startTracking(UNBREAKABLE, Boolean.FALSE);
        this.dataTracker.startTracking(CAN_BEAM, Boolean.FALSE);
        this.dataTracker.startTracking(IS_SWORD_CHARGING, Boolean.FALSE);
        this.dataTracker.startTracking(ATTACK, 0);
        this.dataTracker.startTracking(BEAM_LOCATION, new BlockPos(0, 0, 0));
        this.dataTracker.startTracking(BEAM_LENGTH, 0f);
        this.dataTracker.startTracking(BEAM_HEIGHT, 0f);
    }

    private PlayState predicate(AnimationState<?> state) {
        if (this.isDead()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("death_phase_2"));
        } else if (this.getSpawning()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("spawn_phase_1"));
        } else if (this.getUnbreakable()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("unbreakable_phase_1"));
        } else if (this.isInitiatingPhaseTwo()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("initiate_phase_2"));
        } else {
            if (this.isPhaseTwo()) {
                switch (this.getPhaseTwoAttack()) {
                    case BLINDING_LIGHT ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("blinding_light_phase_2"));
                    case CORE_BEAM ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("core_beam_phase_2"));
                    case IDLE -> {
                        if (this.isAttacking()) {
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("walk_phase_2"));
                        } else {
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("idle_phase_2"));
                        }
                    }
                    case MOONFALL ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("obliterate_phase_2"));
                    case MOONVEIL ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("moon_explosion_phase_2"));
                    case SWORD_OF_LIGHT ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("sword_of_light_phase_2"));
                    case THRUST -> state.getController().setAnimation(RawAnimation.begin().thenPlay("thrust_phase_2"));
                }
            } else {
                switch (this.getPhaseOneAttack()) {
                    case BLINDING_LIGHT ->
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("blinding_light_phase_1"));
                    case IDLE -> {
                        if (this.isAttacking()) {
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("walk_phase_1"));
                        } else {
                            state.getController().setAnimation(RawAnimation.begin().thenPlay("idle_phase_1"));
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
                }
            }
        }
        return PlayState.CONTINUE;
    }

    public static enum MoonknightPhaseOne {
        IDLE,
        MACE_OF_SPADES,
        OBLITERATE,
        SUMMON,
        RUPTURE,
        BLINDING_LIGHT,
    }

    public static enum MoonknightPhaseTwo {
        IDLE,
        SWORD_OF_LIGHT,
        MOONFALL,
        MOONVEIL,
        THRUST,
        BLINDING_LIGHT,
        CORE_BEAM,
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
     * Will I ever re-do the whole thing again one day? Perhaps. We'll definietly see...
     */
}
