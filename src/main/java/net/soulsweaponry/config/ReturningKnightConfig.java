package net.soulsweaponry.config;

public class ReturningKnightConfig extends MidnightConfig {

    // Stats
    @Entry(min = 1, max = 1000000D) public static double health = 500D;
    @Entry(min = 1, max = 1000000D) public static double armor = 15D;
    @Entry public static double animation_speed = 1D;

    // General Cooldowns
    @Entry(min = 0) public static float attack_cooldown_modifier = 1f;
    @Entry(min = 0) public static float attack_cooldown_reduction_per_attacking_player = 5;
    @Entry(min = 0) public static float special_cooldown_modifier = 1f;
    @Entry(min = 0) public static float special_cooldown_reduction_per_attacking_player = 5;

    // Attacks, Weights & Cooldowns
    @Entry(min = 0) public static float damage_modifier = 1f;

    // Blinding Light
    @Entry(min = 0) public static float blinding_light_damage = 10f;
    @Entry(min = 0) public static float blinding_light_weight = 10;
    @Entry(min = 0) public static float blinding_light_cooldown = 30;
    @Entry(min = 0) public static float blinding_light_special_cooldown = 0;

    // Children of the grave
    @Entry(min = 0) public static float children_of_the_grave_min_enemy_count = 2;
    @Entry(min = 0) public static float children_of_the_grave_extra_enemy_bound = 3;
    @Entry(min = 0) public static float children_of_the_grave_min_healer_count = 1;
    @Entry(min = 0) public static float children_of_the_grave_extra_healer_bound = 2;
    @Entry(min = 0) public static float children_of_the_grave_weight = 15;
    @Entry(min = 0) public static float children_of_the_grave_cooldown = 40;
    @Entry(min = 0) public static float children_of_the_grave_special_cooldown = 100;

    // Eruption
    @Entry(min = 0) public static float eruption_range_blocks = 18;
    @Entry(min = 0) public static float eruption_damage = 30;
    @Entry(min = 0) public static float eruption_weight = 10;
    @Entry(min = 0) public static float eruption_cooldown = 40;
    @Entry(min = 0) public static float eruption_special_cooldown = 80;

    // Obliterate
    @Entry(min = 0) public static float obliterate_damage = 50f;
    @Entry(min = 0) public static float obliterate_phase_2_shadow_damage = 50f;
    @Entry(min = 0) public static float obliterate_phase_2_shadow_pillar_radius = 2f;
    @Entry(min = 0) public static float obliterate_phase_2_shadow_pillar_damage = 30f;
    @Entry(min = 0) public static float obliterate_phase_2_shadow_pillar_ripple_amount = 6;
    @Entry(min = 0) public static float obliterate_weight = 20;
    @Entry(min = 0) public static float obliterate_cooldown = 20;
    @Entry(min = 0) public static float obliterate_special_cooldown = 0;
    @Entry(min = 0) public static float obliterate_phase_2_weight = 20;
    @Entry(min = 0) public static float obliterate_phase_2_cooldown = 20;
    @Entry(min = 0) public static float obliterate_phase_2_special_cooldown = 0;

    // Mace of Spades 1
    @Entry(min = 0) public static float mace_of_spades_1_swipe_damage = 20;
    @Entry(min = 0) public static float mace_of_spades_1_phase_2_shadow_swipe_damage = 20;
    @Entry(min = 0) public static float mace_of_spades_1_smash_damage = 30;
    @Entry(min = 0) public static float mace_of_spades_1_phase_2_shadow_smash_damage = 30;
    @Entry(min = 0) public static float mace_of_spades_1_phase_2_shadow_pillar_radius = 2f;
    @Entry(min = 0) public static float mace_of_spades_1_phase_2_shadow_pillar_damage = 30f;
    @Entry(min = 0) public static float mace_of_spades_1_phase_2_shadow_pillar_ripple_amount = 4;
    @Entry(min = 0) public static float mace_of_spades_1_weight = 10;
    @Entry(min = 0) public static float mace_of_spades_1_cooldown = 20;
    @Entry(min = 0) public static float mace_of_spades_1_special_cooldown = 0;
    @Entry(min = 0) public static float mace_of_spades_1_phase_2_weight = 10;
    @Entry(min = 0) public static float mace_of_spades_1_phase_2_cooldown = 20;
    @Entry(min = 0) public static float mace_of_spades_1_phase_2_special_cooldown = 0;

