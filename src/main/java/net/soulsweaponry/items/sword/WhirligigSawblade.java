package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.usagetick.Sawblade;

public class WhirligigSawblade extends ModdedSword {

    private static final Sawblade SAWBLADE = new Sawblade(
            ConfigConstructor.whirligig_sawblade_ability_range,
            ConfigConstructor.whirligig_sawblade_ability_expansion,
            ConfigConstructor.whirligig_sawblade_ability_damage,
            ConfigConstructor.whirligig_sawblade_ability_bonus_damage_per_level,
            ConfigConstructor.whirligig_sawblade_ability_enchant_bonus_damage_mod,
            ConfigConstructor.whirligig_sawblade_knockback,
            (int) ConfigConstructor.whirligig_sawblade_bleed_added,
            ConfigConstructor.whirligig_sawblade_bonus_bleed_per_level,
            (int) ConfigConstructor.whirligig_sawblade_bleed_effect_duration,
            (int) ConfigConstructor.whirligig_sawblade_bleed_effect_bonus_duration_per_level,
            (int) ConfigConstructor.whirligig_sawblade_bleed_effect_amp,
            ConfigConstructor.whirligig_sawblade_bleed_effect_bonus_amp_per_level,
            (int) ConfigConstructor.whirligig_sawblade_use_time,
            (int) ConfigConstructor.whirligig_sawblade_bonus_use_time_per_level,
            (int) ConfigConstructor.whirligig_sawblade_min_cooldown,
            (int) ConfigConstructor.whirligig_sawblade_cooldown,
            (int) ConfigConstructor.whirligig_sawblade_reduced_cooldown_per_level
    );

    public WhirligigSawblade(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.whirligig_sawblade_damage, ConfigConstructor.whirligig_sawblade_attack_speed, settings);
        this.addAbility(SAWBLADE);
    }


    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_whirligig_sawblade;
    }
}