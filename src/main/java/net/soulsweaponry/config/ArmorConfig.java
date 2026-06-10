package net.soulsweaponry.config;

public class ArmorConfig extends MidnightConfig {

    @Entry public static boolean disable_armor_recipes = false;

    @Entry public static boolean disable_recipe_arkenplate = false;
    @Entry public static boolean disable_recipe_chaos_helmet = false;
    @Entry public static boolean disable_recipe_soul_ingot_helmet = false;
    @Entry public static boolean disable_recipe_soul_ingot_chestplate = false;
    @Entry public static boolean disable_recipe_soul_ingot_leggings = false;
    @Entry public static boolean disable_recipe_soul_ingot_boots = false;
    @Entry public static boolean disable_recipe_soul_robes_helmet = false;
    @Entry public static boolean disable_recipe_soul_robes_chestplate = false;
    @Entry public static boolean disable_recipe_soul_robes_leggings = false;
    @Entry public static boolean disable_recipe_soul_robes_boots = false;
    @Entry public static boolean disable_recipe_forlorn_helmet = false;
    @Entry public static boolean disable_recipe_forlorn_chestplate = false;
    @Entry public static boolean disable_recipe_forlorn_leggings = false;
    @Entry public static boolean disable_recipe_forlorn_boots = false;
    @Entry public static boolean disable_recipe_withered_chest = false;

    @Entry public static boolean disable_recipe_enhanced_arkenplate = false;
    @Entry public static boolean disable_recipe_enhanced_withered_chest = false;

    @Entry public static boolean disable_use_arkenplate = false;
    @Entry public static boolean disable_use_enhanced_arkenplate = false;
    @Entry public static boolean disable_use_chaos_crown = false;
    @Entry public static boolean disable_use_chaos_robes = false;
    @Entry public static boolean disable_use_hallowheart = false;
    @Entry public static boolean disable_use_enhanced_hallowheart = false;
    @Entry public static boolean disable_use_soul_ingot_armor = false;
    @Entry public static boolean disable_use_soul_robes_armor = false;
    @Entry public static boolean disable_use_forlorn_armor = false;

    @Entry public static boolean is_fireproof_arkenplate = true;
    @Entry public static boolean is_fireproof_chaos_crown = true;
    @Entry public static boolean is_fireproof_chaos_helmet = true;
    @Entry public static boolean is_fireproof_chaos_robes = false;
    @Entry public static boolean is_fireproof_hallowheart = true;

    @Entry public static boolean is_fireproof_soul_ingot_set = false;
    @Entry public static boolean is_fireproof_soul_robes_set = false;
    @Entry public static boolean is_fireproof_forlorn_set = true;

    @Entry public static float[] chaos_armor_armor_points = {4, 7, 10, 5};
    @Entry public static float[] chaos_armor_base_posture_increase = {0, 0, 0, 0};
    @Entry public static float[] chaos_armor_posture_buildup_resistances = {0, 0, 0, 0};
    @Entry public static float[] chaos_armor_bleed_buildup_resistances = {40, 70, 100, 60};
    @Entry public static float[] chaos_armor_bleed_damage_resistances = {30, 50, 60, 40};

    @Entry public static float[] enhanced_chaos_armor_armor_points = {4, 7, 10, 5};
    @Entry public static float[] enhanced_chaos_armor_base_posture_increase = {0, 0, 0, 0};
    @Entry public static float[] enhanced_chaos_armor_posture_buildup_resistances = {0, 0, 0, 0};
    @Entry public static float[] enhanced_chaos_armor_bleed_buildup_resistances = {40, 70, 100, 60};
    @Entry public static float[] enhanced_chaos_armor_bleed_damage_resistances = {30, 50, 60, 40};

    @Entry public static float[] chaos_set_armor_points = {2, 3, 4, 1};
    @Entry public static float[] chaos_set_base_posture_increase = {0, 0, 0, 0};
    @Entry public static float[] chaos_set_posture_buildup_resistances = {0, 0, 0, 0};
    @Entry public static float[] chaos_set_bleed_buildup_resistances = {0, 0, 0, 0};
    @Entry public static float[] chaos_set_bleed_damage_resistances = {0, 0, 0, 0};

