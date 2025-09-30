package net.soulsweaponry.items.dagger;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.FinalWounds;

public class MehrunesRazor extends ModdedDagger {

    private static final FinalWounds FINAL_WOUNDS = new FinalWounds(
            ConfigConstructor.mehrunes_razor_kill_trigger_cap,
            ConfigConstructor.mehrunes_razor_kill_chance_over_health_cap,
            ConfigConstructor.mehrunes_razor_kill_chance_under_health_cap,
            ConfigConstructor.mehrunes_razor_missing_health_trigger_cap,
            ConfigConstructor.mehrunes_razor_missing_health_chance_over_health_cap,
            ConfigConstructor.mehrunes_razor_missing_health_chance_under_health_cap,
            ConfigConstructor.mehrunes_razor_missing_health_modifier,
            ConfigConstructor.mehrunes_razor_missing_health_modifier_against_players,
            ConfigConstructor.mehrunes_razor_missing_health_max_bonus_damage
    );

    public MehrunesRazor(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.mehrunes_razor_damage, ConfigConstructor.mehrunes_razor_attack_speed, settings, ConfigConstructor.mehrunes_razor_posture_break_crit_hit_percent_bonus);
        this.addAbility(FINAL_WOUNDS);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_mehrunes_razor;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return new String[0];
    }
}
