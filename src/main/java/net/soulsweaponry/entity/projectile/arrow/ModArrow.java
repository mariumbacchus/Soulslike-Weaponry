package net.soulsweaponry.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;

public abstract class ModArrow extends ArrowEntity {

    private ItemStack arrowStack = ItemStack.EMPTY;
    private ItemStack weaponStack = ItemStack.EMPTY;

    public ModArrow(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public ModArrow(EntityType<? extends ArrowEntity> type, double x, double y, double z, World world, ItemStack arrowStack, ItemStack weaponStack) {
        this(type, world);
        this.setPosition(x, y, z);
        this.arrowStack = arrowStack.copy();
        this.weaponStack = weaponStack.copy();
    }

    public ModArrow(EntityType<? extends ArrowEntity> type, LivingEntity owner, World world, ItemStack arrowStack, ItemStack weaponStack) {
        this(type, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), world, arrowStack, weaponStack);
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        }
    }

    public void setArrowStack(ItemStack arrowStack) {
        this.arrowStack = arrowStack;
    }

    public void setWeaponStack(ItemStack weaponStack) {
        this.weaponStack = weaponStack;
    }

    /**
     * @return whether the arrow should apply custom status effects based on the effect-arrow-item used
     */
    public abstract boolean canHaveArrowEffects();

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ArrowStack", NbtElement.COMPOUND_TYPE)) {
            this.arrowStack = ItemStack.fromNbt(nbt.getCompound("ArrowStack"));
        }
        if (nbt.contains("WeaponStack", NbtElement.COMPOUND_TYPE)) {
            this.weaponStack = ItemStack.fromNbt(nbt.getCompound("WeaponStack"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.arrowStack != null) {
            nbt.put("ArrowStack", this.arrowStack.writeNbt(new NbtCompound()));
        }
        if (this.weaponStack != null) {
            nbt.put("WeaponStack", this.weaponStack.writeNbt(new NbtCompound()));
        }
    }
}