    @Entry public static float[] soul_ingot_armor_points = {3, 5, 7, 3};
    @Entry public static float[] soul_ingot_base_posture_increase = {0, 0, 0, 0};
    @Entry public static float[] soul_ingot_posture_buildup_resistances = {0, 0, 0, 0};
    @Entry public static float[] soul_ingot_bleed_buildup_resistances = {2.5f, 7.5f, 10, 5};
    @Entry public static float[] soul_ingot_bleed_damage_resistances = {2, 4, 6, 3};

    @Entry public static float[] soul_robes_armor_points = {2, 3, 4, 3};
    @Entry public static float[] soul_robes_base_posture_increase = {0, 0, 0, 0};
    @Entry public static float[] soul_robes_posture_buildup_resistances = {0, 0, 0, 0};
    @Entry public static float[] soul_robes_bleed_buildup_resistances = {0, 0, 0, 0};
    @Entry public static float[] soul_robes_bleed_damage_resistances = {0, 0, 0, 0};

    @Entry public static float[] forlorn_armor_armor_points = {3, 6, 8, 3};
    @Entry public static float[] forlorn_armor_base_posture_increase = {10, 20, 35, 15};
    @Entry public static float[] forlorn_armor_posture_buildup_resistances = {20, 40, 50, 30};
    @Entry public static float[] forlorn_armor_bleed_buildup_resistances = {35, 50, 75, 40};
    @Entry public static float[] forlorn_armor_bleed_damage_resistances = {10, 30, 40, 20};

    @Entry public static float[] withered_armor_armor_points = {4, 7, 10, 5};
    @Entry public static float[] withered_armor_base_posture_increase = {0, 0, 0, 0};
    @Entry public static float[] withered_armor_posture_buildup_resistances = {10, 20, 25, 15};
    @Entry public static float[] withered_armor_bleed_buildup_resistances = {15, 30, 40, 25};
    @Entry public static float[] withered_armor_bleed_damage_resistances = {0, 0, 0, 0};

    @Entry public static float[] enhanced_withered_armor_armor_points = {4, 7, 10, 5};
    @Entry public static float[] enhanced_withered_armor_base_posture_increase = {0, 0, 0, 0};
    @Entry public static float[] enhanced_withered_armor_posture_buildup_resistances = {10, 20, 25, 15};
    @Entry public static float[] enhanced_withered_armor_bleed_buildup_resistances = {15, 30, 40, 25};
    @Entry public static float[] enhanced_withered_armor_bleed_damage_resistances = {0, 0, 0, 0};

    @Entry public static float chaos_cape_corrupt_ground_range = 3;
    @Entry public static float chaos_cape_corrupt_ground_range_per_level = 1;
    @Entry public static float chaos_cape_corrupt_ground_status_effect_range = 3;
    @Entry public static float chaos_cape_corrupt_ground_status_effect_range_per_level = 1;
    @Entry public static float chaos_cape_corrupt_ground_status_effect_wither_duration = 80;
    @Entry public static float chaos_cape_corrupt_ground_status_effect_wither_amp = 1;

    @Entry public static float chaos_crown_luck_given = 1;
    @Entry public static float chaos_crown_flip_effect_duration_mod = 0.33f;
    @Entry public static float chaos_crown_flip_effect_amp_mod = 0.5f;
    @Entry public static float chaos_crown_flip_effect_min_cooldown = 100;
    @Entry public static float chaos_crown_flip_effect_cooldown = 300;
    @Entry public static float chaos_crown_flip_effect_reduced_cooldown_per_level = 40;

