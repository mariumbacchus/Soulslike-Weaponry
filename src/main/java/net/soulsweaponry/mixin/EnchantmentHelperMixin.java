package net.soulsweaponry.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.items.IUndeadBonus;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.registry.WeaponRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyReturnValue(method = "getAttackDamage", at = @At("TAIL"))
    private static float modifyAttackDamage(float originalDamage, ItemStack stack, EntityGroup group) {
        if (stack.getItem() instanceof IConfigDisable disable && disable.isDisabled(stack)) {
            return originalDamage;
        }
        float modifiedDamage = originalDamage;
        if (stack.isOf(WeaponRegistry.STING.get()) && group == EntityGroup.ARTHROPOD) {
            modifiedDamage += ConfigConstructor.sting_bonus_arthropod_damage;
        }
        if (group == EntityGroup.UNDEAD && stack.getItem() instanceof IUndeadBonus undeadBonus && undeadBonus.isRighteous()) {
            modifiedDamage += undeadBonus.getUndeadBonus(stack) + (float) EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack);
        }
        return modifiedDamage;
    }
}
