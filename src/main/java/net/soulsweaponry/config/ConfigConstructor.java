package net.soulsweaponry.config;

public class ConfigConstructor extends MidnightConfig {

    @Entry public static boolean inform_player_about_disabled_use = true;
    @Entry public static boolean inform_player_about_no_bound_freyr_sword = true;
    @Entry public static boolean inform_player_about_no_soulbound_thrown_weapon = true;
    @Entry public static boolean inform_player_about_cooldown_effect = true;
    @Entry public static boolean inform_player_about_no_souls_to_collect = true;
    @Entry public static boolean inform_player_about_collected_souls = true;

    // Config changes won't apply to generated json files/runtime so no point in having these as entries.
    public static int moonstone_ore_vein_size = 5;
    public static int moonstone_ore_count_per_chunk = 4;
    public static int moonstone_ore_min_height = -63;
    public static int moonstone_ore_max_height = 16;

    public static int verglas_ore_vein_size = 3;
    public static int verglas_ore_count_per_chunk = 48;
    public static int verglas_ore_min_height = -80;
    public static int verglas_ore_max_height = 120;

    @Entry public static boolean disable_weapon_recipes = false;
    @Entry public static boolean disable_gun_recipes = false;
    @Entry public static boolean disable_armor_recipes = false;

    @Entry public static boolean disable_all_enchantments = false;
    @Entry public static boolean disable_enchantment_fast_hands = false;
    @Entry public static boolean disable_enchantment_posture_breaker = false;
    @Entry public static boolean disable_enchantment_stagger = false;

    @Entry public static boolean disable_recipe_bloodthirster = false;
    @Entry public static boolean disable_recipe_bluemoon_greatsword = false;
    @Entry public static boolean disable_recipe_bluemoon_shortsword = false;
    @Entry public static boolean disable_recipe_comet_spear = false;
    @Entry public static boolean disable_recipe_darkin_blade = false;
    @Entry public static boolean disable_recipe_dawnbreaker = false;
    @Entry public static boolean disable_recipe_heap_of_raw_iron = false;
    @Entry public static boolean disable_recipe_dragonslayer_swordspear = false;
    @Entry public static boolean disable_recipe_dragon_staff = false;
    @Entry public static boolean disable_recipe_draugr = false;
    @Entry public static boolean disable_recipe_galeforce = false;
    @Entry public static boolean disable_recipe_rageblade = false;
    @Entry public static boolean disable_recipe_lich_bane = false;
    @Entry public static boolean disable_recipe_moonlight_greatsword = false;
    @Entry public static boolean disable_recipe_moonlight_shortsword = false;
    @Entry public static boolean disable_recipe_nightfall = false;
    @Entry public static boolean disable_recipe_soul_reaper = false;
    @Entry public static boolean disable_recipe_forlorn_scythe = true;
    @Entry public static boolean disable_recipe_whirligig_sawblade = false;
    @Entry public static boolean disable_recipe_withered_wabbajack = false;
    @Entry public static boolean disable_recipe_leviathan_axe = false;
    @Entry public static boolean disable_recipe_skofnung = false;
    @Entry public static boolean disable_recipe_pure_moonlight_greatsword = false;
    @Entry public static boolean disable_recipe_mjolnir = false;
    @Entry public static boolean disable_recipe_sword_of_freyr = false;
    @Entry public static boolean disable_recipe_sting = false;
    @Entry public static boolean disable_recipe_featherlight = false;
    @Entry public static boolean disable_recipe_crucible_sword = false;
    @Entry public static boolean disable_recipe_darkin_scythe = false;
    @Entry public static boolean disable_recipe_kirkhammer = false;
    @Entry public static boolean disable_recipe_ludwigs_holy_blade = false;
    @Entry public static boolean disable_recipe_draupnir_spear = false;
    @Entry public static boolean disable_recipe_holy_moonlight_sword = false;
    @Entry public static boolean disable_recipe_master_sword = false;
    @Entry public static boolean disable_recipe_frostmourne = false;
    @Entry public static boolean disable_recipe_nights_edge = false;
    @Entry public static boolean disable_recipe_empowered_dawnbreaker = false;
    @Entry public static boolean disable_recipe_kraken_slayer_bow = true;
    @Entry public static boolean disable_recipe_kraken_slayer_crossbow = false;
    @Entry public static boolean disable_recipe_darkmoon_longbow = false;

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

