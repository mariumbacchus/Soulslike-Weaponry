package net.soulsweaponry.items.spear;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.BladeDance;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowGhostGlaive;

public class GlaiveOfHodir extends ModdedSword {

    private static final BladeDance BLADE_DANCE = new BladeDance(
            ConfigConstructor.glaive_of_hodir_blade_dance_bonus_damage_per_amp,
            ConfigConstructor.glaive_of_hodir_blade_dance_bonus_attack_speed_per_amp,
            (int) ConfigConstructor.glaive_of_hodir_blade_dance_max_amp,
            (user, itemStack) -> {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                        (int) ConfigConstructor.glaive_of_hodir_blade_dance_resistance_duration,
                        (int) (ConfigConstructor.glaive_of_hodir_blade_dance_resistance_amplifier)
                ));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION,
                        (int) ConfigConstructor.glaive_of_hodir_blade_dance_absorption_duration,
                        (int) (ConfigConstructor.glaive_of_hodir_blade_dance_absorption_amplifier)
                ));
            },
            (int) ConfigConstructor.glaive_of_hodir_blade_dance_max_amp_effects_min_cooldown,
            (int) ConfigConstructor.glaive_of_hodir_blade_dance_max_amp_effects_cooldown,
            (int) ConfigConstructor.glaive_of_hodir_blade_dance_max_amp_effects_reduced_cooldown_per_level
    );
    private static final ThrowGhostGlaive THROW_GHOST_GLAIVE = new ThrowGhostGlaive(
            ConfigConstructor.glaive_of_hodir_projectile_damage,
            ConfigConstructor.glaive_of_hodir_projectile_bonus_damage_per_level,
            2f,
            (int) ConfigConstructor.glaive_of_hodir_projectile_posture_loss,
            (int) ConfigConstructor.glaive_of_hodir_projectile_max_age,
            (int) ConfigConstructor.glaive_of_hodir_projectile_min_cooldown,
            (int) ConfigConstructor.glaive_of_hodir_projectile_cooldown,
            (int) ConfigConstructor.glaive_of_hodir_projectile_reduced_cooldown_per_level
    );

    public GlaiveOfHodir(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.glaive_of_hodir_damage, ConfigConstructor.glaive_of_hodir_attack_speed, settings);
        this.addAbility(BLADE_DANCE, THROW_GHOST_GLAIVE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_glaive_of_hodir;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_glaive_of_hodir;
    }
}