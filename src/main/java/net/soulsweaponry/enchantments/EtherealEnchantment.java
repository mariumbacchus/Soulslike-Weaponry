package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.gun.GunItem;
import net.soulsweaponry.registry.EnchantRegistry;

public class EtherealEnchantment extends Enchantment {

    public EtherealEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantRegistry.GUN, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return level * 25;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 50;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof GunItem;
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return !(other instanceof RicochetEnchantment) && super.canAccept(other);
    }
}