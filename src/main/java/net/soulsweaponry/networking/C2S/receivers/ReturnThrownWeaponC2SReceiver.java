package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.ReturningProjectile;
import net.soulsweaponry.entitydata.ReturningProjectileData;
import net.soulsweaponry.networking.C2S.packets.ReturnThrownWeaponC2S;

import java.util.UUID;

public class ReturnThrownWeaponC2SReceiver {

    public static void receive(ReturnThrownWeaponC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            UUID uuid = ReturningProjectileData.getReturningProjectileUuid(player);
            Text text = Text.translatable("soulsweapons.weapon.no_soulbound_weapon");
            if (uuid != null) {
                Entity entity = serverWorld.getEntity(uuid);
                if (entity instanceof ReturningProjectile projectile) {
                    if (!projectile.shouldReturn()) {
                        serverWorld.playSound(null, player.getBlockPos(), SoundEvents.ITEM_TRIDENT_RETURN, SoundCategory.PLAYERS, 1f, 1f);
                    }
                    projectile.setShouldReturn(true);
                } else if (ConfigConstructor.inform_player_about_no_soulbound_thrown_weapon) {
                    player.sendMessage(text, true);
                }
            } else if (ConfigConstructor.inform_player_about_no_soulbound_thrown_weapon) {
                player.sendMessage(text, true);
            }
        });
    }
}
