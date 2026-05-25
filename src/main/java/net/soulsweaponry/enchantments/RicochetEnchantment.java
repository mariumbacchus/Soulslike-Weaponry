package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.gun.GunItem;
import net.soulsweaponry.registry.EnchantRegistry;

public class RicochetEnchantment extends Enchantment {

    public RicochetEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantRegistry.GUN, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return (int) ConfigConstructor.ricochet_enchant_max_level;
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
        return !(other instanceof EtherealEnchantment) && super.canAccept(other);
    }
}