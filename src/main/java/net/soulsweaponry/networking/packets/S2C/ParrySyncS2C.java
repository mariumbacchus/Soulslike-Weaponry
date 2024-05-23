package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.client.entitydata.ClientParryData;
import net.soulsweaponry.entitydata.ParryData;

import java.util.function.Supplier;

public class ParrySyncS2C {

    private final int parryFrames;

    public ParrySyncS2C(int parryFrames) {
        this.parryFrames = parryFrames;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(this.parryFrames);
    }

    //Same as decode/fromBytes
    public ParrySyncS2C(PacketByteBuf buf) {
        this.parryFrames = buf.readInt();
    }

    public int getParryFrames() {
        return this.parryFrames;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, ParrySyncS2C packet) {
        ClientParryData.setParryFrames(packet.getParryFrames());
        //MinecraftClient.getInstance().player.getPersistentData().putInt(ParryData.PARRY_FRAMES_ID, packet.getParryFrames());
    }
}
