package net.soulsweaponry.config;

public class EntityConfig extends MidnightConfig {

    @Entry public static double dark_sorcerer_health = 10D;
    @Entry public static double dark_sorcerer_bonus_armor = 0D;

    @Entry public static boolean can_evil_forlorn_spawn = true;
    @Entry(min=0) public static float evil_forlorn_spawnweight = 15;
    @Entry public static double evil_forlorn_health = 30D;
    @Entry public static double evil_forlorn_bonus_armor = 0D;

    @Entry public static double forlorn_health = 30D;
    @Entry public static double forlorn_bonus_armor = 0D;

    @Entry public static double frost_giant_health = 50D;
    @Entry public static double frost_giant_armor = 8D;

    @Entry public static boolean can_moderately_sized_chungus_spawn = true;
    @Entry(min=0) public static float moderately_sized_chungus_spawnweight = 100;
    @Entry public static float chungus_monolith_radius = 32;
    @Entry public static double moderately_sized_chungus_heath = 14D;
    @Entry public static double moderately_sized_chungus_armor = 0D;
    @Entry public static boolean can_chungus_barter = true;

    @Entry public static double remnant_health = 20D;
    @Entry public static double remnant_bonus_armor = 0D;

    @Entry public static double rime_spectre_health = 10D;
    @Entry public static double rime_spectre_armor = 0D;

    @Entry public static double soulmass_health = 75D;
    @Entry public static double soulmass_armor = 10D;

    @Entry public static double familiar_ghost_health = 10D;
    @Entry public static double familiar_ghost_armor = 0D;

    @Entry public static double warmth_health = 20D;
    @Entry public static double warmth_armor = 0D;

    @Entry public static boolean can_withered_demon_spawn = true;
    @Entry(min=0) public static float withered_demon_spawnweight = 20;
    @Entry public static double withered_demon_health = 80D;
    @Entry public static double withered_demon_armor = 2D;

    @Entry public static boolean can_bosses_break_blocks = true;

    @Entry public static boolean decaying_king_disable_respawn = false;
    @Entry public static boolean decaying_king_consume_item_on_summoning = true;
    @Entry(min = 1, max = 1000000D) public static double decaying_king_health = 600D;
    @Entry(min = 1, max = 1000000D) public static double decaying_king_armor = 10D;
    @Entry(min = 0) public static float decaying_king_attack_cooldown_ticks = 20;
    @Entry(min = 0) public static float decaying_king_special_cooldown_ticks = 60;
    @Entry(min = 0) public static float decaying_king_damage_modifier = 1f;
    @Entry(min = 0) public static float decaying_king_xp = 500;
    @Entry public static boolean decaying_king_disables_shields = true;
    @Entry public static boolean decaying_king_is_fire_immune = true;
    @Entry public static boolean decaying_king_has_inverted_heal_and_harm = true;
    @Entry public static String[] decaying_king_status_effect_blacklist = {};

    @Entry public static boolean returning_knight_disable_respawn = false;
    @Entry public static boolean returning_knight_consume_item_on_summoning = true;
    @Entry(min = 1, max = 1000000D) public static double returning_knight_health = 500D;
    @Entry(min = 1, max = 1000000D) public static double returning_knight_armor = 15D;
    @Entry(min = 0) public static float returning_knight_attack_cooldown_ticks = 40;
    @Entry(min = 0) public static float returning_knight_special_cooldown_ticks = 80;
    @Entry(min = 0) public static float returning_knight_summon_cooldown_ticks = 200;
    @Entry(min = 0) public static float returning_knight_damage_modifier = 1f;
    @Entry(min = 0) public static float returning_knight_xp = 500;
    @Entry public static String[] returning_knight_projectile_immunity_whitelist = {};
    @Entry public static boolean returning_knight_disables_shields = true;
    @Entry public static boolean returning_knight_is_fire_immune = true;
    @Entry public static boolean returning_knight_has_inverted_heal_and_harm = true;
    @Entry public static String[] returning_knight_status_effect_blacklist = {"minecraft:poison"};

