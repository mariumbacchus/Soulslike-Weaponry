package net.soulsweaponry.entity.mobs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.dimension.DimensionType;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BigChungus extends TameableEntity implements InventoryOwner {

    private static final TrackedData<Integer> STATE = DataTracker.registerData(BigChungus.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> AGGRESSIVE = DataTracker.registerData(BigChungus.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> TRADE_TICKS = DataTracker.registerData(BigChungus.class, TrackedDataHandlerRegistry.INTEGER);
    private boolean healthUpdated = false;
    private final SimpleInventory inventory = new SimpleInventory(1);
    private int maxTradeCount = 16;
    private float turnChance = 1f / this.maxTradeCount;
    public static final RegistryKey<LootTable> CHUNGUS_TRADES = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(SoulsWeaponry.ModId, "gameplay/chungus_bartering"));

    public BigChungus(EntityType<? extends BigChungus> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 50;
        this.setTamed(false, false);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 2.0D, false));
        this.goalSelector.add(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, true, p -> (!p.hasStatusEffect(EffectRegistry.CHUNGUS_TONIC_EFFECT) || this.isAggressive()) && !this.isTamed()));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, MobEntity.class, true, entity -> entity instanceof Monster
                && !(entity instanceof CreeperEntity) && this.isTamed() && !this.isTeammate(entity)));
        this.targetSelector.add(6, new RevengeGoal(this).setGroupRevenge());
    }

    public static DefaultAttributeContainer.Builder createChungusAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, ConfigConstructor.moderatly_sized_chungus_heath)
                .add(EntityAttributes.GENERIC_ARMOR, ConfigConstructor.moderatly_sized_chungus_armor)
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
        int radius = (int) ConfigConstructor.chungus_monolith_radius;
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
            switch (rand) {
                case 1 -> {
                    this.setState(ChungusStates.BOSNIAN);
                    this.updateStats(50f, 200);
                }
                case 2,3,4,5 -> this.setState(ChungusStates.DREAM);
                default -> this.setState(ChungusStates.NORMAL);
            }
            this.healthUpdated = true;
        }
        if (!this.getInventory().isEmpty() && !this.isAggressive()) {
            this.increaseTradeTicks(1);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 250, true, false));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 30, 250, true, false));
            if (this.getTradeTicks() >= 60 && !this.getWorld().isClient) {
                this.inventory.clearToList();
                switch (this.getState()) {
                    case BOSNIAN -> ParticleHandler.particleSphereList(this.getWorld(), 10, this.getX(), this.getY(), this.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.2f);
                    case DREAM -> {
                        Item item = this.getRandom().nextBoolean() ? Items.ENDER_PEARL : Items.BLAZE_ROD;
                        for (int i = 0; i < this.getRandom().nextBetween(1, 9); i++) {
                            this.trade(item.getDefaultStack());
                        }
                    }
                    default -> this.trade(this.getBarterItem());
                }
                this.setTradeTicks(0);
            }
        }
    }

    private void trade(ItemStack stack) {
        ItemEntity entity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), stack);
        entity.setVelocity(0, 0.5f, 0);
        this.getWorld().spawnEntity(entity);
        this.maxTradeCount = Math.max(this.maxTradeCount - 1, 1);
        this.turnChance = 1f / this.maxTradeCount;
        if (this.getRandom().nextFloat() < this.turnChance) {
            this.setState(ChungusStates.BOSNIAN);
            this.updateStats(50f, 200);
            ParticleHandler.particleSphereList(this.getWorld(), 10, this.getX(), this.getY(), this.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.2f);
        }
    }

    private void updateStats(float health, int exp) {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(health);
        this.setHealth(health);
        this.experiencePoints = exp;
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

    public ChungusStates getState() {
        return ChungusStates.values()[this.dataTracker.get(STATE)];
    }

    public void setState(ChungusStates state) {
        for (int i = 0; i < ChungusStates.values().length; i++) {
            if (ChungusStates.values()[i].equals(state)) {
                this.dataTracker.set(STATE, i);
            }
        }
    }

    public void setStateId(int state) {
        this.dataTracker.set(STATE, state);
    }

    public int getStateId() {
        return this.dataTracker.get(STATE);
    }

    public boolean isAggressive() {
        return this.dataTracker.get(AGGRESSIVE);
    }

    public void setAggressive(boolean bl) {
        this.dataTracker.set(AGGRESSIVE, bl);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(STATE, 0);
        builder.add(TRADE_TICKS, 0);
        builder.add(AGGRESSIVE, false);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("healthUpdated")) {
            this.healthUpdated = nbt.getBoolean("healthUpdated");
        }
        if (nbt.contains("state")) {
            this.setStateId(nbt.getInt("state"));
        }
        if (nbt.contains("tradeCounter")) {
            this.maxTradeCount = nbt.getInt("tradeCounter");
        }
        if (nbt.contains("turnChance")) {
            this.turnChance = nbt.getFloat("turnChance");
        }
        if (nbt.contains("aggressive")) {
            this.setAggressive(nbt.getBoolean("aggressive"));
        }
        this.readInventory(nbt, this.getRegistryManager());
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("healthUpdated", this.healthUpdated);
        nbt.putInt("state", this.getStateId());
        nbt.putInt("tradeCounter", this.maxTradeCount);
        nbt.putFloat("turnChance", this.turnChance);
        nbt.putBoolean("aggressive", this.isAggressive());
        this.writeInventory(nbt, this.getRegistryManager());
    }

    protected ItemStack addItem(ItemStack stack) {
        return this.inventory.addStack(stack);
    }

    @Override
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
        super.dropEquipment(world, source, causedByPlayer);
        this.inventory.clearToList().forEach(this::dropStack);
    }

    @Override
    public SimpleInventory getInventory() {
        return inventory;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (ConfigConstructor.can_chungus_barter && stack.isOf(ItemRegistry.CHUNGUS_EMERALD) && this.getInventory().isEmpty() && !this.isAggressive()) {
            this.addItem(stack);
            if (!player.isCreative()) {
                stack.decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        if (stack.isOf(WeaponRegistry.CHUNGUS_STAFF) && !((IConfigDisable)stack.getItem()).isDisabled(stack) && !this.isAggressive() && !this.isTamed()) {
            this.setTamed(true, false);
            this.setOwner(player);
            this.setTarget(null);
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
            this.navigation.stop();
            return ActionResult.SUCCESS;
        }
        if (this.isTamed() && this.isOwner(player) && this.getTradeTicks() == 0) {
            this.setSitting(!this.isSitting());
            if (this.isSitting()) {
                this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
            } else {
                this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private ItemStack getBarterItem() {
        var server = this.getWorld().getServer();
        if (server == null) {
            return Items.DIRT.getDefaultStack();
        }
        LootTable lootTable = server.getReloadableRegistries().getLootTable(CHUNGUS_TRADES);
        List<ItemStack> list = lootTable.generateLoot(
                new LootContextParameterSet.Builder((ServerWorld)this.getWorld()).add(LootContextParameters.THIS_ENTITY, this).build(LootContextTypes.BARTER)
        );
        return list.isEmpty() ? Items.DIRT.getDefaultStack() : list.getFirst();
    }

    @Override
    public boolean canPickUpLoot() {
        return this.inventory.isEmpty() && ConfigConstructor.can_chungus_barter;
    }

    @Override
    protected void loot(ItemEntity item) {
        if (item.getStack().isOf(ItemRegistry.CHUNGUS_EMERALD)) {
            this.getInventory().addStack(item.getStack());
            this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, this.getSoundCategory(), 1f, 1f);
            item.getStack().decrement(1);
        }
    }

    @Override
    public boolean isTeammate(Entity other) {
        if (other instanceof Tameable) {
            if (((Tameable)other).getOwner() != null && this.getOwner() != null) {
                if (((Tameable)other).getOwner() == this.getOwner()) {
                    return true;
                }
            }
        }
        return super.isTeammate(other);
    }

    public static boolean canSpawnInDark(EntityType<? extends MobEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && isSpawnDark(world, pos, random) && canMobSpawn(type, world, spawnReason, pos, random);
    }

    public static boolean isSpawnDark(ServerWorldAccess world, BlockPos pos, Random random) {
        if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
            return false;
        } else {
            DimensionType dimensionType = world.getDimension();
            int i = dimensionType.monsterSpawnBlockLightLimit();
            if (i < 15 && world.getLightLevel(LightType.BLOCK, pos) > i) {
                return false;
            } else {
                int j = world.toServerWorld().isThundering() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos);
                return j <= dimensionType.monsterSpawnLightTest().get(random);
            }
        }
    }

    public enum ChungusStates {
        NORMAL, BOSNIAN, DREAM
    }
}