package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.FreyrSwordSummonData;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.networking.S2C.packets.FreyrSwordSummonDataSyncS2C;

public class FreyrSwordSummonDataSyncS2CReceiver {

    public static void receive(FreyrSwordSummonDataSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver)client.player).getPersistentData().putUuid(FreyrSwordSummonData.SUMMON_ID, payload.freyrSwordUuid());
    }
}
