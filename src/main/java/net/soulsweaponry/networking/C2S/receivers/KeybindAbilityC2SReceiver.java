package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.networking.C2S.packets.KeybindAbilityC2S;
import net.soulsweaponry.util.IKeybindAbility;

public class KeybindAbilityC2SReceiver {

    public static void receive(KeybindAbilityC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            //TODO remove these calls below when all abilities have been made
            for (Hand hand : Hand.values()) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.getItem() instanceof IKeybindAbility keybindItem) {
                    if (stack.getItem() instanceof IConfigDisable configDisable && configDisable.isDisabled(stack)) {
                        return;
                    }
                    keybindItem.useKeybindAbilityServer(serverWorld, stack, player);
                    player.stopUsingItem();
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
            //TODO remove above
            for (ItemStack armorStack : player.getArmorItems()) {
                if (armorStack.getItem() instanceof IHasAbilities abilityItem) {
                    if (!abilityItem.isDisabled(armorStack)) {
                        abilityItem.getAbilities().forEach(a -> a.useKeybindAbilityServer(serverWorld, armorStack, player));
                    }
                }
            }
            for (Hand hand : Hand.values()) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.getItem() instanceof IHasAbilities abilityItem) {
                    if (!abilityItem.isDisabled(stack)) {
                        abilityItem.getAbilities().forEach(a -> a.useKeybindAbilityServer(serverWorld, stack, player));
                    }
                }
            }
        });
    }
}
