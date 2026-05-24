package net.soulsweaponry.items.gun;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.usagetick.GatlingSilverBullets;

public class GatlingGun extends GunItem {

    private static final GatlingSilverBullets SHOOT_SILVER_BULLET = new GatlingSilverBullets(
            ConfigConstructor.gatling_gun_damage,
            ConfigConstructor.gatling_gun_velocity,
            ConfigConstructor.gatling_gun_divergence,
            (int) ConfigConstructor.gatling_gun_posture_loss,
            ConfigConstructor.gatling_gun_posture_loss_per_enchant_level,
            (int) ConfigConstructor.gatling_gun_projectile_amount,
            ConfigConstructor.gatling_gun_projectile_amount_per_level,
            (int) ConfigConstructor.gatling_gun_bullets_needed_with_infinity,
            (int) ConfigConstructor.gatling_gun_bullets_needed,
            (int) ConfigConstructor.gatling_gun_level_to_unlock_infinity,
            3, 60, 25,
            (int) ConfigConstructor.gatling_gun_min_cooldown,
            (int) ConfigConstructor.gatling_gun_cooldown,
            (int) ConfigConstructor.gatling_gun_reduced_cooldown_per_fast_hands,
            2, 0.15f,
            (int) ConfigConstructor.gatling_gun_max_use_time,
            (int) ConfigConstructor.gatling_gun_bonus_max_use_time_per_fast_hands, 4,
            ConfigConstructor.gatling_gun_bullets_bypass_entity_invincibility_frames
    );

    public GatlingGun(Settings settings) {
        super(settings);
        this.addAbility(SHOOT_SILVER_BULLET);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_gatling_gun;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_gatling_gun;
    }
}