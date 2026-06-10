package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.ApplyEchoEffect;
import net.soulsweaponry.items.abilities.posthit.BlazingBlade;
import net.soulsweaponry.items.abilities.posthit.BonusMagicDamage;

public class LichBane extends ModdedSword {

    private static final BlazingBlade BLAZING_BLADE = new BlazingBlade(
            WeaponConfig.lich_bane_post_hit_base_fire_seconds,
            WeaponConfig.lich_bane_post_hit_bonus_fire_seconds_per_level,
            WeaponConfig.lich_bane_post_hit_bonus_fire_seconds_per_fire_aspect_level
    );
    private static final BonusMagicDamage BONUS_MAGIC_DAMAGE = new BonusMagicDamage(
            WeaponConfig.lich_bane_spellblade_bonus_magic_damage,
            WeaponConfig.lich_bane_spellblade_bonus_magic_damage_per_level,
            WeaponConfig.lich_bane_spellblade_target_is_player_mod
    );
    private static final ApplyEchoEffect APPLY_ECHO_EFFECT = new ApplyEchoEffect(
            (int) WeaponConfig.lich_bane_echo_duration,
            WeaponConfig.lich_bane_echo_duration_per_level,
            (int) WeaponConfig.lich_bane_echo_amp,
            WeaponConfig.lich_bane_echo_amp_per_level,
            WeaponConfig.lich_bane_echo_saved_damage_taken_mod,
            WeaponConfig.lich_bane_echo_saved_damage_taken_bonus_added_to_mod_per_level,
            (int) WeaponConfig.lich_bane_echo_min_cooldown,
            (int) WeaponConfig.lich_bane_echo_cooldown,
            (int) WeaponConfig.lich_bane_echo_reduced_cooldown_per_level
    );

    public LichBane(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.lich_bane_damage, WeaponConfig.lich_bane_attack_speed, settings);
        this.addAbility(BLAZING_BLADE, BONUS_MAGIC_DAMAGE, APPLY_ECHO_EFFECT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_lich_bane;
    }
}