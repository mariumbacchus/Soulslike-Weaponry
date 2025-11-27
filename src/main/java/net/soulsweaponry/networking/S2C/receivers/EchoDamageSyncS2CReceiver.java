package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.EchoDamageData;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.networking.S2C.packets.EchoDamageSyncS2C;

public class EchoDamageSyncS2CReceiver {

    public static void receive(EchoDamageSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        float data = payload.echoDamage();
        float mod = payload.savedDamageMod();
        ((IEntityDataSaver) client.player).getPersistentData().putFloat(EchoDamageData.ECHO_DAMAGE_ID, data);
        ((IEntityDataSaver) client.player).getPersistentData().putFloat(EchoDamageData.ECHO_DAMAGE_SAVED_MOD_ID, mod);
    }
}
