package net.soulsweaponry.items.crossbow;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.customarrows.ThirdShotTrue;

public class KrakenSlayerCrossbow extends ModdedCrossbow {

    private static final ThirdShotTrue THIRD_SHOT_CROSSBOW = new ThirdShotTrue(
            ConfigConstructor.kraken_slayer_crossbow_true_damage,
            ConfigConstructor.kraken_slayer_crossbow_bonus_true_damage_per_level,
            "CROSSBOW",
            (int) ConfigConstructor.kraken_slayer_crossbow_stacks_per_shot,
            ConfigConstructor.kraken_slayer_crossbow_bonus_stacks_per_shot_per_level,
            (int) ConfigConstructor.kraken_slayer_crossbow_max_stacks_until_true_damage
    );

    public KrakenSlayerCrossbow(Settings settings, TagKey<Item> repairTag) {
        super(settings, createConfig((int) ConfigConstructor.kraken_slayer_crossbow_pull_time_ticks,
                ConfigConstructor.kraken_slayer_crossbow_damage, ConfigConstructor.kraken_slayer_crossbow_bonus_velocity),
                repairTag);
        this.addAbility(THIRD_SHOT_CROSSBOW);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_kraken_slayer_crossbow;
    }
}