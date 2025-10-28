package net.soulsweaponry.items.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;

public interface ICooldownItem {

    /**
     * Applies item cooldown as long as the player is not in creative mode.
     */
    default void applyItemCooldown(Item item, PlayerEntity player, int cooldown) {
        if (!player.isCreative()) {
            player.getItemCooldownManager().set(item, cooldown);
        }
    }

    /**
     * Applies item cooldown as long as the player is not in creative mode.
     */
    default void applyItemCooldown(ItemStack stack, PlayerEntity player, int cooldown) {
        this.applyItemCooldown(stack.getItem(), player, cooldown);
    }

    /**
     * Will still apply cooldown regardless if the player is creative or not.
     */
    default void applyItemCooldownNoCheck(Item item, PlayerEntity player, int cooldown) {
        player.getItemCooldownManager().set(item, cooldown);
    }

    /**
     * Will still apply cooldown regardless if the player is creative or not.
     */
    default void applyItemCooldownNoCheck(ItemStack item, PlayerEntity player, int cooldown) {
        this.applyItemCooldownNoCheck(item.getItem(), player, cooldown);
    }

    /**
     * Applies the {@link EffectRegistry#COOLDOWN} effect on the player.
     */
    default void applyEffectCooldown(PlayerEntity player, int cooldown) {
        this.applyEffectCooldown(player, cooldown, true);
    }

    /**
     * Applies the {@link EffectRegistry#COOLDOWN} effect on the player.
     */
    default void applyEffectCooldown(PlayerEntity player, int cooldown, boolean showParticles) {
        if (!player.isCreative()) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.COOLDOWN, cooldown, 0, false, showParticles));
        }
    }


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

    default boolean isCoolingDown(PlayerEntity user, ItemStack stack) {
        return user.getItemCooldownManager().isCoolingDown(stack.getItem());
    }

    default boolean hasCooldownEffect(PlayerEntity user) {
        return user.hasStatusEffect(EffectRegistry.COOLDOWN);
    }

    //TODO remove these under (are just for testing since i cant launch without them since all other items use these methods)
    @Deprecated
    default boolean canEnchantReduceCooldown(ItemStack stack) {return false; }
    @Deprecated
    default String[] getReduceCooldownEnchantIds(ItemStack stack) {return new String[]{""};}

    @Deprecated
    default int getReduceCooldownEnchantLevel(ItemStack stack) {
        return 0;
    }

    @Deprecated
    default int getReduceLifeStealCooldownEnchantLevel(ItemStack stack) {
        return 0;
    }

    @Deprecated
    default int getMaxLevel(String[] ids, ItemStack stack) {
        return 0;
    }

    @Deprecated
    default int getReducedCooldownEnchantLevel(ItemStack stack, String enchantId) {
        return 0;
    }

    @Deprecated
    default void applyItemCooldown(PlayerEntity player, int cooldown) {}

    @Deprecated
    default void applyItemCooldownNoCheck(PlayerEntity player, int cooldown) {}
}
