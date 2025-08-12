package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

import java.util.UUID;

public record SummonUUIDsSyncS2C(String listId, UUID[] uuids) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "summons_uuids_sync");
    public static final CustomPayload.Id<SummonUUIDsSyncS2C> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, SummonUUIDsSyncS2C> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> {
                        buf.writeInt(pkt.uuids().length);
                        buf.writeString(pkt.listId());
                        for (UUID uuid : pkt.uuids()) {
                            buf.writeUuid(uuid);
                        }
                    },
                    buf -> {
                        int len = buf.readInt();
                        String listId = buf.readString();
                        UUID[] arr = new UUID[len];
                        for (int i = 0; i < len; i++) {
                            arr[i] = buf.readUuid();
                        }
                        return new SummonUUIDsSyncS2C(listId, arr);
                    }
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}