package net.soulsweaponry.client.registry;

import com.mrcrayfish.controllable.client.binding.ButtonBindings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.networking.C2S.packets.*;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.IKeybindAbility;
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
                    //TODO remove these calls below when all abilities have been made
                    for (Hand hand : Hand.values()) {
                        ItemStack stack = player.getStackInHand(hand);
                        if (stack.getItem() instanceof IKeybindAbility abilityItem) {
                            if (stack.getItem() instanceof IConfigDisable configDisable && configDisable.isDisabled(stack)) {
                                configDisable.notifyDisabled(player);
                            } else {
                                abilityItem.useKeybindAbilityClient(client.world, player.getStackInHand(hand), player);
                            }
                        }
                    }
                    for (ItemStack armorStack : player.getArmorItems()) {
                        if (armorStack.getItem() instanceof IKeybindAbility abilityItem) {
                            if (armorStack.getItem() instanceof IConfigDisable configDisable && configDisable.isDisabled(armorStack)) {
                                configDisable.notifyDisabled(player);
                            } else {
                                abilityItem.useKeybindAbilityClient(client.world, armorStack, player);
                            }
                        }
                    }
                    //TODO remove above
                    for (ItemStack armorStack : player.getArmorItems()) {
                        if (armorStack.getItem() instanceof IHasAbilities abilityItem) {
                            if (abilityItem.isDisabled(armorStack)) {
                                abilityItem.notifyDisabled(player);
                            } else {
                                abilityItem.getAbilities().forEach(a -> a.useKeybindAbilityClient(client.world, armorStack, player));
                            }
                        }
                    }
                    for (Hand hand : Hand.values()) {
                        ItemStack stack = player.getStackInHand(hand);
                        if (stack.getItem() instanceof IHasAbilities abilityItem) {
                            if (abilityItem.isDisabled(stack)) {
                                abilityItem.notifyDisabled(player);
                            } else {
                                abilityItem.getAbilities().forEach(a -> a.useKeybindAbilityClient(client.world, stack, player));
                            }
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
            boolean effect = effectShootMoonlight.isPressed();
            boolean melee = client.options.attackKey.isPressed() && client.mouse.isCursorLocked();
            boolean controller = false;
            if (WeaponUtil.isModLoaded("controllable")) {
                controller = ButtonBindings.ATTACK.isButtonDown();
            }
            if (effect || melee || controller) {
                if (client.player != null) {
                    boolean accept = false;
                    if (effect && client.player.hasStatusEffect(EffectRegistry.MOON_HERALD) && !client.player.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING)) {
                        accept = true;
                    } else if (melee || controller) {
                        for (Hand hand : Hand.values()) {
                            ItemStack stack = client.player.getStackInHand(hand);
                            boolean moonlight = stack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD) && !ConfigConstructor.disable_use_moonlight_shortsword;
                            boolean bluemoon = stack.isOf(WeaponRegistry.BLUEMOON_SHORTSWORD) && !ConfigConstructor.disable_use_bluemoon_shortsword;
                            // Sending message each left click with the item is a bit much so don't do that
                            if (moonlight || bluemoon) {
                                accept = true;
                            }
                        }
                    }
                    if (accept) {
                        ClientPlayNetworking.send(new MoonlightC2S());
                    }
                }
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
