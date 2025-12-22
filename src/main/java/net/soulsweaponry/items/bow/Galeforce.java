package net.soulsweaponry.items.bow;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.abilitykeybind.Cloudburst;
import net.soulsweaponry.items.abilities.customarrows.GaleArrows;

public class Galeforce extends ModdedBow {

    private static final GaleArrows GALE_ARROWS = new GaleArrows(
            (int) ConfigConstructor.galeforce_gale_arrows_speed_duration,
            ConfigConstructor.galeforce_gale_arrows_speed_bonus_duration_per_level,
            (int) ConfigConstructor.galeforce_gale_arrows_speed_amp,
            ConfigConstructor.galeforce_gale_arrows_speed_bonus_amp_per_level
    );
    private static final Cloudburst CLOUDBURST = new Cloudburst(
            ConfigConstructor.galeforce_cloudburst_damage,
            ConfigConstructor.galeforce_cloudburst_bonus_damage_per_level,
            ConfigConstructor.galeforce_cloudburst_velocity,
            ConfigConstructor.galeforce_cloudburst_bonus_velocity_per_level,
            (int) ConfigConstructor.galeforce_cloudburst_speed_duration,
            ConfigConstructor.galeforce_cloudburst_speed_bonus_duration_per_level,
            (int) ConfigConstructor.galeforce_cloudburst_speed_amp,
            ConfigConstructor.galeforce_cloudburst_speed_bonus_amp_per_level,
            (int) ConfigConstructor.galeforce_cloudburst_min_cooldown,
            (int) ConfigConstructor.galeforce_cloudburst_cooldown,
            (int) ConfigConstructor.galeforce_cloudburst_reduced_cooldown_per_level
    );

    public Galeforce(Settings settings, TagKey<Item> repairTag) {
        super(settings, createConfig((int) ConfigConstructor.galeforce_pull_time_ticks,
                ConfigConstructor.galeforce_damage, ConfigConstructor.galeforce_bonus_velocity),
                repairTag);
        this.addAbility(GALE_ARROWS, CLOUDBURST);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_galeforce;
    }
}