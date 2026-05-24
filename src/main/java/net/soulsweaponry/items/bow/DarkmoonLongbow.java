package net.soulsweaponry.items.bow;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.abilitykeybind.ArrowStorm;
import net.soulsweaponry.items.abilities.customarrows.MoonlightArrowAbility;

import java.util.function.Supplier;

public class DarkmoonLongbow extends ModdedBow {

    private static final ArrowStorm ARROW_STORM = new ArrowStorm(
            ConfigConstructor.darkmoon_longbow_arrow_storm_damage,
            ConfigConstructor.darkmoon_longbow_arrow_storm_bonus_damage_per_level,
            (int) ConfigConstructor.darkmoon_longbow_arrow_storm_min_cooldown,
            (int) ConfigConstructor.darkmoon_longbow_arrow_storm_cooldown,
            (int) ConfigConstructor.darkmoon_longbow_arrow_storm_reduced_cooldown_per_level
    );
    private static final MoonlightArrowAbility MOONLIGHT_ARROW = new MoonlightArrowAbility();

    public DarkmoonLongbow(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, createConfig((int) ConfigConstructor.darkmoon_longbow_pull_time_ticks,
                        ConfigConstructor.darkmoon_longbow_damage, ConfigConstructor.darkmoon_longbow_velocity),
                repairIngredientSupplier);
        this.addAbility(ARROW_STORM, MOONLIGHT_ARROW);

    }

    /* TODO ranged weapon api doenst exist for 1.20.1 forge so gotta make it myself
    * public DarkmoonLongbow(Settings settings) {
        super(settings);
        this.addTooltipAbility( TooltipAbilities.SLOW_PULL, TooltipAbilities.MOONLIGHT_ARROW, TooltipAbilities.ARROW_STORM);
        ((IProjectileWeapon)this).setProjectileDamage(ConfigConstructor.darkmoon_longbow_damage);
        ((IProjectileWeapon)this).setCustomLaunchVelocity((double) ConfigConstructor.darkmoon_longbow_max_velocity);
    }
    * */

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_darkmoon_longbow;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_darkmoon_longbow;
    }
}