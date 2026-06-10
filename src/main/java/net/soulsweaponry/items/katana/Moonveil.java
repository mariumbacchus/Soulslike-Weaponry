package net.soulsweaponry.items.katana;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.Bleed;
import net.soulsweaponry.items.abilities.stoppedusing.MoonveilHorizontal;
import net.soulsweaponry.items.abilities.stoppedusing.sneaking.MoonveilVertical;

public class Moonveil extends ModdedSword {

    private static final Bleed BLEED = new Bleed((int) WeaponConfig.moonveil_bleed_post_hit, WeaponConfig.moonveil_bleed_post_hit_bonus_per_bloodthirsty_amp);
    private static final MoonveilHorizontal MOONVEIL_HORIZONTAL = new MoonveilHorizontal(
            (int) WeaponConfig.moonveil_wave_projectile_max_age, WeaponConfig.moonveil_wave_base_damage, WeaponConfig.moonveil_wave_bonus_damage_per_level,
            (int) WeaponConfig.moonveil_wave_min_cooldown, (int) WeaponConfig.moonveil_wave_cooldown, (int) WeaponConfig.moonveil_wave_reduced_cooldown_per_level
    );
    private static final MoonveilVertical MOONVEIL_VERTICAL = new MoonveilVertical(
            (int) WeaponConfig.moonveil_vertical_projectile_max_age, WeaponConfig.moonveil_vertical_base_damage, WeaponConfig.moonveil_vertical_bonus_damage_per_level,
            (int) WeaponConfig.moonveil_vertical_min_cooldown, (int) WeaponConfig.moonveil_vertical_cooldown, (int) WeaponConfig.moonveil_vertical_reduced_cooldown_per_level
    );

    public Moonveil(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.moonveil_damage, WeaponConfig.moonveil_attack_speed, settings);
        this.addAbility(BLEED, MOONVEIL_HORIZONTAL, MOONVEIL_VERTICAL);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_moonveil;
    }
}
