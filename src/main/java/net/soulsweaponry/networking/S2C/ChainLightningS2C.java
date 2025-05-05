package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.soulsweaponry.particles.ChainLightningHandler;
import org.joml.Vector3f;

public class ChainLightningS2C {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        Vector3f from = buf.readVector3f();
        Vector3f to = buf.readVector3f();
        client.execute(() -> handle(client, from, to));
    }

    private static void handle(MinecraftClient client, Vector3f from, Vector3f to) {
        if (client.world != null) {
            ChainLightningHandler.spawnChainLightning(client.world, from, to);
        }
    }
}
