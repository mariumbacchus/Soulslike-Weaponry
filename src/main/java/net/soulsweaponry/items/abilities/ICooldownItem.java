package net.soulsweaponry.items.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;

public interface ICooldownItem {

    /**
     * Applies item cooldown as long as the player is not in creative mode.
     */
    default void applyItemCooldown(ItemStack stack, PlayerEntity player, int cooldown) {
        if (!player.isCreative()) {
            player.getItemCooldownManager().set(stack, cooldown);
        }
    }

    /**
     * Will still apply cooldown regardless if the player is creative or not.
     */
    default void applyItemCooldownNoCheck(ItemStack stack, PlayerEntity player, int cooldown) {
        player.getItemCooldownManager().set(stack, cooldown);
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
        }
    }

    default boolean isCoolingDown(PlayerEntity user, ItemStack stack) {
        return user.getItemCooldownManager().isCoolingDown(stack);
    }

    default boolean hasCooldownEffect(PlayerEntity user) {
        return user.hasStatusEffect(EffectRegistry.COOLDOWN);
    }
}
