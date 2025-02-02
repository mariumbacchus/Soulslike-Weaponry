package net.soulsweaponry.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ModArrow extends ArrowEntity {

    public ModArrow(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public ModArrow(EntityType<? extends ArrowEntity> type, double x, double y, double z, World world) {
        this(type, world);
        this.setPosition(x, y, z);
    }

    public ModArrow(EntityType<? extends ArrowEntity> type, LivingEntity owner, World world) {
        this(type, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), world);
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        }
    }

    /**
     * Used in {@link net.soulsweaponry.mixin.ArrowItemMixin} to check if it should apply custom arrow stack
     * effects or not.
     * @param arrowStack arrow item stack
     * @param bowStack bow item stack
     * @return whether the arrow should apply custom status effects based on the effect-arrow-item used
     */
    public abstract boolean canHaveArrowEffects(ItemStack arrowStack, ItemStack bowStack);
}
