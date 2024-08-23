package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.particles.ParticleHandler;

import java.util.List;
import java.util.function.Supplier;

public class ParticleOutburstS2C {

    private final double x;
    private final double y;
    private final double z;
    private final int amount;
    private final ParticleEffect particle;
    private final Vec3d velDividers;
    private final float sizeMod;
    private final ItemStack item;

    /**
     * Server to client packet that will result in a sphere forming with the given particle.
     * @param amount amount of particles
     * @param x x position
     * @param y y position
     * @param z z position
     * @param particle particle type, i.e FLAME, SMOKE etc.
     * @param velDividers velocity dividers that will modify the direction the particles go
     * @param sizeMod modifies the size of the whole particle event
     * @param item for when ItemParticleOption is used, will pass in the item that should be rendered to the ItemParticleOption
     */
    public ParticleOutburstS2C(int amount, double x, double y, double z, ParticleEffect particle, Vec3d velDividers, float sizeMod, ItemStack item) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.amount = amount;
        this.item = item;
        this.particle = particle;
        this.velDividers = velDividers;
        this.sizeMod = sizeMod;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(Registries.PARTICLE_TYPE.getRawId(this.particle.getType()));
        buf.writeInt(this.amount);
        buf.writeItemStack(this.item);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeDouble(this.velDividers.x);
        buf.writeDouble(this.velDividers.y);
        buf.writeDouble(this.velDividers.z);
        buf.writeFloat(this.sizeMod);
        this.particle.write(buf);
    }

    //Same as decode/fromBytes
    public ParticleOutburstS2C(PacketByteBuf buf) {
        ParticleType<?> particletype = Registries.PARTICLE_TYPE.get(buf.readInt());
        this.particle = this.readParticle(buf, particletype);
        this.amount = buf.readInt();
        this.item = buf.readItemStack();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.velDividers = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
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

    private <T extends ParticleEffect> T readParticle(PacketByteBuf pBuffer, ParticleType<T> pParticleType) {
        return pParticleType.getParametersFactory().read(pParticleType, pBuffer);
    }

    public ParticleEffect getParticle() {
        return this.particle;
    }

    public Vec3d getVelDividers() {
        return this.velDividers;
    }

    public float getSizeMod() {
        return this.sizeMod;
    }

    private ItemStackParticleEffect getItemParticleOption() {
        return new ItemStackParticleEffect(ParticleTypes.ITEM, this.item);
    }

    private ItemStack getItemStack() {
        return this.item;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, ParticleOutburstS2C packet) {
        ParticleEffect particleEffect;
        if (!packet.getItemStack().isOf(Items.AIR)) {
            particleEffect = packet.getItemParticleOption();
        } else {
            particleEffect = packet.getParticle();
        }
        List<Vec3d> list = ParticleHandler.getParticleOutburstCords(packet.getAmount(), packet.getVelDividers(), packet.getSizeMod());
        for (Vec3d vec : list) {
            world.addParticle(particleEffect, packet.getX(), packet.getY(), packet.getZ(), vec.getX(), vec.getY(), vec.getZ());
        }
    }
}
