package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.networking.S2C.packets.PostureSyncS2C;

public class PostureSyncS2CReceiver {

    public static void receive(PostureSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        int lvl = payload.posture();
        ((IEntityDataSaver) client.player).getPersistentData().putInt(PostureData.POSTURE_ID, lvl);
    }
}
