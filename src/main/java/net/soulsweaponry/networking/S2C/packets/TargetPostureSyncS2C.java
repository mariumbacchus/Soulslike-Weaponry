package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record TargetPostureSyncS2C(int posture, String name, int maxPosture) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "target_posture_data_sync");
    public static final Id<TargetPostureSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, TargetPostureSyncS2C> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.INTEGER, TargetPostureSyncS2C::posture,
                    PacketCodecs.STRING, TargetPostureSyncS2C::name,
                    PacketCodecs.INTEGER, TargetPostureSyncS2C::maxPosture,
                    TargetPostureSyncS2C::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}