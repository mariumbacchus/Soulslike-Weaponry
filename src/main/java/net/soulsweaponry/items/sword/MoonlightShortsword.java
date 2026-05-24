package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.attackclick.ShootSmallMoonlight;

public class MoonlightShortsword extends ModdedSword {

    private static final ShootSmallMoonlight SHOOT_SMALL_MOONLIGHT = new ShootSmallMoonlight(
            ConfigConstructor.moonlight_shortsword_projectile_velocity,
            ConfigConstructor.moonlight_shortsword_projectile_damage,
            ConfigConstructor.moonlight_shortsword_projectile_bonus_damage_per_level,
            ConfigConstructor.moonlight_shortsword_projectile_bonus_damage_per_moon_herald_amp,
            (int) ConfigConstructor.moonlight_shortsword_projectile_min_cooldown,
            (int) ConfigConstructor.moonlight_shortsword_projectile_cooldown,
            ConfigConstructor.moonlight_shortsword_projectile_reduced_cooldown_per_level,
            (int) ConfigConstructor.moonlight_shortsword_projectile_cooldown_with_lunar_herald_effect
    );

    public MoonlightShortsword(ToolMaterial toolMaterial, Settings settings) {
        this(toolMaterial, (int) ConfigConstructor.moonlight_shortsword_damage, ConfigConstructor.moonlight_shortsword_attack_speed, settings);
    }

    public MoonlightShortsword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.addAbility(SHOOT_SMALL_MOONLIGHT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_moonlight_shortsword;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_moonlight_shortsword;
    }
}