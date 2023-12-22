package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.util.ParticleHandler;

import java.util.function.Supplier;

public class ParticleOutburstS2C {

    private final double x;
    private final double y;
    private final double z;
    private final int amount;
    private final ParticleOptions particle;
    private final Vec3 velDividers;
    private final float sizeMod;

    public ParticleOutburstS2C(int amount, double x, double y, double z, ParticleOptions particle, Vec3 velDividers, float sizeMod) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.amount = amount;
        this.particle = particle;
        this.velDividers = velDividers;
        this.sizeMod = sizeMod;
    }

    // Same as encode
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(Registry.PARTICLE_TYPE.getId(this.particle.getType()));
        buf.writeInt(this.amount);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeDouble(this.velDividers.x);
        buf.writeDouble(this.velDividers.y);
        buf.writeDouble(this.velDividers.z);
        buf.writeFloat(this.sizeMod);
        this.particle.writeToNetwork(buf);
    }

    //Same as decode/fromBytes
    public ParticleOutburstS2C(FriendlyByteBuf buf) {
        ParticleType<?> particletype = Registry.PARTICLE_TYPE.byId(buf.readInt());
        this.particle = this.readParticle(buf, particletype);
        this.amount = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.velDividers = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.sizeMod = buf.readFloat();
    }

    public int getAmount() {
        return this.amount;
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

    private <T extends ParticleOptions> T readParticle(FriendlyByteBuf pBuffer, ParticleType<T> pParticleType) {
        return pParticleType.getDeserializer().fromNetwork(pParticleType, pBuffer);
    }

    public ParticleOptions getParticle() {
        return this.particle;
    }

    public Vec3 getVelDividers() {
        return this.velDividers;
    }

    public float getSizeMod() {
        return this.sizeMod;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientLevel level = Minecraft.getInstance().level;
            this.handlePacket(level, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientLevel level, ParticleOutburstS2C packet) {
        ParticleHandler.particleOutburst(level, packet.getAmount(), packet.getX(), packet.getY(), packet.getZ(), packet.getParticle(), packet.getVelDividers(), packet.getSizeMod());
    }
}
