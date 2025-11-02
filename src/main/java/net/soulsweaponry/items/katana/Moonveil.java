package net.soulsweaponry.items.katana;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.Bleed;
import net.soulsweaponry.items.abilities.stoppedusing.MoonveilHorizontal;
import net.soulsweaponry.items.abilities.stoppedusing.sneaking.MoonveilVertical;

public class Moonveil extends ModdedSword {

    private static final Bleed BLEED = new Bleed((int) ConfigConstructor.moonveil_bleed_post_hit, ConfigConstructor.moonveil_bleed_post_hit_bonus_per_bloodthirsty_amp);
    private static final MoonveilHorizontal MOONVEIL_HORIZONTAL = new MoonveilHorizontal(
            ConfigConstructor.moonveil_wave_base_damage, ConfigConstructor.moonveil_wave_bonus_damage_per_level,
            (int) ConfigConstructor.moonveil_wave_min_cooldown, (int) ConfigConstructor.moonveil_wave_cooldown, (int) ConfigConstructor.moonveil_wave_reduced_cooldown_per_level
    );
    private static final MoonveilVertical MOONVEIL_VERTICAL = new MoonveilVertical(
            ConfigConstructor.moonveil_vertical_base_damage, ConfigConstructor.moonveil_vertical_bonus_damage_per_level,
            (int) ConfigConstructor.moonveil_vertical_min_cooldown, (int) ConfigConstructor.moonveil_vertical_cooldown, (int) ConfigConstructor.moonveil_vertical_reduced_cooldown_per_level
    );

    public Moonveil(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.moonveil_damage, ConfigConstructor.moonveil_attack_speed, settings);
        this.addAbility(BLEED, MOONVEIL_HORIZONTAL, MOONVEIL_VERTICAL);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_moonveil;
    }
}
