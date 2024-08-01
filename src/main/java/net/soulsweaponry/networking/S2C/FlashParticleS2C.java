package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.soulsweaponry.particles.ParticleHandler;

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
        Particle flash = MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.FLASH, x, y, z, 0, 0, 0);
        flash.setBoundingBox(new Box(BlockPos.ofFloored(x, y, z)).expand(sizeMod));
        flash.setColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
    }
}