package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.otherkeybind.Parry;
import net.soulsweaponry.util.ModTags;

import java.util.Optional;
import java.util.function.Supplier;

public class ParryC2S {

    public ParryC2S() {

    }

    //Same as decode
    public ParryC2S(PacketByteBuf buf) {

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

    private void handlePacket(ServerPlayerEntity player, ParryC2S packet) {
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
    }
}
