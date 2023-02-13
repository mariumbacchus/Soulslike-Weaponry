package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.GunItem;

public class VisceralEnchantment extends Enchantment {

    public VisceralEnchantment(Rarity weight, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.BOW, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return 10 + level * 10;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof GunItem ? true : super.isAcceptableItem(stack);
    }
}
