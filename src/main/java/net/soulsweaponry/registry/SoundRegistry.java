package net.soulsweaponry.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.soulsweaponry.SoulsWeaponry;

public class SoundRegistry {

    public static final Identifier MOONLIGHT_BIG_SOUND_ID = registerId("moonlight_big");
    public static final Identifier MOONLIGHT_SMALL_SOUND_ID = registerId("moonlight_small");
    public static final Identifier BIG_CHUNGUS_SOUND_ID = registerId("big_chungus");
    public static final Identifier BIG_CHUNGUS_SONG_SOUND_ID = registerId("big_chungus_song");
    public static final Identifier FART_SOUND_ID = registerId("fart");
    public static final Identifier DEMON_IDLE_ID = registerId("demon_idle");
    public static final Identifier DEMON_WALK_ID = registerId("demon_walk");
    public static final Identifier DEMON_DAMAGE_ID = registerId("demon_damage");
    public static final Identifier DEMON_DEATH_ID = registerId("demon_death");
    public static final Identifier DEMON_BOSS_IDLE_ID = registerId("decaying_king_idle");
    public static final Identifier DEMON_BOSS_DEATH_ID = registerId("decaying_king_death");
    public static final Identifier DEMON_BOSS_HURT_ID = registerId("decaying_king_hurt");
    public static final Identifier NIGHTFALL_SPAWN_ID = registerId("spawn_undead");
    public static final Identifier NIGHTFALL_SHIELD_ID = registerId("shield");
    public static final Identifier NIGHTFALL_BONK_ID = registerId("nightfall_bonk");
    public static final Identifier DEATH_SCREAMS_ID = registerId("death_screams");
    public static final Identifier KNIGHT_HIT_ID = registerId("knight_hit");
    public static final Identifier KNIGHT_DEATH_ID = registerId("knight_death");
    public static final Identifier NIGHT_SHADE_DAMAGE_ID = registerId("night_shade_damage");
    public static final Identifier NIGHT_SHADE_IDLE_ID = registerId("night_shade_idle");
    public static final Identifier NIGHT_SHADE_DEATH_ID = registerId("night_shade_death");
    public static final Identifier GATLING_GUN_STARTUP_ID = registerId("gatling_gun_startup");
    public static final Identifier GATLING_GUN_BARRAGE_ID = registerId("gatling_gun_barrage");
    public static final Identifier GATLING_GUN_STOP_ID = registerId("gatling_gun_stop");
    public static final Identifier DAWNBREAKER_ID = registerId("dawnbreaker_sound");
    public static final Identifier SOULMASS_IDLE_ID = registerId("soulmass_idle");
    public static final Identifier CRIT_HIT_ID = registerId("crit_hit");
    public static final Identifier POSTURE_BREAK_ID = registerId("posture_break");
    public static final Identifier RESTORE_ID = registerId("restore");
    public static final Identifier SHARPEN_ID = registerId("sharpen");
    public static final Identifier SWORD_HIT_SHIELD_ID = registerId("sword_hit_shield");
    public static final Identifier KNIGHT_SWIPE_ID = registerId("knight_swipe");
    public static final Identifier KNIGHT_DEATH_LAUGH_ID = registerId("knight_death_laugh");
    public static final Identifier BLINDING_LIGHT_EXPLOSION_ID = registerId("blinding_light_explosion");
    public static final Identifier KNIGHT_CORE_BEAM_ID = registerId("knight_core_beam");
    public static final Identifier KNIGHT_CHARGE_SWORD_ID = registerId("knight_charge_sword");
    public static final Identifier KNIGHT_SWORD_SMASH_ID = registerId("knight_sword_smash");
    public static final Identifier KNIGHT_THRUST_SWORD_ID = registerId("knight_thrust_sword");
    public static final Identifier SLICE_TARGET_ID = registerId("slice_target");
    public static final Identifier UMBRAL_TRESPASS_ID = registerId("umbral_trespass_sound");
    public static final Identifier TRICK_WEAPON_SOUND_ID = registerId("trick_weapon");
    public static final Identifier HARD_BOSS_SPAWN_SOUND_ID = registerId("hard_boss_spawn");

