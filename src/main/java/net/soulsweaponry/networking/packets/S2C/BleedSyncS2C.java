package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.client.entitydata.ClientBleedData;

import java.util.function.Supplier;

public class BleedSyncS2C {

    private final int bleed;

    public BleedSyncS2C(int bleed) {
        this.bleed = bleed;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(this.bleed);
    }

    //Same as decode/fromBytes
    public BleedSyncS2C(PacketByteBuf buf) {
        this.bleed = buf.readInt();
    }

    public int getBleed() {
        return this.bleed;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, BleedSyncS2C packet) {
        ClientBleedData.setBleed(packet.getBleed());
    }
}