    // Mace of Spades 2
    @Entry(min = 0) public static float mace_of_spades_2_swipe_damage = 20;
    @Entry(min = 0) public static float mace_of_spades_2_phase_2_shadow_swipe_damage = 20;
    @Entry(min = 0) public static float mace_of_spades_2_smash_damage = 30;
    @Entry(min = 0) public static float mace_of_spades_2_phase_2_shadow_smash_damage = 30;
    @Entry(min = 0) public static float mace_of_spades_2_phase_2_shadow_pillar_radius = 2f;
    @Entry(min = 0) public static float mace_of_spades_2_phase_2_shadow_pillar_damage = 30f;
    @Entry(min = 0) public static float mace_of_spades_2_phase_2_shadow_pillar_arm_length = 8;
    @Entry(min = 0) public static float mace_of_spades_2_weight = 10;
    @Entry(min = 0) public static float mace_of_spades_2_cooldown = 20;
    @Entry(min = 0) public static float mace_of_spades_2_special_cooldown = 0;
    @Entry(min = 0) public static float mace_of_spades_2_phase_2_weight = 10;
    @Entry(min = 0) public static float mace_of_spades_2_phase_2_cooldown = 20;
    @Entry(min = 0) public static float mace_of_spades_2_phase_2_special_cooldown = 0;

    // Mace of Spades 3
    @Entry(min = 0) public static float mace_of_spades_3_one_handed_swipe_damage = 20;
    @Entry(min = 0) public static float mace_of_spades_3_two_handed_swipe_damage = 25;
    @Entry(min = 0) public static float mace_of_spades_3_charged_swipe_damage = 30;
    @Entry(min = 0) public static float mace_of_spades_3_phase_2_shadow_swipe_damage = 20;
    @Entry(min = 0) public static float mace_of_spades_3_phase_2_shadow_spin_damage = 25;
    @Entry(min = 0) public static float mace_of_spades_3_phase_2_shadow_spin_darkness_orb_amount_per_tick = 10;
    @Entry(min = 0) public static float mace_of_spades_3_phase_2_shadow_spin_darkness_orb_damage = 10;
    @Entry(min = 0) public static float mace_of_spades_3_phase_2_shadow_spin_darkness_orb_speed = 1f;
    @Entry(min = 0) public static float mace_of_spades_3_phase_2_shadow_thrust_damage = 30;
    @Entry(min = 0) public static float mace_of_spades_3_weight = 10;
    @Entry(min = 0) public static float mace_of_spades_3_cooldown = 20;
    @Entry(min = 0) public static float mace_of_spades_3_special_cooldown = 0;
    @Entry(min = 0) public static float mace_of_spades_3_phase_2_weight = 10;
    @Entry(min = 0) public static float mace_of_spades_3_phase_2_cooldown = 20;
    @Entry(min = 0) public static float mace_of_spades_3_phase_2_special_cooldown = 0;

    // Mace of Spades 4
    @Entry(min = 0) public static float mace_of_spades_4_swipe_damage = 18;
    @Entry(min = 0) public static float mace_of_spades_4_spin_damage = 23;
    @Entry(min = 0) public static float mace_of_spades_4_thrust_damage = 28;
    @Entry(min = 0) public static float mace_of_spades_4_weight = 10;
    @Entry(min = 0) public static float mace_of_spades_4_cooldown = 20;
    @Entry(min = 0) public static float mace_of_spades_4_special_cooldown = 0;

    //Seismic Wave
    @Entry(min = 0) public static float seismic_wave_swipe_damage = 18;
    @Entry(min = 0) public static float seismic_wave_eruption_damage = 25;
    @Entry(min = 0) public static float seismic_wave_weight = 10;
    @Entry(min = 0) public static float seismic_wave_cooldown = 40;
    @Entry(min = 0) public static float seismic_wave_special_cooldown = 60;

    // Obliterate Fakeout (Phase 2)
    @Entry(min = 0) public static float obliterate_fakeout_phase_2_damage = 40f;
    @Entry(min = 0) public static float obliterate_fakeout_phase_2_shadow_spin_damage = 20f;
    @Entry(min = 0) public static float obliterate_fakeout_phase_2_shadow_spin_darkness_orb_amount_per_tick = 8;
    @Entry(min = 0) public static float obliterate_fakeout_phase_2_shadow_spin_darkness_orb_damage = 10;
    @Entry(min = 0) public static float obliterate_fakeout_phase_2_shadow_spin_darkness_orb_speed = 1f;
    @Entry(min = 0) public static float obliterate_fakeout_phase_2_weight = 3;
    @Entry(min = 0) public static float obliterate_fakeout_phase_2_cooldown = 10;
    @Entry(min = 0) public static float obliterate_fakeout_phase_2_special_cooldown = 0;

