package net.soulsweaponry.networking;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public class PacketIds {

    // Server to Client
    public static final Identifier SPHERE_PARTICLES = new Identifier(SoulsWeaponry.ModId, "sphere_particles");
    public static final Identifier OUTBURST_PARTICLES = new Identifier(SoulsWeaponry.ModId, "outburst_particles");
    public static final Identifier FLASH_PARTICLE = new Identifier(SoulsWeaponry.ModId, "flash_particle");
    public static final Identifier SINGLE_PARTICLE = new Identifier(SoulsWeaponry.ModId, "single_particle");
    public static final Identifier PARRY_SYNC = new Identifier(SoulsWeaponry.ModId, "parry_data_sync");
    public static final Identifier POSTURE_SYNC = new Identifier(SoulsWeaponry.ModId, "posture_data_sync");
    public static final Identifier SYNC_FREYR_SWORD_SUMMON_DATA = new Identifier(SoulsWeaponry.ModId, "freyr_sword_summon_uuid_sync");
    public static final Identifier SYNC_RETURNING_PROJECTILE_DATA = new Identifier(SoulsWeaponry.ModId, "returning_projectile_uuid_sync");
    public static final Identifier SYNC_DAMAGE_RIDING_DATA = new Identifier(SoulsWeaponry.ModId, "should_damage_riding_sync");
    public static final Identifier SYNC_UMBRAL_DAMAGE_COOLDOWN = new Identifier(SoulsWeaponry.ModId, "umbral_trespass_damage_cooldown_sync");

    // Client to Server
    public static final Identifier MOONLIGHT = new Identifier(SoulsWeaponry.ModId, "moonlight");
    public static final Identifier RETURN_FREYR_SWORD = new Identifier(SoulsWeaponry.ModId, "check_can_freyr_return");
    public static final Identifier STATIONARY_FREYR_SWORD = new Identifier(SoulsWeaponry.ModId, "switch_stationary_freyr");
    public static final Identifier COLLECT_SUMMONS = new Identifier(SoulsWeaponry.ModId, "collect_summons_to_soul_reaper");
    public static final Identifier SWITCH_TRICK_WEAPON = new Identifier(SoulsWeaponry.ModId, "switch_trick_weapon");
    public static final Identifier KEYBIND_ABILITY = new Identifier(SoulsWeaponry.ModId, "keybind_ability");
    public static final Identifier DAMAGING_BOX = new Identifier(SoulsWeaponry.ModId, "damaging_box");
    public static final Identifier RETURN_THROWN_WEAPON = new Identifier(SoulsWeaponry.ModId, "try_return_thrown_weapons");
    public static final Identifier SUMMONS_UUIDS = new Identifier(SoulsWeaponry.ModId, "summons_uuids");
    public static final Identifier PARRY = new Identifier(SoulsWeaponry.ModId, "parry_keybind");
}