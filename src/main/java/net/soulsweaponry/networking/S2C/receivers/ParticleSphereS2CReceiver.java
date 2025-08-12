package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.networking.S2C.packets.ParticleSphereS2C;
import net.soulsweaponry.particles.ParticleHandler;

import java.util.List;

public class ParticleSphereS2CReceiver {

    public static void receive(ParticleSphereS2C pkt, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        if (client.world == null) {
            return;
        }
        client.execute(() -> {
            ClientWorld world = client.world;
            List<Vec3d> list = ParticleHandler.getSphereParticleCords(pkt.amount(), pkt.sizeMod());
            for (Vec3d vec : list) {
                world.addParticle(pkt.effect(), pkt.x(), pkt.y(), pkt.z(), vec.getX(), vec.getY(), vec.getZ());
            }
        });
    }
}
