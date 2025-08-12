package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record ParticleSphereS2C(ParticleEffect effect, double x, double y, double z,
                               int amount, float sizeMod) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "sphere_particles");
    public static final CustomPayload.Id<ParticleSphereS2C> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ParticleSphereS2C> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> {
                        PacketCodecs.registryCodec(ParticleTypes.TYPE_CODEC)
                                .encode(buf, pkt.effect());
                        buf.writeDouble(pkt.x());
                        buf.writeDouble(pkt.y());
                        buf.writeDouble(pkt.z());
                        buf.writeInt(pkt.amount());
                        buf.writeFloat(pkt.sizeMod());
                    },
                    buf -> {
                        ParticleEffect effect = PacketCodecs
                                .registryCodec(ParticleTypes.TYPE_CODEC)
                                .decode(buf);
                        double x = buf.readDouble();
                        double y = buf.readDouble();
                        double z = buf.readDouble();
                        int amount = buf.readInt();
                        float sizeMod = buf.readFloat();
                        return new ParticleSphereS2C(effect, x, y, z, amount, sizeMod);
                    }
            );

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}