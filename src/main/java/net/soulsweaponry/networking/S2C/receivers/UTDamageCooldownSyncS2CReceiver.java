package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.UmbralTrespassData;
import net.soulsweaponry.networking.S2C.packets.UTDamageCooldownSyncS2C;

/**
 * Receiver for determining the effects upon exiting Umbral Trespass ability used in {@link net.soulsweaponry.items.UmbralTrespassItem}.
 */
public class UTDamageCooldownSyncS2CReceiver {

    public static void receive(UTDamageCooldownSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver)client.player).getPersistentData().putFloat(UmbralTrespassData.UMBRAL_DAMAGE_ID, payload.damage());
        ((IEntityDataSaver)client.player).getPersistentData().putInt(UmbralTrespassData.COOLDOWN_ID, payload.cooldown());
        ((IEntityDataSaver)client.player).getPersistentData().putBoolean(UmbralTrespassData.HEAL_ID, payload.heal());
    }
}
