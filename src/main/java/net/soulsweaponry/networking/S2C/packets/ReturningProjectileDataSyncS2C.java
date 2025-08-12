package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

import java.util.UUID;

public record ReturningProjectileDataSyncS2C(UUID projectileUuid) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "returning_projectile_uuid_sync");
    public static final Id<ReturningProjectileDataSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ReturningProjectileDataSyncS2C> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> buf.writeUuid(pkt.projectileUuid()),
                    buf -> new ReturningProjectileDataSyncS2C(buf.readUuid())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}