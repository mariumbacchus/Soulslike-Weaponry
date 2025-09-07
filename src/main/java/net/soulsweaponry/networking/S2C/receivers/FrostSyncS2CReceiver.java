package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.networking.S2C.packets.FrostSyncS2C;

public class FrostSyncS2CReceiver {

    public static void receive(FrostSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver)client.player).getPersistentData().putInt(FrostData.FROST_VALUE_ID, payload.frostValue());
        ((IEntityDataSaver)client.player).getPersistentData().putBoolean(FrostData.FROST_COOLING_DOWN_ID, payload.isFrostCoolingDown());
    }
}
