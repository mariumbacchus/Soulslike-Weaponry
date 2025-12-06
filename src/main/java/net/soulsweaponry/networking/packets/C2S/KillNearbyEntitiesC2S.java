package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KillNearbyEntitiesC2S {

    public KillNearbyEntitiesC2S() {

    }

    //Same as decode
    public KillNearbyEntitiesC2S(PacketByteBuf buf) {

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

    private void handlePacket(ServerPlayerEntity player, KillNearbyEntitiesC2S packet) {
        for (Entity entity : player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(100D))) {
            entity.kill();
        }
    }
}
