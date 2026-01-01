package net.soulsweaponry.entitydata;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.networking.S2C.packets.MaxPostureSyncS2C;
import net.soulsweaponry.networking.S2C.packets.PostureSyncS2C;

public class PostureData {

    public static final String POSTURE_ID = "posture";
    public static final String MAX_POSTURE_ID = "max_posture";

    public static void addPostureLoss(LivingEntity entity, int amount) {
        if (!EntityPosture.isPostureDisabled(entity) && !entity.isDead() && !entity.getWorld().isClient) {
            if (amount > 0) {
                amount = EntityPosture.getPostureLoss(entity, amount);
            }
            addPostureLoss((IEntityDataSaver) entity, amount, EntityPosture.getMaxPostureLoss(entity));
        }
    }

    private static void addPostureLoss(IEntityDataSaver entity, int amount, int max) {
        NbtCompound nbt = entity.getPersistentData();
        if (!nbt.contains(POSTURE_ID)) {
            nbt.putInt(POSTURE_ID, 0);
        }
        int posture = nbt.getInt(POSTURE_ID);
        if (posture < 0) {
            posture = 0;
        } else {
            posture = Math.min(posture + amount, max);
        }
        nbt.putInt(POSTURE_ID, posture);
        if (entity instanceof ServerPlayerEntity) {
            syncData(posture, (ServerPlayerEntity) entity);
        }
    }

    public static int getPosture(LivingEntity entity) {
        IEntityDataSaver target = (IEntityDataSaver)entity;
        if (!target.getPersistentData().contains(POSTURE_ID)) {
            target.getPersistentData().putInt(POSTURE_ID, 0);
        }
        return target.getPersistentData().getInt(POSTURE_ID);
    }

    public static void reducePosture(LivingEntity entity, int amount) {
        addPostureLoss(entity, -amount);
    }

    public static void setPosture(IEntityDataSaver entity, int amount) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putInt(POSTURE_ID, amount);
        if (entity instanceof ServerPlayerEntity) {
            syncData(amount, (ServerPlayerEntity) entity);
        }
    }

    public static void syncData(int data, ServerPlayerEntity entity) {
        ServerPlayNetworking.send(entity, new PostureSyncS2C(data));
    }

    /**
     * NOTE: Only used client side by the posture hud bar.
     * The value is updated server side and then applied to the entity client side
     * when getting the max posture from {@link EntityPosture}.
     */
    public static int getMaxPosture(PlayerEntity entity) {
        IEntityDataSaver target = (IEntityDataSaver)entity;
        if (!target.getPersistentData().contains(MAX_POSTURE_ID)) {
            target.getPersistentData().putInt(MAX_POSTURE_ID, 0);
        }
        return target.getPersistentData().getInt(MAX_POSTURE_ID);
    }

    public static void updateMaxPosture(PlayerEntity entity, int amount) {
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();
        nbt.putInt(MAX_POSTURE_ID, amount);
        if (entity instanceof ServerPlayerEntity) {
            syncMaxPosture(amount, (ServerPlayerEntity) entity);
        }
    }

    public static void syncMaxPosture(int data, ServerPlayerEntity entity) {
        ServerPlayNetworking.send(entity, new MaxPostureSyncS2C(data));
    }
}
