package net.soulsweaponry.items.bow;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.abilities.abilitykeybind.Cloudburst;
import net.soulsweaponry.items.abilities.customarrows.GaleArrows;

import java.util.function.Supplier;

public class Galeforce extends ModdedBow {

    private static final GaleArrows GALE_ARROWS = new GaleArrows(
            (int) WeaponConfig.galeforce_gale_arrows_speed_duration,
            WeaponConfig.galeforce_gale_arrows_speed_bonus_duration_per_level,
            (int) WeaponConfig.galeforce_gale_arrows_speed_amp,
            WeaponConfig.galeforce_gale_arrows_speed_bonus_amp_per_level
    );
    private static final Cloudburst CLOUDBURST = new Cloudburst(
            WeaponConfig.galeforce_cloudburst_damage,
            WeaponConfig.galeforce_cloudburst_bonus_damage_per_level,
            WeaponConfig.galeforce_cloudburst_velocity,
            WeaponConfig.galeforce_cloudburst_bonus_velocity_per_level,
            (int) WeaponConfig.galeforce_cloudburst_speed_duration,
            WeaponConfig.galeforce_cloudburst_speed_bonus_duration_per_level,
            (int) WeaponConfig.galeforce_cloudburst_speed_amp,
            WeaponConfig.galeforce_cloudburst_speed_bonus_amp_per_level,
            (int) WeaponConfig.galeforce_cloudburst_min_cooldown,
            (int) WeaponConfig.galeforce_cloudburst_cooldown,
            (int) WeaponConfig.galeforce_cloudburst_reduced_cooldown_per_level
    );

    public Galeforce(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, createConfig((int) WeaponConfig.galeforce_pull_time_ticks,
                WeaponConfig.galeforce_damage, WeaponConfig.galeforce_bonus_velocity),
                repairIngredientSupplier);
        this.addAbility(GALE_ARROWS, CLOUDBURST);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_galeforce;
    }
}