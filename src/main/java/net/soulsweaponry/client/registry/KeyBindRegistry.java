package net.soulsweaponry.client.registry;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.C2S.Example;
import org.lwjgl.glfw.GLFW;

public class KeyBindRegistry {

    private static KeyMapping returnFreyrSword;
    private static KeyMapping stationaryFreyrSword;
    private static KeyMapping collectSummons;
    public static KeyMapping switchWeapon;
    public static KeyMapping keybindAbility;
    private static KeyMapping parry;
    public static KeyMapping effectShootMoonlight;

    public static void register() {
        returnFreyrSword = registerKey("return_freyr_sword", GLFW.GLFW_KEY_R);
        stationaryFreyrSword = registerKey("freyr_sword_stationary", GLFW.GLFW_KEY_Z);
        collectSummons = registerKey("collect_summons_soul_reaper", GLFW.GLFW_KEY_V);
        switchWeapon = registerKey("switch_weapon", GLFW.GLFW_KEY_B);
        keybindAbility = registerKey("keybind_ability", GLFW.GLFW_KEY_LEFT_ALT);
        parry = registerKey("parry", GLFW.GLFW_KEY_RIGHT_ALT);
        effectShootMoonlight = registerKey("effect_shoot_moonlight", GLFW.GLFW_KEY_H);
    }

    public static void registerKeyInputs(TickEvent.ClientTickEvent event) {
        while (returnFreyrSword.consumeClick()) {
            ModMessages.sendToServer(new Example(Minecraft.getInstance().player.blockPosition()));//Sends example packet, TODO make real packets and register events for all keybinds
        }
    }

    private static KeyMapping registerKey(String name, int keycode) {
        KeyMapping key = new KeyMapping("key." + SoulsWeaponry.MOD_ID + "." + name, keycode, "category." + SoulsWeaponry.MOD_ID + ".main");
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