    @Entry public static boolean old_champions_remains_disable_respawn = false;
    @Entry public static boolean old_champions_remains_consume_item_on_summoning = false;
    @Entry(min = 1, max = 1000000D) public static double old_champions_remains_health = 300D;
    @Entry(min = 1, max = 1000000D) public static double old_champions_remains_armor = 10D;
    @Entry(min = 0) public static float old_champions_remains_attack_cooldown_ticks = 10;
    @Entry(min = 0) public static float old_champions_remains_special_cooldown_ticks = 300;
    @Entry(min = 0) public static float old_champions_remains_damage_modifier = 1f;
    @Entry public static float old_champions_remains_hits_before_growing_resistant = 3;
    @Entry public static float old_champions_remains_max_projectile_hits_before_immune = 3;
    @Entry public static float old_champions_remains_bleed_applied = 100;
    @Entry public static float old_champions_remains_xp = 200;
    @Entry public static boolean old_champions_remains_disables_shields = true;
    @Entry public static boolean old_champions_remains_disables_shields_all_attacks = false;
    @Entry public static boolean old_champions_remains_is_fire_immune = true;
    @Entry public static boolean old_champions_remains_has_inverted_heal_and_harm = true;
    @Entry public static String[] old_champions_remains_status_effect_blacklist = {"soulsweapons:bleed"};

    @Entry(min = 1, max = 1000000D) public static double frenzied_shade_health = 150D;
    @Entry(min = 1, max = 1000000D) public static double frenzied_shade_armor = 2D;
    @Entry public static float frenzied_shade_damage_modifier = 1f;
    @Entry public static float frenzied_shade_cooldown = 10;
    @Entry public static float frenzied_shade_xp = 400;
    @Entry public static boolean frenzied_shade_disables_shields = false;
    @Entry public static boolean frenzied_shade_is_fire_immune = true;
    @Entry public static boolean frenzied_shade_has_inverted_heal_and_harm = true;
    @Entry public static String[] frenzied_shade_status_effect_blacklist = {};

    @Entry public static boolean chaos_monarch_disable_respawn = false;
    @Entry public static boolean chaos_monarch_consume_item_on_summoning = true;
    @Entry(min = 1, max = 1000000D) public static double chaos_monarch_health = 450D;
    @Entry(min = 1, max = 1000000D) public static double chaos_monarch_armor = 4D;
    @Entry(min = 0) public static float chaos_monarch_attack_cooldown_ticks = 20;
    @Entry(min = 0) public static float chaos_monarch_damage_modifier = 1f;
    @Entry(min = 0) public static float chaos_monarch_xp = 500;
    @Entry public static boolean chaos_monarch_wither_ground = true;
    @Entry public static float chaos_monarch_wither_ground_range = 6;
    @Entry public static boolean chaos_monarch_disables_shields = true;
    @Entry public static boolean chaos_monarch_is_fire_immune = true;
    @Entry public static boolean chaos_monarch_has_inverted_heal_and_harm = true;
    @Entry public static String[] chaos_monarch_status_effect_blacklist = {"soulsweapons:bleed", "minecraft:levitation", "minecraft:wither"};

    @Entry public static boolean fallen_icon_disable_respawn = false;
    @Entry public static boolean fallen_icon_consume_item_on_summoning = true;
    @Entry(min = 1, max = 1000000D) public static double fallen_icon_health = 550D;
    @Entry(min = 1, max = 1000000D) public static double fallen_icon_armor = 20D;
    @Entry(min = 0) public static float fallen_icon_attack_cooldown_ticks_phase_1 = 30;
    @Entry(min = 0) public static float fallen_icon_attack_cooldown_ticks_phase_2 = 0;
    @Entry(min = 0) public static float fallen_icon_special_cooldown_ticks = 50;
    @Entry(min = 0) public static float fallen_icon_damage_modifier = 1f;
    @Entry(min = 0) public static float fallen_icon_xp = 600;
    @Entry public static String[] fallen_icon_projectile_immunity_whitelist = {};
    @Entry public static boolean fallen_icon_disables_shields = true;
    @Entry public static boolean fallen_icon_is_fire_immune = true;
    @Entry public static boolean fallen_icon_has_inverted_heal_and_harm = false;
    @Entry public static String[] fallen_icon_status_effect_blacklist = {"minecraft:poison"};

