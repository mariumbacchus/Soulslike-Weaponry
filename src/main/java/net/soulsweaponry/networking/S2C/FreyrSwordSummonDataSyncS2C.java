package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entitydata.FreyrSwordSummonData;
import net.soulsweaponry.entitydata.IEntityDataSaver;

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

    public static void receive(FreyrSwordSummonDataSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver)client.player).getPersistentData().putUuid(FreyrSwordSummonData.SUMMON_ID, payload.freyrSwordUuid());
    }
}