    @Entry public static boolean disable_use_bluemoon_shortsword = false;
    @Entry public static boolean disable_use_bluemoon_greatsword = false;
    @Entry public static boolean disable_use_moonlight_shortsword = false;
    @Entry public static boolean disable_use_moonlight_greatsword = false;
    @Entry public static boolean disable_use_pure_moonlight_greatsword = false;
    @Entry public static boolean disable_use_bloodthirster = false;
    @Entry public static boolean disable_use_darkin_blade = false;
    @Entry public static boolean disable_use_dragon_staff = false;
    @Entry public static boolean disable_use_withered_wabbajack = false;
    @Entry public static boolean disable_use_whirligig_sawblade = false;
    @Entry public static boolean disable_use_dragonslayer_swordspear = false;
    @Entry public static boolean disable_use_rageblade = false;
    @Entry public static boolean disable_use_heap_of_raw_iron = false;
    @Entry public static boolean disable_use_nightfall = false;
    @Entry public static boolean disable_use_comet_spear = false;
    @Entry public static boolean disable_use_lich_bane = false;
    @Entry public static boolean disable_use_galeforce = false;
    @Entry public static boolean disable_use_draugr = false;
    @Entry public static boolean disable_use_dawnbreaker = false;
    @Entry public static boolean disable_use_soul_reaper = false;
    @Entry public static boolean disable_use_forlorn_scythe = false;
    @Entry public static boolean disable_use_leviathan_axe = false;
    @Entry public static boolean disable_use_skofnung = false;
    @Entry public static boolean disable_use_skofnung_stone = false;
    @Entry public static boolean disable_use_mjolnir = false;
    @Entry public static boolean disable_use_sword_of_freyr = false;
    // @Entry public static boolean disable_use_sting = false;
    @Entry public static boolean disable_use_featherlight = false;
    @Entry public static boolean disable_use_crucible_sword = false;
    @Entry public static boolean disable_use_darkin_scythe = false;
    @Entry public static boolean disable_use_darkin_scythe_prime = false;
    @Entry public static boolean disable_use_shadow_assassin_scythe = false;
    @Entry public static boolean disable_use_draupnir_spear = false;
    @Entry public static boolean disable_use_holy_moonlight_greatsword = false;
    @Entry public static boolean disable_use_holy_moonlight_sword = false;
    @Entry public static boolean disable_use_frostmourne = false;
    @Entry public static boolean disable_use_master_sword = false;
    @Entry public static boolean disable_use_nights_edge = false;
    @Entry public static boolean disable_use_empowered_dawnbreaker = false;
    @Entry public static boolean disable_use_kraken_slayer_bow = false;
    @Entry public static boolean disable_use_kraken_slayer_crossbow = false;
    @Entry public static boolean disable_use_darkmoon_longbow = false;
    @Entry public static boolean disable_use_hunter_pistol = false;
    @Entry public static boolean disable_use_hunter_blunderbuss = false;
    @Entry public static boolean disable_use_gatling_gun = false;
    @Entry public static boolean disable_use_hunter_cannon = false;
    @Entry public static boolean disable_use_kirkhammer = false;
    @Entry public static boolean disable_use_silver_sword = false;
    @Entry public static boolean disable_use_ludwigs_holy_greatsword = false;
    @Entry public static boolean disable_use_moonstone_ring = false;
    @Entry public static boolean disable_use_chaos_orb = false;

    @Entry public static boolean disable_use_arkenplate = false;
    @Entry public static boolean disable_use_chaos_crown = false;
    @Entry public static boolean disable_use_chaos_robes = false;
    @Entry public static boolean disable_use_soul_ingot_armor = false;
    @Entry public static boolean disable_use_soul_robes_armor = false;
    @Entry public static boolean disable_use_forlorn_armor = false;
    @Entry public static boolean disable_use_withered_chest = false;

