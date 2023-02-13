package net.soulsweaponry.registry;

import com.google.common.collect.Iterables;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.items.FreyrSword;
import net.soulsweaponry.items.MoonlightShortsword;
import net.minecraft.entity.data.DataTracker.Entry;

public class PacketsServer {

    public static void initServer() {
        ServerPlayNetworking.registerGlobalReceiver(PacketRegistry.MOONLIGHT, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
                if (serverWorld != null) {
                    MoonlightShortsword.summonSmallProjectile(serverWorld, player);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketRegistry.RETURN_FREYR_SWORD, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
                if (serverWorld != null) {
                    //NOTE: reading bufs does not work for some reason
                    for (Entry<?> entry : player.getDataTracker().getAllEntries()) {
                        if (entry.getData() == FreyrSword.SUMMON_UUID && player.getDataTracker().get(FreyrSword.SUMMON_UUID).isPresent() && player.getBlockPos() != null) {
                            Entity sword = serverWorld.getEntity(player.getDataTracker().get(FreyrSword.SUMMON_UUID).get());
                            if (sword != null && sword instanceof FreyrSwordEntity) {
                                sword.setPos(player.getX(), player.getY(), player.getZ());
                                ((FreyrSwordEntity)sword).removeEntity();
                            }
                        }
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketRegistry.STATIONARY_FREYR_SWORD, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
                if (serverWorld != null) {
                    for (Entry<?> entry : player.getDataTracker().getAllEntries()) {
                        if (entry.getData() == FreyrSword.SUMMON_UUID && player.getDataTracker().get(FreyrSword.SUMMON_UUID).isPresent() && player.getBlockPos() != null) {
                            Entity entity = serverWorld.getEntity(player.getDataTracker().get(FreyrSword.SUMMON_UUID).get());
                            if (entity != null && entity instanceof FreyrSwordEntity) {
                                FreyrSwordEntity sword = ((FreyrSwordEntity)entity);
                                sword.setStationaryPos(player.getBlockPos());
                            }
                        }
                    }
                }
            });
        });
    }
}
