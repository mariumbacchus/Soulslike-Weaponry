package net.soulsweaponry.items.sword;

import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.items.abilities.posthit.GivesEssence;
import net.soulsweaponry.items.abilities.statboost.EssenceBoostStats;

public class HolyMoonlightSword extends TrickWeapon {

    private static final EssenceBoostStats ESSENCE_BOOST_STATS = new EssenceBoostStats(
            ConfigConstructor.holy_moonlight_sword_max_bonus_damage,
            ConfigConstructor.holy_moonlight_sword_max_bonus_attack_speed,
            (int) ConfigConstructor.holy_moonlight_ability_essence_needed
    );
    private static final GivesEssence GIVES_ESSENCE = new GivesEssence(
            (int) ConfigConstructor.holy_moonlight_sword_essence_added_post_hit,
            (int) ConfigConstructor.holy_moonlight_sword_bonus_essence_added_post_hit_per_level,
            (int) ConfigConstructor.holy_moonlight_ability_essence_needed
    );

    public HolyMoonlightSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.holy_moonlight_sword_damage, ConfigConstructor.holy_moonlight_sword_attack_speed, settings,
                ConfigConstructor.is_fireproof_holy_moonlight_sword, ConfigConstructor.disable_use_holy_moonlight_sword,
                ConfigConstructor.holy_moonlight_sword_righteous_base_undead_bonus_damage, ConfigConstructor.holy_moonlight_sword_righteous_undead_bonus_damage_per_level);
        this.addAbility(ESSENCE_BOOST_STATS, GIVES_ESSENCE);
    }
}