    @Entry public static boolean is_fireproof_bloodthirster = true;
    @Entry public static boolean is_fireproof_bluemoon_greatsword = false;
    @Entry public static boolean is_fireproof_bluemoon_shortsword = false;
    @Entry public static boolean is_fireproof_comet_spear = true;
    @Entry public static boolean is_fireproof_darkin_blade = true;
    @Entry public static boolean is_fireproof_dawnbreaker = true;
    @Entry public static boolean is_fireproof_heap_of_raw_iron = false;
    @Entry public static boolean is_fireproof_dragonslayer_swordspear = true;
    @Entry public static boolean is_fireproof_dragon_staff = true;
    @Entry public static boolean is_fireproof_draugr = true;
    @Entry public static boolean is_fireproof_galeforce = true;
    @Entry public static boolean is_fireproof_rageblade = true;
    @Entry public static boolean is_fireproof_lich_bane = true;
    @Entry public static boolean is_fireproof_moonlight_greatsword = true;
    @Entry public static boolean is_fireproof_moonlight_shortsword = true;
    @Entry public static boolean is_fireproof_moonlight_ring = true;
    @Entry public static boolean is_fireproof_nightfall = true;
    @Entry public static boolean is_fireproof_soul_reaper = true;
    @Entry public static boolean is_fireproof_forlorn_scythe = true;
    @Entry public static boolean is_fireproof_whirligig_sawblade = true;
    @Entry public static boolean is_fireproof_withered_wabbajack = true;
    @Entry public static boolean is_fireproof_leviathan_axe = true;
    @Entry public static boolean is_fireproof_skofnung = true;
    @Entry public static boolean is_fireproof_pure_moonlight_greatsword = true;
    @Entry public static boolean is_fireproof_mjolnir = true;
    @Entry public static boolean is_fireproof_sword_of_freyr = true;
    @Entry public static boolean is_fireproof_sting = false;
    @Entry public static boolean is_fireproof_featherlight = true;
    @Entry public static boolean is_fireproof_crucible_sword = true;
    @Entry public static boolean is_fireproof_darkin_scythe = true;
    @Entry public static boolean is_fireproof_darkin_scythe_prime = true;
    @Entry public static boolean is_fireproof_shadow_assassin_scythe = true;
    @Entry public static boolean is_fireproof_kirkhammer = false;
    @Entry public static boolean is_fireproof_ludwigs_holy_blade = false;
    @Entry public static boolean is_fireproof_silver_sword = false;
    @Entry public static boolean is_fireproof_draupnir_spear = true;
    @Entry public static boolean is_fireproof_holy_moonlight_sword = true;
    @Entry public static boolean is_fireproof_holy_moonlight_greatsword = true;
    @Entry public static boolean is_fireproof_master_sword = true;
    @Entry public static boolean is_fireproof_frostmourne = true;
    @Entry public static boolean is_fireproof_nights_edge = true;
    @Entry public static boolean is_fireproof_empowered_dawnbreaker = true;
    @Entry public static boolean is_fireproof_kraken_slayer_bow = true;
    @Entry public static boolean is_fireproof_kraken_slayer_crossbow = true;
    @Entry public static boolean is_fireproof_darkmoon_longbow = true;

    @Entry public static boolean is_fireproof_blunderbuss = false;
    @Entry public static boolean is_fireproof_gatling_gun = false;
    @Entry public static boolean is_fireproof_hunter_cannon = false;
    @Entry public static boolean is_fireproof_hunter_pistol = false;

    @Entry public static boolean is_fireproof_chaos_set = true;
    @Entry public static boolean is_fireproof_soul_ingot_set = false;
    @Entry public static boolean is_fireproof_soul_robes_set = false;
    @Entry public static boolean is_fireproof_forlorn_set = true;
    @Entry public static boolean is_fireproof_withered_set = true;

    @Entry(min=0,max=100) public static int withered_demon_spawnweight = 20;
    @Entry(min=0,max=100) public static int moderatly_sized_chungus_spawnweight = 100;
    @Entry public static int chungus_monolith_radius = 32;
    @Entry(min=0,max=100) public static int evil_forlorn_spawnweight = 15;

    @Entry public static boolean can_withered_demon_spawn = true;
    @Entry public static boolean can_moderatly_sized_chungus_spawn = true;
    @Entry public static boolean can_evil_forlorn_spawn = true;

    @Entry public static boolean can_bosses_break_blocks = true;

    @Entry public static int[] chaos_armor_armor_points = {4, 7, 10, 5};
    @Entry public static int[] chaos_set_armor_points = {2, 3, 4, 1};
    @Entry public static int[] soul_ingot_armor_points = {3, 5, 7, 3};
    @Entry public static int[] soul_robes_armor_points = {2, 3, 4, 3};
    @Entry public static int[] forlorn_armor_armor_points = {3, 6, 8, 3};
    @Entry public static int[] withered_armor_armor_points = {4, 7, 10, 5};

    @Entry public static int chaos_crown_flip_effect_cooldown = 300;
    @Entry public static int arkenplate_shockwave_cooldown = 200;
    @Entry public static float arkenplate_shockwave_knockback = 2f;
    @Entry public static float arkenplate_shockwave_damage = 6f;
    @Entry(min=0,max=1) public static float arkenplate_mirror_trigger_percent = 0.334f;
    @Entry public static float forlorn_set_bonus_range = 6f;
    @Entry public static float forlorn_set_bonus_heal = 2f;
    @Entry(min=0,max=1) public static float withered_chest_strength_trigger_percent_1 = 0.5f;
    @Entry(min=0,max=1) public static float withered_chest_strength_trigger_percent_2 = 0.25f;
    @Entry public static int withered_chest_ability_cooldown = 500;
    @Entry public static int withered_chest_apply_wither_duration = 100;
    @Entry public static int withered_chest_apply_wither_amplifier = 0;
    @Entry public static int withered_chest_apply_fire_seconds = 6;
    @Entry public static int withered_chest_life_leach_duration = 400;
    @Entry public static int withered_chest_life_leach_amplifier = 0;

