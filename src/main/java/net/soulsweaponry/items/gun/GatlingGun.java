package net.soulsweaponry.items.gun;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.GunConfig;
import net.soulsweaponry.items.abilities.usagetick.GatlingSilverBullets;

// NOTE: Remember to add the item to ConventionalItemTags.BOW_TOOLS or something like that to make UseAction.BOW animation work
public class GatlingGun extends GunItem {

    private static final GatlingSilverBullets SHOOT_SILVER_BULLET = new GatlingSilverBullets(
            GunConfig.gatling_gun_damage,
            GunConfig.gatling_gun_velocity,
            GunConfig.gatling_gun_divergence,
            (int) GunConfig.gatling_gun_posture_loss,
            GunConfig.gatling_gun_posture_loss_per_enchant_level,
            (int) GunConfig.gatling_gun_projectile_amount,
            GunConfig.gatling_gun_projectile_amount_per_level,
            (int) GunConfig.gatling_gun_bullets_needed_with_infinity,
            (int) GunConfig.gatling_gun_bullets_needed,
            (int) GunConfig.gatling_gun_level_to_unlock_infinity,
            3, 60, 25,
            (int) GunConfig.gatling_gun_min_cooldown,
            (int) GunConfig.gatling_gun_cooldown,
            (int) GunConfig.gatling_gun_reduced_cooldown_per_fast_hands,
            2, 0.15f,
            (int) GunConfig.gatling_gun_max_use_time,
            (int) GunConfig.gatling_gun_bonus_max_use_time_per_fast_hands, 4,
            GunConfig.gatling_gun_bullets_bypass_entity_invincibility_frames
    );

    public GatlingGun(Settings settings) {
        super(settings);
        this.addAbility(SHOOT_SILVER_BULLET);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return GunConfig.disable_use_gatling_gun;
    }
}