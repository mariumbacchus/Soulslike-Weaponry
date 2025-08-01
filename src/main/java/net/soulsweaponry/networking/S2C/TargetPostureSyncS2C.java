package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.TargetPostureData;

public record TargetPostureSyncS2C(int posture, String name, int maxPosture) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "target_posture_data_sync");
    public static final Id<TargetPostureSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, TargetPostureSyncS2C> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.INTEGER, TargetPostureSyncS2C::posture,
                    PacketCodecs.STRING, TargetPostureSyncS2C::name,
                    PacketCodecs.INTEGER, TargetPostureSyncS2C::maxPosture,
                    TargetPostureSyncS2C::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(TargetPostureSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver)client.player).getPersistentData().putInt(TargetPostureData.POSTURE_ID, payload.posture());
        ((IEntityDataSaver)client.player).getPersistentData().putString(TargetPostureData.NAME_ID, payload.name());
        ((IEntityDataSaver)client.player).getPersistentData().putInt(TargetPostureData.MAX_POSTURE_ID, payload.maxPosture());
    }
}