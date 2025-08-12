package net.soulsweaponry.networking.C2S.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record GiveResistanceC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "give_or_clear_resistance");
    public static final CustomPayload.Id<GiveResistanceC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, GiveResistanceC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new GiveResistanceC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