    @Entry(min = 1, max = 1000000D) public static double day_stalker_health = 600D;
    @Entry(min = 1, max = 1000000D) public static double day_stalker_armor = 15D;
    @Entry(min = 0) public static float day_stalker_damage_modifier = 1f;
    @Entry(min = 0) public static float day_stalker_empowered_projectile_damage_taken_modifier_phase_1 = 0.8f;
    @Entry(min = 0) public static float day_stalker_empowered_projectile_damage_taken_modifier_phase_2 = 0.6f;
    @Entry(min = 0) public static float day_stalker_xp = 500;
    @Entry public static boolean day_stalker_disables_shields = true;
    @Entry public static boolean day_stalker_is_fire_immune = true;
    @Entry public static boolean day_stalker_has_inverted_heal_and_harm = false;
    @Entry(min = 0, max = 1) public static double day_stalker_projectile_cause_air_combustion_below_percent_health = 0.5;
    @Entry public static String[] day_stalker_status_effect_blacklist = {};
    @Entry(min = 0) public static double day_stalker_cooldown_modifier_phase_1 = 1D;
    @Entry(min = 0) public static double day_stalker_cooldown_modifier_phase_2 = 1D;
    @Entry(min = 0) public static double day_stalker_special_cooldown_modifier_phase_1 = 1D;
    @Entry(min = 0) public static double day_stalker_special_cooldown_modifier_phase_2 = 1D;

    @Entry(min = 0) public static float duo_fight_time_before_switch = 400;

    @Entry(min = 1, max = 1000000D) public static double night_prowler_health = 500D;
    @Entry(min = 1, max = 1000000D) public static double night_prowler_armor = 10D;
    @Entry(min = 0) public static float night_prowler_damage_modifier = 1f;
    @Entry(min = 0) public static float night_prowler_eclipse_healing = 3f;
    @Entry(min = 0) public static boolean night_prowler_eclipse_skulls_glow = false;
    @Entry(min = 0) public static boolean night_prowler_eclipse_skulls_destroy_blocks = true;
    @Entry(min = 0) public static float night_prowler_eclipse_skulls_explosion_power = 2f;
    @Entry(min = 0) public static float night_prowler_eclipse_skulls_explosion_power_charged = 4f;
    @Entry(min = 0) public static float night_prowler_eclipse_skull_waves_before_charged_skull_wave = 3;
    @Entry(min = 0, max = 1) public static double night_prowler_projectile_heal_below_percent_health = 0.5;
    @Entry public static String[] night_prowler_projectile_immunity_whitelist = {};
    @Entry(min = 0) public static float night_prowler_projectile_heal_amount = 5f;
    @Entry(min = 0) public static double night_prowler_teleport_chance = 0.3D;
    @Entry(min = 0) public static float night_prowler_xp = 500;
    @Entry public static boolean night_prowler_disables_shields = true;
    @Entry public static boolean night_prowler_is_fire_immune = true;
    @Entry public static boolean night_prowler_has_inverted_heal_and_harm = false;
    @Entry public static String[] night_prowler_status_effect_blacklist = {};
    @Entry(min = 0) public static double night_prowler_cooldown_modifier_phase_1 = 1D;
    @Entry(min = 0) public static double night_prowler_cooldown_modifier_phase_2 = 1D;
    @Entry(min = 0) public static double night_prowler_special_cooldown_modifier_phase_1 = 1D;
    @Entry(min = 0) public static double night_prowler_special_cooldown_modifier_phase_2 = 1D;
}