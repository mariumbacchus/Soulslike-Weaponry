package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.soulsweaponry.items.sword.MoonlightShortsword;
import net.soulsweaponry.networking.C2S.packets.MoonlightC2S;

public class MoonlightC2SReceiver {

    public static void receive(MoonlightC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> MoonlightShortsword.summonSmallProjectile(serverWorld, player));
    }
}
