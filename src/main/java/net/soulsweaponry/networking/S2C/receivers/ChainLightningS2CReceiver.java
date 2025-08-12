package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.networking.S2C.packets.ChainLightningS2C;
import net.soulsweaponry.particles.ChainLightningHandler;

public class ChainLightningS2CReceiver {

    public static void receive(ChainLightningS2C packet, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        if (client.world == null) {
            return;
        }
        client.execute(() -> ChainLightningHandler.spawnChainLightning(client.world, packet.from(), packet.to()));
    }
}
