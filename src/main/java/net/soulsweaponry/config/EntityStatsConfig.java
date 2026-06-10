package net.soulsweaponry.config;

public class EntityStatsConfig extends MidnightConfig {

    @Entry public static boolean disable_posture_mechanic_for_all_mobs = false;
    @Entry public static float base_posture_unit = 120;
    @Entry public static float base_posture_buildup_resistance_unless_overridden = 0f;
    @Entry public static float posture_loss_reduction_amount = 1;
    @Entry public static float posture_loss_reduction_interval = 8;

    @Entry public static float posture_break_damage_per_amp = 6f;
    @Entry public static float posture_break_player_damage_per_amp = 2f;
    @Entry(min=0, max=1) public static float posture_break_percent_health_damage = 0.05f;

    @Entry public static boolean disable_bleed_mechanic_for_all_mobs = false;
    @Entry public static float max_bleed = 200;
    @Entry public static float base_bleed_buildup_resistance_unless_overridden = 0f;
    @Entry public static float base_bleed_damage_resistance_unless_overridden = 0f;
    @Entry public static float bleed_reduction_amount = 1;
    @Entry public static float bleed_reduction_interval = 10;
    @Entry public static float bleed_base_damage = 6f;
    @Entry(min=0, max=1) public static float bleed_percent_health_damage = 0.1f;
    @Entry public static float bleed_effect_increase_per_amp = 6;
    @Entry public static float bleed_effect_base_increase = 4;

    @Entry public static boolean disable_frost_buildup_mechanic_for_all_mobs = false;
    @Entry public static float max_frost_buildup = 200;
    @Entry public static float base_frost_buildup_resistance_unless_overridden = 0f;
    @Entry public static float base_frost_damage_resistance_unless_overridden = 0f;
    @Entry public static float frost_reduction_amount = 3;
    @Entry public static float frost_reduction_interval = 4;
    @Entry public static float frost_base_damage = 4f;
    @Entry(min=0, max=1) public static float frost_percent_health_damage = 0.15f;
    @Entry public static double frost_explosion_range = 1.25;
    @Entry public static float frost_permafrost_spread_effect_amp = 2;
    @Entry public static float permafrost_effect_base_frost_buildup = 1;
    @Entry public static float permafrost_effect_buildup_per_amp = 1;
}