    @Entry(min=0,max=1) public static float arkenplate_aftershock_activate_percent_health_threshold = 0.5f;
    @Entry(min=0,max=1) public static float arkenplate_aftershock_activate_bonus_percent_health_threshold_per_level = 0.05f;
    @Entry public static float arkenplate_aftershock_knockback = 1f;
    @Entry public static float arkenplate_aftershock_bonus_knockback_per_level = 0.5f;
    @Entry public static float arkenplate_aftershock_damage = 6f;
    @Entry public static float arkenplate_aftershock_bonus_damage_per_level = 2f;
    @Entry public static float arkenplate_aftershock_expansion_radius = 5f;
    @Entry public static float arkenplate_aftershock_min_cooldown = 60;
    @Entry public static float arkenplate_aftershock_cooldown = 160;
    @Entry public static float arkenplate_aftershock_reduced_cooldown_per_level = 20;
    @Entry(min=0,max=1) public static float arkenplate_unbreakable_activate_percent_threshold = 0.5f;
    @Entry(min=0,max=1) public static float arkenplate_unbreakable_activate_bonus_percent_threshold_per_level = 0.05f;
    @Entry public static float arkenplate_unbreakable_resistance_amp = 1;
    @Entry public static float arkenplate_unbreakable_resistance_bonus_amp_per_level = 0.3f;
    @Entry public static float arkenplate_unbreakable_magic_resistance_amp = 1;
    @Entry public static float arkenplate_unbreakable_magic_resistance_bonus_amp_per_level = 0.4f;

    @Entry(min=0,max=1) public static float enhanced_arkenplate_aftershock_activate_percent_health_threshold = 0.5f;
    @Entry(min=0,max=1) public static float enhanced_arkenplate_aftershock_activate_bonus_percent_health_threshold_per_level = 0.05f;
    @Entry public static float enhanced_arkenplate_aftershock_knockback = 1.5f;
    @Entry public static float enhanced_arkenplate_aftershock_bonus_knockback_per_level = 0.6f;
    @Entry public static float enhanced_arkenplate_aftershock_damage = 7f;
    @Entry public static float enhanced_arkenplate_aftershock_bonus_damage_per_level = 3f;
    @Entry public static float enhanced_arkenplate_aftershock_expansion_radius = 5f;
    @Entry public static float enhanced_arkenplate_aftershock_min_cooldown = 50;
    @Entry public static float enhanced_arkenplate_aftershock_cooldown = 160;
    @Entry public static float enhanced_arkenplate_aftershock_reduced_cooldown_per_level = 30;
    @Entry public static float enhanced_arkenplate_aftershock_weakness_duration = 160;
    @Entry public static float enhanced_arkenplate_aftershock_weakness_amp = 2;
    @Entry(min=0,max=1) public static float enhanced_arkenplate_unbreakable_activate_percent_threshold = 0.5f;
    @Entry(min=0,max=1) public static float enhanced_arkenplate_unbreakable_activate_bonus_percent_threshold_per_level = 0.05f;
    @Entry public static float enhanced_arkenplate_unbreakable_resistance_amp = 1;
    @Entry public static float enhanced_arkenplate_unbreakable_resistance_bonus_amp_per_level = 0.3f;
    @Entry public static float enhanced_arkenplate_unbreakable_magic_resistance_amp = 1;
    @Entry public static float enhanced_arkenplate_unbreakable_magic_resistance_bonus_amp_per_level = 0.4f;
    @Entry(min=0,max=1) public static float enhanced_arkenplate_mirror_trigger_percent = 0.5f;
    @Entry(min=0,max=1) public static float enhanced_arkenplate_mirror_bonus_trigger_percent_per_level = 0.05f;

    @Entry public static float forlorn_armor_soul_feast_range = 6f;
    @Entry public static float forlorn_armor_soul_feast_bonus_range_per_level = 1f;
    @Entry public static float forlorn_armor_soul_feast_heal = 2f;
    @Entry public static float forlorn_armor_soul_feast_bonus_heal_per_level = 0.5f;

    @Entry public static float soul_ingot_armor_fortified_resistance_duration = 40;
    @Entry public static float soul_ingot_armor_fortified_resistance_amp = 0;
    @Entry public static float soul_ingot_armor_fortified_resistance_amp_per_level = 0.2f;

