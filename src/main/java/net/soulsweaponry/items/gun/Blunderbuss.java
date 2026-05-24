package net.soulsweaponry.items.gun;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.use.ShootSilverBullet;

public class Blunderbuss extends GunItem {

    private static final ShootSilverBullet SHOOT_SILVER_BULLET = new ShootSilverBullet(
            ConfigConstructor.blunderbuss_damage,
            ConfigConstructor.blunderbuss_velocity,
            ConfigConstructor.blunderbuss_divergence,
            (int) ConfigConstructor.blunderbuss_posture_loss,
            ConfigConstructor.blunderbuss_posture_loss_per_enchant_level,
            (int) ConfigConstructor.blunderbuss_projectile_amount,
            ConfigConstructor.blunderbuss_projectile_amount_per_level,
            (int) ConfigConstructor.blunderbuss_bullets_needed_with_infinity,
            (int) ConfigConstructor.blunderbuss_bullets_needed,
            (int) ConfigConstructor.blunderbuss_level_to_unlock_infinity,
            1, 60, 25,
            (int) ConfigConstructor.blunderbuss_min_cooldown,
            (int) ConfigConstructor.blunderbuss_cooldown,
            (int) ConfigConstructor.blunderbuss_reduced_cooldown_per_fast_hands_level,
            50, 0.2f,
            ConfigConstructor.blunderbuss_bullets_bypass_entity_invincibility_frames
    );

    public Blunderbuss(Settings settings) {
        super(settings);
        this.addAbility(SHOOT_SILVER_BULLET);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_hunter_blunderbuss;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_blunderbuss;
    }
}