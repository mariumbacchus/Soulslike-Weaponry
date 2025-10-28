package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.networking.C2S.packets.KeybindAbilityC2S;

public class KeybindAbilityC2SReceiver {

    public static void receive(KeybindAbilityC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            for (ItemStack armorStack : player.getArmorItems()) {
                if (armorStack.getItem() instanceof IHasAbilities abilityItem) {
                    abilityItem.useKeybindAbilityServer(serverWorld, armorStack, player, null);
                }
            }
            for (Hand hand : Hand.values()) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.getItem() instanceof IHasAbilities abilityItem) {
                    abilityItem.useKeybindAbilityServer(serverWorld, stack, player, hand);
                }
            }
        });
    }
}