    @Entry public static int bloodthirster_damage = 8;
    @Entry public static float bloodthirster_attack_speed = 1.6f;
    @Entry public static boolean bloodthirster_overshields = true;
    @Entry public static int lifesteal_item_base_healing = 2;
    @Entry public static boolean lifesteal_item_heal_scales = true;
    @Entry public static int lifesteal_item_cooldown = 60;
    @Entry public static int bluemoon_greatsword_damage = 8;
    @Entry public static float bluemoon_greatsword_attack_speed = 1.2f;
    @Entry public static int bluemoon_greatsword_charge_needed = 8;
    @Entry public static int bluemoon_greatsword_charge_added_post_hit = 1;
    @Entry public static int bluemoon_shortsword_damage = 7;
    @Entry public static float bluemoon_shortsword_attack_speed = 1.6f;
    @Entry public static int bluemoon_shortsword_projectile_cooldown = 120;
    @Entry public static int comet_spear_damage = 8;
    @Entry public static float comet_spear_attack_speed = 1.4f;
    @Entry public static float comet_spear_projectile_damage = 8.0f;
    @Entry public static int comet_spear_ability_damage = 10;
    @Entry public static int comet_spear_skyfall_ability_cooldown = 400;
    @Entry public static int comet_spear_throw_ability_cooldown = 25;
    @Entry public static float comet_spear_calculated_fall_base_radius = 0f;
    @Entry public static float comet_spear_calculated_fall_height_increase_radius_modifier = 2f;
    @Entry public static float comet_spear_calculated_fall_max_radius = 100f;
    @Entry public static float comet_spear_calculated_fall_max_damage = 100f;
    @Entry public static float comet_spear_calculated_fall_height_increase_damage_modifier = 0.2f;
    @Entry public static float comet_spear_calculated_fall_target_launch_modifier = 0.35f;
    @Entry public static boolean comet_spear_calculated_fall_should_heal = false;
    @Entry public static float comet_spear_calculated_fall_heal_from_damage_modifier = 0.1f;
    @Entry public static int crucible_sword_normal_damage = 9;
    @Entry public static float crucible_sword_attack_speed = 1.6f;
    @Entry public static int crucible_sword_empowered_damage = 30;
    @Entry public static int crucible_sword_empowered_cooldown = 300;
    @Entry public static int darkin_blade_damage = 11;
    @Entry public static float darkin_blade_attack_speed = 1f;
    @Entry public static int darkin_blade_ability_damage = 12;
    @Entry public static int darkin_blade_ability_cooldown = 150;
    @Entry public static float darkin_blade_calculated_fall_base_radius = 3f;
    @Entry public static float darkin_blade_calculated_fall_height_increase_radius_modifier = 1.75f;
    @Entry public static float darkin_blade_calculated_fall_target_launch_modifier = 0.25f;
    @Entry public static float darkin_blade_calculated_fall_max_radius = 100f;
    @Entry public static float darkin_blade_calculated_fall_max_damage = 100f;
    @Entry public static float darkin_blade_calculated_fall_height_increase_damage_modifier = 0.2f;
    @Entry public static boolean darkin_blade_calculated_fall_should_heal = true;
    @Entry public static float darkin_blade_calculated_fall_heal_from_damage_modifier = 0.1f;
    @Entry public static int darkin_scythe_damage = 9;
    @Entry public static float darkin_scythe_attack_speed = 1f;
    @Entry public static int darkin_scythe_bonus_damage = 3;
    @Entry public static int darkin_scythe_max_souls = 100;
    @Entry public static float darkin_scythe_prime_ability_damage = 20f;
    @Entry public static float darkin_scythe_prime_attack_speed = 1f;
    @Entry(min=0,max=100) public static float darkin_scythe_prime_ability_percent_health_damage = 10f;
    @Entry public static int darkin_scythe_prime_ability_cooldown = 400;
    @Entry public static float darkin_scythe_prime_heal_modifier = 0.25f;
    @Entry public static int darkin_scythe_prime_ticks_before_dismount = 80;
    @Entry public static int darkmoon_longbow_damage = 9;
    @Entry public static float darkmoon_longbow_max_velocity = 3f;
    @Entry public static int darkmoon_longbow_pull_time_ticks = 25;
    @Entry public static double darkmoon_longbow_ability_damage = 13D;
    @Entry public static int darkmoon_longbow_ability_cooldown_ticks = 150;
    @Entry public static int dawnbreaker_damage = 8;
    @Entry public static float dawnbreaker_attack_speed = 1.6f;
    @Entry public static float dawnbreaker_ability_damage = 10.0f;
    @Entry(min=0,max=1) public static double dawnbreaker_ability_percent_chance_addition = 0.0D;
    @Entry public static boolean dawnbreaker_affect_all_entities = false;
    @Entry public static int empowered_dawnbreaker_damage = 10;
    @Entry public static float empowered_dawnbreaker_attack_speed = 1.2f;
    @Entry public static float empowered_dawnbreaker_ability_damage = 15f;
    @Entry public static int empowered_dawnbreaker_ability_cooldown = 180;
    @Entry public static int heap_of_raw_iron_damage = 10;
    @Entry public static float heap_of_raw_iron_attack_speed = 1f;
    @Entry public static int heap_of_raw_iron_cooldown = 200;
    @Entry public static float heap_of_raw_iron_calculated_fall_base_radius = 2.5f;
    @Entry public static float heap_of_raw_iron_calculated_fall_height_increase_radius_modifier = 1.6f;
    @Entry public static float heap_of_raw_iron_calculated_fall_target_launch_modifier = 0.25f;
    @Entry public static float heap_of_raw_iron_calculated_fall_max_radius = 100f;
    @Entry public static float heap_of_raw_iron_calculated_fall_max_damage = 100f;
    @Entry public static float heap_of_raw_iron_calculated_fall_height_increase_damage_modifier = 0.2f;
    @Entry public static boolean heap_of_raw_iron_calculated_fall_should_heal = false;
    @Entry public static float heap_of_raw_iron_calculated_fall_heal_from_damage_modifier = 0.1f;
    @Entry public static int dragonslayer_swordspear_damage = 8;
    @Entry public static float dragonslayer_swordspear_attack_speed = 1.4f;
    @Entry public static int dragonslayer_swordspear_rain_bonus_damage = 1;
    @Entry public static float dragonslayer_swordspear_rain_total_attack_speed = 1.8f;
    @Entry public static float dragonslayer_swordspear_projectile_damage = 7.0f;
    @Entry public static float dragonslayer_swordspear_ability_damage = 6.0f;
    @Entry public static int dragonslayer_swordspear_lightning_amount = 1;
    @Entry public static int dragonslayer_swordspear_throw_cooldown = 100;
    @Entry public static int dragonslayer_swordspear_ability_cooldown = 300;
    @Entry public static int dragon_staff_damage = 8;
    @Entry public static float dragon_staff_attack_speed = 1.2f;
    @Entry public static int dragon_staff_aura_strength = 1;
    @Entry public static int dragon_staff_cooldown = 100;
    @Entry public static int dragon_staff_use_time = 100;
    @Entry public static int draugr_damage_at_night = 11;
    @Entry public static float draugr_attack_speed = 1.6f;
    @Entry public static int draupnir_spear_damage = 8;
    @Entry public static float draupnir_spear_attack_speed = 1.4f;
    @Entry public static float draupnir_spear_projectile_damage = 10f;
    @Entry public static float draupnir_spear_detonate_power = 1f;
    @Entry public static int draupnir_spear_throw_cooldown = 40;
    @Entry public static int draupnir_spear_detonate_cooldown = 100;
    @Entry public static int draupnir_spear_summon_spears_cooldown = 300;
    @Entry public static int draupnir_spear_max_age = 400;
    @Entry public static int forlorn_scythe_damage = 11;
    @Entry public static float forlorn_scythe_attack_speed = 1f;
    @Entry public static int featherlight_damage = 8;
    @Entry public static float featherlight_attack_speed = 1.6f;
    @Entry public static float featherlight_calculated_fall_base_radius = 2.5f;
    @Entry public static float featherlight_calculated_fall_height_increase_radius_modifier = 1.6f;
    @Entry public static float featherlight_calculated_fall_target_launch_modifier = 0.20f;
    @Entry public static float featherlight_calculated_fall_max_radius = 100f;
    @Entry public static float featherlight_calculated_fall_max_damage = 100f;
    @Entry public static float featherlight_calculated_fall_height_increase_damage_modifier = 0.2f;
    @Entry public static boolean featherlight_calculated_fall_should_heal = false;
    @Entry public static float featherlight_calculated_fall_heal_from_damage_modifier = 0.1f;
    @Entry public static int frostmourne_damage = 11;
    @Entry public static float frostmourne_attack_speed = 1.6f;
    @Entry public static int frostmourne_summoned_allies_cap = 50;
    @Entry public static int galeforce_damage = 10;
    @Entry public static float galeforce_max_velocity = 3f;
    @Entry public static int galeforce_pull_time_ticks = 20;
    @Entry public static int galeforce_speed_effect_duration_ticks = 50;
    @Entry public static int galeforce_speed_effect_amplifier = 4;
    @Entry public static int galeforce_dash_cooldown = 80;
    @Entry public static int rageblade_damage = 7;
    @Entry public static float rageblade_attack_speed = 1.6f;
    @Entry public static boolean rageblade_haste_cap = true;
    @Entry public static int holy_moonlight_greatsword_damage = 10;
    @Entry public static float holy_moonlight_greatsword_attack_speed = 1.2f;
    @Entry public static int holy_moonlight_ability_charge_needed = 50;
    @Entry public static int holy_moonlight_greatsword_charge_added_post_hit = 3;
    @Entry public static float holy_moonlight_ability_damage = 20f;
    @Entry public static float holy_moonlight_ability_knockup = 0.3f;
    @Entry public static int holy_moonlight_ability_cooldown = 150;
    @Entry public static int holy_moonlight_ruptures_amount = 8;
    @Entry public static int holy_moonlight_sword_damage = 7;
    @Entry public static float holy_moonlight_sword_attack_speed = 1.6f;
    @Entry public static int holy_moonlight_sword_max_bonus_damage = 2;
    @Entry public static int holy_moonlight_sword_charge_added_post_hit = 1;
    @Entry public static int kirkhammer_damage = 9;
    @Entry public static float kirkhammer_attack_speed = 1f;
    @Entry public static int kirkhammer_silver_sword_damage = 6;
    @Entry public static float kirkhammer_silver_sword_attack_speed = 1.6f;
    @Entry public static int kraken_slayer_damage = 7;
    @Entry public static float kraken_slayer_max_velocity = 3f;
    @Entry public static int kraken_slayer_pull_time_ticks = 10;
    @Entry public static int kraken_slayer_crossbow_damage = 9;
    @Entry public static float kraken_slayer_crossbow_max_velocity = 3.15f;
    @Entry public static int kraken_slayer_crossbow_pull_time_ticks = 15;
    @Entry public static float kraken_slayer_bonus_true_damage = 4f;
    @Entry public static float kraken_slayer_player_true_damage_taken_modifier = 0.4f;
    @Entry public static int leviathan_axe_damage = 10;
    @Entry public static float leviathan_axe_attack_speed = 1.2f;
    @Entry public static float leviathan_axe_projectile_damage = 7f;
    @Entry public static double leviathan_axe_return_speed = 4D;
    @Entry public static int lich_bane_damage = 7;
    @Entry public static float lich_bane_attack_speed = 1.6f;
    @Entry public static float lich_bane_bonus_magic_damage = 2f;
    @Entry public static int ludwigs_holy_greatsword_damage = 8;
    @Entry public static float ludwigs_holy_greatsword_attack_speed = 1.2f;
    @Entry public static float righteous_undead_bonus_damage = 2f;
    @Entry public static int master_sword_damage = 8;
    @Entry public static float master_sword_attack_speed = 1.6f;
    @Entry public static float master_sword_projectile_damage = 11.0f;
    @Entry public static int mjolnir_damage = 9;
    @Entry public static float mjolnir_attack_speed = 1.2f;
    @Entry public static int mjolnir_rain_bonus_damage = 2;
    @Entry public static float mjolnir_rain_total_attack_speed = 1.2f;
    @Entry public static float mjolnir_smash_damage = 8f;
    @Entry public static float mjolnir_projectile_damage = 7f;
    @Entry public static int mjolnir_lightning_smash_cooldown = 200;
    @Entry public static int mjolnir_lightning_circle_amount = 3;
    @Entry public static int mjolnir_riptide_cooldown = 300;
    @Entry public static double mjolnir_return_speed = 4D;
    @Entry public static int moonlight_greatsword_damage = 9;
    @Entry public static float moonlight_greatsword_attack_speed = 1.2f;
    @Entry public static float moonlight_greatsword_projectile_damage = 8.0f;
    @Entry public static int pure_moonlight_greatsword_damage = 11;
    @Entry public static float pure_moonlight_greatsword_attack_speed = 1.2f;
    @Entry public static int moonlight_shortsword_damage = 8;
    @Entry public static float moonlight_shortsword_attack_speed = 1.6f;
    @Entry public static float moonlight_shortsword_projectile_damage = 3.0f;
    @Entry public static int moonlight_shortsword_projectile_cooldown = 13;
    @Entry public static int moonlight_ring_projectile_cooldown = 5;
    @Entry public static int nightfall_damage = 11;
    @Entry public static float nightfall_attack_speed = 1f;
    @Entry public static float nightfall_ability_damage = 18.0f;
    @Entry public static int nightfall_ability_shield_power = 2;
    @Entry public static int nightfall_shield_cooldown = 500;
    @Entry public static int nightfall_smash_cooldown = 300;
    @Entry public static int nightfall_summoned_allies_cap = 50;
    @Entry(min=0, max=1) public static double nightfall_summon_chance = 0.3D;
    @Entry public static float nightfall_calculated_fall_base_radius = 3f;
    @Entry public static float nightfall_calculated_fall_height_increase_radius_modifier = 1.5f;
    @Entry public static float nightfall_calculated_fall_target_launch_modifier = 0.30f;
    @Entry public static float nightfall_calculated_fall_max_radius = 100f;
    @Entry public static float nightfall_calculated_fall_max_damage = 100f;
    @Entry public static float nightfall_calculated_fall_height_increase_damage_modifier = 0.2f;
    @Entry public static boolean nightfall_calculated_fall_should_heal = false;
    @Entry public static float nightfall_calculated_fall_heal_from_damage_modifier = 0.1f;
    @Entry public static int nights_edge_weapon_damage = 10;
    @Entry public static float nights_edge_weapon_attack_speed = 1.2f;
    @Entry public static int nights_edge_ability_cooldown = 120;
    @Entry public static float nights_edge_ability_damage = 10f;
    @Entry public static int shadow_assassin_scythe_shadow_step_bonus_damage = 2;
    @Entry public static float shadow_assassin_scythe_attack_speed = 1f;
    @Entry public static int shadow_assassin_scythe_shadow_step_ticks = 60;
    @Entry public static int shadow_assassin_scythe_shadow_step_cooldown = 160;
    @Entry public static float shadow_assassin_scythe_ability_damage = 40f;
    @Entry public static int shadow_assassin_scythe_ability_cooldown = 300;
    @Entry public static int shadow_assassin_scythe_ticks_before_dismount = 120;
    @Entry public static int skofnung_damage = 8;
    @Entry public static float skofnung_attack_speed = 1.6f;
    @Entry public static int skofnung_bonus_damage = 2;
    @Entry public static int skofnung_disable_heal_duration = 100;
    @Entry public static int skofnung_stone_additional_empowered_strikes = 8;
    @Entry public static int soul_reaper_damage = 11;
    @Entry public static float soul_reaper_attack_speed = 1f;
    @Entry public static int soul_reaper_summoned_allies_cap = 50;
    @Entry public static int sting_damage = 6;
    @Entry public static float sting_attack_speed = 1.6f;
    @Entry public static float sting_bonus_arthropod_damage = 4f;
    @Entry public static int sword_of_freyr_damage = 7;
    @Entry public static float sword_of_freyr_attack_speed = 1.6f;
    @Entry public static boolean sword_of_freyr_friendly_fire = true;
    //@Entry public static float sword_of_freyr_animation_speed = 1.0f;
    @Entry public static int whirligig_sawblade_damage = 8;
    @Entry public static float whirligig_sawblade_attack_speed = 1.6f;
    @Entry public static float whirligig_sawblade_ability_damage = 5.0f;
    @Entry public static int whirligig_sawblade_cooldown = 100;
    @Entry public static int whirligig_sawblade_use_time = 100;
    @Entry public static int withered_wabbajack_damage = 8;
    @Entry public static float withered_wabbajack_attack_speed = 1.2f;

