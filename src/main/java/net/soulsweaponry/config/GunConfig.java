package net.soulsweaponry.config;

public class GunConfig extends MidnightConfig {

    @Entry public static boolean disable_gun_recipes = false;

    @Entry public static boolean disable_use_hunter_pistol = false;
    @Entry public static boolean disable_use_hunter_blunderbuss = false;
    @Entry public static boolean disable_use_gatling_gun = false;
    @Entry public static boolean disable_use_hunter_cannon = false;

    @Entry public static boolean is_fireproof_blunderbuss = false;
    @Entry public static boolean is_fireproof_gatling_gun = false;
    @Entry public static boolean is_fireproof_hunter_cannon = false;
    @Entry public static boolean is_fireproof_hunter_pistol = false;

    @Entry public static boolean can_projectiles_apply_posture_loss = true;
    @Entry public static float silver_bullet_undead_bonus_damage = 4f;
    @Entry(min=0) public static float silver_bullet_posture_loss_on_player_modifier = 0.75f;

    @Entry public static float blunderbuss_damage = 8f;
    @Entry public static float blunderbuss_velocity = 3f;
    @Entry public static float blunderbuss_divergence = 10f;
    @Entry public static float blunderbuss_projectile_amount = 3;
    @Entry public static float blunderbuss_projectile_amount_per_level = 0.4f;
    @Entry public static float blunderbuss_posture_loss = 30;
    @Entry public static float blunderbuss_posture_loss_per_enchant_level = 3;
    @Entry public static float blunderbuss_min_cooldown = 50;
    @Entry public static float blunderbuss_cooldown = 200;
    @Entry public static float blunderbuss_reduced_cooldown_per_fast_hands_level = 8;
    @Entry public static float blunderbuss_bullets_needed = 3;
    @Entry public static float blunderbuss_level_to_unlock_infinity = 2;
    @Entry public static boolean blunderbuss_bullets_bypass_entity_invincibility_frames = true;
    @Entry public static float blunderbuss_bullets_needed_with_infinity = 3;

    @Entry public static float gatling_gun_damage = 3;
    @Entry public static float gatling_gun_velocity = 3f;
    @Entry public static float gatling_gun_divergence = 3f;
    @Entry public static float gatling_gun_projectile_amount = 1;
    @Entry public static float gatling_gun_projectile_amount_per_level = 0;
    @Entry public static float gatling_gun_posture_loss = 10;
    @Entry public static float gatling_gun_posture_loss_per_enchant_level = 3;
    @Entry public static float gatling_gun_max_use_time = 100;
    @Entry public static float gatling_gun_bonus_max_use_time_per_fast_hands = 70;
    @Entry public static float gatling_gun_min_cooldown = 20;
    @Entry public static float gatling_gun_cooldown = 120;
    @Entry public static float gatling_gun_reduced_cooldown_per_fast_hands = 24;
    @Entry public static float gatling_gun_bullets_needed = 1;
    @Entry public static float gatling_gun_bullets_needed_with_infinity = 1;
    @Entry public static float gatling_gun_level_to_unlock_infinity = 2;
    @Entry public static boolean gatling_gun_bullets_bypass_entity_invincibility_frames = true;

    @Entry public static float hunter_cannon_damage = 30f;
    @Entry public static float hunter_cannon_velocity = 3f;
    @Entry public static float hunter_cannon_divergence = 1f;
    @Entry public static float hunter_cannon_projectile_amount = 1;
    @Entry public static float hunter_cannon_projectile_amount_per_level = 0;
    @Entry public static float hunter_cannon_posture_loss = 120;
    @Entry public static float hunter_cannon_posture_loss_per_enchant_level = 50;
    @Entry public static float hunter_cannon_min_cooldown = 80;
    @Entry public static float hunter_cannon_cooldown = 300;
    @Entry public static float hunter_cannon_reduced_cooldown_per_fast_hands = 32;
    @Entry public static float hunter_cannon_bullets_needed = 10;
    @Entry public static float hunter_cannon_bullets_needed_with_infinity = 10;
    @Entry public static float hunter_cannon_level_to_unlock_infinity = 2;
    @Entry public static float hunter_cannon_launch_power = 2f;
    @Entry public static boolean hunter_cannon_bullets_bypass_entity_invincibility_frames = false;

    @Entry public static float hunter_pistol_damage = 6f;
    @Entry public static float hunter_pistol_velocity = 3f;
    @Entry public static float hunter_pistol_divergence = 1f;
    @Entry public static float hunter_pistol_projectile_amount = 1;
    @Entry public static float hunter_pistol_projectile_amount_per_level = 0;
    @Entry public static float hunter_pistol_posture_loss = 50;
    @Entry public static float hunter_pistol_posture_loss_per_enchant_level = 10;
    @Entry public static float hunter_pistol_min_cooldown = 10;
    @Entry public static float hunter_pistol_cooldown = 50;
    @Entry public static float hunter_pistol_reduced_cooldown_per_fast_hands = 8;
    @Entry public static float hunter_pistol_bullets_needed = 1;
    @Entry public static float hunter_pistol_bullets_needed_with_infinity = 1;
    @Entry public static float hunter_pistol_level_to_unlock_infinity = 2;
    @Entry public static boolean hunter_pistol_bullets_bypass_entity_invincibility_frames = false;
}
