package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.ReturningProjectileData;

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

    public static void receive(ReturningProjectileDataSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver)client.player).getPersistentData().putUuid(ReturningProjectileData.PROJECTILE_ID, payload.projectileUuid());
    }
}