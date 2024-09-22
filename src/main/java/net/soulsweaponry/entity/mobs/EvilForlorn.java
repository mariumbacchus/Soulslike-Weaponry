package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.soulsweaponry.config.ConfigConstructor;

import java.util.Collections;

public class EvilForlorn extends Forlorn {

    public static boolean canSpawn = ConfigConstructor.can_evil_forlorn_spawn;

    public EvilForlorn(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(false);
        Forlorn.initEquip(this, Collections.emptyMap());
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
        this.targetSelector.add(4, (new RevengeGoal(this)).setGroupRevenge());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL && !getWorld().isClient) {
            this.discard();
        }
        if (this.isInLava() && this.age % 10 == 0) {
            this.damage(this.getDamageSources().magic(), 1f);
        }
    }

    @Override
    public boolean isGlowing() {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public int getTeamColorValue() {
        return 0x9e0010;
    }

    @Override
    public boolean canSpawn(WorldView view) {
        BlockState state = this.getWorld().getBlockState(this.getBlockPos());
        return view.doesNotIntersectEntities(this) && !getWorld().containsFluid(this.getBoundingBox())
                && state.getBlock().canMobSpawnInside(state)
                && getWorld().getDifficulty() != Difficulty.PEACEFUL
                && !getWorld().getBlockState(this.getBlockPos().down()).isOf(Blocks.WARPED_WART_BLOCK)
                && !getWorld().getBlockState(this.getBlockPos().down()).isOf(Blocks.WARPED_NYLIUM)
                && this.getBlockY() < 100 && this.getBlockY() > 40;
    }

    public static boolean canSpawn(EntityType<EvilForlorn> evilForlornEntityType, ServerWorldAccess world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return EvilForlorn.canSpawn;
    }
}