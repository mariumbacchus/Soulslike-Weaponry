package net.soulsweaponry.client.registry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.C2S.Example;
import net.soulsweaponry.registry.EffectRegistry;
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
            //ModMessages.sendToServer(new Example(Minecraft.getInstance().player.blockPosition()));//Sends example packet, TODO make real packets and register events for all keybinds
            //ClientPlayNetworking.send(PacketIds.RETURN_FREYR_SWORD, PacketByteBufs.empty());
        }
        while (stationaryFreyrSword.wasPressed()) {
            //ClientPlayNetworking.send(PacketIds.STATIONARY_FREYR_SWORD, PacketByteBufs.empty());
        }
        while (collectSummons.wasPressed()) {
            //ClientPlayNetworking.send(PacketIds.COLLECT_SUMMONS, PacketByteBufs.empty());
        }
        while (switchWeapon.wasPressed()) {
            //ClientPlayNetworking.send(PacketIds.SWITCH_TRICK_WEAPON, PacketByteBufs.empty());
        }
        while (keybindAbility.wasPressed()) {
            //ClientPlayNetworking.send(PacketIds.KEYBIND_ABILITY, PacketByteBufs.empty());
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
                //ClientPlayNetworking.send(PacketIds.PARRY, PacketByteBufs.empty());
            } catch (Exception ignored) {}
        }
        while (effectShootMoonlight.wasPressed()) {
            /*if (client.player != null && client.player.hasStatusEffect(EffectRegistry.MOON_HERALD.get()) && !client.player.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING.get())) {
                //PacketByteBuf buf = PacketByteBufs.create();
                //ClientPlayNetworking.send(PacketIds.MOONLIGHT, buf);
                //client.player.getItemCooldownManager().set(ItemRegistry.MOONSTONE_RING.get(), ConfigConstructor.moonlight_ring_projectile_cooldown);
            }*/
        }
        while (returnThrownWeapon.wasPressed()) {
            //ClientPlayNetworking.send(PacketIds.RETURN_THROWN_WEAPON, PacketByteBufs.empty());
        }
    }

    private static KeyBinding registerKeyboard(String name, int keycode) {
        KeyBinding key = new KeyBinding("key." + SoulsWeaponry.MOD_ID + "." + name, keycode, "category." + SoulsWeaponry.MOD_ID + ".main");
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
