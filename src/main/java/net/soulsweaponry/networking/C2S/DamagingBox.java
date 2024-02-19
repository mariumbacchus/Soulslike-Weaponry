package net.soulsweaponry.networking.C2S;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class DamagingBox {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        BlockPos blockPos = buf.readBlockPos();
        double expansion = buf.readDouble();
        float damage = buf.readFloat();
        float knockbackStrength = buf.readFloat();
        float knockbackX = buf.readFloat();
        float knockbackZ = buf.readFloat();
        SoundEvent sound = Registries.SOUND_EVENT.get(buf.readIdentifier());
        BlockPos soundPos = buf.readBlockPos();
        server.execute(() -> {
            ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.getWorld()).orNull();
            if (serverWorld != null) {
                for (Entity entity : serverWorld.getOtherEntities(player, new Box(blockPos).expand(expansion))) {
                    if (entity instanceof LivingEntity target) {
                        target.damage(player.getDamageSources().mobAttack(player), damage);
                        if (knockbackX == 0 && knockbackZ == 0) {
                            double x = blockPos.getX() - target.getX();
                            double z = blockPos.getZ() - target.getZ();
                            target.takeKnockback(knockbackStrength, x, z);
                        } else {
                            target.takeKnockback(knockbackStrength, knockbackX, knockbackZ);
                        }
                    }
                }
                serverWorld.playSound(null, soundPos, sound, SoundCategory.PLAYERS, 1f, 1f);
            }
        });
    }
}