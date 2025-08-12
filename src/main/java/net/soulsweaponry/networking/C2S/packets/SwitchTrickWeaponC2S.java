package net.soulsweaponry.networking.C2S.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record SwitchTrickWeaponC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "switch_trick_weapon");
    public static final CustomPayload.Id<SwitchTrickWeaponC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, SwitchTrickWeaponC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new SwitchTrickWeaponC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}