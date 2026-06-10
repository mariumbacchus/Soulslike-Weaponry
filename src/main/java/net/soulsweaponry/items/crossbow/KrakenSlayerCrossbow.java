package net.soulsweaponry.items.crossbow;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.abilities.customarrows.ThirdShotTrue;

import java.util.function.Supplier;

public class KrakenSlayerCrossbow extends ModdedCrossbow {

    private static final ThirdShotTrue THIRD_SHOT_CROSSBOW = new ThirdShotTrue(
            WeaponConfig.kraken_slayer_crossbow_true_damage,
            WeaponConfig.kraken_slayer_crossbow_bonus_true_damage_per_level,
            "CROSSBOW",
            (int) WeaponConfig.kraken_slayer_crossbow_stacks_per_shot,
            WeaponConfig.kraken_slayer_crossbow_bonus_stacks_per_shot_per_level,
            (int) WeaponConfig.kraken_slayer_crossbow_max_stacks_until_true_damage
    );

    public KrakenSlayerCrossbow(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, createConfig((int) WeaponConfig.kraken_slayer_crossbow_pull_time_ticks,
                WeaponConfig.kraken_slayer_crossbow_damage, WeaponConfig.kraken_slayer_crossbow_bonus_velocity),
                repairIngredientSupplier);
        this.addAbility(THIRD_SHOT_CROSSBOW);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_kraken_slayer_crossbow;
    }
}