package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.client.entitydata.ClientParryData;

import java.util.function.Supplier;

public class ParrySyncS2C {

    private final int parryTicks;
    private final int parryFrames;
    private final int maxParryTicks;

    public ParrySyncS2C(int parryTicks, int parryFrames, int maxParryTicks) {
        this.parryTicks = parryTicks;
        this.parryFrames = parryFrames;
        this.maxParryTicks = maxParryTicks;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(this.parryTicks);
        buf.writeInt(this.parryFrames);
        buf.writeInt(this.maxParryTicks);
    }

    //Same as decode/fromBytes
    public ParrySyncS2C(PacketByteBuf buf) {
        this.parryTicks = buf.readInt();
        this.parryFrames = buf.readInt();
        this.maxParryTicks = buf.readInt();
    }

    public int getParryTicks() {
        return parryTicks;
    }

    public int getParryFrames() {
        return parryFrames;
    }

    public int getMaxParryTicks() {
        return maxParryTicks;
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
        ClientParryData.setParryTicks(packet.getParryTicks());
        ClientParryData.setParryFrames(packet.getParryFrames());
        ClientParryData.setMaxParryTicks(packet.getMaxParryTicks());
        //MinecraftClient.getInstance().player.getPersistentData().putInt(ParryData.PARRY_FRAMES_ID, packet.getParryFrames());
    }
}
