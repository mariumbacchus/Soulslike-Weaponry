package net.soulsweaponry.items.gun;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.GunConfig;
import net.soulsweaponry.items.abilities.use.ShootSilverBullet;

public class HunterPistol extends GunItem {

    private static final ShootSilverBullet SHOOT_SILVER_BULLET = new ShootSilverBullet(
            GunConfig.hunter_pistol_damage,
            GunConfig.hunter_pistol_velocity,
            GunConfig.hunter_pistol_divergence,
            (int) GunConfig.hunter_pistol_posture_loss,
            GunConfig.hunter_pistol_posture_loss_per_enchant_level,
            (int) GunConfig.hunter_pistol_projectile_amount,
            GunConfig.hunter_pistol_projectile_amount_per_level,
            (int) GunConfig.hunter_pistol_bullets_needed_with_infinity,
            (int) GunConfig.hunter_pistol_bullets_needed,
            (int) GunConfig.hunter_pistol_level_to_unlock_infinity,
            1, 60, 25,
            (int) GunConfig.hunter_pistol_min_cooldown,
            (int) GunConfig.hunter_pistol_cooldown,
            (int) GunConfig.hunter_pistol_reduced_cooldown_per_fast_hands,
            10, 0.1f,
            GunConfig.hunter_pistol_bullets_bypass_entity_invincibility_frames
    );

    public HunterPistol(Settings settings) {
        super(settings);
        this.addAbility(SHOOT_SILVER_BULLET);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return GunConfig.disable_use_hunter_pistol;
    }
}