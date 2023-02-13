package net.soulsweaponry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.GunItem;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    
    @Unique
    private static Enchantment currentEnchantment;

    @Redirect(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
    private static boolean isAcceptableItem(EnchantmentTarget enchantmentTarget, Item item) {
        ItemStack stack = new ItemStack(item);

        if (item instanceof GunItem) {
            return currentEnchantment.isAcceptableItem(stack);
        }

        return enchantmentTarget.isAcceptableItem(item);
    }
}
