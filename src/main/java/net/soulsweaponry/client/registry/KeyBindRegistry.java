package net.soulsweaponry.client.registry;

import com.mrcrayfish.controllable.client.binding.ButtonBindings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.C2S.*;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import org.lwjgl.glfw.GLFW;

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

    public static void register(RegisterKeyMappingsEvent event) {
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

        event.register(KeyBindRegistry.returnFreyrSword);
        event.register(KeyBindRegistry.stationaryFreyrSword);
        event.register(KeyBindRegistry.collectSummons);
        event.register(KeyBindRegistry.switchWeapon);
        event.register(KeyBindRegistry.keybindAbility);
        event.register(KeyBindRegistry.parry);
        event.register(KeyBindRegistry.effectShootMoonlight);
        event.register(KeyBindRegistry.returnThrownWeapon);
        event.register(KeyBindRegistry.showItemTooltip);
        event.register(KeyBindRegistry.showItemLore);

        if (!FMLLoader.isProduction()) {
            killNearbyEntities = registerKeyboard("kill_nearby_entities", GLFW.GLFW_KEY_K);
            giveResistance = registerKeyboard("give_or_clear_resistance", GLFW.GLFW_KEY_J);
        }
    }

    public static void registerKeyInputs() {
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
            }
        }
        while (parry.wasPressed()) {
            try {
                ModMessages.sendToServer(new ParryC2S());
            } catch (Exception ignored) {}
        }
        boolean effect = effectShootMoonlight.isPressed();
        boolean melee = client.options.attackKey.isPressed() && client.mouse.isCursorLocked();
        boolean controller = false;
        if (WeaponUtil.isModLoaded("controllable")) {
            controller = ButtonBindings.ATTACK.isButtonPressed();
        }
        if (effect || melee || controller) {
            if (client.player != null) {
                boolean accept = false;
                if (effect && client.player.hasStatusEffect(EffectRegistry.MOON_HERALD.get()) && !client.player.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING.get())) {
                    accept = true;
                    client.player.getItemCooldownManager().set(ItemRegistry.MOONSTONE_RING.get(), (int) ConfigConstructor.moonstone_ring_projectile_cooldown);
                } else if (melee || controller) {
                    for (Hand hand : Hand.values()) {
                        ItemStack stack = client.player.getStackInHand(hand);
                        boolean moonlight = stack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD.get()) && !ConfigConstructor.disable_use_moonlight_shortsword;
                        boolean bluemoon = stack.isOf(WeaponRegistry.BLUEMOON_SHORTSWORD.get()) && !ConfigConstructor.disable_use_bluemoon_shortsword;
                        // Sending message each left click with the item is a bit much so don't do that
                        if (moonlight || bluemoon) {
                            accept = true;
                        }
                    }
                }
                if (accept) {
                    ModMessages.sendToServer(new MoonlightC2S());
                }
            }
        }
        while (returnThrownWeapon.wasPressed()) {
            ModMessages.sendToServer(new ReturnThrownWeaponC2S());
        }
        if (!FMLLoader.isProduction()) {
            while (killNearbyEntities.wasPressed()) {
                ModMessages.sendToServer(new KillNearbyEntitiesC2S());
            }
            while (giveResistance.wasPressed()) {
                ModMessages.sendToServer(new GiveResistanceC2S());
            }
        }
    }

    private static KeyBinding registerKeyboard(String name, int keycode) {
        return new KeyBinding("key." + SoulsWeaponry.ModId + "." + name, keycode, "category." + SoulsWeaponry.ModId + ".main");
    }
}
