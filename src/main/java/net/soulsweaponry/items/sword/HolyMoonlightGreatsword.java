package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.bonusdamage.UndeadBonus;
import net.soulsweaponry.items.abilities.predicate.EssenceNeeded;
import net.soulsweaponry.items.abilities.posthit.GivesEssence;
import net.soulsweaponry.items.abilities.stoppedusing.Moonfall;

public class HolyMoonlightGreatsword extends ModdedSword {

    private static final UndeadBonus UNDEAD_BONUS = new UndeadBonus(
            WeaponConfig.holy_moonlight_greatsword_righteous_base_undead_bonus_damage,
            WeaponConfig.holy_moonlight_greatsword_righteous_undead_bonus_damage_per_level
    );
    private static final EssenceNeeded ESSENCE_NEEDED = new EssenceNeeded(
            (int) WeaponConfig.holy_moonlight_ability_essence_needed, true, (int) WeaponConfig.holy_moonlight_ability_item_upgrade_needed_to_remove_essence_requirement
    );
    private static final GivesEssence GIVES_ESSENCE = new GivesEssence(
            (int) WeaponConfig.holy_moonlight_greatsword_essence_added_post_hit,
            (int) WeaponConfig.holy_moonlight_greatsword_bonus_essence_added_post_hit_per_level,
            (int) WeaponConfig.holy_moonlight_ability_essence_needed
    );
    private static final Moonfall MOONFALL = new Moonfall(
            (int) WeaponConfig.holy_moonlight_moonfall_ruptures_amount,
            WeaponConfig.holy_moonlight_moonfall_bonus_ruptures_amount_per_level,
            WeaponConfig.holy_moonlight_moonfall_rupture_radius,
            WeaponConfig.holy_moonlight_moonfall_damage,
            WeaponConfig.holy_moonlight_moonfall_bonus_damage_per_level,
            WeaponConfig.holy_moonlight_moonfall_bonus_damage_enchant_mod,
            WeaponConfig.holy_moonlight_moonfall_knockup,
            WeaponConfig.holy_moonlight_moonfall_bonus_knockup_per_level,
            (int) WeaponConfig.holy_moonlight_moonfall_min_cooldown,
            (int) WeaponConfig.holy_moonlight_moonfall_cooldown,
            (int) WeaponConfig.holy_moonlight_moonfall_reduced_cooldown_per_level,
            (int) WeaponConfig.holy_moonlight_moonfall_reduced_cooldown_per_moon_herald_effect_amp
    );

    public HolyMoonlightGreatsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.holy_moonlight_greatsword_damage, WeaponConfig.holy_moonlight_greatsword_attack_speed, settings);
        this.addAbility(MOONFALL, UNDEAD_BONUS, ESSENCE_NEEDED, GIVES_ESSENCE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_holy_moonlight_greatsword;
    }
}