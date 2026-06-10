package net.soulsweaponry.items.staff;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.usagetick.DragonMist;
import net.soulsweaponry.items.abilities.abilitykeybind.ShootDragonProjectile;

public class DragonStaff extends ModdedSword {

    private static final ShootDragonProjectile SHOOT_DRAGON_PROJECTILE = new ShootDragonProjectile(
            1.5f,
            (int) WeaponConfig.dragon_staff_projectile_max_age,
            WeaponConfig.dragon_staff_projectile_cloud_base_radius,
            WeaponConfig.dragon_staff_projectile_cloud_bonus_radius_per_level,
            (int) WeaponConfig.dragon_staff_projectile_cloud_duration,
            (int) WeaponConfig.dragon_staff_projectile_cloud_bonus_duration_per_level,
            WeaponConfig.dragon_staff_projectile_cloud_radius_growth,
            WeaponConfig.dragon_staff_projectile_cloud_bonus_radius_growth_per_level,
            (int) WeaponConfig.dragon_staff_projectile_cloud_effect_duration,
            (int) WeaponConfig.dragon_staff_projectile_cloud_effect_amp,
            (int) WeaponConfig.dragon_staff_projectile_min_cooldown,
            (int) WeaponConfig.dragon_staff_projectile_cooldown,
            (int) WeaponConfig.dragon_staff_projectile_reduced_cooldown_per_level
    );
    private static final DragonMist DRAGON_MIST = new DragonMist(
            WeaponConfig.dragon_staff_vigorous_fog_heal_tamed_entities_owned_by_others,
            WeaponConfig.dragon_staff_vigorous_fog_damage_and_heal,
            WeaponConfig.dragon_staff_vigorous_fog_bonus_damage_and_heal_per_level,
            (int) WeaponConfig.dragon_staff_vigorous_fog_mist_effect_duration,
            (int) WeaponConfig.dragon_staff_vigorous_fog_mist_effect_amp,
            (int) WeaponConfig.dragon_staff_vigorous_fog_max_use_time,
            (int) WeaponConfig.dragon_staff_vigorous_fog_bonus_max_use_time_per_level,
            (int) WeaponConfig.dragon_staff_vigorous_fog_min_cooldown,
            (int) WeaponConfig.dragon_staff_vigorous_fog_cooldown,
            (int) WeaponConfig.dragon_staff_vigorous_fog_reduced_cooldown_per_level
    );

    public DragonStaff(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.dragon_staff_damage, WeaponConfig.dragon_staff_attack_speed, settings);
        this.addAbility(SHOOT_DRAGON_PROJECTILE, DRAGON_MIST);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_dragon_staff;
    }
}