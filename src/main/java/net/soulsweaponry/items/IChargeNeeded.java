package net.soulsweaponry.items;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Optional;

public interface IChargeNeeded {

    String CHARGE = "current_charge";

    int getMaxCharge();
    int getAddedCharge(ItemStack stack);
    boolean acceptsMoonHeraldEffect(ItemStack stack);

    default int getCharge(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.CHARGE)).orElse(0);
    }

    default void addCharge(ItemStack stack, int amount) {
        Integer charge = stack.get(ComponentRegistry.CHARGE);
        if (charge != null) {
            int currentCharge = this.getCharge(stack);
            int newCharge = currentCharge + amount + WeaponUtil.getEnchantDamageBonus(stack);
            int maxCharge = this.getMaxCharge();
            stack.set(ComponentRegistry.CHARGE, Math.min(newCharge, maxCharge));
        } else {
            stack.set(ComponentRegistry.CHARGE, 0);
        }
    }

    default boolean isCharged(ItemStack stack) {
        return this.getCharge(stack) >= this.getMaxCharge();
    }
}