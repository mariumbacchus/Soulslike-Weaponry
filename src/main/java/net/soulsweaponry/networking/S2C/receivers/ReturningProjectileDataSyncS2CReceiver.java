package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.ReturningProjectileData;
import net.soulsweaponry.networking.S2C.packets.ReturningProjectileDataSyncS2C;

public class ReturningProjectileDataSyncS2CReceiver {

    public static void receive(ReturningProjectileDataSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver)client.player).getPersistentData().putUuid(ReturningProjectileData.PROJECTILE_ID, payload.projectileUuid());
    }
}
