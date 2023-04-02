package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.soulsweaponry.events.AttemptAttackCallback;
import net.soulsweaponry.networking.PacketRegistry;

public class EventRegistry {

    public static void init() {
        AttemptAttackCallback.EVENT.register((player, world) -> {
            for (Hand hand : Hand.values()) {
                if (player.hasStatusEffect(EffectRegistry.MOON_HERALD) || player.getStackInHand(hand).isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD)) {
                    if (world.isClient) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(PacketRegistry.MOONLIGHT, buf);
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}
