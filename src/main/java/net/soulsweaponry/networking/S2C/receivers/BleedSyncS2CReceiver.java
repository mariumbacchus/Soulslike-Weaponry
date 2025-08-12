package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.networking.S2C.packets.BleedSyncS2C;

public class BleedSyncS2CReceiver {

    public static void receive(BleedSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        int lvl = payload.bleedLevel();
        ((IEntityDataSaver) client.player).getPersistentData().putInt(BleedData.BLEED_ID, lvl);
    }
}
