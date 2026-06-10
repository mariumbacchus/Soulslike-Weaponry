package net.soulsweaponry.items.bow;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.abilities.customarrows.ThirdShotTrue;

import java.util.function.Supplier;

public class KrakenSlayer extends ModdedBow {

    private static final ThirdShotTrue THIRD_SHOT_BOW = new ThirdShotTrue(
            WeaponConfig.kraken_slayer_bow_true_damage,
            WeaponConfig.kraken_slayer_bow_bonus_true_damage_per_level,
            WeaponConfig.kraken_slayer_bow_use_animation,
            (int) WeaponConfig.kraken_slayer_bow_stacks_per_shot,
            WeaponConfig.kraken_slayer_bow_bonus_stacks_per_shot_per_level,
            (int) WeaponConfig.kraken_slayer_bow_max_stacks_until_true_damage
    );

    public KrakenSlayer(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, createConfig((int) WeaponConfig.kraken_slayer_bow_pull_time_ticks,
                WeaponConfig.kraken_slayer_bow_damage, WeaponConfig.kraken_slayer_bow_bonus_velocity),
                repairIngredientSupplier);
        this.addAbility(THIRD_SHOT_BOW);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_kraken_slayer_bow;
    }
}