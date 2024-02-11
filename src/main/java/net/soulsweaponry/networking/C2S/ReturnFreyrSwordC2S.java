package net.soulsweaponry.networking.C2S;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.items.FreyrSword;

import java.util.Optional;
import java.util.UUID;

public class ReturnFreyrSwordC2S {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        server.execute(() -> {
            ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
            if (serverWorld != null) {
                //NOTE: reading bufs does not work for some reason
                try {
                    Optional<UUID> op = player.getDataTracker().get(FreyrSword.SUMMON_UUID);
                    if (op.isPresent() && player.getBlockPos() != null) {
                        Entity sword = serverWorld.getEntity(op.get());
                        if (sword instanceof FreyrSwordEntity) {
                            if (!((FreyrSwordEntity)sword).insertStack(player)) {
                                sword.setPos(player.getX(), player.getEyeY(), player.getZ());
                                ((FreyrSwordEntity)sword).dropStack();
                            }
                            sword.discard();
                        } else {
                            player.sendMessage(new LiteralText("There is no Freyr Sword bound to you!"), true);
                        }
                    }
                } catch (Exception e) {
                    player.sendMessage(new LiteralText("There is no Freyr Sword bound to you!"), true);
                }
            }
        });
    }
}
