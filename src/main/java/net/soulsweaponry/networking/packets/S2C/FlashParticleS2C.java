package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.util.ParticleHandler;

import java.util.function.Supplier;

public class FlashParticleS2C {

    private final double x;
    private final double y;
    private final double z;
    private final ParticleHandler.RGB rgb;
    private final float expansion;

    public FlashParticleS2C(double x, double y, double z, ParticleHandler.RGB rgb, float expansion) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rgb = rgb;
        this.expansion = expansion;
    }

    // Same as encode
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.rgb.getRed());
        buf.writeFloat(this.rgb.getGreen());
        buf.writeFloat(this.rgb.getBlue());
        buf.writeFloat(this.expansion);
    }

    //Same as decode/fromBytes
    public FlashParticleS2C(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.rgb = new ParticleHandler.RGB(buf.readFloat(), buf.readFloat(), buf.readFloat());
        this.expansion = buf.readFloat();
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public ParticleHandler.RGB getRgb() {
        return this.rgb;
    }

    public float getExpansion() {
        return this.expansion;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientLevel world = Minecraft.getInstance().level;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientLevel world, FlashParticleS2C packet) {
        ParticleHandler.flashParticle(world, packet.getX(), packet.getY(), packet.getZ(), packet.getRgb(), packet.getExpansion());
    }
}
