package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.TargetPostureData;
import net.soulsweaponry.networking.S2C.packets.TargetPostureSyncS2C;

public class TargetPostureSyncS2CReceiver {

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
