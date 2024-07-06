package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.soulsweaponry.config.ConfigConstructor;

public interface IConfigDisable {

    boolean isDisabled();

    default void notifyDisabled(LivingEntity user) {
        if (ConfigConstructor.inform_player_about_disabled_use) {
            user.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.useDisabled","This item is disabled"));
        }
    }
}