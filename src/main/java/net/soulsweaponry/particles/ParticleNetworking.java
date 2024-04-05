package net.soulsweaponry.particles;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ParticleNetworking {
    
    /**
     * Sends a generic packet with the parameter {@code soulFlame} being whether
     * some particles should be replaced with {@code SOUL_FLAME} or not.
     */
    public static void sendServerParticlePacket(ServerWorld world, Identifier packetId, BlockPos blockPos, boolean soulFlame) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(blockPos);
        buf.writeBoolean(soulFlame);
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, packetId, buf);
        }
    }

    /**
     * Sends a generic packet with the parameters {@code points} being numbers of particles.
     */
    public static void sendServerParticlePacket(ServerWorld world, Identifier packetId, BlockPos blockPos, int points) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(blockPos);
        buf.writeInt(points);
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, packetId, buf);
        }
    }

    /**
     * Sends a generic packet that will spawn particles based on the given {@code packetId} on the given area {@code blockPos}.
     */
    public static void sendServerParticlePacket(ServerWorld world, Identifier packetId, BlockPos blockPos) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(blockPos);
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, packetId, buf);
        }
    }

    /**
     * Summon particles at a more specific location.
     * <p>
     * Used in {@link net.soulsweaponry.entity.effect.Freezing} for a constant stream of snow particles to spawn based on the targets
     * {@code width} and {@code height}, but {@link net.soulsweaponry.entity.ai.goal.ReturningKnightGoal} uses it for the 
     * {@code GROUND_RUPTURE_ID} to position an erruption closer to the target. Having {@code points} parameter for some
     * reason doesn't work, I am guessing it overloads the buf packet.
     */
    public static void specificServerParticlePacket(ServerWorld world, Identifier packetId, BlockPos blockPos, double x, float y) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(blockPos);
        buf.writeDouble(x);
        buf.writeFloat(y);
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, packetId, buf);
        }
    }

    /**
     * Summon particle on target with additional specific {@code double}.
     * <p>
     * Used in {@link net.soulsweaponry.entity.ai.goal.FreyrSwordGoal} to spawn {@code SWEEP_ATTACK} particle at eye height,
     * or in {@link net.soulsweaponry.entity.ai.goal.ReturningKnightGoal} to spawn {@code BLINDING_LIGHT_ID} explosion at the targets
     * eye height.
     */
    public static void specificServerParticlePacket(ServerWorld world, Identifier packetId, BlockPos blockPos, double height) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(blockPos);
        buf.writeDouble(height);
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, packetId, buf);
        }
    }
}
