package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.gun.GunItem;
import net.soulsweaponry.registry.EnchantRegistry;

public class FrostsilverEnchantment extends Enchantment {

    public FrostsilverEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantRegistry.GUN, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return 10 + level * 10;
    }

    @Override
    public int getMaxLevel() {
        return (int) ConfigConstructor.frostsilver_enchant_max_level;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof GunItem;
    }
}