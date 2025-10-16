package net.soulsweaponry.items.scythe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.targetdamaged.Omnivamp;
import net.soulsweaponry.items.abilities.use.UmbralTrespass;

public class DarkinScythePrime extends ModdedSword {

    private static final UmbralTrespass UMBRAL_TRESPASS = new UmbralTrespass(
            ConfigConstructor.darkin_scythe_prime_umbral_trespass_damage,
            ConfigConstructor.darkin_scythe_prime_umbral_trespass_bonus_damage_per_level,
            ConfigConstructor.darkin_scythe_prime_umbral_trespass_bonus_enchant_damage_modifier,
            (int) ConfigConstructor.darkin_scythe_prime_umbral_trespass_min_cooldown,
            (int) ConfigConstructor.darkin_scythe_prime_umbral_trespass_cooldown,
            (int) ConfigConstructor.darkin_scythe_prime_umbral_trespass_reduced_cooldown_per_level,
            ConfigConstructor.darkin_scythe_prime_umbral_trespass_heal_modifier,
            (int) ConfigConstructor.darkin_scythe_prime_umbral_trespass_ticks_before_dismount,
            ConfigConstructor.darkin_scythe_prime_umbral_trespass_bonus_percent_max_health_damage
    );
    private static final Omnivamp OMNIVAMP = new Omnivamp(
            ConfigConstructor.darkin_scythe_prime_omnivamp_base_heal,
            ConfigConstructor.darkin_scythe_prime_omnivamp_bonus_heal_per_level,
            (int) ConfigConstructor.darkin_scythe_prime_omnivamp_min_cooldown,
            (int) ConfigConstructor.darkin_scythe_prime_omnivamp_cooldown,
            (int) ConfigConstructor.darkin_scythe_prime_omnivamp_reduced_cooldown_per_level
    );

    public DarkinScythePrime(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) (ConfigConstructor.darkin_scythe_damage + ConfigConstructor.darkin_scythe_bonus_damage), ConfigConstructor.darkin_scythe_prime_attack_speed, settings);
        this.addAbility(UMBRAL_TRESPASS, OMNIVAMP);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_darkin_scythe_prime;
    }
}