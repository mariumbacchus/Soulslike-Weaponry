package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record ParticleOutburstS2C(ParticleEffect effect, double x, double y, double z, int amount,
                                  double dividerX, double dividerY, double dividerZ, float sizeMod) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "outburst_particles");
    public static final CustomPayload.Id<ParticleOutburstS2C> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ParticleOutburstS2C> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> {
                        PacketCodecs.registryCodec(ParticleTypes.TYPE_CODEC)
                                .encode(buf, pkt.effect());
                        buf.writeDouble(pkt.x());
                        buf.writeDouble(pkt.y());
                        buf.writeDouble(pkt.z());
                        buf.writeInt(pkt.amount());
                        buf.writeDouble(pkt.dividerX());
                        buf.writeDouble(pkt.dividerY());
                        buf.writeDouble(pkt.dividerZ());
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
                        double dividerX = buf.readDouble();
                        double dividerY = buf.readDouble();
                        double dividerZ = buf.readDouble();
                        float sizeMod = buf.readFloat();
                        return new ParticleOutburstS2C(effect, x, y, z, amount, dividerX, dividerY, dividerZ, sizeMod);
                    }
            );

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}