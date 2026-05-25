package net.soulsweaponry.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.soulsweaponry.registry.EnchantRegistry;
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
        enchantments.removeIf(enchantment -> EnchantRegistry.GUN_ENCHANTS.contains(enchantment.enchantment));
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

    @Inject(method = "getEfficiency", at = @At("RETURN"))
    private static void getEfficiency(LivingEntity entity, CallbackInfoReturnable<Integer> info) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = entity.getStackInHand(hand);
            //TODO get nbt and return addition
        }
    }
}