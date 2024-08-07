package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.UmbralTrespassData;

public class UTDamageCooldownSyncS2C {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        if (client.player != null) {
            ((IEntityDataSaver)client.player).getPersistentData().putFloat(UmbralTrespassData.UMBRAL_DAMAGE_ID, buf.readFloat());
            ((IEntityDataSaver)client.player).getPersistentData().putInt(UmbralTrespassData.COOLDOWN_ID, buf.readInt());
            ((IEntityDataSaver)client.player).getPersistentData().putBoolean(UmbralTrespassData.HEAL_ID, buf.readBoolean());
        }
    }
}
