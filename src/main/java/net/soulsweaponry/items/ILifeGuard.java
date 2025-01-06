package net.soulsweaponry.items;

import net.minecraft.item.ItemStack;

public interface ILifeGuard {

    double getLifeGuardPercent(ItemStack stack);
    double getLifeSaveChance(ItemStack stack);
    float getLifeSaveExplosionDamage(ItemStack stack);
    float getLifeSaveExplosionKnockback(ItemStack stack);
    int getLifeSaveStackDamage(ItemStack stack);
}