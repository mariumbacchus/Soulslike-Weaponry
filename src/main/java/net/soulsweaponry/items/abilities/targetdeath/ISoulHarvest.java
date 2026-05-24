package net.soulsweaponry.items.abilities.targetdeath;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;

public interface ISoulHarvest extends IAbility {

    default void handleKill(LivingEntity target, ItemStack stack) {
        if (target.getType().isIn(ModTags.Entities.BOSSES)) {
            addAmount(stack, 50);
        } else {
            this.addKillCounter(stack);
        }
    }

    default void addKillCounter(ItemStack stack) {
        addAmount(stack, 1);
    }

    default void addAmount(ItemStack stack, int amount) {
        amount += NbtHelper.getInt(stack, NbtIds.SOULS_HARVESTED, 0);
        NbtHelper.putInt(stack, NbtIds.SOULS_HARVESTED, amount);
    }

    default int getSouls(ItemStack stack) {
        return NbtHelper.getInt(stack, NbtIds.SOULS_HARVESTED, 0);
    }

    default boolean canCollectSouls() {
        return true;
    }
}
