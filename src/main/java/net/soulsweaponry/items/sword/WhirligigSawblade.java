package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.usagetick.Sawblade;

public class WhirligigSawblade extends ModdedSword {

    private static final Sawblade SAWBLADE = new Sawblade(
            WeaponConfig.whirligig_sawblade_ability_range,
            WeaponConfig.whirligig_sawblade_ability_expansion,
            WeaponConfig.whirligig_sawblade_ability_damage,
            WeaponConfig.whirligig_sawblade_ability_bonus_damage_per_level,
            WeaponConfig.whirligig_sawblade_ability_enchant_bonus_damage_mod,
            WeaponConfig.whirligig_sawblade_knockback,
            (int) WeaponConfig.whirligig_sawblade_bleed_added,
            WeaponConfig.whirligig_sawblade_bonus_bleed_per_level,
            (int) WeaponConfig.whirligig_sawblade_bleed_effect_duration,
            (int) WeaponConfig.whirligig_sawblade_bleed_effect_bonus_duration_per_level,
            (int) WeaponConfig.whirligig_sawblade_bleed_effect_amp,
            WeaponConfig.whirligig_sawblade_bleed_effect_bonus_amp_per_level,
            (int) WeaponConfig.whirligig_sawblade_use_time,
            (int) WeaponConfig.whirligig_sawblade_bonus_use_time_per_level,
            (int) WeaponConfig.whirligig_sawblade_min_cooldown,
            (int) WeaponConfig.whirligig_sawblade_cooldown,
            (int) WeaponConfig.whirligig_sawblade_reduced_cooldown_per_level
    );

    public WhirligigSawblade(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.whirligig_sawblade_damage, WeaponConfig.whirligig_sawblade_attack_speed, settings);
        this.addAbility(SAWBLADE);
    }


    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_whirligig_sawblade;
    }
}