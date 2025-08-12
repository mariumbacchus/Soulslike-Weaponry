package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record MaxPostureSyncS2C(int posture) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "max_posture_data_sync");
    public static final Id<MaxPostureSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, MaxPostureSyncS2C> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, MaxPostureSyncS2C::posture, MaxPostureSyncS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}