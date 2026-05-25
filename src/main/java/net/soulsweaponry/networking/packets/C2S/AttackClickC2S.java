package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.items.abilities.IHasAbilities;

import java.util.function.Supplier;

public class AttackClickC2S {

    public AttackClickC2S() {

    }

    //Same as decode
    public AttackClickC2S(PacketByteBuf buf) {

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

    private void handlePacket(ServerPlayerEntity player, AttackClickC2S packet) {
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
        if (stack.getItem() instanceof IHasAbilities hasAbilities) {
            hasAbilities.onAttackClickServer(player.getServerWorld(), stack, player);
        }
    }
}
