package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.particles.ChainLightningHandler;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ChainLightningS2C {

    private final Vector3f from;
    private final Vector3f to;

    public ChainLightningS2C(Vector3f from, Vector3f to) {
        this.from = from;
        this.to = to;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeVector3f(from);
        buf.writeVector3f(to);
    }

    //Same as decode/fromBytes
    public ChainLightningS2C(PacketByteBuf buf) {
        this.from = buf.readVector3f();
        this.to = buf.readVector3f();
    }

    public Vector3f getFrom() {
        return this.from;
    }

    public Vector3f getTo() {
        return this.to;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, ChainLightningS2C packet) {
        ChainLightningHandler.spawnChainLightning(world, packet.getFrom(), packet.getTo());
    }
}
