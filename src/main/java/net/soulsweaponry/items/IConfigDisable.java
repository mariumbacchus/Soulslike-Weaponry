package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.soulsweaponry.config.CommonConfig;

public interface IConfigDisable {

    boolean isDisabled();

    default void notifyDisabled(LivingEntity user) {
        if (CommonConfig.INFORM_PLAYER_ABOUT_DISABLED_USE.get() && user instanceof PlayerEntity player) {
            player.sendMessage(new TranslatableText("soulsweapons.weapon.useDisabled"), false);
        }
    }
}