package net.soulsweaponry.mixin;

import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.WeaponRegistry;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    
    /*@Unique
    private static Enchantment currentEnchantment;

    @Redirect(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
    private static boolean isAcceptableItem(EnchantmentTarget enchantmentTarget, Item item) {
        ItemStack stack = new ItemStack(item);

        if (item instanceof GunItem) {
            return currentEnchantment.isAcceptableItem(stack);
        }

        return enchantmentTarget.isAcceptableItem(item);
    }*/

    @Inject(method = "getAttackDamage", at = @At("TAIL"), cancellable = true)
    private static void interceptGetDamage(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> cir) {
        if (stack.isOf(WeaponRegistry.STING) && group == EntityGroup.ARTHROPOD) {
            float value = cir.getReturnValue() + ConfigConstructor.sting_bonus_arthropod_damage;
            cir.setReturnValue(value);
        }
    }
}