	public static SoundEvent MOONLIGHT_BIG_EVENT = new SoundEvent(MOONLIGHT_BIG_SOUND_ID);
	public static SoundEvent MOONLIGHT_SMALL_EVENT = new SoundEvent(MOONLIGHT_SMALL_SOUND_ID);
	public static SoundEvent BIG_CHUNGUS_EVENT = new SoundEvent(BIG_CHUNGUS_SOUND_ID);
	public static SoundEvent BIG_CHUNGUS_SONG_EVENT = new SoundEvent(BIG_CHUNGUS_SONG_SOUND_ID);
    public static SoundEvent FART_EVENT = new SoundEvent(FART_SOUND_ID);
	public static SoundEvent DEMON_IDLE_EVENT = new SoundEvent(DEMON_IDLE_ID);
    public static SoundEvent DEMON_WALK_EVENT = new SoundEvent(DEMON_WALK_ID);
    public static SoundEvent DEMON_DAMAGE_EVENT = new SoundEvent(DEMON_DAMAGE_ID);
    public static SoundEvent DEMON_DEATH_EVENT = new SoundEvent(DEMON_DEATH_ID);
    public static SoundEvent DEMON_BOSS_IDLE_EVENT = new SoundEvent(DEMON_BOSS_IDLE_ID);
    public static SoundEvent DEMON_BOSS_DEATH_EVENT = new SoundEvent(DEMON_BOSS_DEATH_ID);
    public static SoundEvent DEMON_BOSS_HURT_EVENT = new SoundEvent(DEMON_BOSS_HURT_ID);
    public static SoundEvent NIGHTFALL_SPAWN_EVENT = new SoundEvent(NIGHTFALL_SPAWN_ID);
    public static SoundEvent NIGHTFALL_SHIELD_EVENT = new SoundEvent(NIGHTFALL_SHIELD_ID);
    public static SoundEvent NIGHTFALL_BONK_EVENT = new SoundEvent(NIGHTFALL_BONK_ID);
    public static SoundEvent DEATH_SCREAMS_EVENT = new SoundEvent(DEATH_SCREAMS_ID);
    public static SoundEvent KNIGHT_HIT_EVENT = new SoundEvent(KNIGHT_HIT_ID);
    public static SoundEvent KNIGHT_DEATH_EVENT = new SoundEvent(KNIGHT_DEATH_ID);
    public static SoundEvent NIGHT_SHADE_DAMAGE_EVENT = new SoundEvent(NIGHT_SHADE_DAMAGE_ID);
    public static SoundEvent NIGHT_SHADE_IDLE_EVENT = new SoundEvent(NIGHT_SHADE_IDLE_ID);
    public static SoundEvent NIGHT_SHADE_DEATH_EVENT = new SoundEvent(NIGHT_SHADE_DEATH_ID);
    public static SoundEvent GATLING_GUN_STARTUP_EVENT = new SoundEvent(GATLING_GUN_STARTUP_ID);
    public static SoundEvent GATLING_GUN_BARRAGE_EVENT = new SoundEvent(GATLING_GUN_BARRAGE_ID);
    public static SoundEvent GATLING_GUN_STOP_EVENT = new SoundEvent(GATLING_GUN_STOP_ID);
    public static SoundEvent DAWNBREAKER_EVENT = new SoundEvent(DAWNBREAKER_ID);
    public static SoundEvent SOULMASS_IDLE_EVENT = new SoundEvent(SOULMASS_IDLE_ID);
    public static SoundEvent CRIT_HIT_EVENT = new SoundEvent(CRIT_HIT_ID);
    public static SoundEvent POSTURE_BREAK_EVENT = new SoundEvent(POSTURE_BREAK_ID);
    public static SoundEvent RESTORE_EVENT = new SoundEvent(RESTORE_ID);
    public static SoundEvent SHARPEN_EVENT = new SoundEvent(SHARPEN_ID);
    public static SoundEvent SWORD_HIT_SHIELD_EVENT = new SoundEvent(SWORD_HIT_SHIELD_ID);
    public static SoundEvent KNIGHT_SWIPE_EVENT = new SoundEvent(KNIGHT_SWIPE_ID);
    public static SoundEvent KNIGHT_DEATH_LAUGH_EVENT = new SoundEvent(KNIGHT_DEATH_LAUGH_ID);
    public static SoundEvent BLINDING_LIGHT_EXPLOSION_EVENT = new SoundEvent(BLINDING_LIGHT_EXPLOSION_ID);
    public static SoundEvent KNIGHT_CORE_BEAM_EVENT = new SoundEvent(KNIGHT_CORE_BEAM_ID);
    public static SoundEvent KNIGHT_THRUST_SWORD_EVENT = new SoundEvent(KNIGHT_THRUST_SWORD_ID);
    public static SoundEvent KNIGHT_SWORD_SMASH_EVENT = new SoundEvent(KNIGHT_SWORD_SMASH_ID);
    public static SoundEvent KNIGHT_CHARGE_SWORD_EVENT = new SoundEvent(KNIGHT_CHARGE_SWORD_ID);
    public static SoundEvent SLICE_TARGET_EVENT = new SoundEvent(SLICE_TARGET_ID);
    public static SoundEvent UMBRAL_TRESPASS_EVENT = new SoundEvent(UMBRAL_TRESPASS_ID);
    public static SoundEvent TRICK_WEAPON_EVENT = new SoundEvent(TRICK_WEAPON_SOUND_ID);
    public static SoundEvent HARD_BOSS_SPAWN_EVENT = new SoundEvent(HARD_BOSS_SPAWN_SOUND_ID);

