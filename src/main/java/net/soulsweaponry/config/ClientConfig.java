package net.soulsweaponry.config;

public class ClientConfig extends MidnightConfig {

    @Client @Entry public static boolean always_show_item_tooltip = false;
    @Client @Entry public static boolean always_show_item_lore = false;
    @Client @Entry public static boolean disable_target_posture_hud = false;
    @Client @Entry public static boolean disable_player_posture_hud = false;
    @Client @Entry public static boolean disable_player_bleed_hud = false;
}
