package net.soulsweaponry.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SoulsWeaponry.MOD_ID);

    public static final RegistryObject<SoundEvent> MOONLIGHT_BIG_EVENT = registerSound("moonlight_big");
    public static final RegistryObject<SoundEvent>  MOONLIGHT_SMALL_EVENT = registerSound("moonlight_small");
    public static final RegistryObject<SoundEvent>  BIG_CHUNGUS_EVENT = registerSound("big_chungus");
    public static final RegistryObject<SoundEvent>  BIG_CHUNGUS_SONG_EVENT = registerSound("big_chungus_song");
    public static final RegistryObject<SoundEvent>  FART_EVENT = registerSound("fart");
    public static final RegistryObject<SoundEvent>  DEMON_IDLE_EVENT = registerSound("demon_idle");
    public static final RegistryObject<SoundEvent>  DEMON_WALK_EVENT = registerSound("demon_walk");
    public static final RegistryObject<SoundEvent>  DEMON_DAMAGE_EVENT = registerSound("demon_damage");
    public static final RegistryObject<SoundEvent>  DEMON_DEATH_EVENT = registerSound("demon_death");
    public static final RegistryObject<SoundEvent>  DEMON_BOSS_IDLE_EVENT = registerSound("decaying_king_idle");
    public static final RegistryObject<SoundEvent>  DEMON_BOSS_DEATH_EVENT = registerSound("decaying_king_death");
    public static final RegistryObject<SoundEvent>  DEMON_BOSS_HURT_EVENT = registerSound("decaying_king_hurt");
    public static final RegistryObject<SoundEvent>  NIGHTFALL_SPAWN_EVENT = registerSound("spawn_undead");
    public static final RegistryObject<SoundEvent>  NIGHTFALL_SHIELD_EVENT = registerSound("shield");
    public static final RegistryObject<SoundEvent>  NIGHTFALL_BONK_EVENT = registerSound("nightfall_bonk");
    public static final RegistryObject<SoundEvent>  DEATH_SCREAMS_EVENT = registerSound("death_screams");
    public static final RegistryObject<SoundEvent>  KNIGHT_HIT_EVENT = registerSound("knight_hit");
    public static final RegistryObject<SoundEvent>  KNIGHT_DEATH_EVENT = registerSound("knight_death");
    public static final RegistryObject<SoundEvent>  NIGHT_SHADE_DAMAGE_EVENT = registerSound("night_shade_damage");
    public static final RegistryObject<SoundEvent>  NIGHT_SHADE_IDLE_EVENT = registerSound("night_shade_idle");
    public static final RegistryObject<SoundEvent>  NIGHT_SHADE_DEATH_EVENT = registerSound("night_shade_death");
    public static final RegistryObject<SoundEvent>  GATLING_GUN_STARTUP_EVENT = registerSound("gatling_gun_startup");
    public static final RegistryObject<SoundEvent>  GATLING_GUN_BARRAGE_EVENT = registerSound("gatling_gun_barrage");
    public static final RegistryObject<SoundEvent>  GATLING_GUN_STOP_EVENT = registerSound("gatling_gun_stop");
    public static final RegistryObject<SoundEvent>  DAWNBREAKER_EVENT = registerSound("dawnbreaker_sound");
    public static final RegistryObject<SoundEvent>  SOULMASS_IDLE_EVENT = registerSound("soulmass_idle");
    public static final RegistryObject<SoundEvent>  CRIT_HIT_EVENT = registerSound("crit_hit");
    public static final RegistryObject<SoundEvent>  POSTURE_BREAK_EVENT = registerSound("posture_break");
    public static final RegistryObject<SoundEvent>  RESTORE_EVENT = registerSound("restore");
    public static final RegistryObject<SoundEvent>  SHARPEN_EVENT = registerSound("sharpen");
    public static final RegistryObject<SoundEvent>  SWORD_HIT_SHIELD_EVENT = registerSound("sword_hit_shield");
    public static final RegistryObject<SoundEvent>  KNIGHT_SWIPE_EVENT = registerSound("knight_swipe");
    public static final RegistryObject<SoundEvent>  KNIGHT_DEATH_LAUGH_EVENT = registerSound("knight_death_laugh");
    public static final RegistryObject<SoundEvent>  BLINDING_LIGHT_EXPLOSION_EVENT = registerSound("blinding_light_explosion");
    public static final RegistryObject<SoundEvent>  KNIGHT_CORE_BEAM_EVENT = registerSound("knight_core_beam");
    public static final RegistryObject<SoundEvent>  KNIGHT_CHARGE_SWORD_EVENT = registerSound("knight_charge_sword");
    public static final RegistryObject<SoundEvent>  KNIGHT_SWORD_SMASH_EVENT = registerSound("knight_sword_smash");
    public static final RegistryObject<SoundEvent>  KNIGHT_THRUST_SWORD_EVENT = registerSound("knight_thrust_sword");
    public static final RegistryObject<SoundEvent>  SLICE_TARGET_EVENT = registerSound("slice_target");
    public static final RegistryObject<SoundEvent>  UMBRAL_TRESPASS_EVENT = registerSound("umbral_trespass_sound");
    public static final RegistryObject<SoundEvent>  TRICK_WEAPON_EVENT = registerSound("trick_weapon");
    public static final RegistryObject<SoundEvent>  HARD_BOSS_SPAWN_EVENT = registerSound("hard_boss_spawn");
    public static final RegistryObject<SoundEvent>  WARMTH_BUFF_EVENT = registerSound("warmth_buff");
    public static final RegistryObject<SoundEvent>  WARMTH_DIE_EVENT = registerSound("warmth_die");
    public static final RegistryObject<SoundEvent>  OVERHEAT_CHARGE_EVENT = registerSound("overheat_charge");
    public static final RegistryObject<SoundEvent>  DAY_STALKER_DECIMATE = registerSound("day_stalker_decimate");
    public static final RegistryObject<SoundEvent>  DAY_STALKER_CHAOS_STORM = registerSound("day_stalker_chaos_storm");
    public static final RegistryObject<SoundEvent>  DAY_STALKER_FLAMES_EDGE_NORMAL = registerSound("day_stalker_spin_normal");
    public static final RegistryObject<SoundEvent>  DAY_STALKER_FLAMES_EDGE_EMPOWERED = registerSound("day_stalker_spin_empowered");
    public static final RegistryObject<SoundEvent>  DAY_STALKER_RADIANCE = registerSound("day_stalker_radiance");
    public static final RegistryObject<SoundEvent>  DAY_STALKER_WINDUP = registerSound("day_stalker_windup");
    public static final RegistryObject<SoundEvent>  DAY_STALKER_PULL = registerSound("day_stalker_pull");
    public static final RegistryObject<SoundEvent>  HARD_BOSS_DEATH_SHORT = registerSound("hard_boss_death_short");
    public static final RegistryObject<SoundEvent>  HARD_BOSS_DEATH_LONG = registerSound("hard_boss_death_long");
    public static final RegistryObject<SoundEvent>  SCYTHE_SWIPE = registerSound("scythe_swipe");
    public static final RegistryObject<SoundEvent>  NIGHT_SKULL_DIE = registerSound("night_skull_die");
    public static final RegistryObject<SoundEvent>  ENGULF = registerSound("engulf");
    public static final RegistryObject<SoundEvent>  NIGHT_PROWLER_SCREAM = registerSound("night_prowler_scream");
    public static final RegistryObject<SoundEvent>  PARTNER_DIES = registerSound("partner_dies");
    public static final RegistryObject<SoundEvent>  DARKNESS_RISE = registerSound("darkness_rise");
    public static final RegistryObject<SoundEvent>  TRINITY = registerSound("trinity");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(SoulsWeaponry.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}