    @Entry public static boolean calculated_fall_hits_immune_entities = false;
    @Entry public static boolean ultra_heavy_haste_when_strength = true;
    @Entry public static float ultra_heavy_posture_loss_modifier_when_stagger_enchant = 2f;
    @Entry public static boolean prioritize_off_hand_shield_over_weapon = false;

    @Entry public static int max_posture_loss = 200;
    @Entry public static int stagger_enchant_posture_loss_applied_per_level = 5;
    @Entry(min=0) public static float stagger_enchant_posture_loss_on_player_modifier = 1f;
    @Entry public static boolean enable_shield_parry = true;
    @Entry public static int shield_parry_cooldown = 40;
    @Entry(min=2) public static int shield_parry_max_animation_frames = 10;
    @Entry(min=2) public static int shield_parry_frames = 3;
    @Entry public static boolean can_projectiles_apply_posture_loss = true;
    @Entry public static float silver_bullet_undead_bonus_damage = 4f;
    @Entry(min=0) public static float silver_bullet_posture_loss_on_player_modifier = 0.75f;
    @Entry public static float blunderbuss_damage = 13f;
    @Entry public static float blunderbuss_velocity = 3f;
    @Entry public static float blunderbuss_divergence = 10f;
    @Entry public static int blunderbuss_projectile_amount = 3;
    @Entry public static int blunderbuss_posture_loss = 25;
    @Entry public static int blunderbuss_cooldown = 80;
    @Entry public static int blunderbuss_bullets_needed = 2;
    @Entry public static float gatling_gun_damage = 3;
    @Entry public static float gatling_gun_velocity = 3f;
    @Entry public static float gatling_gun_divergence = 3f;
    @Entry public static int gatling_gun_posture_loss = 9;
    @Entry public static int gatling_gun_max_time = 100;
    @Entry public static int gatling_gun_cooldown = 120;
    @Entry public static int gatling_gun_bullets_needed = 1;
    @Entry public static float hunter_cannon_damage = 30f;
    @Entry public static float hunter_cannon_velocity = 3f;
    @Entry public static float hunter_cannon_divergence = 1f;
    @Entry public static int hunter_cannon_posture_loss = 60;
    @Entry public static int hunter_cannon_cooldown = 300;
    @Entry public static int hunter_cannon_bullets_needed = 10;
    @Entry public static float hunter_pistol_damage = 6f;
    @Entry public static float hunter_pistol_velocity = 3f;
    @Entry public static float hunter_pistol_divergence = 1f;
    @Entry public static int hunter_pistol_posture_loss = 30;
    @Entry public static int hunter_pistol_cooldown = 50;
    @Entry public static int hunter_pistol_bullets_needed = 1;

