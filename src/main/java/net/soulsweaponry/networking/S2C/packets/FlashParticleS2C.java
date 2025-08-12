package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record FlashParticleS2C(double x, double y, double z, int color, float sizeMod) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "flash_particle");
    public static final CustomPayload.Id<FlashParticleS2C> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, FlashParticleS2C> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.DOUBLE.cast(), FlashParticleS2C::x,
                    PacketCodecs.DOUBLE.cast(), FlashParticleS2C::y,
                    PacketCodecs.DOUBLE.cast(), FlashParticleS2C::z,
                    PacketCodecs.INTEGER.cast(), FlashParticleS2C::color,
                    PacketCodecs.FLOAT.cast(),   FlashParticleS2C::sizeMod,
                    FlashParticleS2C::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}