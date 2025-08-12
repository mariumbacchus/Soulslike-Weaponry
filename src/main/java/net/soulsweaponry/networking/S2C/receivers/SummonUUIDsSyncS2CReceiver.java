package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.networking.S2C.packets.SummonUUIDsSyncS2C;
import net.soulsweaponry.util.NbtHelper;

public class SummonUUIDsSyncS2CReceiver {

    public static void receive(SummonUUIDsSyncS2C packet, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        if (client.player == null) return;

        client.execute(() -> {
            NbtHelper.saveUUIDArr(
                    ((IEntityDataSaver) client.player).getPersistentData(),
                    packet.uuids(),
                    packet.listId()
            );
        });
    }
}
