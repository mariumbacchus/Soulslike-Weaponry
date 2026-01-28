package net.soulsweaponry.items.bow;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.customarrows.ThirdShotTrue;

import java.util.function.Supplier;

public class KrakenSlayer extends ModdedBow {

    private static final ThirdShotTrue THIRD_SHOT_BOW = new ThirdShotTrue(
            ConfigConstructor.kraken_slayer_bow_true_damage,
            ConfigConstructor.kraken_slayer_bow_bonus_true_damage_per_level,
            ConfigConstructor.kraken_slayer_bow_use_animation,
            (int) ConfigConstructor.kraken_slayer_bow_stacks_per_shot,
            ConfigConstructor.kraken_slayer_bow_bonus_stacks_per_shot_per_level,
            (int) ConfigConstructor.kraken_slayer_bow_max_stacks_until_true_damage
    );

    public KrakenSlayer(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, createConfig((int) ConfigConstructor.kraken_slayer_bow_pull_time_ticks,
                        ConfigConstructor.kraken_slayer_bow_damage, ConfigConstructor.kraken_slayer_bow_bonus_velocity),
                repairIngredientSupplier);
        this.addAbility(THIRD_SHOT_BOW);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_kraken_slayer_bow;
    }
}