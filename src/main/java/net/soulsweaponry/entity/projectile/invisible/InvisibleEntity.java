package net.soulsweaponry.entity.projectile.invisible;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.soulsweaponry.registry.WeaponRegistry;

public abstract class InvisibleEntity extends PersistentProjectileEntity {

    private ItemStack stack;

    public InvisibleEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
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
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ItemStack", NbtElement.COMPOUND_TYPE)) {
            this.stack = ItemStack.fromNbt(nbt.getCompound("ItemStack"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("ItemStack", this.stack.writeNbt(new NbtCompound()));
    }
}
