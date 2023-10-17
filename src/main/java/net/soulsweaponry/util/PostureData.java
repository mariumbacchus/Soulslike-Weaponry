package net.soulsweaponry.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.PacketRegistry;

public class PostureData {

    public static final String POSTURE_ID = "posture";

    public static int addPosture(IEntityDataSaver entity, int amount) {
        NbtCompound nbt = entity.getPersistentData();
        int posture = nbt.getInt(POSTURE_ID);
        if (posture < 0) {
            posture = 0;
        } else {
            posture += amount;
        }
        nbt.putInt(POSTURE_ID, posture);
        if (entity instanceof ServerPlayerEntity) {
            syncData(posture, (ServerPlayerEntity) entity);
        }
        return posture;
    }

    public static int getPosture(LivingEntity entity) {
        return ((IEntityDataSaver)entity).getPersistentData().getInt(POSTURE_ID);
    }

    public static int reducePosture(IEntityDataSaver entity, int amount) {
        return addPosture(entity, -amount);
    }

    public static int setPosture(IEntityDataSaver entity, int amount) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putInt(POSTURE_ID, amount);
        if (entity instanceof ServerPlayerEntity) {
            syncData(amount, (ServerPlayerEntity) entity);
        }
        return amount;
    }

    public static void syncData(int data, ServerPlayerEntity entity) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(data);
        ServerPlayNetworking.send(entity, PacketRegistry.POSTURE, buf);
    }
}