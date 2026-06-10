package net.soulsweaponry.items.gun;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.GunConfig;
import net.soulsweaponry.items.abilities.use.ShootCannonball;

public class HunterCannon extends GunItem {

    private static final ShootCannonball SHOOT_SILVER_CANNONBALL = new ShootCannonball(
            GunConfig.hunter_cannon_damage,
            GunConfig.hunter_cannon_velocity,
            GunConfig.hunter_cannon_divergence,
            (int) GunConfig.hunter_cannon_posture_loss,
            GunConfig.hunter_cannon_posture_loss_per_enchant_level,
            (int) GunConfig.hunter_cannon_projectile_amount,
            GunConfig.hunter_cannon_projectile_amount_per_level,
            (int) GunConfig.hunter_cannon_bullets_needed_with_infinity,
            (int) GunConfig.hunter_cannon_bullets_needed,
            (int) GunConfig.hunter_cannon_level_to_unlock_infinity,
            3, 120, 60,
            (int) GunConfig.hunter_cannon_min_cooldown,
            (int) GunConfig.hunter_cannon_cooldown,
            (int) GunConfig.hunter_cannon_reduced_cooldown_per_fast_hands,
            50, 0.4f,
            GunConfig.hunter_cannon_launch_power,
            GunConfig.hunter_cannon_bullets_bypass_entity_invincibility_frames
    );

    public HunterCannon(Settings settings) {
        super(settings);
        this.addAbility(SHOOT_SILVER_CANNONBALL);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return GunConfig.disable_use_hunter_cannon;
    }
}