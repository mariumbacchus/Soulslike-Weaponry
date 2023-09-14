package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import net.soulsweaponry.util.ParticleNetworking;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Optional;
import java.util.UUID;

public class NightProwler extends BossEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    public int deathTicks;
    public int phaseTwoTicks;
    public int phaseTwoMaxTransitionTicks = 120;
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

    public NightProwler(EntityType<? extends NightProwler> entityType, World world) {
        super(entityType, world, Color.PURPLE);
        this.drops.add(WeaponRegistry.SOUL_REAPER);
        this.drops.add(WeaponRegistry.FORLORN_SCYTHE);
        this.drops.add(ItemRegistry.LORD_SOUL_NIGHT_PROWLER);
    }

    @Override
    protected void initGoals() {
        //TODO add goals
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

    private PlayState attacks(AnimationState<?> state) {
        //TODO add attack animations
        return PlayState.CONTINUE;
    }

    private PlayState idle(AnimationState<?> state) {
        //TODO add idle animations
        return PlayState.CONTINUE;
    }

    private PlayState cape(AnimationState<?> state) {
        //TODO add cape animations
        return PlayState.CONTINUE;
    }

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
    }
    
    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks == this.getTicksUntilDeath() && !this.world.isClient()) {
            this.world.sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(world, this.getBlockPos(), true, SoundRegistry.DAWNBREAKER_EVENT);
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
    }

    public boolean isEmpowered() {
        return (!this.getWorld().isClient && this.getWorld().isDay()) || this.isPhaseTwo();
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

    @Override
    public double getBossMaxHealth() {
        return ConfigConstructor.night_prowler_health;
    }

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 120D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, ConfigConstructor.night_prowler_health)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 6D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.8D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "attacks", 0, this::attacks));
        controllers.add(new AnimationController<>(this, "idle", 0, this::idle));
        controllers.add(new AnimationController<>(this, "cape", 0, this::cape));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    public enum Attacks {
        IDLE, DEATH, TRINITY, REAPING_SLASH, NIGHTS_EMBRACE, RIPPLE_FANG, BLADES_REACH, SOUL_REAPER,
        DIMINISHING_LIGHT, DARKNESS_RISE, ECLIPSE, ENGULF, BLACKFLAME_SNAKE, LUNAR_DISPLACEMENT, DEATHBRINGERS_GRASP
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (!this.getWorld().isClient) {
            LivingEntity partner = this.getPartner((ServerWorld) this.getWorld());
            if (!this.isPhaseTwo() && (partner == null || partner.isDead())) {
                this.setInitiatePhaseTwo(true);
            }
        }
        // TODO heal når noe dør rundt
        if (this.isInitiatingPhaseTwo()) {
            this.phaseTwoTicks++;
            int maxHealTicks = this.phaseTwoMaxTransitionTicks - 40;
            float healPerTick = this.getMaxHealth() / maxHealTicks;
            this.heal(healPerTick);
            if (this.phaseTwoTicks == 76) {
                this.getWorld().playSound(null, this.getBlockPos(), SoundRegistry.DAY_STALKER_RADIANCE, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.phaseTwoTicks == 81) {
                if (!getWorld().isClient) {
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) getWorld(), PacketRegistry.DEATH_EXPLOSION_PACKET_ID, this.getBlockPos(), false);
                }
//                DayStalkerGoal placeHolder = new DayStalkerGoal(this, 1D, true);
//                placeHolder.aoe(4D, 50f, 4f); TODO explode, damage entities
            }
            if (this.phaseTwoTicks >= phaseTwoMaxTransitionTicks) {
                this.setPhaseTwo(true);
                this.setInitiatePhaseTwo(false);
                this.setFlying(false);
            }
        }
        this.setRemainingAniTicks(Math.max(this.getRemainingAniTicks() - 1, 0));
        if (this.getRemainingAniTicks() <= 0 && this.shouldWaitAnimation()) {
            this.setWaitAnimation(false);
            this.setAttackAnimation(NightProwler.Attacks.IDLE);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInitiatingPhaseTwo()) {
            return false;
        }
        // TODO teleport away or closer if not attacking and getting hit?
        if (source.isOf(DamageTypes.FALL)) {
            return false;
        }
        if (this.getAttackAnimation().equals(Attacks.ECLIPSE)) {
            amount = amount * 0.75f;
        }
        return super.damage(source, amount);
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
}
