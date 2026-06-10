package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.attackclick.ShootSmallMoonlight;

public class BluemoonShortsword extends ModdedSword {

    private static final ShootSmallMoonlight SHOOT_SMALL_MOONLIGHT_BLUEMOON = new ShootSmallMoonlight(
            WeaponConfig.bluemoon_shortsword_projectile_velocity,
            WeaponConfig.bluemoon_shortsword_projectile_damage,
            WeaponConfig.bluemoon_shortsword_projectile_bonus_damage_per_level,
            WeaponConfig.bluemoon_shortsword_projectile_bonus_damage_per_moon_herald_amp,
            (int) WeaponConfig.bluemoon_shortsword_projectile_min_cooldown,
            (int) WeaponConfig.bluemoon_shortsword_projectile_cooldown,
            WeaponConfig.bluemoon_shortsword_projectile_reduced_cooldown_per_level,
            (int) WeaponConfig.bluemoon_shortsword_projectile_cooldown_with_lunar_herald_effect
    );

    public BluemoonShortsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.bluemoon_shortsword_damage, WeaponConfig.bluemoon_shortsword_attack_speed, settings);
        this.addAbility(SHOOT_SMALL_MOONLIGHT_BLUEMOON);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_bluemoon_shortsword;
    }
}