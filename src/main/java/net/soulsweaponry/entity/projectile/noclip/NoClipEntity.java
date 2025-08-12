package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.ModPersistentProjectile;

public abstract class NoClipEntity extends ModPersistentProjectile {

    public NoClipEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
        this.setDamage(2D);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.COAL.getDefaultStack();
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

    @Override
    public boolean shouldAllowArrowSticking() {
        return false;
    }
}
