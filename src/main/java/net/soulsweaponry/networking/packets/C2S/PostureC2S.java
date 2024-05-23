package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.entitydata.posture.PostureData;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.PostureSyncS2C;

import java.util.function.Supplier;

public class PostureC2S {

    public PostureC2S() {

    }

    //Same as decode
    public PostureC2S(PacketByteBuf buf) {

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

    private void handlePacket(ServerPlayerEntity player, PostureC2S packet) {
        ModMessages.sendToPlayer(new PostureSyncS2C(PostureData.getPosture(player)), player);
    }
}
