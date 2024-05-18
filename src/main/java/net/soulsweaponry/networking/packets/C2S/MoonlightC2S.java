package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.items.MoonlightShortsword;

import java.util.function.Supplier;

public class MoonlightC2S {

    public MoonlightC2S() {

    }

    //Same as decode
    public MoonlightC2S(PacketByteBuf buf) {

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

    private void handlePacket(ServerPlayerEntity player, MoonlightC2S packet) {
        MoonlightShortsword.summonSmallProjectile(player.getWorld(), player);
    }
}
