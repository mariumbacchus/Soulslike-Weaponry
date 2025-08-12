package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record PostureSyncS2C(int posture) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "posture_data_sync");
    public static final Id<PostureSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, PostureSyncS2C> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, PostureSyncS2C::posture, PostureSyncS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}