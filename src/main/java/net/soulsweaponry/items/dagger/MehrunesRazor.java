package net.soulsweaponry.items.dagger;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.abilities.posthit.FinalWounds;

public class MehrunesRazor extends ModdedDagger {

    private static final FinalWounds FINAL_WOUNDS = new FinalWounds(
            WeaponConfig.mehrunes_razor_kill_trigger_cap,
            WeaponConfig.mehrunes_razor_kill_chance_over_health_cap,
            WeaponConfig.mehrunes_razor_kill_chance_under_health_cap,
            WeaponConfig.mehrunes_razor_missing_health_trigger_cap,
            WeaponConfig.mehrunes_razor_missing_health_chance_over_health_cap,
            WeaponConfig.mehrunes_razor_missing_health_chance_under_health_cap,
            WeaponConfig.mehrunes_razor_missing_health_modifier,
            WeaponConfig.mehrunes_razor_missing_health_modifier_against_players,
            WeaponConfig.mehrunes_razor_missing_health_max_bonus_damage
    );

    public MehrunesRazor(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.mehrunes_razor_damage, WeaponConfig.mehrunes_razor_attack_speed, settings, WeaponConfig.mehrunes_razor_posture_break_crit_hit_percent_bonus);
        this.addAbility(FINAL_WOUNDS);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_mehrunes_razor;
    }
}
