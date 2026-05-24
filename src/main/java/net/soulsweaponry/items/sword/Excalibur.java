package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.immunity.LightBringer;
import net.soulsweaponry.items.abilities.stoppedusing.SonicBoom;
import net.soulsweaponry.items.abilities.userdamaged.LifeGuard;

public class Excalibur extends ModdedSword {

    private static final LifeGuard LIFE_GUARD = new LifeGuard(
            ConfigConstructor.excalibur_life_guard_reduced_damage_percent,
            ConfigConstructor.excalibur_life_guard_reduced_damage_percent_per_level,
            ConfigConstructor.excalibur_life_save_chance_percent,
            ConfigConstructor.excalibur_life_save_chance_percent_per_level,
            ConfigConstructor.excalibur_life_save_explosion_damage,
            ConfigConstructor.excalibur_life_save_explosion_bonus_damage_per_level,
            ConfigConstructor.excalibur_life_save_explosion_knockback,
            ConfigConstructor.excalibur_life_save_explosion_bonus_knockback_per_level,
            ConfigConstructor.excalibur_life_save_explosion_range,
            ConfigConstructor.excalibur_life_save_explosion_range_per_level,
            (int) ConfigConstructor.excalibur_life_save_stack_damage,
            (int) ConfigConstructor.excalibur_life_save_reduced_stack_damage_per_level
    );
    private static final SonicBoom SONIC_BOOM = new SonicBoom(
            ConfigConstructor.excalibur_sonic_boom_target_search_range,
            ConfigConstructor.excalibur_sonic_boom_bonus_target_search_range_per_level,
            ConfigConstructor.excalibur_sonic_boom_max_range,
            ConfigConstructor.excalibur_sonic_boom_bonus_max_range_per_level,
            ConfigConstructor.excalibur_sonic_boom_damage,
            ConfigConstructor.excalibur_sonic_boom_bonus_damage_per_level,
            ConfigConstructor.excalibur_sonic_boom_bonus_enchant_damage_mod,
            ConfigConstructor.excalibur_sonic_boom_knockback_power_mod,
            ConfigConstructor.excalibur_sonic_boom_knockback_power_mod_per_level,
            (int) ConfigConstructor.excalibur_sonic_boom_min_cooldown,
            (int) ConfigConstructor.excalibur_sonic_boom_cooldown,
            (int) ConfigConstructor.excalibur_sonic_boom_reduced_cooldown_per_level
    );
    private static final LightBringer LIGHTBRINGER = new LightBringer(
            (int) ConfigConstructor.excalibur_lightbringer_effects_duration,
            ConfigConstructor.excalibur_lightbringer_effects_duration_per_level,
            (int) ConfigConstructor.excalibur_lightbringer_effects_amplifier,
            ConfigConstructor.excalibur_lightbringer_effects_amplifier_per_level
    );

    public Excalibur(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.excalibur_damage, ConfigConstructor.excalibur_attack_speed, settings);
        this.addAbility(LIFE_GUARD, SONIC_BOOM, LIGHTBRINGER);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_excalibur;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_excalibur;
    }
}