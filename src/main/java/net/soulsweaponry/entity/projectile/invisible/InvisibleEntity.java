package net.soulsweaponry.entity.projectile.invisible;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public abstract class InvisibleEntity extends PersistentProjectileEntity {

    public InvisibleEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, ItemStack stack) {
        super(entityType, world, stack);
        this.noClip = true;
        this.setDamage(2D);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public InvisibleEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, LivingEntity owner) {
        super(entityType, owner, world, Items.ARROW.getDefaultStack());
        this.noClip = true;
        this.setDamage(2D);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public InvisibleEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, owner, world, stack);
        this.noClip = true;
        this.setDamage(2D);
        this.pickupType = PickupPermission.DISALLOWED;
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
}
