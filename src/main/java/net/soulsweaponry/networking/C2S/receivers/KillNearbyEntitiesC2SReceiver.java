package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.soulsweaponry.entity.mobs.boss.BossEntity;
import net.soulsweaponry.networking.C2S.packets.KillNearbyEntitiesC2S;

public class KillNearbyEntitiesC2SReceiver {

    public static void receive(KillNearbyEntitiesC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            for (Entity entity : serverWorld.getOtherEntities(player, player.getBoundingBox().expand(100D))) {
                if (player.isSneaking() && entity instanceof BossEntity<?>) {
                    continue;
                }
                entity.kill();
            }
        });
    }
}
