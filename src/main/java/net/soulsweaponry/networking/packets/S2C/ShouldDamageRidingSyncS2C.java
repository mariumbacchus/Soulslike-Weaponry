package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.client.entitydata.ClientUmbralTrespassData;

import java.util.function.Supplier;

public class ShouldDamageRidingSyncS2C {

    private final boolean shouldDamage;

    public ShouldDamageRidingSyncS2C(boolean shouldDamage) {
        this.shouldDamage = shouldDamage;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeBoolean(this.shouldDamage);
    }

    //Same as decode/fromBytes
    public ShouldDamageRidingSyncS2C(PacketByteBuf buf) {
        this.shouldDamage = buf.readBoolean();
    }

    public boolean shouldDamageRiding() {
        return this.shouldDamage;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, ShouldDamageRidingSyncS2C packet) {
        ClientUmbralTrespassData.setShouldDamageRiding(packet.shouldDamageRiding());
    }
}
