package net.soulsweaponry.networking.C2S.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record CollectSummonsC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId,"collect_summons_to_weapon");
    public static final CustomPayload.Id<CollectSummonsC2S> TYPE =
            new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, CollectSummonsC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> {},
                    buf -> new CollectSummonsC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}