package net.soulsweaponry.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.soulsweaponry.SoulsWeaponry;

public class SoundRegistry {

    public static SoundEvent MOONLIGHT_BIG_EVENT = registerSound("moonlight_big");
    public static SoundEvent MOONLIGHT_SMALL_EVENT = registerSound("moonlight_small");
    public static SoundEvent BIG_CHUNGUS_EVENT = registerSound("big_chungus");
    public static SoundEvent BIG_CHUNGUS_SONG_EVENT = registerSound("big_chungus_song");
    public static SoundEvent FART_EVENT = registerSound("fart");
    public static SoundEvent DEMON_IDLE_EVENT = registerSound("demon_idle");
    public static SoundEvent DEMON_WALK_EVENT = registerSound("demon_walk");
    public static SoundEvent DEMON_DAMAGE_EVENT = registerSound("demon_damage");
    public static SoundEvent DEMON_DEATH_EVENT = registerSound("demon_death");
    public static SoundEvent DEMON_BOSS_IDLE_EVENT = registerSound("decaying_king_idle");
    public static SoundEvent DEMON_BOSS_DEATH_EVENT = registerSound("decaying_king_death");
    public static SoundEvent DEMON_BOSS_HURT_EVENT = registerSound("decaying_king_hurt");
    public static SoundEvent NIGHTFALL_SPAWN_EVENT = registerSound("spawn_undead");
    public static SoundEvent NIGHTFALL_SHIELD_EVENT = registerSound("shield");
    public static SoundEvent NIGHTFALL_BONK_EVENT = registerSound("nightfall_bonk");
    public static SoundEvent DEATH_SCREAMS_EVENT = registerSound("death_screams");
    public static SoundEvent KNIGHT_HIT_EVENT = registerSound("knight_hit");
    public static SoundEvent KNIGHT_DEATH_EVENT = registerSound("knight_death");
    public static SoundEvent NIGHT_SHADE_DAMAGE_EVENT = registerSound("night_shade_damage");
    public static SoundEvent NIGHT_SHADE_IDLE_EVENT = registerSound("night_shade_idle");
    public static SoundEvent NIGHT_SHADE_DEATH_EVENT = registerSound("night_shade_death");
    public static SoundEvent GATLING_GUN_STARTUP_EVENT = registerSound("gatling_gun_startup");
    public static SoundEvent GATLING_GUN_BARRAGE_EVENT = registerSound("gatling_gun_barrage");
    public static SoundEvent GATLING_GUN_STOP_EVENT = registerSound("gatling_gun_stop");
    public static SoundEvent DAWNBREAKER_EVENT = registerSound("dawnbreaker_sound");
    public static SoundEvent SOULMASS_IDLE_EVENT = registerSound("soulmass_idle");
    public static SoundEvent CRIT_HIT_EVENT = registerSound("crit_hit");
    public static SoundEvent POSTURE_BREAK_EVENT = registerSound("posture_break");
    public static SoundEvent RESTORE_EVENT = registerSound("restore");
    public static SoundEvent SHARPEN_EVENT = registerSound("sharpen");
    public static SoundEvent SWORD_HIT_SHIELD_EVENT = registerSound("sword_hit_shield");
    public static SoundEvent KNIGHT_SWIPE_EVENT = registerSound("knight_swipe");
    public static SoundEvent KNIGHT_DEATH_LAUGH_EVENT = registerSound("knight_death_laugh");
    public static SoundEvent BLINDING_LIGHT_EXPLOSION_EVENT = registerSound("blinding_light_explosion");
    public static SoundEvent KNIGHT_CORE_BEAM_EVENT = registerSound("knight_core_beam");
    public static SoundEvent KNIGHT_CHARGE_SWORD_EVENT = registerSound("knight_charge_sword");
    public static SoundEvent KNIGHT_SWORD_SMASH_EVENT = registerSound("knight_sword_smash");
    public static SoundEvent KNIGHT_THRUST_SWORD_EVENT = registerSound("knight_thrust_sword");
    public static SoundEvent SLICE_TARGET_EVENT = registerSound("slice_target");
    public static SoundEvent UMBRAL_TRESPASS_EVENT = registerSound("umbral_trespass_sound");
    public static SoundEvent TRICK_WEAPON_EVENT = registerSound("trick_weapon");
    public static SoundEvent HARD_BOSS_SPAWN_EVENT = registerSound("hard_boss_spawn");
    public static SoundEvent WARMTH_BUFF_EVENT = registerSound("warmth_buff");
    public static SoundEvent WARMTH_DIE_EVENT = registerSound("warmth_die");
    public static SoundEvent OVERHEAT_CHARGE_EVENT = registerSound("overheat_charge");
    public static SoundEvent DAY_STALKER_DECIMATE = registerSound("day_stalker_decimate");
    public static SoundEvent DAY_STALKER_CHAOS_STORM = registerSound("day_stalker_chaos_storm");
    public static SoundEvent DAY_STALKER_FLAMES_EDGE_NORMAL = registerSound("day_stalker_spin_normal");
    public static SoundEvent DAY_STALKER_FLAMES_EDGE_EMPOWERED = registerSound("day_stalker_spin_empowered");
    public static SoundEvent DAY_STALKER_RADIANCE = registerSound("day_stalker_radiance");
    public static SoundEvent DAY_STALKER_WINDUP = registerSound("day_stalker_windup");
    public static SoundEvent DAY_STALKER_PULL = registerSound("day_stalker_pull");
    public static SoundEvent HARD_BOSS_DEATH_SHORT = registerSound("hard_boss_death_short");
    public static SoundEvent HARD_BOSS_DEATH_LONG = registerSound("hard_boss_death_long");
    public static SoundEvent SCYTHE_SWIPE = registerSound("scythe_swipe");
    public static SoundEvent NIGHT_SKULL_DIE = registerSound("night_skull_die");
    public static SoundEvent ENGULF = registerSound("engulf");
    public static SoundEvent NIGHT_PROWLER_SCREAM = registerSound("night_prowler_scream");
    public static SoundEvent PARTNER_DIES = registerSound("partner_dies");
    public static SoundEvent DARKNESS_RISE = registerSound("darkness_rise");
    public static SoundEvent TRINITY = registerSound("trinity");

	public static Identifier registerId(String name) {
		return new Identifier(SoulsWeaponry.ModId + ":" + name);
	}

    public static SoundEvent registerSound(String id) {
        Identifier identifier = registerId(id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}
