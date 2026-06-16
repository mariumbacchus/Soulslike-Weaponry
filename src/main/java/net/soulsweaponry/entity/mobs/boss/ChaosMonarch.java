package net.soulsweaponry.entity.mobs.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar.Color;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ChaosMonarchGoal;
import net.soulsweaponry.items.abilities.inventorytick.CorruptGround;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class ChaosMonarch extends BossEntity<ChaosMonarch.States> implements GeoEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private int spawnTicks;
    public static final CorruptGround CORRUPT_GROUND = new CorruptGround(
            (int) EntityConfig.chaos_monarch_wither_ground_range,
            0, 3, 0,
            List.of(new StatusEffectInstance(StatusEffects.WITHER, 80, 1))
    );

    public ChaosMonarch(EntityType<? extends BossEntity> entityType, World world) {
        super(entityType, world, Color.PURPLE, States.class);
    }

    private PlayState predicate(AnimationState<?> state) {
        if (this.isDead()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("death"));
        } else {
            switch (this.getState()) {
                case SPAWN -> state.getController().setAnimation(RawAnimation.begin().thenPlay("spawn"));
                case TELEPORT -> state.getController().setAnimation(RawAnimation.begin().thenPlay("teleport"));
                case MELEE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("swing_staff"));
                case LIGHTNING -> state.getController().setAnimation(RawAnimation.begin().thenPlay("lightning_call"));
                case SHOOT -> state.getController().setAnimation(RawAnimation.begin().thenPlay("shoot"));
                case BARRAGE -> state.getController().setAnimation(RawAnimation.begin().thenPlay("barrage"));
                default -> state.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
	protected void initGoals() {
        this.goalSelector.add(1, new ChaosMonarchGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, AccursedLordBoss.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(5, (new RevengeGoal(this)).setGroupRevenge());
		super.initGoals();
	}

    public static DefaultAttributeContainer.Builder createBossAttributes() {
        return HostileEntity.createHostileAttributes()
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 60D)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, EntityConfig.chaos_monarch_health)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0D)
        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
        .add(EntityAttributes.GENERIC_ARMOR, EntityConfig.chaos_monarch_armor)
        .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0D);
    }

    @Override
    public int getTicksUntilDeath() {
        return 80;
    }

    @Override
    public List<ParticleEffect> getDeathParticles() {
        return List.of(ParticleTypes.LARGE_SMOKE, ParticleTypes.DRAGON_BREATH, ParticleRegistry.PURPLE_FLAME);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.isSpawning()) {
            this.spawnTicks++;
            ParticleEffect[] dragonParticles = {ParticleTypes.DRAGON_BREATH, ParticleTypes.DRAGON_BREATH};
            ParticleEffect[] portalParticles = {ParticleTypes.PORTAL};
            if (this.spawnTicks % 2 == 0 && this.spawnTicks < 20) {
                this.particleExplosion(portalParticles, 4f);
            }
            if (this.spawnTicks == 40) {
                this.particleExplosion(dragonParticles, .5f);
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.spawnTicks >= 60) {
                this.setState(States.IDLE);
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source == (this.getWorld().getDamageSources().lightningBolt())) {
            return false;
        }
        if (source.isOf(DamageTypes.WITHER)) {
            return false;
        }
        return super.damage(source, amount);
    }

    @Override
    public int getXp() {
        return (int) EntityConfig.chaos_monarch_xp;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.hasStatusEffect(EffectRegistry.DECAY) && this.age % 10 == 0) {
            this.heal(this.getStatusEffect(EffectRegistry.DECAY).getAmplifier() + 1 + this.getAttackingPlayers().size());
            for (LivingEntity target : this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(3D))) {
                if (!(target instanceof PlayerEntity) && target != this) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 3));
                }
            }
        }
        if (EntityConfig.chaos_monarch_wither_ground) {
            this.turnBlocks(this.getWorld(), this.getBlockPos());
        }
    }

    @Override
    public boolean isSpawning() {
        return this.getState() == States.SPAWN;
    }

    @Override
    public SoundEvent getBossMusic() {
        return null;
    }

    @Override
    public boolean hasBossMusic() {
        return false;
    }

    private void turnBlocks(World world, BlockPos blockPos) {
        CORRUPT_GROUND.turnBlocks(this, world, blockPos, ArmorRegistry.CHAOS_ROBES.getDefaultStack());
    }

    private void particleExplosion(ParticleEffect[] particles, float sizeModifier) {
        this.roundParticleOutburst(this.getWorld(), 1000, particles, this.getX(), this.getY() + 3, this.getZ(), sizeModifier);
    }

    public void roundParticleOutburst(World world, double points, ParticleEffect[] particles, double x, double y, double z, float sizeModifier) {
        double phi = Math.PI * (3. - Math.sqrt(5.));
        for (int i = 0; i < points; i++) {
            double velocityY = 1 - (i/(points - 1)) * 2;
            double radius = Math.sqrt(1 - velocityY*velocityY);
            double theta = phi * i;
            double velocityX = Math.cos(theta) * radius;
            double velocityZ = Math.sin(theta) * radius;
            for (ParticleEffect particle : particles) {
                world.addParticle(particle, true, x, y, z, velocityX * sizeModifier, velocityY * sizeModifier, velocityZ * sizeModifier);
            }
        } 
    }
    
    @Override
    public boolean disablesShield() {
        return EntityConfig.chaos_monarch_disables_shields;
    }

    @Override
    public boolean isFireImmune() {
        return EntityConfig.chaos_monarch_is_fire_immune;
    }

    @Override
    public boolean hasInvertedHealingAndHarm() {
        return EntityConfig.chaos_monarch_has_inverted_heal_and_harm;
    }

    @Override
    public String[] getBlacklistedStatusEffects() {
        return EntityConfig.chaos_monarch_status_effect_blacklist;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_DEATH;
    }

    public enum States {
        IDLE, SPAWN, TELEPORT, MELEE, LIGHTNING, SHOOT, BARRAGE
    }
}
