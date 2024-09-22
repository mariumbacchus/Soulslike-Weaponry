package net.soulsweaponry.entity.mobs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;

public class BigChungus extends HostileEntity {

    private static final TrackedData<Boolean> IS_BOSNIAN = DataTracker.registerData(BigChungus.class, TrackedDataHandlerRegistry.BOOLEAN);
    private boolean healthUpdated = false;

    public BigChungus(EntityType<? extends BigChungus> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 50;
    }

    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 2.0D, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createChungusAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 14D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public boolean canSpawn(WorldView view) {
        BlockPos blockUnderEntity = new BlockPos(this.getBlockX(), this.getBlockY() - 1, this.getBlockZ());
        BlockPos positionEntity = new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ());
        BlockState state = this.getWorld().getBlockState(positionEntity);
        return view.doesNotIntersectEntities(this) && this.getWorld().isNight() && !getWorld().containsFluid(this.getBoundingBox())
                && state.getBlock().canMobSpawnInside(state)
                && this.getWorld().getBlockState(blockUnderEntity).allowsSpawning(view, blockUnderEntity, EntityRegistry.BIG_CHUNGUS)
                && this.isSpawnable() && this.checkForMonolith();
    }

    public boolean checkForMonolith() {
        BlockPos entityPos = this.getBlockPos();
        int radius = ConfigConstructor.chungus_monolith_radius;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    BlockPos checkPos = entityPos.add(x, 0, z);
                    Block block = this.getWorld().getBlockState(checkPos).getBlock();
                    if (block == BlockRegistry.CHUNGUS_MONOLITH) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isSpawnable() {
        return ConfigConstructor.can_moderatly_sized_chungus_spawn;
    }

    protected SoundEvent getAmbientSound() {
        return SoundRegistry.BIG_CHUNGUS_EVENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_RABBIT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundRegistry.FART_EVENT;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_PIG_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (!this.healthUpdated) {
            int rand = this.getRandom().nextInt(100);
            this.setBosnian(rand == 1);
            if (this.isBosnian()) {
                this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(50D);
                this.setHealth(50f);
            }
            this.healthUpdated = true;
        }
    }

    public boolean isBosnian() {
        return this.dataTracker.get(IS_BOSNIAN);
    }

    public void setBosnian(boolean bl) {
        this.dataTracker.set(IS_BOSNIAN, bl);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IS_BOSNIAN, false);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("healthUpdated")) {
            this.healthUpdated = nbt.getBoolean("healthUpdated");
        }
        if (nbt.contains("bosnian")) {
            this.setBosnian(nbt.getBoolean("bosnian"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("healthUpdated", this.healthUpdated);
        nbt.putBoolean("bosnian", this.isBosnian());
    }
}