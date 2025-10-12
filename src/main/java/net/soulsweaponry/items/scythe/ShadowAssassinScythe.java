package net.soulsweaponry.items.scythe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.statboost.ShadowStep;
import net.soulsweaponry.items.abilities.use.UmbralTrespass;

public class ShadowAssassinScythe extends ModdedSword {

    private static final UmbralTrespass UMBRAL_TRESPASS = new UmbralTrespass(
            ConfigConstructor.shadow_assassin_scythe_umbral_trespass_base_damage,
            ConfigConstructor.shadow_assassin_scythe_umbral_trespass_bonus_damage_per_level,
            ConfigConstructor.shadow_assassin_scythe_umbral_trespass_bonus_enchant_damage_modifier,
            (int) ConfigConstructor.shadow_assassin_scythe_umbral_trespass_min_cooldown,
            (int) ConfigConstructor.shadow_assassin_scythe_umbral_trespass_cooldown,
            (int) ConfigConstructor.shadow_assassin_scythe_umbral_trespass_reduced_cooldown_per_level,
            ConfigConstructor.shadow_assassin_scythe_ummbral_trespass_heal_modifier,
            (int) ConfigConstructor.shadow_assassin_scythe_umbral_trespass_ticks_before_dismount,
            ConfigConstructor.shadow_assassin_scythe_umbral_trespass_bonus_percent_max_health_damage
    );
    private static final ShadowStep SHADOW_STEP = new ShadowStep(
            ConfigConstructor.shadow_assassin_scythe_shadow_step_bonus_damage,
            ConfigConstructor.shadow_assassin_scythe_shadow_step_bonus_damage_per_shadow_step_amp,
            (int) ConfigConstructor.shadow_assassin_scythe_shadow_step_ticks,
            (int) ConfigConstructor.shadow_assassin_scythe_shadow_step_base_amp,
            ConfigConstructor.shadow_assassin_scythe_shadow_step_bonus_amp_per_level,
            (int) ConfigConstructor.shadow_assassin_scythe_shadow_step_min_cooldown,
            (int) ConfigConstructor.shadow_assassin_scythe_shadow_step_cooldown,
            (int) ConfigConstructor.shadow_assassin_scythe_shadow_step_reduced_cooldown_per_level
    );

    public ShadowAssassinScythe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) (ConfigConstructor.darkin_scythe_damage + ConfigConstructor.darkin_scythe_bonus_damage), ConfigConstructor.shadow_assassin_scythe_attack_speed, settings);
        this.addAbility(UMBRAL_TRESPASS, SHADOW_STEP);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_shadow_assassin_scythe;
    }
}