package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.ai.goal.NightShadeGoal;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class NightShade extends BossEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int spawnTicks;
    public int deathTicks;
    private boolean isCopy = false;
    private boolean healthUpdated = false;
    private boolean hasDuplicated = false;
    private int duplicateTicks;

    protected static final TrackedData<Integer> ATTACK_STATE = DataTracker.registerData(NightShade.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Boolean> CHARGING = DataTracker.registerData(NightShade.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<BlockPos> POS = DataTracker.registerData(NightShade.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public NightShade(EntityType<? extends NightShade> entityType, World world) {
        super(entityType, world, BossBar.Color.BLUE);
        this.moveControl = new ShadeMoveControl(this);
    }

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 65D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, ConfigConstructor.frenzied_shade_health)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(4, new NightShadeGoal(this));
        this.goalSelector.add(7, new Emerge());
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(5, (new RevengeGoal(this)).setGroupRevenge());
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CHARGING, Boolean.FALSE);
        this.dataTracker.startTracking(ATTACK_STATE, 0);
        this.dataTracker.startTracking(POS, new BlockPos(0,0,0));
    }

    public enum AttackStates {
        IDLE, SPAWN, DEATH, BIG_SWIPES, GENERIC_CHARGE, AOE, DUPLICATE, THROW_MOONLIGHT, SHADOW_ORBS
    }

    public void setTargetPos(BlockPos pos) {
        this.dataTracker.set(POS, pos);
    }

    public BlockPos getTargetPos() {
        return this.dataTracker.get(POS);
    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movement);
        this.checkBlockCollision();
    }

    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
        if (this.isCopy) {
            this.bossBar.setVisible(false);
            if (!this.healthUpdated) {
                this.setHealth((float)ConfigConstructor.frenzied_shade_health / 4f);
                this.healthUpdated = true;
            }
            this.experiencePoints = 20;
        }
    }

    @Override
    protected boolean shouldDropLoot() {
        return !this.isCopy();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getSpawn()) {
            this.spawnTicks++;
            if (spawnTicks >= 40) {
                this.setAttackState(AttackStates.IDLE);
            }
        }
        for(int i = 0; i < 3; ++i) {
            this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
        }
        if (!this.isCopy && !this.hasDuplicated && this.getHealth() <= this.getMaxHealth() / 2.0F) {
            this.setAttackState(AttackStates.DUPLICATE);
            this.duplicateTicks++;
            if (this.duplicateTicks == 20) {
                CustomDeathHandler.deathExplosionEvent(world, this.getPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, ParticleTypes.LARGE_SMOKE, ParticleRegistry.NIGHTFALL_PARTICLE, ParticleRegistry.DARK_STAR);
                this.getNavigation().stop();
                for (int i = -1; i <= 1; i += 2) {
                    NightShade copy = new NightShade(EntityRegistry.NIGHT_SHADE, this.world);
                    copy.setCopy(true);
                    copy.setPos(this.getX(), this.getY(), this.getZ());
                    copy.setVelocity((float) i / 10f, (float) i / 10f, - (float) i / 10f);
                    copy.setSpawn();
                    copy.setTarget(this.getTarget());
                    world.spawnEntity(copy);

                    NightShade copy2 = new NightShade(EntityRegistry.NIGHT_SHADE, this.world);
                    copy2.setCopy(true);
                    copy2.setPos(this.getX(), this.getY(), this.getZ());
                    copy2.setVelocity(- (float) i / 10f, (float) i / 10f,  (float) i / 10f);
                    copy2.setSpawn();
                    copy2.setTarget(this.getTarget());
                    world.spawnEntity(copy2);
                }
            }
            if (this.duplicateTicks >= 60) {
                this.hasDuplicated = true;
                this.setAttackState(AttackStates.IDLE);
            }
        }
    }

    public boolean isCopy() {
        return this.isCopy;
    }

    @Override
    public void setDeath() {
        this.setAttackState(AttackStates.DEATH);
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
            if (this.isCopy) {
                this.world.sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
                this.remove(RemovalReason.KILLED);
                return;
            }
            this.world.sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(world, this.getPos(), SoundRegistry.DAWNBREAKER_EVENT, ParticleTypes.LARGE_SMOKE, ParticleRegistry.NIGHTFALL_PARTICLE, ParticleRegistry.DARK_STAR);
            this.remove(RemovalReason.KILLED);
        }
    }

    public void setAttackState(AttackStates state) {
        for (int i = 0; i < AttackStates.values().length; i++) {
            if (AttackStates.values()[i] == state) {
                this.dataTracker.set(ATTACK_STATE, i);
            }
        }
    }

    public AttackStates getAttackState() {
        return AttackStates.values()[this.dataTracker.get(ATTACK_STATE)];
    }

    public boolean getCharging() {
        return this.dataTracker.get(CHARGING);
    }

    public void setCharging(boolean charging) {
        this.dataTracker.set(CHARGING, charging);
    }

    public boolean getSpawn() {
        return this.getAttackState().equals(AttackStates.SPAWN);
    }

    public void setSpawn() {
        this.setAttackState(AttackStates.SPAWN);
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
                    } else {
                        double e = NightShade.this.getTarget().getX() - NightShade.this.getX();
                        double f = NightShade.this.getTarget().getZ() - NightShade.this.getZ();
                        NightShade.this.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
                    }
                    NightShade.this.bodyYaw = NightShade.this.getYaw();
                }

            }
        }
    }

    class Emerge extends Goal {
        @Override
        public boolean canStart() {
            return NightShade.this.getTarget() == null;
        }

        @Override
        public void tick() {
            if (NightShade.this.getTarget() == null && this.isInsideWall()) {
                NightShade.this.setVelocity(0, 0.1f, 0);
            }
        }

        private boolean isInsideWall() {
            float f = NightShade.this.getDimensions(EntityPose.STANDING).width * 0.8f;
            Box box = Box.of(NightShade.this.getEyePos(), f, 1.0E-6, f);
            return BlockPos.stream(box).anyMatch(pos -> {
                BlockState blockState = NightShade.this.getWorld().getBlockState(pos);
                return !blockState.isAir() && blockState.shouldSuffocate(NightShade.this.getWorld(), pos) && VoxelShapes.matchesAnywhere(blockState.getCollisionShape(NightShade.this.getWorld(), pos).offset(pos.getX(), pos.getY(), pos.getZ()), VoxelShapes.cuboid(box), BooleanBiFunction.AND);
            });
        }
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isDead()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
        }
        switch (this.getAttackState()) {
            case IDLE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
            case SPAWN -> event.getController().setAnimation(new AnimationBuilder().addAnimation("spawn", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            case DEATH -> event.getController().setAnimation(new AnimationBuilder().addAnimation("death", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            case BIG_SWIPES -> event.getController().setAnimation(new AnimationBuilder().addAnimation("big_swipes", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            case GENERIC_CHARGE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("charge", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            case AOE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("aoe", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            case DUPLICATE -> event.getController().setAnimation(new AnimationBuilder().addAnimation("duplicate", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            case THROW_MOONLIGHT -> event.getController().setAnimation(new AnimationBuilder().addAnimation("throw_moonlight", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            case SHADOW_ORBS -> event.getController().setAnimation(new AnimationBuilder().addAnimation("shadow_orbs", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
        }
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    public void setCopy(boolean bl) {
        this.isCopy = bl;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("is_copy", this.isCopy);
        nbt.putBoolean("has_duplicated", this.hasDuplicated);
        nbt.putBoolean("has_health_updated", this.healthUpdated);
    }

    @Override
    public int getXp() {
        return ConfigConstructor.frenzied_shade_xp;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("has_duplicated")) {
            this.hasDuplicated = nbt.getBoolean("has_duplicated");
        }
        if (nbt.contains("is_copy")) this.isCopy = nbt.getBoolean("is_copy");
        if (nbt.contains("has_health_updated")) this.healthUpdated = nbt.getBoolean("has_health_updated");
    }

    @Override
    public boolean isFireImmune() {
        return ConfigConstructor.frenzied_shade_is_fire_immune;
    }

    @Override
    public boolean isUndead() {
        return ConfigConstructor.frenzied_shade_is_undead;
    }

    @Override
    public String getGroupId() {
        return ConfigConstructor.frenzied_shade_group_type;
    }

    @Override
    public boolean disablesShield() {
        return ConfigConstructor.frenzied_shade_disables_shields;
    }

    @Override
    public AnimationFactory getFactory() {
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