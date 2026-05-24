package net.soulsweaponry.items.abilities;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;

public interface IHasEssence extends IAbility {

    int getMaxEssence();

    default boolean hasMaxEssence(ItemStack stack) {
        return getEssence(stack) >= this.getMaxEssence();
    }

    static int getEssence(ItemStack stack) {
        return NbtHelper.getInt(stack, NbtIds.ESSENCE, 0);
    }
}
