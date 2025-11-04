package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.abilitykeybind.NightsEdgeAbility;
import net.soulsweaponry.items.abilities.posthit.Blight;

public class NightsEdgeItem extends ModdedSword {

    private static final Blight BLIGHT = new Blight(
            ConfigConstructor.nights_edge_tainted_edge_status_effect_id,
            (int) ConfigConstructor.nights_edge_tainted_edge_start_amp,
            (int) ConfigConstructor.nights_edge_tainted_edge_bonus_amp_post_hit,
            ConfigConstructor.nights_edge_tainted_edge_bonus_amp_post_hit_per_level,
            (int) ConfigConstructor.nights_edge_tainted_edge_max_amp,
            ConfigConstructor.nights_edge_tainted_edge_bonus_max_amp_per_level,
            (int) ConfigConstructor.nights_edge_tainted_edge_duration,
            (int) ConfigConstructor.nights_edge_tainted_edge_bonus_duration_per_level,
            (int) ConfigConstructor.nights_edge_tainted_edge_status_effect_amp_needed_for_bonus_effect,
            ConfigConstructor.nights_edge_tainted_edge_bonus_effect,
            (int) ConfigConstructor.nights_edge_tainted_edge_bonus_effect_amp,
            ConfigConstructor.nights_edge_tainted_edge_bonus_effect_bonus_amp_per_level,
            (int) ConfigConstructor.nights_edge_tainted_edge_bonus_effect_duration,
            (int) ConfigConstructor.nights_edge_tainted_edge_bonus_effect_bonus_duration_per_level
    );
    private static final NightsEdgeAbility EDGE_OF_NIGHT = new NightsEdgeAbility(
            (int) ConfigConstructor.nights_edge_ability_ripple_amount,
            ConfigConstructor.nights_edge_ability_ripple_bonus_amount_per_level,
            ConfigConstructor.nights_edge_ability_ripple_damage,
            ConfigConstructor.nights_edge_ability_ripple_bonus_damage_per_level,
            (int) ConfigConstructor.nights_edge_ability_line_amount,
            ConfigConstructor.nights_edge_ability_line_bonus_amount_per_level,
            ConfigConstructor.nights_edge_ability_line_damage,
            ConfigConstructor.nights_edge_ability_line_bonus_damage_per_level,
            (int) ConfigConstructor.nights_edge_ability_min_cooldown,
            (int) ConfigConstructor.nights_edge_ability_cooldown,
            (int) ConfigConstructor.nights_edge_ability_reduced_cooldown_per_level
    );

    public NightsEdgeItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.nights_edge_weapon_damage, ConfigConstructor.nights_edge_weapon_attack_speed, settings);
        this.addAbility(BLIGHT, EDGE_OF_NIGHT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_nights_edge;
    }
}