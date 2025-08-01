package net.soulsweaponry.networking.C2S;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.util.IKeybindAbility;

public record KeybindAbilityC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "keybind_ability");
    public static final CustomPayload.Id<KeybindAbilityC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, KeybindAbilityC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new KeybindAbilityC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(KeybindAbilityC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
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
        });
    }
}