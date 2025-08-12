package net.soulsweaponry.networking.S2C.receivers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.soulsweaponry.networking.S2C.packets.StopBossMusicS2C;

public class StopBossMusicS2CReceiver {

    public static void receive(StopBossMusicS2C packet, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        if (client == null) return;
        client.execute(() -> client.getSoundManager().stopSounds(packet.soundId(), SoundCategory.MUSIC));
    }
}
