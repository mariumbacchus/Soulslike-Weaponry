package net.soulsweaponry.networking.C2S;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.ReturningProjectile;
import net.soulsweaponry.entitydata.ReturningProjectileData;

import java.util.UUID;

public record ReturnThrownWeaponC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "try_return_thrown_weapons");
    public static final CustomPayload.Id<ReturnThrownWeaponC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ReturnThrownWeaponC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new ReturnThrownWeaponC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

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