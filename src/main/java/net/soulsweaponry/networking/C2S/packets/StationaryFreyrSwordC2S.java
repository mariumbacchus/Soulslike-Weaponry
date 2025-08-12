package net.soulsweaponry.networking.C2S.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record StationaryFreyrSwordC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "switch_stationary_freyr");
    public static final CustomPayload.Id<StationaryFreyrSwordC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, StationaryFreyrSwordC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new StationaryFreyrSwordC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}