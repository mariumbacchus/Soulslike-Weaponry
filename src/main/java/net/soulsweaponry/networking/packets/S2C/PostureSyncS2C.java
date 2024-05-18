package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.client.entitydata.ClientPostureData;

import java.util.function.Supplier;

public class PostureSyncS2C {

    private final int posture;

    public PostureSyncS2C(int posture) {
        this.posture = posture;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(this.posture);
    }

    //Same as decode/fromBytes
    public PostureSyncS2C(PacketByteBuf buf) {
        this.posture = buf.readInt();
    }

    public int getPosture() {
        return this.posture;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, PostureSyncS2C packet) {
        ClientPostureData.setPosture(packet.getPosture());
    }
}
