package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.attackclick.ShootSmallMoonlight;

public class BluemoonShortsword extends ModdedSword {

    private static final ShootSmallMoonlight SHOOT_SMALL_MOONLIGHT_BLUEMOON = new ShootSmallMoonlight(
            ConfigConstructor.bluemoon_shortsword_projectile_velocity,
            ConfigConstructor.bluemoon_shortsword_projectile_damage,
            ConfigConstructor.bluemoon_shortsword_projectile_bonus_damage_per_level,
            ConfigConstructor.bluemoon_shortsword_projectile_bonus_damage_per_moon_herald_amp,
            (int) ConfigConstructor.bluemoon_shortsword_projectile_min_cooldown,
            (int) ConfigConstructor.bluemoon_shortsword_projectile_cooldown,
            ConfigConstructor.bluemoon_shortsword_projectile_reduced_cooldown_per_level,
            (int) ConfigConstructor.bluemoon_shortsword_projectile_cooldown_with_lunar_herald_effect
    );

    public BluemoonShortsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.bluemoon_shortsword_damage, ConfigConstructor.bluemoon_shortsword_attack_speed, settings);
        this.addAbility(SHOOT_SMALL_MOONLIGHT_BLUEMOON);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_bluemoon_shortsword;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_bluemoon_shortsword;
    }
}