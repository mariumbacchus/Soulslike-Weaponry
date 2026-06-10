package net.soulsweaponry.items.sword;

import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.items.abilities.posthit.GivesEssence;
import net.soulsweaponry.items.abilities.statboost.EssenceBoostStats;

public class HolyMoonlightSword extends TrickWeapon {

    private static final EssenceBoostStats ESSENCE_BOOST_STATS = new EssenceBoostStats(
            WeaponConfig.holy_moonlight_sword_max_bonus_damage,
            WeaponConfig.holy_moonlight_sword_max_bonus_attack_speed,
            (int) WeaponConfig.holy_moonlight_ability_essence_needed
    );
    private static final GivesEssence GIVES_ESSENCE = new GivesEssence(
            (int) WeaponConfig.holy_moonlight_sword_essence_added_post_hit,
            (int) WeaponConfig.holy_moonlight_sword_bonus_essence_added_post_hit_per_level,
            (int) WeaponConfig.holy_moonlight_ability_essence_needed
    );

    public HolyMoonlightSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.holy_moonlight_sword_damage, WeaponConfig.holy_moonlight_sword_attack_speed, settings, WeaponConfig.disable_use_holy_moonlight_sword,
                WeaponConfig.holy_moonlight_sword_righteous_base_undead_bonus_damage, WeaponConfig.holy_moonlight_sword_righteous_undead_bonus_damage_per_level);
        this.addAbility(ESSENCE_BOOST_STATS, GIVES_ESSENCE);
    }
}