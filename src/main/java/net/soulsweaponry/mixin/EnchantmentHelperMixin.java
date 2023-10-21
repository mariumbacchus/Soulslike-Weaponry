package net.soulsweaponry.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.registry.WeaponRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    
    @Unique
    private static Enchantment currentEnchantment;

    /**
     * Sets the static {@code currentEnchantment} in this class to the one being processed.
     * Credits goes to KingVampyre (<a href="https://github.com/KingVampyre/DeepTrenches">...</a>) for this fantastic implementation.
     * <p></p>
     * Note to self! When porting to forge, consider using {@code EnumHelper.addEnchantmentType("weaponType", (item)->(item instanceof WeaponItem));} instead
     * of all this. Check out this when the time comes: (<a href="https://forums.minecraftforge.net/topic/67916-solved-1122-cant-add-new-enchantment-type/">...</a>)
     */
    @Redirect(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;isAvailableForRandomSelection()Z"))
    private static boolean isAvailableForRandomSelection(Enchantment enchantment) {
        currentEnchantment = enchantment;
        return enchantment.isAvailableForRandomSelection();
    }

    @Redirect(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
    private static boolean isAcceptableItem(EnchantmentTarget enchantmentTarget, Item item) {
        if (enchantmentTarget == EnchantmentTarget.BOW) {
            return currentEnchantment.isAcceptableItem(item.getDefaultStack());
        }
        return enchantmentTarget.isAcceptableItem(item);
    }

    @Inject(method = "getAttackDamage", at = @At("TAIL"), cancellable = true)
    private static void interceptGetDamage(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> cir) {
        if (stack.isOf(WeaponRegistry.STING) && group == EntityGroup.ARTHROPOD) {
            float value = cir.getReturnValue() + ConfigConstructor.sting_bonus_arthropod_damage;
            cir.setReturnValue(value);
        }
        if (group == EntityGroup.UNDEAD && (stack.getItem() instanceof TrickWeapon && ((TrickWeapon) stack.getItem()).hasUndeadBonus() || stack.isOf(WeaponRegistry.MASTER_SWORD))) {
            float value = cir.getReturnValue() + ConfigConstructor.righteous_undead_bonus_damage + (float) EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack);
            cir.setReturnValue(value);
        }
    }
}
