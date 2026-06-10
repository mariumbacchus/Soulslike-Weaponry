package net.soulsweaponry.config;

public class EnchantmentConfig extends MidnightConfig {

    @Entry public static boolean disable_all_enchantments = false;
    @Entry public static boolean disable_enchantment_fast_hands = false;
    @Entry public static boolean disable_enchantment_posture_breaker = false;
    @Entry public static boolean disable_enchantment_stagger = false;
    @Entry public static boolean disable_enchantment_ethereal_ammunition = false;
    @Entry public static boolean disable_enchantment_explosive_rounds = false;
    @Entry public static boolean disable_enchantment_chain_lightning = false;
    @Entry public static boolean disable_enchantment_misfire_curse = false;
    @Entry public static boolean disable_enchantment_blight_carrier = false;
    @Entry public static boolean disable_enchantment_frostsilver = false;
    @Entry public static boolean disable_enchantment_phantom_trace = false;
    @Entry public static boolean disable_enchantment_tether = false;
    @Entry public static boolean disable_enchantment_ricochet = false;

    @Entry(min=0) public static float blight_carrier_enchant_blight_duration = 160;
    @Entry(min=0) public static float blight_carrier_enchant_blight_per_level = 2;

    @Entry(min=0) public static float chain_lightning_enchant_damage_mod_per_level = 0.7f;
    @Entry(min=0) public static float chain_lightning_enchant_range_per_level = 2.25f;

    @Entry(min=0) public static float frostsilver_enchant_permafrost_per_level = 4;
    @Entry(min=0) public static float frostsilver_enchant_permafrost_duration = 100;

    @Entry(min=0) public static double misfire_curse_enchant_trigger_chance = 0.3;

    @Entry(min=0) public static float phantom_trace_enchant_phantom_projectile_damage_mod = 0.5f;

    @Entry(min=0) public static float ricochet_enchant_bounce_per_level = 1;

    @Entry(min=0) public static float stagger_enchant_posture_loss_applied_per_level = 5;
    @Entry(min=0) public static float stagger_enchant_posture_loss_on_player_modifier = 1f;

    @Entry(min=0) public static float tether_enchant_drag_mod = 1f;
    @Entry(min=0) public static float tether_enchant_min_activation_range_per_level = 3f;
}
