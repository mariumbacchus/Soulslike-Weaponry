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
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.util.ParticleHandler;

public class ParticleOutburstS2C {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        Identifier particleId = buf.readIdentifier();
        ParticleType<?> particle = Registries.PARTICLE_TYPE.get(particleId);
        int amount = buf.readInt();
        ItemStack item = buf.readItemStack();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        double dividerX = buf.readDouble();
        double dividerY = buf.readDouble();
        double dividerZ = buf.readDouble();
        float sizeMod = buf.readFloat();
        client.execute(() -> handle(client.world, buf, particle, amount, item, x, y, z, new Vec3d(dividerX, dividerY, dividerZ), sizeMod));
    }

    private static ItemStackParticleEffect getItemParticleEffect(ItemStack item) {
        return new ItemStackParticleEffect(ParticleTypes.ITEM, item);
    }

    private static <T extends ParticleEffect> T readParticle(ParticleType<T> particle, PacketByteBuf buf) {
        return particle.getParametersFactory().read(particle, buf);
    }

    private static void handle(ClientWorld world, PacketByteBuf buf, ParticleType<?> particle, int amount, ItemStack item, double x, double y, double z, Vec3d velDividers, float sizeMod) {
        if (!item.isOf(Items.AIR)) {
            ParticleHandler.particleOutburst(world, amount, x, y, z, getItemParticleEffect(item), velDividers, sizeMod);
        } else {
            ParticleHandler.particleOutburst(world, amount, x, y, z, readParticle(particle, buf), velDividers, sizeMod);
        }
    }
}