package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.client.entitydata.ClientEchoData;

import java.util.function.Supplier;

public class EchoSyncS2C {

    private final float echoDamage;
    private final float savedDamageMod;

    public EchoSyncS2C(float echoDamage, float savedDamageMod) {
        this.echoDamage = echoDamage;
        this.savedDamageMod = savedDamageMod;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeFloat(this.echoDamage);
        buf.writeFloat(this.savedDamageMod);
    }

    //Same as decode/fromBytes
    public EchoSyncS2C(PacketByteBuf buf) {
        this.echoDamage = buf.readFloat();
        this.savedDamageMod = buf.readFloat();
    }

    public float getEchoDamage() {
        return this.echoDamage;
    }

    public float getSavedDamageMod() {
        return this.savedDamageMod;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, EchoSyncS2C packet) {
        ClientEchoData.setEchoDamage(packet.getEchoDamage());
        ClientEchoData.setSavedDamageMod(packet.getSavedDamageMod());
    }
}
