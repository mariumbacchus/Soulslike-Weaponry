package net.soulsweaponry.items;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO reduce cooldown with the new upgrade system instead when implemented (add twinkling titanite to weapons to boost damage and stuff)
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
            //NOTE: Config values have just the enchant name so it will struggle to find enchants from other mods I think
            Identifier id = Identifier.of(enchantId);
            RegistryKey<Registry<Enchantment>> enchantmentRegistryKey = RegistryKeys.ENCHANTMENT;
            RegistryKey<Enchantment> key = RegistryKey.of(enchantmentRegistryKey, id);
            ItemEnchantmentsComponent enchComp = stack.getEnchantments();
            for (RegistryEntry<Enchantment> entry : enchComp.getEnchantments()) {
                Optional<RegistryKey<Enchantment>> forKey = entry.getKey();
                if (forKey.isPresent() && forKey.get().equals(key)) {
                    return enchComp.getLevel(entry);
                }
            }
        }
        return 0;
    }

    boolean canEnchantReduceCooldown(ItemStack stack);
    String[] getReduceCooldownEnchantIds(ItemStack stack);

    default void notifyCooldown(LivingEntity user) {
        if (!ConfigConstructor.inform_player_about_cooldown_effect) {
            return;
        }
        if (user instanceof PlayerEntity player) {
            player.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.on_cooldown","Can't cast this ability with the Cooldown effect!"), true);
        } else {
            user.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.on_cooldown","Can't cast this ability with the Cooldown effect!"));
        }
    }
}
