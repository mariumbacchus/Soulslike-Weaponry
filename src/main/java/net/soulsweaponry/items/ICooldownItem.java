package net.soulsweaponry.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ICooldownItem {

    default void applyItemCooldown(PlayerEntity player, int cooldown) {
        if (!player.isCreative()) {
            player.getItemCooldownManager().set((Item) this, cooldown); //NOTE: Will crash if the class is not an item
        }
    }

    /**
     * Will still apply cooldown regardless if the player is creative or not
     */
    default void applyItemCooldownNoCheck(PlayerEntity player, int cooldown) {
        player.getItemCooldownManager().set((Item) this, cooldown);
    }

    default void applyEffectCooldown(PlayerEntity player, int cooldown) {
        if (!player.isCreative()) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.COOLDOWN, cooldown, 0));
        }
    }

    /**
     * Returns highest level out of all the enchants in the array gotten from {@link #getReduceCooldownEnchantIds(ItemStack)}
     */
    default int getReduceCooldownEnchantLevel(ItemStack stack) {
        if (this.canEnchantReduceCooldown(stack)) {
            return this.getMaxLevel(this.getReduceCooldownEnchantIds(stack), stack);
        }
        return 0;
    }

    /**
     * Returns highest level out of all the enchants in the array gotten from {@link ConfigConstructor#lifesteal_item_enchant_reduces_cooldown_ids}
     */
    default int getReduceLifeStealCooldownEnchantLevel(ItemStack stack) {
        if (ConfigConstructor.lifesteal_item_enchant_reduces_cooldown) {
            return this.getMaxLevel(ConfigConstructor.lifesteal_item_enchant_reduces_cooldown_ids, stack);
        }
        return 0;
    }

    default int getMaxLevel(String[] ids, ItemStack stack) {
        List<Integer> levels = new ArrayList<>();
        for (String id : ids) {
            levels.add(this.getReducedCooldownEnchantLevel(stack, id));
        }
        Optional<Integer> op = levels.stream().max(Integer::compare);
        return op.orElse(0);
    }

    default int getReducedCooldownEnchantLevel(ItemStack stack, String enchantId) {
        if (enchantId.equals("damage")) {
            return WeaponUtil.getEnchantDamageBonus(stack);
        } else {
            Identifier id = new Identifier(enchantId);
            Enchantment enchantment = Registries.ENCHANTMENT.get(id);
            if (enchantment != null) {
                return EnchantmentHelper.getLevel(enchantment, stack);
            }
        }
        return 0;
    }

    boolean canEnchantReduceCooldown(ItemStack stack);
    String[] getReduceCooldownEnchantIds(ItemStack stack);
}
