package net.soulsweaponry.client.registry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.C2S.*;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import org.lwjgl.glfw.GLFW;

public class KeyBindRegistry {

    private static KeyBinding returnFreyrSword;
    private static KeyBinding stationaryFreyrSword;
    private static KeyBinding collectSummons;
    public static KeyBinding switchWeapon;
    public static KeyBinding keybindAbility;
    private static KeyBinding parry;
    public static KeyBinding effectShootMoonlight;
    public static KeyBinding returnThrownWeapon;

    public static void register() {
        returnFreyrSword = registerKeyboard("return_freyr_sword", GLFW.GLFW_KEY_R);
        stationaryFreyrSword = registerKeyboard("freyr_sword_stationary", GLFW.GLFW_KEY_Z);
        collectSummons = registerKeyboard("collect_summons_soul_reaper", GLFW.GLFW_KEY_V);
        switchWeapon = registerKeyboard("switch_weapon", GLFW.GLFW_KEY_B);
        keybindAbility = registerKeyboard("keybind_ability", GLFW.GLFW_KEY_LEFT_ALT);
        parry = registerKeyboard("parry", GLFW.GLFW_KEY_RIGHT_ALT);
        effectShootMoonlight = registerKeyboard("effect_shoot_moonlight", GLFW.GLFW_KEY_H);
        returnThrownWeapon = registerKeyboard("return_thrown_weapon", GLFW.GLFW_KEY_N);
    }

    public static void registerKeyInputs(TickEvent.ClientTickEvent event) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        while (returnFreyrSword.wasPressed()) {
            ModMessages.sendToServer(new ReturnFreyrSwordC2S());
        }
        while (stationaryFreyrSword.wasPressed()) {
            ModMessages.sendToServer(new StationaryFreyrSwordC2S());
        }
        while (collectSummons.wasPressed()) {
            ModMessages.sendToServer(new CollectSummonsC2S());
        }
        while (switchWeapon.wasPressed()) {
            ModMessages.sendToServer(new SwitchTrickWeaponC2S());
        }
        while (keybindAbility.wasPressed()) {
            ModMessages.sendToServer(new KeybindAbilityC2S());
            if (client.player != null) {
                ClientPlayerEntity player = client.player;
                for (Hand hand : Hand.values()) {
                    if (player.getStackInHand(hand).getItem() instanceof IKeybindAbility abilityItem) {
                        abilityItem.useKeybindAbilityClient(client.world, player.getStackInHand(hand), player);
                    }
                }
                for (ItemStack armorStack : player.getArmorItems()) {
                    if (armorStack.getItem() instanceof IKeybindAbility abilityItem) {
                        abilityItem.useKeybindAbilityClient(client.world, armorStack, player);
                    }
                }
            }
        }
        while (parry.wasPressed()) {
            try {
                ModMessages.sendToServer(new ParryC2S());
            } catch (Exception ignored) {}
        }
        while (effectShootMoonlight.wasPressed()) {
            if (client.player != null && client.player.hasStatusEffect(EffectRegistry.MOON_HERALD.get()) && !client.player.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING.get())) {
                ModMessages.sendToServer(new MoonlightC2S());
                client.player.getItemCooldownManager().set(ItemRegistry.MOONSTONE_RING.get(), CommonConfig.MOONLIGHT_RING_PROJECTILE_COOLDOWN.get());
            }
        }
        while (returnThrownWeapon.wasPressed()) {
            ModMessages.sendToServer(new ReturnThrownWeaponC2S());
        }
    }

    private static KeyBinding registerKeyboard(String name, int keycode) {
        KeyBinding key = new KeyBinding("key." + SoulsWeaponry.ModId + "." + name, keycode, "category." + SoulsWeaponry.ModId + ".main");
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
