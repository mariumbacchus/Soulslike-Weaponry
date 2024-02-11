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
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SingleParticleS2C {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        Identifier particleId = buf.readIdentifier();
        ParticleType<?> particle = Registry.PARTICLE_TYPE.get(particleId);
        ItemStack item = buf.readItemStack();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        double velX = buf.readDouble();
        double velY = buf.readDouble();
        double velZ = buf.readDouble();
        client.execute(() -> handle(client.world, buf, particle, item, x, y, z, velX, velY, velZ));
    }

    private static ItemStackParticleEffect getItemParticleEffect(ItemStack item) {
        return new ItemStackParticleEffect(ParticleTypes.ITEM, item);
    }

    private static <T extends ParticleEffect> T readParticle(ParticleType<T> particle, PacketByteBuf buf) {
        return particle.getParametersFactory().read(particle, buf);
    }

    private static void handle(ClientWorld world, PacketByteBuf buf, ParticleType<?> particle, ItemStack item, double x, double y, double z, double velX, double velY, double velZ) {
        if (!item.isOf(Items.AIR)) {
            world.addParticle(getItemParticleEffect(item), x, y, z, velX, velY, velZ);
        } else {
            world.addParticle(readParticle(particle, buf), x, y, z, velX, velY, velZ);
        }
    }
}
