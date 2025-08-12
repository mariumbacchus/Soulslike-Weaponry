package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record StopBossMusicS2C(Identifier soundId) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "stop_boss_music");
    public static final CustomPayload.Id<StopBossMusicS2C> TYPE = new CustomPayload.Id<>(ID);

    public static final PacketCodec<RegistryByteBuf, StopBossMusicS2C> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> buf.writeIdentifier(pkt.soundId()),
                    buf -> new StopBossMusicS2C(buf.readIdentifier())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
