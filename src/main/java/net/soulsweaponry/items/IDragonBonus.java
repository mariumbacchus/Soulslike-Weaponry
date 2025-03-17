package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

/**
 * Items of this interface deals more damage to entities inside {@link net.soulsweaponry.util.ModTags.Entities#DRAGONS} tag
 */
public interface IDragonBonus {

    float getBaseDragonBonus(ItemStack stack);

    default float getDragonBonus(ItemStack stack) {
        return this.getBaseDragonBonus(stack) + EnchantmentHelper.getLevel(Enchantments.SWEEPING, stack);
    }
}