package net.soulsweaponry.items.scythe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.targetdamaged.Omnivamp;
import net.soulsweaponry.items.abilities.use.UmbralTrespass;

public class DarkinScythePrime extends ModdedSword {

    private static final UmbralTrespass UMBRAL_TRESPASS = new UmbralTrespass(
            WeaponConfig.darkin_scythe_prime_umbral_trespass_damage,
            WeaponConfig.darkin_scythe_prime_umbral_trespass_bonus_damage_per_level,
            WeaponConfig.darkin_scythe_prime_umbral_trespass_bonus_enchant_damage_modifier,
            (int) WeaponConfig.darkin_scythe_prime_umbral_trespass_min_cooldown,
            (int) WeaponConfig.darkin_scythe_prime_umbral_trespass_cooldown,
            (int) WeaponConfig.darkin_scythe_prime_umbral_trespass_reduced_cooldown_per_level,
            WeaponConfig.darkin_scythe_prime_umbral_trespass_heal_modifier,
            (int) WeaponConfig.darkin_scythe_prime_umbral_trespass_ticks_before_dismount,
            WeaponConfig.darkin_scythe_prime_umbral_trespass_bonus_percent_max_health_damage,
            WeaponConfig.darkin_scythe_prime_umbral_trespass_max_range_from_target
    );
    private static final Omnivamp OMNIVAMP = new Omnivamp(
            WeaponConfig.darkin_scythe_prime_omnivamp_base_heal,
            WeaponConfig.darkin_scythe_prime_omnivamp_bonus_heal_per_level,
            (int) WeaponConfig.darkin_scythe_prime_omnivamp_min_cooldown,
            (int) WeaponConfig.darkin_scythe_prime_omnivamp_cooldown,
            (int) WeaponConfig.darkin_scythe_prime_omnivamp_reduced_cooldown_per_level
    );

    public DarkinScythePrime(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) (WeaponConfig.darkin_scythe_damage + WeaponConfig.darkin_scythe_bonus_damage), WeaponConfig.darkin_scythe_prime_attack_speed, settings);
        this.addAbility(UMBRAL_TRESPASS, OMNIVAMP);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_darkin_scythe_prime;
    }
}