	public static void init() {
		registerSound(MOONLIGHT_BIG_SOUND_ID, MOONLIGHT_BIG_EVENT);
		registerSound(MOONLIGHT_SMALL_SOUND_ID, MOONLIGHT_SMALL_EVENT);
		registerSound(BIG_CHUNGUS_SOUND_ID, BIG_CHUNGUS_EVENT);
		registerSound(BIG_CHUNGUS_SONG_SOUND_ID, BIG_CHUNGUS_SONG_EVENT);
		registerSound(FART_SOUND_ID, FART_EVENT);
		registerSound(DEMON_IDLE_ID, DEMON_IDLE_EVENT);
		registerSound(DEMON_WALK_ID, DEMON_WALK_EVENT);
		registerSound(DEMON_DAMAGE_ID, DEMON_DAMAGE_EVENT);
		registerSound(DEMON_DEATH_ID, DEMON_DEATH_EVENT);
		registerSound(DEMON_BOSS_IDLE_ID, DEMON_BOSS_IDLE_EVENT);
		registerSound(DEMON_BOSS_DEATH_ID, DEMON_BOSS_DEATH_EVENT);
		registerSound(DEMON_BOSS_HURT_ID, DEMON_BOSS_HURT_EVENT);
		registerSound(NIGHTFALL_SPAWN_ID, NIGHTFALL_SPAWN_EVENT);
		registerSound(NIGHTFALL_SHIELD_ID, NIGHTFALL_SHIELD_EVENT);
		registerSound(NIGHTFALL_BONK_ID, NIGHTFALL_BONK_EVENT);
		registerSound(DEATH_SCREAMS_ID, DEATH_SCREAMS_EVENT);
		registerSound(KNIGHT_HIT_ID, KNIGHT_HIT_EVENT);
		registerSound(KNIGHT_DEATH_ID, KNIGHT_DEATH_EVENT);
		registerSound(NIGHT_SHADE_DAMAGE_ID, NIGHT_SHADE_DAMAGE_EVENT);
		registerSound(NIGHT_SHADE_IDLE_ID, NIGHT_SHADE_IDLE_EVENT);
		registerSound(NIGHT_SHADE_DEATH_ID, NIGHT_SHADE_DEATH_EVENT);
		registerSound(GATLING_GUN_STARTUP_ID, GATLING_GUN_STARTUP_EVENT);
		registerSound(GATLING_GUN_BARRAGE_ID, GATLING_GUN_BARRAGE_EVENT);
		registerSound(GATLING_GUN_STOP_ID, GATLING_GUN_STOP_EVENT);
		registerSound(DAWNBREAKER_ID, DAWNBREAKER_EVENT);
		registerSound(SOULMASS_IDLE_ID, SOULMASS_IDLE_EVENT);
        registerSound(CRIT_HIT_ID, CRIT_HIT_EVENT);
        registerSound(POSTURE_BREAK_ID, POSTURE_BREAK_EVENT);
        registerSound(RESTORE_ID, RESTORE_EVENT);
        registerSound(SHARPEN_ID, SHARPEN_EVENT);
        registerSound(SWORD_HIT_SHIELD_ID, SWORD_HIT_SHIELD_EVENT);
        registerSound(KNIGHT_SWIPE_ID, KNIGHT_SWIPE_EVENT);
        registerSound(KNIGHT_DEATH_LAUGH_ID, KNIGHT_DEATH_LAUGH_EVENT);
        registerSound(BLINDING_LIGHT_EXPLOSION_ID, BLINDING_LIGHT_EXPLOSION_EVENT);
        registerSound(KNIGHT_CORE_BEAM_ID, KNIGHT_CORE_BEAM_EVENT);
        registerSound(KNIGHT_CHARGE_SWORD_ID, KNIGHT_CHARGE_SWORD_EVENT);
        registerSound(KNIGHT_SWORD_SMASH_ID, KNIGHT_SWORD_SMASH_EVENT);
        registerSound(KNIGHT_THRUST_SWORD_ID, KNIGHT_THRUST_SWORD_EVENT);
        registerSound(SLICE_TARGET_ID, SLICE_TARGET_EVENT);
        registerSound(UMBRAL_TRESPASS_ID, UMBRAL_TRESPASS_EVENT);
        registerSound(TRICK_WEAPON_SOUND_ID, TRICK_WEAPON_EVENT);
        registerSound(HARD_BOSS_SPAWN_SOUND_ID, HARD_BOSS_SPAWN_EVENT);
	}

	public static Identifier registerId(String name) {
		return new Identifier(SoulsWeaponry.ModId + ":" + name);
	}

    public static SoundEvent registerSound(Identifier id, SoundEvent event) {
		return Registry.register(Registry.SOUND_EVENT, id, event);
	}
    
}
