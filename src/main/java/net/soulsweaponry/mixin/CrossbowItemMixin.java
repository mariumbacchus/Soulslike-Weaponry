package net.soulsweaponry.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.IShootModProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(method = "getPullTime", at = @At("HEAD"), cancellable = true)
    private static void applyModdedPullTime(ItemStack stack, CallbackInfoReturnable<Integer> info) {
        if (stack.getItem() instanceof IShootModProjectile custom) {
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            info.setReturnValue(i == 0 ? custom.getPullTime() : custom.getPullTime() - 5 * i);
            info.cancel();
        }
    }
}