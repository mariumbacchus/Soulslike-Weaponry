package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.soulsweaponry.networking.S2C.packets.SingleParticleS2C;

public class SingleParticleS2CReceiver {

    public static void receive(SingleParticleS2C pkt, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        if (client.world == null) {
            return;
        }
        client.execute(() -> {
            ClientWorld world = client.world;
            world.addParticle(pkt.effect(), pkt.x(), pkt.y(), pkt.z(), pkt.velX(), pkt.velY(), pkt.velZ());
        });
    }
}
