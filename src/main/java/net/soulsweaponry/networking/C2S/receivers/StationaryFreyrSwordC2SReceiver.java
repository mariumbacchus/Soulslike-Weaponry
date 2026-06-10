package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.entitydata.FreyrSwordSummonData;
import net.soulsweaponry.networking.C2S.packets.StationaryFreyrSwordC2S;

import java.util.UUID;

public class StationaryFreyrSwordC2SReceiver {

    public static void receive(StationaryFreyrSwordC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            Text text = Text.translatableWithFallback("soulsweapons.weapon.no_freyr_sword", "There is no Freyr Sword bound to you!");
            UUID uuid = FreyrSwordSummonData.getSummonUuid(player);
            if (uuid != null && player.getBlockPos() != null) {
                Entity entity = serverWorld.getEntity(uuid);
                if (entity instanceof FreyrSwordEntity sword) {
                    sword.setStationaryPos(player.getBlockPos());
                } else if (WeaponConfig.inform_player_about_no_bound_freyr_sword) {
                    player.sendMessage(text, true);
                }
            }
        });
    }
}
