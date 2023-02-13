package net.soulsweaponry.entity.mobs;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AttackWithOwnerGoal;
import net.minecraft.entity.ai.goal.TrackOwnerAttackerGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.ai.goal.FreyrSwordGoal;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

/**
 * NOTE TO SELF: <p>
 * This class had many random comments with dead code before now. It was me attempting to use the animation ticks
 * as the attack ticks, instead of having two seperate ticks, so if the animation speed increases
 * on the entity, the damage timing will work fine. Well, it's been a week now with me non-stop
 * trying to figure things out through data tracking, nbts, sound, particle and custom instruction
 * keyframes, but alas, nothing works. For some reason, I can't pass any data beyond the tick() function
 * or animation predicate functions, meaning I can't even pass booleans or doubles to neither the attack
 * goal nor mobTick() function. Moreover, I can't even interact with any entities beyond gathering their
 * information, not even the .damage() method works. Maybe some day I'll figure it out, but by the looks of it, 
 * other mods also use the two ticks seperately, so for now, I admit defeat. <p>
 * TLDR; Animation functions cannot interact with entities, only gather their information. The tick() function
 * will damage anything regardless if this entity despawned or not. The mobTick() method or Goals cannot
 * get any information from the animation functions, since it can't edit any variables to the entity. <p>
 * Methods I've tried: <p>
 * - Tracked data boolean changed in the animation function. <p>
 * - Tracked data blockPos if not null then animation function SHOULD damage all entities in box of blockPos. <p>
 * - Register custom controllers and interact with entities.
 */
