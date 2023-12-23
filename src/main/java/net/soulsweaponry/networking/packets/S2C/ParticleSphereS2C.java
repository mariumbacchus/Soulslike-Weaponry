package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.util.ParticleHandler;

import java.util.function.Supplier;

public class ParticleSphereS2C {

    private final double x;
    private final double y;
    private final double z;
    private final int amount;
    private final ParticleOptions particle;
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
    public ParticleSphereS2C(int amount, double x, double y, double z, ParticleOptions particle, float sizeMod, ItemStack item) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.amount = amount;
        this.item = item;
        this.particle = particle;
        this.sizeMod = sizeMod;
    }

    // Same as encode
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(Registry.PARTICLE_TYPE.getId(this.particle.getType()));
        buf.writeInt(this.amount);
        buf.writeItem(this.item);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.sizeMod);
        this.particle.writeToNetwork(buf);
    }

    //Same as decode/fromBytes
    public ParticleSphereS2C(FriendlyByteBuf buf) {
        ParticleType<?> particletype = Registry.PARTICLE_TYPE.byId(buf.readInt());
        this.particle = this.readParticle(buf, particletype);
        this.amount = buf.readInt();
        this.item = buf.readItem();
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

    private <T extends ParticleOptions> T readParticle(FriendlyByteBuf pBuffer, ParticleType<T> pParticleType) {
        return pParticleType.getDeserializer().fromNetwork(pParticleType, pBuffer);
    }

    private ItemParticleOption getItemParticleOption() {
        return new ItemParticleOption(ParticleTypes.ITEM, this.item);
    }

    private ItemStack getItemStack() {
        return this.item;
    }

    public ParticleOptions getParticle() {
        return this.particle;
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

    private void handlePacket(ClientLevel level, ParticleSphereS2C packet) {
        if (!packet.getItemStack().is(Items.AIR)) {
            ParticleHandler.particleSphere(level, packet.getAmount(), packet.getX(), packet.getY(), packet.getZ(), packet.getItemParticleOption(), packet.getSizeMod());
        } else {
            ParticleHandler.particleSphere(level, packet.getAmount(), packet.getX(), packet.getY(), packet.getZ(), packet.getParticle(), packet.getSizeMod());
        }
    }
}
