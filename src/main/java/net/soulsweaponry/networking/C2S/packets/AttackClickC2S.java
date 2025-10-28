package net.soulsweaponry.networking.C2S.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record AttackClickC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "attack_click");
    public static final Id<AttackClickC2S> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, AttackClickC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new AttackClickC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}