package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record SingleParticleS2C(ParticleEffect effect, double x, double y, double z,
                                 double velX, double velY, double velZ) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "single_particle");
    public static final CustomPayload.Id<SingleParticleS2C> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, SingleParticleS2C> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> {
                        PacketCodecs.registryCodec(ParticleTypes.TYPE_CODEC)
                                .encode(buf, pkt.effect());
                        buf.writeDouble(pkt.x());
                        buf.writeDouble(pkt.y());
                        buf.writeDouble(pkt.z());
                        buf.writeDouble(pkt.velX());
                        buf.writeDouble(pkt.velY());
                        buf.writeDouble(pkt.velZ());
                    },
                    buf -> {
                        ParticleEffect effect = PacketCodecs
                                .registryCodec(ParticleTypes.TYPE_CODEC)
                                .decode(buf);
                        double x = buf.readDouble();
                        double y = buf.readDouble();
                        double z = buf.readDouble();
                        double velX = buf.readDouble();
                        double velY = buf.readDouble();
                        double velZ = buf.readDouble();
                        return new SingleParticleS2C(effect, x, y, z, velX, velY, velZ);
                    }
            );

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }

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