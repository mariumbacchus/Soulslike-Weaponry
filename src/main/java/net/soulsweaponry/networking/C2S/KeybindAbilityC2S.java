package net.soulsweaponry.networking.C2S;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.util.IKeybindAbility;

public class KeybindAbilityC2S {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        server.execute(() -> {
            ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
            if (serverWorld != null) {
                for (Hand hand : Hand.values()) {
                    ItemStack stack = player.getStackInHand(hand);
                    if (stack.getItem() instanceof IKeybindAbility keybindItem) {
                        if (stack.getItem() instanceof IConfigDisable configDisable && configDisable.isDisabled(stack)) {
                            return;
                        }
                        keybindItem.useKeybindAbilityServer(serverWorld, stack, player);
                    }
                }
                for (ItemStack armorStack : player.getArmorItems()) {
                    if (armorStack.getItem() instanceof IKeybindAbility abilityItem) {
                        if (armorStack.getItem() instanceof IConfigDisable configDisable && configDisable.isDisabled(armorStack)) {
                            return;
                        }
                        abilityItem.useKeybindAbilityServer(serverWorld, armorStack, player);
                    }
                }
            }
        });
    }
}
