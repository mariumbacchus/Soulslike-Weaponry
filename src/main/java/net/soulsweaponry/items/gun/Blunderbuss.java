package net.soulsweaponry.items.gun;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.GunConfig;
import net.soulsweaponry.items.abilities.use.ShootSilverBullet;

public class Blunderbuss extends GunItem {

    private static final ShootSilverBullet SHOOT_SILVER_BULLET = new ShootSilverBullet(
            GunConfig.blunderbuss_damage,
            GunConfig.blunderbuss_velocity,
            GunConfig.blunderbuss_divergence,
            (int) GunConfig.blunderbuss_posture_loss,
            GunConfig.blunderbuss_posture_loss_per_enchant_level,
            (int) GunConfig.blunderbuss_projectile_amount,
            GunConfig.blunderbuss_projectile_amount_per_level,
            (int) GunConfig.blunderbuss_bullets_needed_with_infinity,
            (int) GunConfig.blunderbuss_bullets_needed,
            (int) GunConfig.blunderbuss_level_to_unlock_infinity,
            1, 60, 25,
            (int) GunConfig.blunderbuss_min_cooldown,
            (int) GunConfig.blunderbuss_cooldown,
            (int) GunConfig.blunderbuss_reduced_cooldown_per_fast_hands_level,
            50, 0.2f,
            GunConfig.blunderbuss_bullets_bypass_entity_invincibility_frames
    );

    public Blunderbuss(Settings settings) {
        super(settings);
        this.addAbility(SHOOT_SILVER_BULLET);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return GunConfig.disable_use_hunter_blunderbuss;
    }
}