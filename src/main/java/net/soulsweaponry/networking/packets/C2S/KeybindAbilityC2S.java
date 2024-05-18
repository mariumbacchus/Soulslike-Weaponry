package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.util.IKeybindAbility;

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
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof IKeybindAbility keybindItem) {
                keybindItem.useKeybindAbilityServer(player.getWorld(), stack, player);
            }
        }
        for (ItemStack armorStack : player.getArmorItems()) {
            if (armorStack.getItem() instanceof IKeybindAbility abilityItem) {
                abilityItem.useKeybindAbilityServer(player.getWorld(), armorStack, player);
            }
        }
    }
}