    @Entry public static float soul_robes_armor_fortified_magic_resistance_duration = 40;
    @Entry public static float soul_robes_armor_fortified_magic_resistance_amp = 3;
    @Entry public static float soul_robes_armor_fortified_magic_resistance_amp_per_level = 0.2f;
    @Entry public static float soul_robes_armor_fortified_night_vision_duration = 400;
    @Entry public static float soul_robes_armor_fortified_night_vision_amp = 0;

    @Entry public static float withered_chest_infectious_damage = 1;
    @Entry public static float withered_chest_infectious_damage_per_level = 1;
    @Entry public static float withered_chest_infectious_knockback = 0.5f;
    @Entry public static float withered_chest_infectious_knockback_per_level = 0;
    @Entry public static float withered_chest_infectious_apply_wither_duration = 100;
    @Entry public static float withered_chest_infectious_apply_wither_duration_per_level = 20;
    @Entry public static float withered_chest_infectious_apply_wither_amplifier = 0;
    @Entry public static float withered_chest_infectious_apply_wither_amplifier_per_level = 0.4f;
    @Entry public static float withered_chest_infectious_apply_fire_seconds = 0;
    @Entry public static float withered_chest_infectious_apply_fire_seconds_per_level = 0;
    @Entry public static float withered_chest_unceasing_life_leach_duration = 400;
    @Entry public static float withered_chest_unceasing_life_leach_duration_per_level = 40;
    @Entry public static float withered_chest_unceasing_life_leach_amplifier = 0;
    @Entry public static float withered_chest_unceasing_life_leach_amplifier_per_level = 0.4f;
    @Entry public static float withered_chest_unceasing_min_cooldown = 100;
    @Entry public static float withered_chest_unceasing_cooldown = 500;
    @Entry public static float withered_chest_unceasing_reduced_cooldown_per_level = 60;

    @Entry public static float enhanced_withered_chest_infectious_damage = 1;
    @Entry public static float enhanced_withered_chest_infectious_damage_per_level = 1;
    @Entry public static float enhanced_withered_chest_infectious_knockback = 0.5f;
    @Entry public static float enhanced_withered_chest_infectious_knockback_per_level = 0;
    @Entry public static float enhanced_withered_chest_infectious_apply_wither_duration = 100;
    @Entry public static float enhanced_withered_chest_infectious_apply_wither_duration_per_level = 20;
    @Entry public static float enhanced_withered_chest_infectious_apply_wither_amplifier = 0;
    @Entry public static float enhanced_withered_chest_infectious_apply_wither_amplifier_per_level = 0.4f;
    @Entry public static float enhanced_withered_chest_infectious_apply_fire_seconds = 6;
    @Entry public static float enhanced_withered_chest_infectious_apply_fire_seconds_per_level = 1;

    @Entry public static float enhanced_withered_chest_unceasing_life_leach_duration = 400;
    @Entry public static float enhanced_withered_chest_unceasing_life_leach_duration_per_level = 40;
    @Entry public static float enhanced_withered_chest_unceasing_life_leach_amplifier = 0;
    @Entry public static float enhanced_withered_chest_unceasing_life_leach_amplifier_per_level = 0.4f;
    @Entry public static float enhanced_withered_chest_unceasing_min_cooldown = 100;
    @Entry public static float enhanced_withered_chest_unceasing_cooldown = 500;
    @Entry public static float enhanced_withered_chest_unceasing_reduced_cooldown_per_level = 60;

    @Entry public static float enhanced_withered_chest_exalt_amp_per_missing_health_percent = 0.05f;
    @Entry public static float enhanced_withered_chest_exalt_amp_per_missing_health_percent_bonus_per_level = 0.01f;
    @Entry public static float enhanced_withered_chest_exalt_amp_max = 4;
    @Entry public static float enhanced_withered_chest_exalt_amp_max_increase_per_level = 0.4f;
    @Entry public static float enhanced_withered_chest_exalt_duration = 20;

}
