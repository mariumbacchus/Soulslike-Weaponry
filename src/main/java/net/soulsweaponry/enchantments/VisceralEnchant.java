package net.soulsweaponry.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.soulsweaponry.items.GunItem;
import net.soulsweaponry.registry.EnchantmentRegistry;

public class VisceralEnchant extends Enchantment {

    public VisceralEnchant(Rarity pRarity, EquipmentSlot... pApplicableSlots) {
        super(pRarity, EnchantmentRegistry.GUN, pApplicableSlots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int level) {
        return 10 + level * 10;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof GunItem;
    }
}
