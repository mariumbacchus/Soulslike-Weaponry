package net.soulsweaponry.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.registry.WeaponRegistry;

public class WeaponUtil {
    
    public static final Enchantment[] DAMAGE_ENCHANTS = {Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS};
    public static final String PREV_TRICK_WEAPON = "trick_weapon_to_transform";

    /**
     * Define all trick weapons here, which is gathered by PacketsServer to switch to the given weapon's index
     */
    public static final TrickWeapon[] TRICK_WEAPONS = {
            WeaponRegistry.KIRKHAMMER,
            WeaponRegistry.SILVER_SWORD,
            WeaponRegistry.HOLY_GREATSWORD
    };

    /**
     * Returns level of the damage enchant, for example {@code 5} for Sharpness V or {@code 4} for Smite IV
     */
    public static int getEnchantDamageBonus(ItemStack stack) {
        for (Enchantment ench : DAMAGE_ENCHANTS) {
            if (EnchantmentHelper.getLevel(ench, stack) > 0) {
                return EnchantmentHelper.getLevel(ench, stack);
            }
        }
        return 0;
    }
}
