package net.soulsweaponry.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface ICooldownItem {

    default void applyCooldown(PlayerEntity player, int cooldown) {
        if (!player.isCreative()) {
            player.getItemCooldownManager().set((Item) this, cooldown); //NOTE: Will crash if the class is not an item
        }
    }

    int getReduceCooldownEnchantLevel(ItemStack stack);
}
