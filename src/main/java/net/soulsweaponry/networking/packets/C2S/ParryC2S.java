package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.item.ShieldItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entitydata.parry.ParryData;

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
        if (CommonConfig.ENABLE_SHIELD_PARRY.get() && player.getStackInHand(Hand.OFF_HAND).getItem() instanceof ShieldItem item && !player.getItemCooldownManager().isCoolingDown(item)) {
            ParryData.setParryFrames(player, 1);
            player.getItemCooldownManager().set(item, player.isCreative() ? 10 : CommonConfig.SHIELD_PARRY_COOLDOWN.get());
        }
    }
}