public class FreyrSwordEntity extends TameableEntity implements IAnimatable {

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private ItemStack stack;
    public static final BlockPos NULLISH_POS = new BlockPos(0, 0, 0);
    private static final TrackedData<Boolean> ATTACKING = DataTracker.registerData(FreyrSwordEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<BlockPos> STATIONARY = DataTracker.registerData(FreyrSwordEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Boolean> IS_STATIONARY = DataTracker.registerData(FreyrSwordEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public FreyrSwordEntity(EntityType<? extends FreyrSwordEntity> entityType, World world) {
        super(entityType, world);
        this.stack = new ItemStack(WeaponRegistry.FREYR_SWORD);
    }

    public FreyrSwordEntity(World world, PlayerEntity owner, ItemStack stack) {
        super(EntityRegistry.FREYR_SWORD_ENTITY_TYPE, world);
        this.stack = stack.copy();
        this.setTamed(true);
        this.setOwner(owner);
    }

    public <E extends Entity & IAnimatable> PlayState attack(AnimationEvent<E> event) {
        if (this.getAnimationAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack_east", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().clearAnimationCache();
        return PlayState.STOP;
    }

    private <E extends Entity & IAnimatable> PlayState idle(AnimationEvent<E> event) {
        if (!this.getAnimationAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
            return PlayState.CONTINUE;
        } else {
            return PlayState.STOP;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<FreyrSwordEntity> attackController = new AnimationController<>(this, "attackController", 0, this::attack);
        AnimationController<FreyrSwordEntity> idleController = new AnimationController<>(this, "idleController", 0, this::idle);
        //attackController.registerCustomInstructionListener(this::attackTargetListener); //Check GeoExampleEntity for more info/examples.
        data.addAnimationController(attackController);
        data.addAnimationController(idleController);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new FreyrSwordGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, HostileEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, SlimeEntity.class, true));
        super.initGoals();
    }

    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.getOwner() != null) {
            if (!this.isBlockPosNullish(this.getStationaryPos()) /* != null */) {
                if (this.getTarget() == null || this.squaredDistanceTo(this.stationaryAsVec3d()) > this.getFollowRange()) {
                    this.updatePosition(this.getStationaryPos().getX(), this.getStationaryPos().getY(), this.getStationaryPos().getZ());
                    this.setAnimationAttacking(false);
                }
            } else {
                if (this.getTarget() == null || this.squaredDistanceTo(this.getOwner()) > this.getFollowRange()) {
                    Vec3d vecOwner = this.getOwner().getRotationVector();
                    double xAdd = 0;                    
                    if (this.getOwner().getPitch() < -50 || this.getOwner().getPitch() > 50) {
                        xAdd = vecOwner.getX() > 0 ? -1 : 1;
                    }
                    Vec3d vecEdited = vecOwner.multiply(-1.5).add(this.getOwner().getPos()).add(xAdd, 0, 0);
                    this.updatePositionAndAngles(vecEdited.getX(), this.getOwner().getY(), vecEdited.getZ(), this.getOwner().getYaw() / 2, this.getOwner().getPitch());
                    this.setAnimationAttacking(false);
                }
            }
        } /* else {
            if (this.age > this.getNoOwnerAge()) {
                this.remove(RemovalReason.DISCARDED);
            }
        } */
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.getOwner() == null || (this.getOwner() != null && this.getOwner().equals(player))) {
            this.removeEntity();
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    public void removeEntity() {
        if (world.isClient) {
            ((ClientWorld)world).removeEntity(this.getId(), RemovalReason.DISCARDED);
        } else {
            this.dropEquipment(DamageSource.GENERIC, 0, true);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        this.dropEquipment(DamageSource.GENERIC, 0, true);
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        this.stack.damage(10, this.getRandom(), null);
        if (!((this.stack.getMaxDamage() - this.stack.getDamage()) <= 0)) {
            ItemEntity entity = this.dropStack(this.stack);
            if (entity != null) entity.setCovetedItem();
        } else if (this.getBlockPos() != null) {
            this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    /**
     * Since servers crash when the input is null, this function checks if the blockPos is
     * "null-ish", since it is highly unlikely the player will be on the coords xyz 0,
     * it will consider that position to be equal to null, from there on out
     * deciding whether it should stay behind the owner or not.
     */
    public boolean isBlockPosNullish(BlockPos pos) {
        if (pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0) return true;
        else return false;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        //Spawn particles around
        if (this.age % 4 == 0) {
            double random = this.getRandom().nextDouble();
            this.world.addParticle(ParticleTypes.GLOW, false, 
                this.getX() + random/4 - random/8, this.getEyeY() - random*6 + random*6/2, this.getZ() + random/4 - random/8, 
                random/16 - random/32, random - random/2, random/16 - random/32);
        }
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    /**
     * When the owner disconnects, the entity will lose it's bond. It will then
     * despawn after 3 seconds.
     */
    public int getNoOwnerAge() {
        return 60; //1200
    }

    public static DefaultAttributeContainer.Builder createEntityAttributes() {
        return PathAwareEntity.createLivingAttributes()
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100)
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 50)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, (double) ConfigConstructor.sword_of_freyr_damage);
    }

    public double getFollowRange() {
        return this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
    }

    public ItemStack asItemStack() {
        return this.stack;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void setAnimationAttacking(boolean bl) {
        this.dataTracker.set(ATTACKING, bl);
    }

    private boolean getAnimationAttacking() {
        return this.dataTracker.get(ATTACKING);
    }

    public BlockPos getStationaryPos() {
        return this.dataTracker.get(STATIONARY);
    }

    public void setStationaryPos(BlockPos pos) {
        if (this.dataTracker.get(IS_STATIONARY)) {
            this.dataTracker.set(STATIONARY, NULLISH_POS);
            this.dataTracker.set(IS_STATIONARY, false);
        } else {
            this.dataTracker.set(STATIONARY, pos);
            this.dataTracker.set(IS_STATIONARY, true);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() != null && this.getOwner() != null && source.getAttacker().equals(this.getOwner())) {
            return false;
        }
        return super.damage(source, amount);
    }

    public Vec3d stationaryAsVec3d() {
        return new Vec3d(this.getStationaryPos().getX(), this.getStationaryPos().getY(), this.getStationaryPos().getZ());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACKING, Boolean.FALSE);
        this.dataTracker.startTracking(STATIONARY, NULLISH_POS);
        this.dataTracker.startTracking(IS_STATIONARY, Boolean.FALSE);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR;
    }

    @Override
    public PassiveEntity createChild(ServerWorld var1, PassiveEntity var2) {
        return null;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }
}
