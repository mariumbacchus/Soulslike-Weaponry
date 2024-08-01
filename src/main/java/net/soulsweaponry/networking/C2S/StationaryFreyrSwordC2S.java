package net.soulsweaponry.networking.C2S;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.entitydata.FreyrSwordSummonData;

import java.util.UUID;

public class StationaryFreyrSwordC2S {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        server.execute(() -> {
            ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
            if (serverWorld != null) {
                Text text = Text.translatable("soulsweapons.weapon.no_freyr_sword");
                UUID uuid = FreyrSwordSummonData.getSummonUuid(player);
                if (uuid != null && player.getBlockPos() != null) {
                    Entity entity = player.getWorld().getEntity(uuid);
                    if (entity instanceof FreyrSwordEntity sword) {
                        sword.setStationaryPos(player.getBlockPos());
                    } else if (ConfigConstructor.inform_player_about_no_bound_freyr_sword) {
                        player.sendMessage(text, true);
                    }
                }
            }
        });
    }
}