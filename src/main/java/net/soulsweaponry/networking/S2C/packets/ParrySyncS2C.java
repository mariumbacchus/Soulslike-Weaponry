package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record ParrySyncS2C(int parryTick, int parryFrames, int maxTicks) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "parry_data_sync");
    public static final CustomPayload.Id<ParrySyncS2C> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ParrySyncS2C> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.INTEGER.cast(), ParrySyncS2C::parryTick,
                    PacketCodecs.INTEGER.cast(), ParrySyncS2C::parryFrames,
                    PacketCodecs.INTEGER.cast(), ParrySyncS2C::maxTicks,
                    ParrySyncS2C::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}