    // Children of the Grave + Eruption (Phase 2)
    @Entry(min = 0) public static float children_of_the_grave_eruption_phase_2_summon_radius = 10;
    @Entry(min = 0) public static float children_of_the_grave_eruption_phase_2_summon_amount = 4;
    @Entry(min = 0) public static float children_of_the_grave_eruption_phase_2_eruption_spawn_radius = 16;
    @Entry(min = 0) public static float children_of_the_grave_eruption_phase_2_eruption_damage = 30f;
    @Entry(min = 0) public static float children_of_the_grave_eruption_phase_2_eruption_amount = 64;
    @Entry(min = 0) public static float children_of_the_grave_eruption_phase_2_weight = 10;
    @Entry(min = 0) public static float children_of_the_grave_eruption_phase_2_cooldown = 20;
    @Entry(min = 0) public static float children_of_the_grave_eruption_phase_2_special_cooldown = 0;
    @Entry(min = 0) public static float children_of_the_grave_eruption_phase_2_cooldown_until_next_summon_after_last_kill = 300;

    // Dark Circle (Unbreakable + Blinding Reflection) (Phase 2)
    @Entry(min = 0) public static float dark_circle_phase_2_slam_damage = 20;
    @Entry(min = 0) public static float dark_circle_phase_2_slam_radius_horizontal = 7;
    @Entry(min = 0) public static float dark_circle_phase_2_slam_slowness_amp = 0;
    @Entry(min = 0) public static float dark_circle_phase_2_slam_slowness_duration = 100;
    @Entry(min = 0) public static float dark_circle_phase_2_slam_darkness_duration = 100;
    @Entry(min = 0) public static boolean dark_circle_phase_2_darkness_orb_turns_darkness_into_blindness = true;
    @Entry(min = 0) public static float dark_circle_phase_2_darkness_orb_count = 48;
    @Entry(min = 0) public static float dark_circle_phase_2_darkness_orb_ring_radius = 24;
    @Entry(min = 0) public static float dark_circle_phase_2_darkness_orb_damage = 14;
    @Entry(min = 0) public static float dark_circle_phase_2_darkness_orb_speed = 0.8f;
    @Entry(min = 0) public static float dark_circle_phase_2_darkness_orb_lifetime_ticks = 35;
    @Entry(min = 0) public static float dark_circle_phase_2_weight = 20;
    @Entry(min = 0) public static float dark_circle_phase_2_cooldown = 30;
    @Entry(min = 0) public static float dark_circle_phase_2_special_cooldown = 100;

    // Events
    // Unbreakable
    @Entry public static float unbreakable_percent_health_trigger = 0.5f;
    @Entry public static float unbreakable_resistance_amp = 2;
    @Entry public static float unbreakable_attacking_players_needed_to_increase_resistance_amp = 3;
    @Entry public static float unbreakable_resistance_amp_with_more_players = 3;
    // Bullet hell
    @Entry public static float bullet_hell_percent_health_trigger_phase_2 = 0.5f;
    @Entry public static float bullet_hell_duration_ticks = 400;
    @Entry public static float bullet_hell_darkness_orb_spawn_interval_ticks = 3;
    @Entry public static float bullet_hell_darkness_orb_lifetime_ticks = 40;
    @Entry public static float bullet_hell_darkness_orb_damage = 8f;
    @Entry public static float bullet_hell_darkness_orb_speed = 1f;
    @Entry public static double bullet_hell_darkness_orb_rotations_degrees_per_tick = 1.6D;
    @Entry public static float bullet_hell_darkness_orb_vertical_spacing = 3;
    @Entry public static float bullet_hell_shadow_pillar_star_spawn_interval_ticks = 80;
    @Entry public static float bullet_hell_shadow_pillar_star_arms_amount = 6;
    @Entry public static float bullet_hell_shadow_pillars_per_star_arm = 10;
    @Entry public static double bullet_hell_shadow_pillars_spacing = 2D;
    @Entry public static float bullet_hell_shadow_pillars_warmup = 20;
    @Entry public static float bullet_hell_shadow_pillars_radius = 1.25f;
    @Entry public static float bullet_hell_shadow_pillars_damage = 24f;

    // Misc
    @Entry public static boolean disable_respawn = false;
    @Entry public static boolean consume_item_on_summoning = true;
    @Entry(min = 0) public static float xp = 500;
    @Entry public static String[] projectile_immunity_whitelist = {};
    @Entry public static boolean disables_shields = true;
    @Entry public static boolean is_fire_immune = true;
    @Entry public static boolean has_inverted_heal_and_harm = true;
    @Entry public static String[] status_effect_blacklist = {"minecraft:poison"};
}
