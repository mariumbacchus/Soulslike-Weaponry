package net.soulsweaponry.items.misc;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.abilities.abilitykeybind.LunarHerald;
import net.soulsweaponry.items.abilities.attackclick.ShootSmallMoonlight;

public class MoonstoneRing extends ModdedItem {

    public static final ShootSmallMoonlight MOONSTONE_RING_SHOOT_MOONLIGHT = new ShootSmallMoonlight(
            WeaponConfig.moonstone_ring_lunar_herald_projectile_velocity,
            WeaponConfig.moonstone_ring_lunar_herald_projectile_damage,
            0, // Stack is only used when held, can't even upgrade normal items yet either
            WeaponConfig.moonstone_ring_lunar_herald_projectile_bonus_damage_per_moon_herald_amp,
            0,
            (int) WeaponConfig.moonstone_ring_lunar_herald_projectile_cooldown_when_held,
            0,
            (int) WeaponConfig.moonstone_ring_lunar_herald_projectile_cooldown_when_keybind
    );
    private static final LunarHerald LUNAR_HERALD = new LunarHerald(
            (int) WeaponConfig.moonstone_ring_lunar_herald_duration,
            WeaponConfig.moonstone_ring_lunar_herald_bonus_duration_per_level,
            (int) WeaponConfig.moonstone_ring_lunar_herald_base_amplifier,
            WeaponConfig.moonstone_ring_lunar_herald_bonus_amp_per_level
    );

    public MoonstoneRing(Settings settings) {
        super(settings);
        this.addAbility(LUNAR_HERALD, MOONSTONE_RING_SHOOT_MOONLIGHT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_moonstone_ring;
    }
}
