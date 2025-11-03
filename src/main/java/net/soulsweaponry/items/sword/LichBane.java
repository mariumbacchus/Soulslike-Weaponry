package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.BlazingBlade;
import net.soulsweaponry.items.abilities.posthit.BonusMagicDamage;

public class LichBane extends ModdedSword {

    private static final BlazingBlade BLAZING_BLADE = new BlazingBlade(
            ConfigConstructor.lich_bane_post_hit_base_fire_seconds,
            ConfigConstructor.lich_bane_post_hit_bonus_fire_seconds_per_level,
            ConfigConstructor.lich_bane_post_hit_bonus_fire_seconds_per_fire_aspect_level
    );
    private static final BonusMagicDamage BONUS_MAGIC_DAMAGE = new BonusMagicDamage(
            ConfigConstructor.lich_bane_spellblade_bonus_magic_damage,
            ConfigConstructor.lich_bane_spellblade_bonus_magic_damage_per_level,
            ConfigConstructor.lich_bane_spellblade_target_is_player_mod
    );

    public LichBane(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.lich_bane_damage, ConfigConstructor.lich_bane_attack_speed, settings);
        this.addAbility(BLAZING_BLADE, BONUS_MAGIC_DAMAGE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_lich_bane;
    }
}