    @Entry(min=1, max=1000000D) public static double decaying_king_health = 500D;
    @Entry(min=0) public static int decaying_king_attack_cooldown_ticks = 20;
    @Entry(min=0) public static int decaying_king_special_cooldown_ticks = 60;
    @Entry(min=0) public static float decaying_king_damage_modifier = 1f;
    @Entry(min=0) public static int decaying_king_xp = 500;

    @Entry(min=1, max=1000000D) public static double returning_knight_health = 400D;
    @Entry(min=0) public static int returning_knight_attack_cooldown_ticks = 40;
    @Entry(min=0) public static int returning_knight_special_cooldown_ticks = 80;
    @Entry(min=0) public static float returning_knight_damage_modifier = 1f;
    @Entry(min=0) public static int returning_knight_xp = 500;

    @Entry(min=1, max=1000000D) public static double old_champions_remains_health = 200D;
    @Entry(min=0) public static int old_champions_remains_attack_cooldown_ticks = 10;
    @Entry(min=0) public static int old_champions_remains_special_cooldown_ticks = 300;
    @Entry(min=0) public static float old_champions_remains_damage_modifier = 1f;
    @Entry public static int old_champions_remains_hits_before_growing_resistant = 3;
    @Entry public static int old_champions_remains_xp = 200;
    @Entry(min=1, max=1000000D) public static double frenzied_shade_health = 100D;
    @Entry public static float frenzied_shade_damage_modifier = 1f;
    @Entry public static int frenzied_shade_cooldown = 10;
    @Entry public static int frenzied_shade_xp = 400;

