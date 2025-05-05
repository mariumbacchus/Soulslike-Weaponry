package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class ChainLightningEnchantment extends Enchantment {

    //TODO register and make it rare
    //TODO make only guns get this (via mixin)
    //TODO make silver bullets apply on entity hit (trigger ability)
    public ChainLightningEnchantment(Rarity weight, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.BOW, slotTypes);
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
        return 3;
    }
}
