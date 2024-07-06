package net.soulsweaponry.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.enchantments.FastHandsEnchantment;
import net.soulsweaponry.enchantments.VisceralEnchantment;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.registry.WeaponRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    /**
     * Removes custom enchants and checks whether they can be applied again.
     * Credit goes to <a href="https://github.com/Majrusz/MajruszLibrary">Majrusz</a> for this fix instead of using @Redirect.
     * NOTE: Check if rework is required when porting to fabric 0.15.0
     */
    @Inject(at = @At("RETURN"), cancellable = true, method = "getPossibleEntries")
    private static void interceptEnchantEntries(int power, ItemStack stack, boolean isTreasure, CallbackInfoReturnable<List<EnchantmentLevelEntry>> info) {
        List<EnchantmentLevelEntry> enchantments = info.getReturnValue();
        // Re-implement this if other enchants are made
        enchantments.removeIf(enchantment -> enchantment.enchantment instanceof FastHandsEnchantment || enchantment.enchantment instanceof VisceralEnchantment);
        boolean bl = stack.isOf(Items.BOOK);
        for (Enchantment enchantment : Registries.ENCHANTMENT) {
            if (enchantment.isTreasure() && !isTreasure || !enchantment.isAvailableForRandomSelection() || !enchantment.isAcceptableItem(stack) && !bl) continue;
            for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; i--) {
                if (power >= enchantment.getMinPower(i) && power <= enchantment.getMaxPower(i)) {
                    enchantments.add(new EnchantmentLevelEntry(enchantment, i));
                    break;
                }
            }
        }
        info.setReturnValue(enchantments);
    }

    @Inject(method = "getAttackDamage", at = @At("TAIL"), cancellable = true)
    private static void interceptGetDamage(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> cir) {
        if (stack.isOf(WeaponRegistry.STING) && group == EntityGroup.ARTHROPOD) {
            float value = cir.getReturnValue() + ConfigConstructor.sting_bonus_arthropod_damage;
            cir.setReturnValue(value);
        }
        if (group == EntityGroup.UNDEAD && (stack.getItem() instanceof TrickWeapon trickWeapon && trickWeapon.hasUndeadBonus() && !trickWeapon.isDisabled() || stack.isOf(WeaponRegistry.MASTER_SWORD))) {
            float value = cir.getReturnValue() + ConfigConstructor.righteous_undead_bonus_damage + (float) EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack);
            cir.setReturnValue(value);
        }
    }
}
