package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.otherkeybind.Parry;
import net.soulsweaponry.networking.C2S.packets.ParryC2S;

import java.util.Optional;

public class ParryC2SReceiver {

    public static void receive(ParryC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        server.execute(() -> {
            for (Hand hand : Hand.values()) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.getItem() instanceof IHasAbilities hasAbilities) {
                    Optional<Parry> op = hasAbilities.findAbility(Parry.class);
                    if (op.isPresent()) {
                        op.get().parry(player, stack);
                        break;
                    }
                }
            }
        });
    }
}
