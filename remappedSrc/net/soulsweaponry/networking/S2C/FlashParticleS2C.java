package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.soulsweaponry.util.ParticleHandler;

public class FlashParticleS2C {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        float red = buf.readFloat();
        float green = buf.readFloat();
        float blue = buf.readFloat();
        float sizeMod = buf.readFloat();
        client.execute(() -> handle(client.world, x, y, z, new ParticleHandler.RGB(red, green, blue), sizeMod));
    }

    private static void handle(ClientWorld world, double x, double y, double z, ParticleHandler.RGB rgb, float sizeMod) {
        ParticleHandler.flashParticle(world, x, y, z, rgb, sizeMod);
    }
}