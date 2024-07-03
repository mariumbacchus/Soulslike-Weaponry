package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.soulsweaponry.config.ConfigConstructor;

public interface IConfigDisable {

    boolean isDisabled();

    default void notifyDisabled(LivingEntity user) {
        if (ConfigConstructor.inform_player_about_disabled_use && user instanceof PlayerEntity player) {
            player.sendMessage(new TranslatableText("soulsweapons.weapon.useDisabled"), false);
        }
    }
}
