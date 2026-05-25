package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.items.abilities.IHasAbilities;

import java.util.function.Supplier;

public class KeybindAbilityC2S {

    public KeybindAbilityC2S() {

    }

    //Same as decode
    public KeybindAbilityC2S(PacketByteBuf buf) {

    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            this.handlePacket(player, this);
        });
        context.setPacketHandled(true);
    }

    private void handlePacket(ServerPlayerEntity player, KeybindAbilityC2S packet) {
        ServerWorld serverWorld = player.getServerWorld();
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
    }
}
