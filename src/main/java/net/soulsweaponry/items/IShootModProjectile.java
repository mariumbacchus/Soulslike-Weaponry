package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IShootModProjectile {

    /**
     * Called in {@link net.soulsweaponry.mixin.ArrowItemMixin} to replace the normal arrow with a custom
     * projectile instance if the bow has one.
     * @param world world
     * @param bowStack item stack of the bow
     * @param arrowStack item stack of the arrow used, can contain data from tipped arrows
     * @param shooter shooter
     * @return new custom projectile instance, null if no custom one is used
     */
    PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow);

    int getPullTime();
}