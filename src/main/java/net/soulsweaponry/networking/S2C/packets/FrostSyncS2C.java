package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record FrostSyncS2C(int frostValue, boolean isFrostCoolingDown) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "frost_data_sync");
    public static final Id<FrostSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, FrostSyncS2C> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.INTEGER, FrostSyncS2C::frostValue,
                    PacketCodecs.BOOL, FrostSyncS2C::isFrostCoolingDown,
                    FrostSyncS2C::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}