package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.config.ConfigConstructor;

public interface IConfigDisable {

    boolean isDisabled(ItemStack stack);

    default void notifyDisabled(LivingEntity user) {
        if (ConfigConstructor.inform_player_about_disabled_use) {
            if (user instanceof PlayerEntity player) {
                player.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.useDisabled","This item is disabled"), true);
            } else {
                user.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.useDisabled","This item is disabled"));
            }
        }
    }

    default void notifyDisabledBossRespawning(LivingEntity user) {
        if (ConfigConstructor.inform_player_about_disabled_boss_respawning) {
            if (user instanceof PlayerEntity player) {
                player.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.bossRespawnDisabled","This boss respawn mechanic has been disabled!"), true);
            } else {
                user.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.bossRespawnDisabled","This boss respawn mechanic has been disabled!"));
            }
        }
    }
}