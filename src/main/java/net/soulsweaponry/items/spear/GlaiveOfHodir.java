package net.soulsweaponry.items.spear;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.BladeDance;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowGhostGlaive;

public class GlaiveOfHodir extends ModdedSword {

    private static final BladeDance BLADE_DANCE = new BladeDance(
            WeaponConfig.glaive_of_hodir_blade_dance_bonus_damage_per_amp,
            WeaponConfig.glaive_of_hodir_blade_dance_bonus_attack_speed_per_amp,
            (int) WeaponConfig.glaive_of_hodir_blade_dance_max_amp,
            (user, itemStack) -> {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                        (int) WeaponConfig.glaive_of_hodir_blade_dance_resistance_duration,
                        (int) (WeaponConfig.glaive_of_hodir_blade_dance_resistance_amplifier)
                ));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION,
                        (int) WeaponConfig.glaive_of_hodir_blade_dance_absorption_duration,
                        (int) (WeaponConfig.glaive_of_hodir_blade_dance_absorption_amplifier)
                ));
            },
            (int) WeaponConfig.glaive_of_hodir_blade_dance_max_amp_effects_min_cooldown,
            (int) WeaponConfig.glaive_of_hodir_blade_dance_max_amp_effects_cooldown,
            (int) WeaponConfig.glaive_of_hodir_blade_dance_max_amp_effects_reduced_cooldown_per_level
    );
    private static final ThrowGhostGlaive THROW_GHOST_GLAIVE = new ThrowGhostGlaive(
            WeaponConfig.glaive_of_hodir_projectile_damage,
            WeaponConfig.glaive_of_hodir_projectile_bonus_damage_per_level,
            2f,
            (int) WeaponConfig.glaive_of_hodir_projectile_posture_loss,
            (int) WeaponConfig.glaive_of_hodir_projectile_max_age,
            (int) WeaponConfig.glaive_of_hodir_projectile_min_cooldown,
            (int) WeaponConfig.glaive_of_hodir_projectile_cooldown,
            (int) WeaponConfig.glaive_of_hodir_projectile_reduced_cooldown_per_level
    );

    public GlaiveOfHodir(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.glaive_of_hodir_damage, WeaponConfig.glaive_of_hodir_attack_speed, settings);
        this.addAbility(BLADE_DANCE, THROW_GHOST_GLAIVE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_glaive_of_hodir;
    }
}