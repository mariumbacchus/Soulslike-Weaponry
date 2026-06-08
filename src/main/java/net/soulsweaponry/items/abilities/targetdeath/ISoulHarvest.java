package net.soulsweaponry.items.abilities.targetdeath;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.ComponentRegistry;

public interface ISoulHarvest extends IAbility {

    default void handleKill(LivingEntity target, ItemStack stack) {
        if (target.getType().isIn(ConventionalEntityTypeTags.BOSSES)) {
            addAmount(stack, 50);
        } else {
            this.addKillCounter(stack);
        }
    }

    default void addKillCounter(ItemStack stack) {
        addAmount(stack, 1);
    }

    default void addAmount(ItemStack stack, int amount) {
        amount += stack.getOrDefault(ComponentRegistry.SOULS_HARVESTED, 0);
        stack.set(ComponentRegistry.SOULS_HARVESTED, amount);
    }

    default int getSouls(ItemStack stack) {
        return stack.getOrDefault(ComponentRegistry.SOULS_HARVESTED, 0);
    }

    default boolean canCollectSouls() {
        return true;
    }
}
