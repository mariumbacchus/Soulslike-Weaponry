package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.entitydata.IEntityDataSaver;

public class FrostSyncS2C {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        if (client.player != null) {
            int frostValue = buf.readInt();
            boolean frostCoolingDown = buf.readBoolean();
            ((IEntityDataSaver)client.player).getPersistentData().putInt(FrostData.FROST_VALUE_ID, frostValue);
            ((IEntityDataSaver)client.player).getPersistentData().putBoolean(FrostData.FROST_COOLING_DOWN_ID, frostCoolingDown);
        }
    }
}
