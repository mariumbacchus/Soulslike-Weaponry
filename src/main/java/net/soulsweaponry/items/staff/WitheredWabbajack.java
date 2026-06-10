package net.soulsweaponry.items.staff;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.entity.projectile.WitheredWabbajackProjectile;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.use.ShootRandomProjectile;

public class WitheredWabbajack extends ModdedSword {

    private static final WitheredWabbajackProjectile.EntityHitAttributes ENTITY_HIT_ATTRIBUTES =
            new WitheredWabbajackProjectile.EntityHitAttributes(
                    (int) WeaponConfig.withered_wabbajack_projectile_entity_hit_power_bound,
                    (int) WeaponConfig.withered_wabbajack_projectile_entity_hit_power_luck_mod,
                    WeaponConfig.withered_wabbajack_projectile_entity_hit_power_luck_factor_mod,
                    (int) WeaponConfig.withered_wabbajack_projectile_entity_hit_effect_amp_bound,
                    (int) WeaponConfig.withered_wabbajack_projectile_entity_hit_effect_amp_luck_mod,
                    WeaponConfig.withered_wabbajack_projectile_entity_hit_effect_amp_luck_factor_mod,
                    (int) WeaponConfig.withered_wabbajack_projectile_entity_hit_effect_duration_bound,
                    (int) WeaponConfig.withered_wabbajack_projectile_entity_hit_effect_duration_luck_mod,
                    WeaponConfig.withered_wabbajack_projectile_entity_hit_effect_duration_luck_factor_mod
    );
    private static final WitheredWabbajackProjectile.CollisionAttributes COLLISION_ATTRIBUTES =
            new WitheredWabbajackProjectile.CollisionAttributes(
                    (int) WeaponConfig.withered_wabbajack_projectile_collision_power_bound,
                    (int) WeaponConfig.withered_wabbajack_projectile_collision_power_luck_mod,
                    WeaponConfig.withered_wabbajack_projectile_collision_power_luck_factor_mod
    );
    private static final ShootRandomProjectile WABBAJACK = new ShootRandomProjectile(
            WeaponConfig.withered_wabbajack_base_luck_factor,
            WeaponConfig.withered_wabbajack_bonus_luck_factor_per_level,
            WeaponConfig.withered_wabbajack_projectile_speed,
            ENTITY_HIT_ATTRIBUTES, COLLISION_ATTRIBUTES
    );

    public WitheredWabbajack(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.withered_wabbajack_damage, WeaponConfig.withered_wabbajack_attack_speed, settings);
        this.addAbility(WABBAJACK);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_withered_wabbajack;
    }
}