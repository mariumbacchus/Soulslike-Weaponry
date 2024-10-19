package net.soulsweaponry.entity.mobs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.*;

import java.util.List;

public class BigChungus extends HostileEntity implements InventoryOwner {

    private static final TrackedData<Boolean> IS_BOSNIAN = DataTracker.registerData(BigChungus.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> TRADE_TICKS = DataTracker.registerData(BigChungus.class, TrackedDataHandlerRegistry.INTEGER);
    private boolean healthUpdated = false;
    private final SimpleInventory inventory = new SimpleInventory(1);
    private int maxTradeCount = 16;
    private float turnChance = 1f / this.maxTradeCount;

    public BigChungus(EntityType<? extends BigChungus> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 50;
    }

    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 2.0D, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true, (p) -> !p.hasStatusEffect(EffectRegistry.CHUNGUS_TONIC_EFFECT)));
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
                this.updateStats();
            }
            this.healthUpdated = true;
        }
        if (!this.getInventory().isEmpty()) {
            this.increaseTradeTicks(1);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 250, true, false));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 30, 250, true, false));
            if (this.getTradeTicks() >= 60 && !this.getWorld().isClient) {
                this.inventory.clearToList();
                if (!this.isBosnian()) {
                    ItemEntity entity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), this.getBarterItem());
                    entity.setVelocity(0, 0.5f, 0);
                    this.getWorld().spawnEntity(entity);
                    this.maxTradeCount = Math.max(this.maxTradeCount - 1, 1);
                    this.turnChance = 1f / this.maxTradeCount;
                    if (this.getRandom().nextFloat() < this.turnChance) {
                        this.setBosnian(true);
                        this.updateStats();
                    }
                } else {
                    ParticleHandler.particleSphereList(this.getWorld(), 10, this.getX(), this.getY(), this.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.2f);
                }
                this.setTradeTicks(0);
            }
        }
    }

    private void updateStats() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(50D);
        this.setHealth(50f);
        this.experiencePoints = 200;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getTradeTicks() > 0) {
            if (this.getTradeTicks() % 4 == 0) {
                for (int i = 0; i < 3; i++) {
                    this.getWorld().addParticle(ParticleTypes.COMPOSTER, this.getParticleX(0.8f), this.getRandomBodyY() + .2f, this.getParticleZ(0.8f), 0, 0, 0);
                }
            }
        }
    }

    public int getTradeTicks() {
        return this.dataTracker.get(TRADE_TICKS);
    }

    public void increaseTradeTicks(int i) {
        this.setTradeTicks(this.getTradeTicks() + i);
    }

    public void setTradeTicks(int i) {
        this.dataTracker.set(TRADE_TICKS, i);
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
        this.dataTracker.startTracking(TRADE_TICKS, 0);
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
        if (nbt.contains("tradeCounter")) {
            this.maxTradeCount = nbt.getInt("tradeCounter");
        }
        if (nbt.contains("turnChance")) {
            this.turnChance = nbt.getFloat("turnChance");
        }
        this.readInventory(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("healthUpdated", this.healthUpdated);
        nbt.putBoolean("bosnian", this.isBosnian());
        nbt.putInt("tradeCounter", this.maxTradeCount);
        nbt.putFloat("turnChance", this.turnChance);
        this.writeInventory(nbt);
    }

    protected ItemStack addItem(ItemStack stack) {
        return this.inventory.addStack(stack);
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        this.inventory.clearToList().forEach(this::dropStack);
    }

    @Override
    public SimpleInventory getInventory() {
        return inventory;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(ItemRegistry.CHUNGUS_EMERALD) && this.getInventory().isEmpty()) {
            this.addItem(stack);
            if (!player.isCreative()) {
                stack.decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    private ItemStack getBarterItem() {
        LootTable lootTable = this.getWorld().getServer().getLootManager().getLootTable(new Identifier(SoulsWeaponry.ModId, "gameplay/chungus_bartering"));
        List<ItemStack> list = lootTable.generateLoot(
                new LootContextParameterSet.Builder((ServerWorld)this.getWorld()).add(LootContextParameters.THIS_ENTITY, this).build(LootContextTypes.BARTER)
        );
        return list.isEmpty() ? Items.DIRT.getDefaultStack() : list.get(0);
    }

    @Override
    public boolean canPickUpLoot() {
        return this.inventory.isEmpty();
    }

    @Override
    protected void loot(ItemEntity item) {
        if (item.getStack().isOf(ItemRegistry.CHUNGUS_EMERALD)) {
            this.getInventory().addStack(item.getStack());
            this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, this.getSoundCategory(), 1f, 1f);
            item.getStack().decrement(1);
        }
    }
}