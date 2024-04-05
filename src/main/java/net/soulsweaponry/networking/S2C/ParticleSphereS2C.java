package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.soulsweaponry.particles.ParticleHandler;

public class ParticleSphereS2C {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        Identifier particleId = buf.readIdentifier();
        ParticleType<?> particle = Registries.PARTICLE_TYPE.get(particleId);
        int amount = buf.readInt();
        ItemStack item = buf.readItemStack();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        float sizeMod = buf.readFloat();
        client.execute(() -> handle(client.world, buf, particle, amount, item, x, y, z, sizeMod));
    }

    private static ItemStackParticleEffect getItemParticleEffect(ItemStack item) {
        return new ItemStackParticleEffect(ParticleTypes.ITEM, item);
    }

    private static <T extends ParticleEffect> T readParticle(ParticleType<T> particle, PacketByteBuf buf) {
        return particle.getParametersFactory().read(particle, buf);
    }

    private static void handle(ClientWorld world, PacketByteBuf buf, ParticleType<?> particle, int amount, ItemStack item, double x, double y, double z, float sizeMod) {
        if (!item.isOf(Items.AIR)) {
            ParticleHandler.particleSphere(world, amount, x, y, z, getItemParticleEffect(item), sizeMod);
        } else {
            ParticleHandler.particleSphere(world, amount, x, y, z, readParticle(particle, buf), sizeMod);
        }
    }
}