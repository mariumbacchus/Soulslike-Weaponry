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
import net.soulsweaponry.networking.C2S.packets.ReturnFreyrSwordC2S;

import java.util.UUID;

public class ReturnFreyrSwordC2SReceiver {

    public static void receive(ReturnFreyrSwordC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            Text text = Text.translatable("soulsweapons.weapon.no_freyr_sword");
            UUID uuid = FreyrSwordSummonData.getSummonUuid(player);
            if (uuid != null && player.getBlockPos() != null) {
                Entity sword = serverWorld.getEntity(uuid);
                if (sword instanceof FreyrSwordEntity freyrSword) {
                    if (!freyrSword.insertStack(player)) {
                        freyrSword.setPos(player.getX(), player.getEyeY(), player.getZ());
                        freyrSword.dropStack();
                    }
                    freyrSword.discard();
                } else if (WeaponConfig.inform_player_about_no_bound_freyr_sword) {
                    player.sendMessage(text, true);
                }
            }
        });
    }
}
