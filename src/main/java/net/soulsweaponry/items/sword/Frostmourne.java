package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.items.abilities.use.SoulReleaseRandomBased;
import net.soulsweaponry.registry.EntityRegistry;

import java.util.List;

public class Frostmourne extends SoulHarvestingItem {

    private static final Permafrost PERMAFROST = new Permafrost(
            (int) ConfigConstructor.frostmourne_frost_buildup_post_hit,
            (int) ConfigConstructor.frostmourne_frost_post_hit_permafrost_base_duration,
            (int) ConfigConstructor.frostmourne_frost_post_hit_permafrost_base_amplifier,
            ConfigConstructor.frostmourne_frost_post_hit_permafrost_amp_per_level
    );
    private static final SoulReleaseRandomBased SOUL_RELEASE_RANDOM_BASED = new SoulReleaseRandomBased(
            (int) ConfigConstructor.frostmourne_summoned_allies_cap,
            "FrostmourneSummons",
            List.of(EntityRegistry.FROST_GIANT.get(), EntityRegistry.RIME_SPECTRE.get()),
            (int) ConfigConstructor.frostmourne_summon_soul_cost,
            ConfigConstructor.frostmourne_summon_bonus_health_per_soul, ConfigConstructor.frostmourne_summon_bonus_health_per_soul_addition_per_level,
            ConfigConstructor.frostmourne_summon_max_bonus_health,
            ConfigConstructor.frostmourne_summon_bonus_attack_damage_per_soul, ConfigConstructor.frostmourne_summon_bonus_attack_damage_per_soul_addition_per_level,
            ConfigConstructor.frostmourne_summon_max_bonus_attack_damage
    );

    public Frostmourne(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.frostmourne_damage, ConfigConstructor.frostmourne_attack_speed, settings);
        this.addAbility(PERMAFROST, SOUL_RELEASE_RANDOM_BASED);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_frostmourne;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_frostmourne;
    }
}