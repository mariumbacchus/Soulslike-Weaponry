package net.soulsweaponry.items.bow;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.abilitykeybind.ArrowStorm;
import net.soulsweaponry.items.abilities.customarrows.MoonlightArrowAbility;

public class DarkmoonLongbow extends ModdedBow {

    private static final ArrowStorm ARROW_STORM = new ArrowStorm(
            ConfigConstructor.darkmoon_longbow_arrow_storm_damage,
            ConfigConstructor.darkmoon_longbow_arrow_storm_bonus_damage_per_level,
            (int) ConfigConstructor.darkmoon_longbow_arrow_storm_min_cooldown,
            (int) ConfigConstructor.darkmoon_longbow_arrow_storm_cooldown,
            (int) ConfigConstructor.darkmoon_longbow_arrow_storm_reduced_cooldown_per_level
    );
    private static final MoonlightArrowAbility MOONLIGHT_ARROW = new MoonlightArrowAbility();

    public DarkmoonLongbow(Settings settings, TagKey<Item> repairTag) {
        super(settings, createConfig((int) ConfigConstructor.darkmoon_longbow_pull_time_ticks,
                ConfigConstructor.darkmoon_longbow_damage, ConfigConstructor.darkmoon_longbow_bonus_velocity),
                repairTag);
        this.addAbility(ARROW_STORM, MOONLIGHT_ARROW);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_darkmoon_longbow;
    }
}