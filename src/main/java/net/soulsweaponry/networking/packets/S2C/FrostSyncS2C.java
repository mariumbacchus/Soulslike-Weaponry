package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.client.entitydata.ClientFrostData;

import java.util.function.Supplier;

public class FrostSyncS2C {

    private final int frostValue;
    private final boolean frostCoolingDown;

    public FrostSyncS2C(int frostValue, boolean frostCoolingDown) {
        this.frostValue = frostValue;
        this.frostCoolingDown = frostCoolingDown;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(this.frostValue);
        buf.writeBoolean(this.frostCoolingDown);
    }

    //Same as decode/fromBytes
    public FrostSyncS2C(PacketByteBuf buf) {
        this.frostValue = buf.readInt();
        this.frostCoolingDown = buf.readBoolean();
    }

    public int getFrostValue() {
        return frostValue;
    }

    public boolean isFrostCoolingDown() {
        return frostCoolingDown;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, FrostSyncS2C packet) {
        ClientFrostData.setFrostValue(packet.getFrostValue());
        ClientFrostData.setFrostCoolingDown(packet.isFrostCoolingDown());
    }
}
