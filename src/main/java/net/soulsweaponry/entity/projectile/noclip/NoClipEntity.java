package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.soulsweaponry.registry.WeaponRegistry;

/**
 * Is No-clip by default and adds methods to change width and height of the bounding box
 */
public abstract class NoClipEntity extends PersistentProjectileEntity {

    private ItemStack stack;
    private static final TrackedData<Float> WIDTH = DataTracker.registerData(NoClipEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(NoClipEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public NoClipEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
        this.setDamage(2D);
        this.pickupType = PickupPermission.DISALLOWED;
        this.stack = new ItemStack(WeaponRegistry.HOLY_MOONLIGHT_GREATSWORD); // Filler stack
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    public boolean isNoClip() {
        return true;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return false;
    }

    @Override
    protected ItemStack asItemStack() {
        return Blocks.AIR.asItem().getDefaultStack();
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    public void setRadius(float radius) {
        this.dataTracker.set(WIDTH, radius);
        this.dataTracker.set(HEIGHT, radius);
    }

    public float getBoundingBoxWidth() {
        return this.dataTracker.get(WIDTH);
    }

    public float getBoundingBoxHeight() {
        return this.dataTracker.get(HEIGHT);
    }

    public void setBoundingBoxWidth(float width) {
        this.dataTracker.set(WIDTH, width);
    }

    public void setBoundingBoxHeight(float height) {
        this.dataTracker.set(HEIGHT, height);
    }

    public float getRadius() {
        return Math.max(this.getBoundingBoxWidth(), this.getBoundingBoxHeight());
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (WIDTH.equals(data) || HEIGHT.equals(data)) {
            this.calculateDimensions();
        }
        super.onTrackedDataSet(data);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(this.getBoundingBoxWidth(), this.getBoundingBoxHeight());
    }

    @Override
    public void calculateDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.calculateDimensions();
        this.setPosition(d, e, f);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(WIDTH, 1.85f);
        this.dataTracker.startTracking(HEIGHT, 1.85f);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ItemStack", NbtElement.COMPOUND_TYPE)) {
            this.stack = ItemStack.fromNbt(nbt.getCompound("ItemStack"));
        }
        if (nbt.contains("BoundingBoxWidth")) {
            this.setBoundingBoxWidth(nbt.getFloat("BoundingBoxWidth"));
        }
        if (nbt.contains("BoundingBoxHeight")) {
            this.setBoundingBoxHeight(nbt.getFloat("BoundingBoxHeight"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("ItemStack", this.stack.writeNbt(new NbtCompound()));
        nbt.putFloat("BoundingBoxWidth", this.getBoundingBoxWidth());
        nbt.putFloat("BoundingBoxHeight", this.getBoundingBoxHeight());
    }
}
