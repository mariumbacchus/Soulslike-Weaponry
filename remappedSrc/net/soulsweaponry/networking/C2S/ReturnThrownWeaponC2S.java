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
import net.soulsweaponry.entity.projectile.ReturningProjectile;

import java.util.Optional;
import java.util.UUID;

public class ReturnThrownWeaponC2S {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        server.execute(() -> {
            ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.method_48926()).orNull();
            if (serverWorld != null) {
                try {
                    Optional<UUID> op = player.getDataTracker().get(ReturningProjectile.THROWN_WEAPON_OPT);
                    if (op.isPresent()) {
                        Entity entity = serverWorld.getEntity(op.get());
                        if (entity instanceof ReturningProjectile projectile) {
                            projectile.setShouldReturn(true);
                        } else {
                            player.sendMessage(Text.literal("There is no 'Soulbound' weapon bound to you!"), true);
                        }
                    }
                } catch (Exception e) {
                    player.sendMessage(Text.literal("There is no 'Soulbound' weapon bound to you!"), true);
                }
            }
        });
    }
}