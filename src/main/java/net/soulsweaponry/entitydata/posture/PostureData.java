package net.soulsweaponry.entitydata.posture;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.PostureSyncS2C;

public class PostureData {

    public static final String POSTURE_ID = "posture";

    public static int addPosture(LivingEntity entity, int amount) {
        NbtCompound nbt = entity.getPersistentData();
        if (!nbt.contains(POSTURE_ID)) {
            nbt.putInt(POSTURE_ID, 0);
        }
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
        if (!entity.getPersistentData().contains(POSTURE_ID)) {
            entity.getPersistentData().putInt(POSTURE_ID, 0);
        }
        return entity.getPersistentData().getInt(POSTURE_ID);
    }

    public static int reducePosture(LivingEntity entity, int amount) {
        return addPosture(entity, -amount);
    }

    public static int setPosture(LivingEntity entity, int amount) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putInt(POSTURE_ID, amount);
        if (entity instanceof ServerPlayerEntity) {
            syncData(amount, (ServerPlayerEntity) entity);
        }
        return amount;
    }

    public static void syncData(int posture, ServerPlayerEntity entity) {
        ModMessages.sendToPlayer(new PostureSyncS2C(posture), entity);
    }

    // Forgot that player capabilities don't apply to entities
    // Commented it all out as a reference for possible later capability instances
    /*private int posture;

    public int getPosture() {
        return posture;
    }

    public void setPosture(int posture) {
        this.posture = posture;
    }

    public void addPosture(int amount) {
        if (this.posture < 0) {
            this.posture = 0;
        } else {
            this.posture += amount;
        }
    }

    public void reducePosture(int amount) {
        this.addPosture(- amount);
    }

    public void copyFrom(PostureData data) {
        this.posture = data.posture;
    }

    public void saveNBTData(NbtCompound nbt) {
        nbt.putInt(POSTURE_ID, this.posture);
    }

    public void loadNBTData(NbtCompound nbt) {
        this.posture = nbt.getInt(POSTURE_ID);
    }

    public static void addPosture(LivingEntity user, int amount) {
        user.getCapability(PostureDataProvider.POSTURE_DATA).ifPresent(data -> {
            data.addPosture(amount);
            if (user instanceof ServerPlayerEntity player) {
                ModMessages.sendToPlayer(new PostureSyncS2C(data.getPosture()), player);
            }
        });
    }

    public static int getPosture(LivingEntity user) {
        return user.getCapability(PostureDataProvider.POSTURE_DATA).map(PostureData::getPosture).orElse(0);
    }

    public static void reducePosture(LivingEntity user, int amount) {
        user.getCapability(PostureDataProvider.POSTURE_DATA).ifPresent(data -> {
            data.reducePosture(amount);
            if (user instanceof ServerPlayerEntity player) {
                ModMessages.sendToPlayer(new PostureSyncS2C(data.getPosture()), player);
            }
        });
    }

    public static void setPosture(LivingEntity user, int amount) {
        user.getCapability(PostureDataProvider.POSTURE_DATA).ifPresent(data -> {
            data.setPosture(amount);
            if (user instanceof ServerPlayerEntity player) {
                ModMessages.sendToPlayer(new PostureSyncS2C(data.getPosture()), player);
            }
        });
    }*/
}