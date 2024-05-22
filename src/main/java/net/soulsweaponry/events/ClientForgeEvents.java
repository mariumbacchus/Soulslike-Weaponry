package net.soulsweaponry.events;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.C2S.MoonlightC2S;
import net.soulsweaponry.registry.WeaponRegistry;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        //NOTE: 1.19 does registry differently, check https://www.youtube.com/watch?v=NN-k74NMKRc&list=PLKGarocXCE1HrC60yuTNTGRoZc6hf5Uvl&index=14 for more info.
        KeyBindRegistry.registerKeyInputs(event);
        MinecraftClient client = MinecraftClient.getInstance();
        if (event.phase == TickEvent.Phase.END && client.options.attackKey.isPressed() && client.world != null && client.player != null) {
            ClientForgeEvents.triggerMoonlightEvent(client.player);
        }
    }

    public static void triggerMoonlightEvent(ClientPlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD.get()) || stack.isOf(WeaponRegistry.BLUEMOON_SHORTSWORD.get())) {
                ModMessages.sendToServer(new MoonlightC2S());
            }
        }
    }
}
