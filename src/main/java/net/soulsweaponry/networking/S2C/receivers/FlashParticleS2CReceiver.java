package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.soulsweaponry.networking.S2C.packets.FlashParticleS2C;

public class FlashParticleS2CReceiver {

    public static void receive(FlashParticleS2C packet, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        if (client.world == null) {
            return;
        }
        client.execute(() -> {
            double x = packet.x();
            double y = packet.y();
            double z = packet.z();
            int argb = packet.color();
            float sizeMod = packet.sizeMod();
            float red = ((argb >> 16) & 0xFF) / 255f;
            float green = ((argb >> 8) & 0xFF) / 255f;
            float blue = (argb & 0xFF) / 255f;
            Particle flash = client.particleManager.addParticle(ParticleTypes.FLASH, x, y, z, 0, 0, 0);
            flash.setBoundingBox(new Box(BlockPos.ofFloored(x, y, z)).expand(sizeMod));
            flash.setColor(red, green, blue);
        });
    }
}
