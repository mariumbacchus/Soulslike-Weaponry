package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.particles.ParticleHandler;

import java.util.function.Supplier;

public class ParticleSphereS2C {

    private final double x;
    private final double y;
    private final double z;
    private final int amount;
    private final ParticleEffect particle;
    private final float sizeMod;
    private final ItemStack item;

    /**
     * Server to client packet that will result in a sphere forming with the given particle.
     * @param amount amount of particles
     * @param x x position
     * @param y y position
     * @param z z position
     * @param particle particle type, i.e FLAME, SMOKE etc.
     * @param sizeMod modifies the size of the whole particle event
     * @param item for when ItemParticleOption is used, will pass in the item that should be rendered to the ItemParticleOption
     */
    public ParticleSphereS2C(int amount, double x, double y, double z, ParticleEffect particle, float sizeMod, ItemStack item) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.amount = amount;
        this.item = item;
        this.particle = particle;
        this.sizeMod = sizeMod;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(Registry.PARTICLE_TYPE.getRawId(this.particle.getType()));
        buf.writeInt(this.amount);
        buf.writeItemStack(this.item);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.sizeMod);
        this.particle.write(buf);
    }

    //Same as decode/fromBytes
    public ParticleSphereS2C(PacketByteBuf buf) {
        ParticleType<?> particletype = Registry.PARTICLE_TYPE.get(buf.readInt());
        this.particle = this.readParticle(buf, particletype);
        this.amount = buf.readInt();
        this.item = buf.readItemStack();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
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

    private ItemStackParticleEffect getItemParticleOption() {
        return new ItemStackParticleEffect(ParticleTypes.ITEM, this.item);
    }

    private ItemStack getItemStack() {
        return this.item;
    }

    public ParticleEffect getParticle() {
        return this.particle;
    }

    public float getSizeMod() {
        return this.sizeMod;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, ParticleSphereS2C packet) {
        if (!packet.getItemStack().isOf(Items.AIR)) {
            ParticleHandler.particleSphere(world, packet.getAmount(), packet.getX(), packet.getY(), packet.getZ(), packet.getItemParticleOption(), packet.getSizeMod());
        } else {
            ParticleHandler.particleSphere(world, packet.getAmount(), packet.getX(), packet.getY(), packet.getZ(), packet.getParticle(), packet.getSizeMod());
        }
    }
}
