package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.client.entitydata.ClientUmbralTrespassData;

import java.util.function.Supplier;

public class TicksBeforeDismountSyncS2C {

    private final int ticks;

    public TicksBeforeDismountSyncS2C(int ticks) {
        this.ticks = ticks;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(this.ticks);
    }

    //Same as decode/fromBytes
    public TicksBeforeDismountSyncS2C(PacketByteBuf buf) {
        this.ticks = buf.readInt();
    }

    public int getTicks() {
        return this.ticks;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, TicksBeforeDismountSyncS2C packet) {
        ClientUmbralTrespassData.setTicksBeforeDismount(packet.getTicks());
    }
}
