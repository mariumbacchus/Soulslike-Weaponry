package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.util.NbtHelper;

import java.util.UUID;

public class SummonUUIDsSyncS2C {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        if (client.player != null) {
            int length = buf.readInt();
            String listId = buf.readString();
            NbtCompound nbt = ((IEntityDataSaver)client.player).getPersistentData();
            UUID[] list = new UUID[length];
            for (int i = 0; i < length; i++) {
                list[i] = buf.readUuid();
            }
            NbtHelper.saveUUIDArr(nbt, list, listId);
        }
    }
}