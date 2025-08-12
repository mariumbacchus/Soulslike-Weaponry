package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.networking.S2C.packets.ParticleOutburstS2C;
import net.soulsweaponry.particles.ParticleHandler;

import java.util.List;

public class ParticleOutburstS2CReceiver {

    public static void receive(ParticleOutburstS2C pkt, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        if (client.world == null) {
            return;
        }
        client.execute(() -> {
            ClientWorld world = client.world;
            List<Vec3d> list = ParticleHandler.getParticleOutburstCords(pkt.amount(), new Vec3d(pkt.dividerX(), pkt.dividerY(), pkt.dividerZ()), pkt.sizeMod());
            for (Vec3d vec : list) {
                world.addParticle(pkt.effect(), pkt.x(), pkt.y(), pkt.z(), vec.x, vec.y, vec.z);
            }
        });
    }
}
