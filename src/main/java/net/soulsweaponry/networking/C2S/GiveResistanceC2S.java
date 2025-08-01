package net.soulsweaponry.networking.C2S;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record GiveResistanceC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "give_or_clear_resistance");
    public static final CustomPayload.Id<GiveResistanceC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, GiveResistanceC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new GiveResistanceC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(GiveResistanceC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        server.execute(() -> {
            if (player.hasStatusEffect(StatusEffects.RESISTANCE)) {
                player.removeStatusEffect(StatusEffects.RESISTANCE);
            } else {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400000, 30, false, true));
            }
        });
    }
}
