package net.soulsweaponry.items.gun;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.use.ShootSilverBullet;

public class HunterPistol extends GunItem {

    private static final ShootSilverBullet SHOOT_SILVER_BULLET = new ShootSilverBullet(
            ConfigConstructor.hunter_pistol_damage,
            ConfigConstructor.hunter_pistol_velocity,
            ConfigConstructor.hunter_pistol_divergence,
            (int) ConfigConstructor.hunter_pistol_posture_loss,
            ConfigConstructor.hunter_pistol_posture_loss_per_enchant_level,
            (int) ConfigConstructor.hunter_pistol_projectile_amount,
            ConfigConstructor.hunter_pistol_projectile_amount_per_level,
            (int) ConfigConstructor.hunter_pistol_bullets_needed_with_infinity,
            (int) ConfigConstructor.hunter_pistol_bullets_needed,
            (int) ConfigConstructor.hunter_pistol_level_to_unlock_infinity,
            1, 60, 25,
            (int) ConfigConstructor.hunter_pistol_min_cooldown,
            (int) ConfigConstructor.hunter_pistol_cooldown,
            (int) ConfigConstructor.hunter_pistol_reduced_cooldown_per_fast_hands,
            10, 0.1f,
            ConfigConstructor.hunter_pistol_bullets_bypass_entity_invincibility_frames
    );

    public HunterPistol(Settings settings) {
        super(settings);
        this.addAbility(SHOOT_SILVER_BULLET);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_hunter_pistol;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_hunter_pistol;
    }
}