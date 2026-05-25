package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.gun.GunItem;
import net.soulsweaponry.registry.EnchantRegistry;

public class ChainLightningEnchantment extends Enchantment {

    public ChainLightningEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantRegistry.GUN, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return level * 20;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 25;
    }

    @Override
    public int getMaxLevel() {
        return (int) ConfigConstructor.chain_lightning_enchant_max_level;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof GunItem;
    }
}