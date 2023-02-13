package net.soulsweaponry.entity.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.soulsweaponry.config.ConfigConstructor;

public class EvilForlorn extends Forlorn {

    public static boolean canSpawn = ConfigConstructor.can_evil_forlorn_spawn;

    public EvilForlorn(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(false);
        Forlorn.initEquip(this);
        this.experiencePoints = 20;
    }

    public static DefaultAttributeContainer.Builder createForlornAttributes() {
        return MobEntity.createMobAttributes()
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 250D)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 15D)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3000000003D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }
    
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(4, (new RevengeGoal(this, new Class[0])).setGroupRevenge());
    }

    public static boolean canSpawn(EntityType<EvilForlorn> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return EvilForlorn.getSpawnable() && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public void tick() {
        super.tick();
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20, 0));
    }

    @Override
    public int getTeamColorValue() {
        return 0x9e0010;
    }

    @Override
    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this) && !world.containsFluid(this.getBoundingBox());
    }

    public static boolean getSpawnable() {
        return EvilForlorn.canSpawn;
    }
}
