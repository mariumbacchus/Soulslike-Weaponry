package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

import java.util.UUID;

public record FreyrSwordSummonDataSyncS2C(UUID freyrSwordUuid) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "freyr_sword_summon_uuid_sync");
    public static final Id<FreyrSwordSummonDataSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, FreyrSwordSummonDataSyncS2C> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> buf.writeUuid(pkt.freyrSwordUuid()),
                    buf -> new FreyrSwordSummonDataSyncS2C(buf.readUuid())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}