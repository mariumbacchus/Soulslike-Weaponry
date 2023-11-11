package net.soulsweaponry.client.registry;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.networking.PacketRegistry;

public class KeyBindRegistry {

    private static KeyBinding returnFreyrSword;
    private static KeyBinding stationaryFreyrSword;
    private static KeyBinding collectSummons;
    public static KeyBinding switchWeapon;
    public static KeyBinding keybindAbility;
    private static KeyBinding parry;
    public static KeyBinding effectShootMoonlight;

    public static void initClient() {
        returnFreyrSword = registerKeyboard("return_freyr_sword", GLFW.GLFW_KEY_R);
        stationaryFreyrSword = registerKeyboard("freyr_sword_stationary", GLFW.GLFW_KEY_Z);
        collectSummons = registerKeyboard("collect_summons_soul_reaper", GLFW.GLFW_KEY_V);
        switchWeapon = registerKeyboard("switch_weapon", GLFW.GLFW_KEY_B);
        keybindAbility = registerKeyboard("keybind_ability", GLFW.GLFW_KEY_LEFT_ALT);
        parry = registerKeyboard("parry", GLFW.GLFW_KEY_RIGHT_ALT);
        effectShootMoonlight = registerKeyboard("effect_shoot_moonlight", GLFW.GLFW_KEY_H);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (returnFreyrSword.wasPressed()) {
                ClientPlayNetworking.send(PacketRegistry.RETURN_FREYR_SWORD, PacketByteBufs.empty());
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (stationaryFreyrSword.wasPressed()) {
                ClientPlayNetworking.send(PacketRegistry.STATIONARY_FREYR_SWORD, PacketByteBufs.empty());
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (collectSummons.wasPressed()) {
                ClientPlayNetworking.send(PacketRegistry.COLLECT_SUMMONS, PacketByteBufs.empty());
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (switchWeapon.wasPressed()) {
                ClientPlayNetworking.send(PacketRegistry.SWITCH_TRICK_WEAPON, PacketByteBufs.empty());
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keybindAbility.wasPressed()) {
                ClientPlayNetworking.send(PacketRegistry.KEYBIND_ABILITY, PacketByteBufs.empty());
                for (Hand hand : Hand.values()) {
                    if (client.player != null && client.player.getStackInHand(hand).getItem() instanceof IKeybindAbility abilityItem) {
                        abilityItem.useKeybindAbilityClient(client.world, client.player.getStackInHand(hand), client.player);
                    }
                }
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (parry.wasPressed()) {
                try {
                    ClientPlayNetworking.send(PacketRegistry.PARRY, PacketByteBufs.empty());
                } catch (Exception ignored) {}
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (effectShootMoonlight.wasPressed()) {
                if (client.player != null && client.player.hasStatusEffect(EffectRegistry.MOON_HERALD) && !client.player.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING)) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(PacketRegistry.MOONLIGHT, buf);
                    client.player.getItemCooldownManager().set(ItemRegistry.MOONSTONE_RING, ConfigConstructor.moonlight_ring_projectile_cooldown);
                }
            }
        });
    }

    private static KeyBinding registerKeyboard(String name, int keycode) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + SoulsWeaponry.ModId + "." + name, InputUtil.Type.KEYSYM, keycode, "category." + SoulsWeaponry.ModId + ".main"));
    }
}
