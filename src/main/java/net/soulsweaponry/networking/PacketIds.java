package net.soulsweaponry.networking;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public class PacketIds {
    //Client
    public static final Identifier SPHERE_PARTICLES = new Identifier(SoulsWeaponry.ModId, "sphere_particles");
    public static final Identifier OUTBURST_PARTICLES = new Identifier(SoulsWeaponry.ModId, "outburst_particles");
    public static final Identifier FLASH_PARTICLE = new Identifier(SoulsWeaponry.ModId, "flash_particle");
    public static final Identifier SINGLE_PARTICLE = new Identifier(SoulsWeaponry.ModId, "single_particle");
    //Server
    public static final Identifier MOONLIGHT = new Identifier(SoulsWeaponry.ModId, "moonlight");
    public static final Identifier RETURN_FREYR_SWORD = new Identifier(SoulsWeaponry.ModId, "check_can_freyr_return");
    public static final Identifier STATIONARY_FREYR_SWORD = new Identifier(SoulsWeaponry.ModId, "switch_stationary_freyr");
    public static final Identifier COLLECT_SUMMONS = new Identifier(SoulsWeaponry.ModId, "collect_summons_to_soul_reaper");
    public static final Identifier SWITCH_TRICK_WEAPON = new Identifier(SoulsWeaponry.ModId, "switch_trick_weapon");
    public static final Identifier KEYBIND_ABILITY = new Identifier(SoulsWeaponry.ModId, "keybind_ability");
    public static final Identifier PARRY = new Identifier(SoulsWeaponry.ModId, "parry_keybind");
    public static final Identifier POSTURE = new Identifier(SoulsWeaponry.ModId, "posture");
}
