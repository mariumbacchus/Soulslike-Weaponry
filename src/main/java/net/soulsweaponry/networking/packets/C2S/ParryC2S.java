package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.util.ModTags;

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
        ItemStack stack = player.getStackInHand(Hand.OFF_HAND);
        if (ConfigConstructor.enable_shield_parry && stack.isIn(ModTags.Items.SHIELDS) && !player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            ParryData.setParryFrames(player, 1);
            player.getItemCooldownManager().set(stack.getItem(), player.isCreative() ? 10 : ConfigConstructor.shield_parry_cooldown);
        }
    }
}
