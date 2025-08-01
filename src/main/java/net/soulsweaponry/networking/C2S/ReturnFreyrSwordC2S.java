package net.soulsweaponry.networking.C2S;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.entitydata.FreyrSwordSummonData;

import java.util.UUID;

public record ReturnFreyrSwordC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "return_freyr_return");
    public static final CustomPayload.Id<ReturnFreyrSwordC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ReturnFreyrSwordC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new ReturnFreyrSwordC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

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
                } else if (ConfigConstructor.inform_player_about_no_bound_freyr_sword) {
                    player.sendMessage(text, true);
                }
            }
        });
    }
}