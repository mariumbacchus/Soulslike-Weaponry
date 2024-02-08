package net.soulsweaponry.items;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.util.WeaponUtil;

public interface IChargeNeeded {

    String CHARGE = "current_charge";

    int getMaxCharge();
    int getAddedCharge(ItemStack stack);

    default int getCharge(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(CHARGE)) {
            return stack.getNbt().getInt(CHARGE);
        }
        return 0;
    }

    default void addCharge(ItemStack stack, int amount) {
        if (stack.hasNbt()) {
            if (stack.getNbt().contains(CHARGE)) {
                int currentCharge = this.getCharge(stack);
                int newCharge = currentCharge + amount + WeaponUtil.getEnchantDamageBonus(stack);
                int maxCharge = this.getMaxCharge();
                stack.getNbt().putInt(CHARGE, Math.min(newCharge, maxCharge));
            } else {
                stack.getNbt().putInt(CHARGE, 0);
            }
        }
    }

    default boolean isCharged(ItemStack stack) {
        return this.getCharge(stack) >= this.getMaxCharge();
    }
}
