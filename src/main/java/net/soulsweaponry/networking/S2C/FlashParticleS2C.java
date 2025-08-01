package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.soulsweaponry.SoulsWeaponry;

public record FlashParticleS2C(double x, double y, double z, int color, float sizeMod) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "flash_particle");
    public static final CustomPayload.Id<FlashParticleS2C> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, FlashParticleS2C> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.DOUBLE.cast(), FlashParticleS2C::x,
                    PacketCodecs.DOUBLE.cast(), FlashParticleS2C::y,
                    PacketCodecs.DOUBLE.cast(), FlashParticleS2C::z,
                    PacketCodecs.INTEGER.cast(), FlashParticleS2C::color,
                    PacketCodecs.FLOAT.cast(),   FlashParticleS2C::sizeMod,
                    FlashParticleS2C::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(FlashParticleS2C packet, ClientPlayNetworking.Context ctx) {
        MinecraftClient client = ctx.client();
        if (client.world == null) {
            return;
        }
        client.execute(() -> {
            double x = packet.x();
            double y = packet.y();
            double z = packet.z();
            int argb = packet.color();
            float sizeMod = packet.sizeMod();
            float red = ((argb >> 16) & 0xFF) / 255f;
            float green = ((argb >> 8) & 0xFF) / 255f;
            float blue = (argb & 0xFF) / 255f;
            Particle flash = client.particleManager.addParticle(ParticleTypes.FLASH, x, y, z, 0, 0, 0);
            flash.setBoundingBox(new Box(BlockPos.ofFloored(x, y, z)).expand(sizeMod));
            flash.setColor(red, green, blue);
        });
    }
}