package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.client.entitydata.ClientTargetPostureData;

import java.util.function.Supplier;

public class TargetPostureSyncS2C {

    private final int posture;
    private final String name;
    private final int maxPosture;

    public TargetPostureSyncS2C(int posture, String name, int maxPosture) {
        this.posture = posture;
        this.name = name;
        this.maxPosture = maxPosture;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(this.posture);
        buf.writeString(this.name);
        buf.writeInt(this.maxPosture);
    }

    //Same as decode/fromBytes
    public TargetPostureSyncS2C(PacketByteBuf buf) {
        this.posture = buf.readInt();
        this.name = buf.readString();
        this.maxPosture = buf.readInt();
    }

    public int getPosture() {
        return this.posture;
    }

    public String getName() {
        return name;
    }

    public int getMaxPosture() {
        return maxPosture;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, TargetPostureSyncS2C packet) {
        ClientTargetPostureData.setTargetPosture(packet.getPosture());
        ClientTargetPostureData.setName(packet.getName());
        ClientTargetPostureData.setMaxPosture(packet.getMaxPosture());
    }
}
