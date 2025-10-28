package net.soulsweaponry.items.abilities;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.registry.ComponentRegistry;

import java.util.Optional;

public interface IHasEssence extends IAbility {

    int getMaxEssence();

    default boolean hasMaxEssence(ItemStack stack) {
        return getEssence(stack) >= this.getMaxEssence();
    }

    static int getEssence(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.ESSENCE)).orElse(0);
    }
}
