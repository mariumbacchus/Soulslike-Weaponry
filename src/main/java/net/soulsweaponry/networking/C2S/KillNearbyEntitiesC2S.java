package net.soulsweaponry.networking.C2S;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record KillNearbyEntitiesC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "kill_nearby_entities");
    public static final CustomPayload.Id<KillNearbyEntitiesC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, KillNearbyEntitiesC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new KillNearbyEntitiesC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(KillNearbyEntitiesC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            for (Entity entity : serverWorld.getOtherEntities(player, player.getBoundingBox().expand(100D))) {
                entity.kill();
            }
        });
    }
}
