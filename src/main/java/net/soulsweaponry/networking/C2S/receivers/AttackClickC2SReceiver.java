package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.networking.C2S.packets.AttackClickC2S;

public class AttackClickC2SReceiver {

    public static void receive(AttackClickC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            // Can only attack with main hand
            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
            if (stack.getItem() instanceof IHasAbilities hasAbilities) {
                hasAbilities.onAttackClickServer(serverWorld, stack, player);
            }
        });
    }
}
