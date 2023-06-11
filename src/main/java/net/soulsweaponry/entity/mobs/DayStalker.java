package net.soulsweaponry.entity.mobs;

import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class DayStalker extends BossEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    public int deathTicks;
    public int ticksUntillDead = 100;
    private double attackAniTick;
    private static final TrackedData<Integer> ATTACKS = DataTracker.registerData(DayStalker.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> PHASE_2 = DataTracker.registerData(DayStalker.class, TrackedDataHandlerRegistry.BOOLEAN);

    public DayStalker(EntityType<? extends DayStalker> entityType, World world) {
        super(entityType, world, Color.YELLOW);
        this.drops.add(WeaponRegistry.DAWNBREAKER);
        this.drops.add(ItemRegistry.LORD_SOUL_DAY_STALKER);
    }

    public void setAttackAnimation(Attacks attack) {
        for (int i = 0; i < AccursedLordBoss.AccursedLordAnimations.values().length; i++) {
            if (Attacks.values()[i].equals(attack)) {
                this.dataTracker.set(ATTACKS, i);
            }
        }
    }

    public Attacks getAttackAnimation() {
        return Attacks.values()[this.dataTracker.get(ATTACKS)];
    }

    public boolean isPhaseTwo() {
        return this.dataTracker.get(PHASE_2);
    }

    public void setPhaseTwo(boolean bl) {
        this.dataTracker.set(PHASE_2, bl);
    }

    private PlayState chains(AnimationState<?> state) {
        if (this.isPhaseTwo()) {
            state.getController().setAnimation(RawAnimation.begin().then("idle_chains_2", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }


    private PlayState idles(AnimationState<?> state) {
        if (this.isPhaseTwo()) {
            state.getController().setAnimation(RawAnimation.begin().then("idle_2", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle_1", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    /*
    * NOTE
    *
    * this.animationTick is only visible if !world.isClient
    *
    */

    private PlayState attacks(AnimationState<?> state) {
        this.attackAniTick = state.animationTick;
        //state.getController().setAnimation(RawAnimation.begin().then("chaos_storm_2", Animation.LoopType.LOOP));
        switch (this.getAttackAnimation()) {
            case DEATH -> {
            }
            case START_PHASE_2 -> {
            }
            case AIR_COMBUSTION -> {
            }
            case DECIMATE -> {
            }
            case DAWNBREAKER -> {
            }
            case CHAOS_STORM -> {
            }
            case FLAMETHROWER -> {
            }
            case SUNFIRE_RUSH -> {
            }
            case CONFLAGRATION -> {
            }
            case FLAMES_EDGE -> {
            }
            case RADIANCE -> {
            }
            case WARMTH -> {
            }
            case OVERHEAT -> {
            }
            case INFERNO -> {
            }
            case FLAMES_REACH -> {
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        //this.setDeath(true);
    }
    
    @Override
    public void updatePostDeath() {
        this.deathTicks++;
        if (this.deathTicks == this.ticksUntillDead && !this.world.isClient()) {
            this.world.sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            CustomDeathHandler.deathExplosionEvent(world, this.getBlockPos(), false, SoundRegistry.DAWNBREAKER_EVENT);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public int getTicksUntilDeath() {
        return 0;
    }

    @Override
    public int getDeathTicks() {
        return this.deathTicks;
    }

    @Override
    public void setDeath() {

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
        return ConfigConstructor.day_stalker_health;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idles", 0, this::idles));
        controllers.add(new AnimationController<>(this, "attacks", 0, this::attacks));
        controllers.add(new AnimationController<>(this, "chains", 0, this::chains));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACKS, 0);
        this.dataTracker.startTracking(PHASE_2, false);
    }

    enum Attacks {
        IDLE, DEATH, START_PHASE_2, AIR_COMBUSTION, DECIMATE, DAWNBREAKER, CHAOS_STORM, FLAMETHROWER, SUNFIRE_RUSH,
        CONFLAGRATION, FLAMES_EDGE, RADIANCE, WARMTH, OVERHEAT, INFERNO, FLAMES_REACH
    }

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 60D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, ConfigConstructor.day_stalker_health)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0D);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putDouble("attack_animation_ticks", this.attackAniTick);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("attack_animation_ticks")) {
            this.attackAniTick = nbt.getDouble("attack_animation_ticks");
        }
    }
}
