package net.soulsweaponry.client.registry;

import com.mrcrayfish.controllable.client.binding.ButtonBindings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.networking.C2S.packets.*;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.soulsweaponry.SoulsWeaponry;

public class KeyBindRegistry {

    public static KeyBinding returnFreyrSword;
    public static KeyBinding stationaryFreyrSword;
    public static KeyBinding collectSummons;
    public static KeyBinding switchWeapon;
    public static KeyBinding keybindAbility;
    public static KeyBinding parry;
    public static KeyBinding effectShootMoonlight;
    public static KeyBinding returnThrownWeapon;
    public static KeyBinding showItemTooltip;
    public static KeyBinding showItemLore;

    public static KeyBinding killNearbyEntities;
    public static KeyBinding giveResistance;

    public static void initClient() {
        returnFreyrSword = registerKeyboard("return_freyr_sword", GLFW.GLFW_KEY_Z);
        stationaryFreyrSword = registerKeyboard("freyr_sword_stationary", GLFW.GLFW_KEY_RIGHT_ALT);
        collectSummons = registerKeyboard("collect_summons_soul_reaper", GLFW.GLFW_KEY_V);
        switchWeapon = registerKeyboard("switch_weapon", GLFW.GLFW_KEY_B);
        keybindAbility = registerKeyboard("keybind_ability", GLFW.GLFW_KEY_LEFT_ALT);
        parry = registerKeyboard("parry", GLFW.GLFW_KEY_R);
        effectShootMoonlight = registerKeyboard("effect_shoot_moonlight", GLFW.GLFW_KEY_H);
        returnThrownWeapon = registerKeyboard("return_thrown_weapon", GLFW.GLFW_KEY_N);
        showItemTooltip = registerKeyboard("show_tooltip", GLFW.GLFW_KEY_UNKNOWN);
        showItemLore = registerKeyboard("show_lore", GLFW.GLFW_KEY_UNKNOWN);

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            killNearbyEntities = registerKeyboard("kill_nearby_entities", GLFW.GLFW_KEY_K);
            giveResistance = registerKeyboard("give_or_clear_resistance", GLFW.GLFW_KEY_J);
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                while (killNearbyEntities.wasPressed()) {
                    ClientPlayNetworking.send(new KillNearbyEntitiesC2S());
                }
            });
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                while (giveResistance.wasPressed()) {
                    ClientPlayNetworking.send(new GiveResistanceC2S());
                }
            });
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (returnFreyrSword.wasPressed()) {
                ClientPlayNetworking.send(new ReturnFreyrSwordC2S());
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (stationaryFreyrSword.wasPressed()) {
                ClientPlayNetworking.send(new StationaryFreyrSwordC2S());
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (collectSummons.wasPressed()) {
                ClientPlayNetworking.send(new CollectSummonsC2S());
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (switchWeapon.wasPressed()) {
                ClientPlayNetworking.send(new SwitchTrickWeaponC2S());
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keybindAbility.wasPressed()) {
                ClientPlayNetworking.send(new KeybindAbilityC2S());
                if (client.player != null) {
                    ClientPlayerEntity player = client.player;
                    for (ItemStack armorStack : player.getArmorItems()) {
                        if (armorStack.getItem() instanceof IHasAbilities abilityItem) {
                            abilityItem.useKeybindAbilityClient(client.world, armorStack, player, null);
                        }
                    }
                    for (Hand hand : Hand.values()) {
                        ItemStack stack = player.getStackInHand(hand);
                        if (stack.getItem() instanceof IHasAbilities abilityItem) {
                            abilityItem.useKeybindAbilityClient(client.world, stack, player, hand);
                        }
                    }
                }
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (parry.wasPressed()) {
                try {
                    ClientPlayNetworking.send(new ParryC2S());
                } catch (Exception ignored) {}
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (effectShootMoonlight.isPressed()) {
                if (client.player != null) {
                    boolean accept = client.player.hasStatusEffect(EffectRegistry.MOON_HERALD)
                            && !client.player.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING.getDefaultStack());
                    if (accept) {
                        ClientPlayNetworking.send(new MoonlightC2S());
                    }
                }
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean melee = client.options.attackKey.isPressed() && client.mouse.isCursorLocked();
            boolean controller = false;
            if (WeaponUtil.isModLoaded("controllable")) {
                controller = ButtonBindings.ATTACK.isButtonDown();
            }
            if (client.player == null) {
                return;
            }
            if (melee || controller) {
                // Can only attack with main hand
                ItemStack stack = client.player.getStackInHand(Hand.MAIN_HAND);
                if (stack.getItem() instanceof IHasAbilities hasAbilities) {
                    hasAbilities.onAttackClickClient(client.world, stack, client.player);
                }
                ClientPlayNetworking.send(new AttackClickC2S());
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (returnThrownWeapon.wasPressed()) {
                ClientPlayNetworking.send(new ReturnThrownWeaponC2S());
            }
        });
    }

    private static KeyBinding registerKeyboard(String name, int keycode) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + SoulsWeaponry.ModId + "." + name, InputUtil.Type.KEYSYM, keycode, "category." + SoulsWeaponry.ModId + ".main"));
    }
}
