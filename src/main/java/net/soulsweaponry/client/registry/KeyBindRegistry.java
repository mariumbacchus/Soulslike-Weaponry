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
import net.soulsweaponry.items.abilities.HasAbilitiesClientHooks;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.C2S.*;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
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
            event.register(KeyBindRegistry.killNearbyEntities);
            event.register(KeyBindRegistry.giveResistance);
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
                for (ItemStack armorStack : player.getArmorItems()) {
                    if (armorStack.getItem() instanceof IHasAbilities abilityItem) {
                        HasAbilitiesClientHooks.useKeybindAbilityClient(abilityItem, client.world, armorStack, player, null);
                    }
                }
                for (Hand hand : Hand.values()) {
                    ItemStack stack = player.getStackInHand(hand);
                    if (stack.getItem() instanceof IHasAbilities abilityItem) {
                        HasAbilitiesClientHooks.useKeybindAbilityClient(abilityItem, client.world, stack, player, hand);
                    }
                }
            }
        }
        while (parry.wasPressed()) {
            try {
                ModMessages.sendToServer(new ParryC2S());
            } catch (Exception ignored) {}
        }

        if (effectShootMoonlight.isPressed()) {
            if (client.player != null) {
                boolean accept = client.player.hasStatusEffect(EffectRegistry.MOON_HERALD.get())
                        && !client.player.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING.get());
                if (accept) {
                    ModMessages.sendToServer(new MoonlightC2S());
                }
            }
        }
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
                HasAbilitiesClientHooks.onAttackClickClient(hasAbilities, client.world, stack, client.player);
            }
            ModMessages.sendToServer(new AttackClickC2S());
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
