package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.GunItem;
import net.soulsweaponry.registry.EnchantRegistry;

public class FastHandsEnchantment extends Enchantment {

    public FastHandsEnchantment(Rarity pRarity, EquipmentSlot... pApplicableSlots) {
        super(pRarity, EnchantRegistry.GUN, pApplicableSlots);
    }

    @Override
    public int getMinPower(int level) {
        return 10 + level * 10;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof GunItem;
    }
}
