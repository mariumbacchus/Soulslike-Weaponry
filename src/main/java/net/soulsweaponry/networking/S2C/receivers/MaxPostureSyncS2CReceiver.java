package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.networking.S2C.packets.MaxPostureSyncS2C;

public class MaxPostureSyncS2CReceiver {

    public static void receive(MaxPostureSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        int lvl = payload.posture();
        ((IEntityDataSaver) client.player).getPersistentData().putInt(PostureData.MAX_POSTURE_ID, lvl);
    }
}
