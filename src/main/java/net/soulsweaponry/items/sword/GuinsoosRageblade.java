package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.inventorytick.SoulOfCinder;
import net.soulsweaponry.items.abilities.posthit.StackingEffectAttacker;

public class GuinsoosRageblade extends ModdedSword {

    private static final StackingEffectAttacker HASTE_POST_HIT = new StackingEffectAttacker(
            ConfigConstructor.rageblade_fury_status_effect_id,
            (int) ConfigConstructor.rageblade_fury_start_amp,
            (int) ConfigConstructor.rageblade_fury_bonus_amp_post_hit,
            ConfigConstructor.rageblade_fury_bonus_amp_post_hit_per_level,
            (int) ConfigConstructor.rageblade_fury_max_amp,
            ConfigConstructor.rageblade_fury_bonus_max_amp_per_level,
            (int) ConfigConstructor.rageblade_fury_duration,
            (int) ConfigConstructor.rageblade_fury_bonus_duration_per_level
    );
    private static final SoulOfCinder SOUL_OF_CINDER = new SoulOfCinder(
            (int) ConfigConstructor.rageblade_soul_of_cinder_duration,
            (int) ConfigConstructor.rageblade_soul_of_cinder_bonus_duration_per_lvl,
            (int) ConfigConstructor.rageblade_soul_of_cinder_amp,
            ConfigConstructor.rageblade_soul_of_cinder_bonus_amp_per_lvl
    );

    public GuinsoosRageblade(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.rageblade_damage, ConfigConstructor.rageblade_attack_speed, settings);
        this.addAbility(HASTE_POST_HIT, SOUL_OF_CINDER);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_rageblade;
    }
}