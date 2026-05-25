package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FlashParticleS2C {

    private final double x;
    private final double y;
    private final double z;
    private final int color;
    private final float expansion;

    public FlashParticleS2C(double x, double y, double z, int color, float expansion) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.expansion = expansion;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeInt(this.color);
        buf.writeFloat(this.expansion);
    }

    //Same as decode/fromBytes
    public FlashParticleS2C(PacketByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.color = buf.readInt();
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

    public int getColor() {
        return color;
    }

    public float getExpansion() {
        return this.expansion;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, FlashParticleS2C packet) {
        int color = packet.getColor();
        float red = ((color >> 16) & 0xFF) / 255f;
        float green = ((color >> 8) & 0xFF) / 255f;
        float blue = (color & 0xFF) / 255f;
        Particle flash = MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.FLASH, packet.getX(), packet.getY(), packet.getZ(), 0, 0, 0);
        flash.setBoundingBox(new Box(BlockPos.ofFloored(x, y, z)).expand(packet.getExpansion()));
        flash.setColor(red, green, blue);
    }
}
