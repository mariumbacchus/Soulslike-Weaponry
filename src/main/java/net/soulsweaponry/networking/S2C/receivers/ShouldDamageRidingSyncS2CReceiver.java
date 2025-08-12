package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.UmbralTrespassData;
import net.soulsweaponry.networking.S2C.packets.ShouldDamageRidingSyncS2C;

public class ShouldDamageRidingSyncS2CReceiver {

    public static void receive(ShouldDamageRidingSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver)client.player).getPersistentData().putBoolean(UmbralTrespassData.DAMAGE_RIDING_ID, payload.damageRiding());
    }
}
