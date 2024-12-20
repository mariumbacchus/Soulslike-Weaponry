package net.soulsweaponry.items;

import net.minecraft.item.ItemStack;

public interface IUndeadBonus {

    boolean isRighteous();
    float getUndeadBonus(ItemStack stack);
}
