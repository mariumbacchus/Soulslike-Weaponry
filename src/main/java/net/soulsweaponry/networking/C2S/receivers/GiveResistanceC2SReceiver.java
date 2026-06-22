package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.C2S.packets.GiveResistanceC2S;

public class GiveResistanceC2SReceiver {

    public static void receive(GiveResistanceC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        server.execute(() -> {
            if (player.hasStatusEffect(StatusEffects.RESISTANCE)) {
                if (player.hasStatusEffect(StatusEffects.SATURATION)) {
                    player.removeStatusEffect(StatusEffects.SATURATION);
                }
                player.removeStatusEffect(StatusEffects.RESISTANCE);
            } else {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400000, 30, false, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 400000, 30, false, true));
            }
        });
    }
}