    @Entry(min=1, max=1000000D) public static double chaos_monarch_health = 400D;
    @Entry(min=0) public static int chaos_monarch_attack_cooldown_ticks = 20;
    @Entry(min=0) public static float chaos_monarch_damage_modifier = 1f;
    @Entry(min=0) public static int chaos_monarch_xp = 500;

    @Entry(min=1, max=1000000D) public static double fallen_icon_health = 500D;
    @Entry(min=0) public static int fallen_icon_attack_cooldown_ticks_phase_1 = 30;
    @Entry(min=0) public static int fallen_icon_attack_cooldown_ticks_phase_2 = 0;
    @Entry(min=0) public static int fallen_icon_special_cooldown_ticks = 50;
    @Entry(min=0) public static float fallen_icon_damage_modifier = 1f;
    @Entry(min=0) public static int fallen_icon_xp = 600;

    @Entry(min=1, max=1000000D) public static double day_stalker_health = 500D;
    @Entry(min=0) public static float day_stalker_damage_modifier = 1f;
    @Entry(min=0) public static float day_stalker_empowered_projectile_damage_taken_modifier_phase_1 = 0.8f;
    @Entry(min=0) public static float day_stalker_empowered_projectile_damage_taken_modifier_phase_2 = 0.6f;
    @Entry(min=0) public static int day_stalker_xp = 500;
    @Entry(min=0) public static double day_stalker_cooldown_modifier_phase_1 = 1D;
    @Entry(min=0) public static double day_stalker_cooldown_modifier_phase_2 = 1D;
    @Entry(min=0) public static double day_stalker_special_cooldown_modifier_phase_1 = 1D;
    @Entry(min=0) public static double day_stalker_special_cooldown_modifier_phase_2 = 1D;
    @Entry(min=0) public static int duo_fight_time_before_switch = 400;
    @Entry(min=1, max=1000000D) public static double night_prowler_health = 400D;
    @Entry(min=0) public static float night_prowler_damage_modifier = 1f;
    @Entry(min=0) public static float night_prowler_eclipse_healing = 3f;
    @Entry(min=0, max=1) public static double night_prowler_projectile_heal_below_percent_health = 0.1667f;
    @Entry(min=0) public static float night_prowler_projectile_heal_amount = 5f;
    @Entry(min=0) public static double night_prowler_teleport_chance = 0.3D;
    @Entry(min=0) public static int night_prowler_xp = 500;
    @Entry(min=0) public static double night_prowler_cooldown_modifier_phase_1 = 1D;
    @Entry(min=0) public static double night_prowler_cooldown_modifier_phase_2 = 1D;
    @Entry(min=0) public static double night_prowler_special_cooldown_modifier_phase_1 = 1D;
    @Entry(min=0) public static double night_prowler_special_cooldown_modifier_phase_2 = 1D;
}