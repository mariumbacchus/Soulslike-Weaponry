package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.networking.S2C.packets.ParrySyncS2C;

public class ParrySyncS2CReceiver {

    public static void receive(ParrySyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver) client.player).getPersistentData().putInt(ParryData.PARRY_TICKS_ID, payload.parryTick());
        ((IEntityDataSaver) client.player).getPersistentData().putInt(ParryData.PARRY_FRAMES_ID, payload.parryFrames());
        ((IEntityDataSaver) client.player).getPersistentData().putInt(ParryData.MAX_PARRY_TICKS_ID, payload.maxTicks());
    }
}
