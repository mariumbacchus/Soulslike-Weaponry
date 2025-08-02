package net.soulsweaponry.entitydata;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.networking.S2C.TargetPostureSyncS2C;

/**
 * Get the posture values and the name of the target the entity is attacking, in other words
 * the posture value and name of {@code entity.getAttacking()} where entity is the input (player)
 */
public class TargetPostureData {

    public static final String POSTURE_ID = "target_posture";
    public static final String MAX_POSTURE_ID = "target_max_posture";
    public static final String NAME_ID = "target_name";

    /**
     * Gets the posture saved on the main entity (player) with the returned value being the posture from
     * the target the main entity is attacking
     */
    public static int getTargetPosture(LivingEntity mainEntity) {
        IEntityDataSaver target = (IEntityDataSaver)mainEntity;
        if (!target.getPersistentData().contains(POSTURE_ID)) {
            target.getPersistentData().putInt(POSTURE_ID, 0);
        }
        return target.getPersistentData().getInt(POSTURE_ID);
    }

    /**
     * Gets the name saved on the main entity (player) with the returned value being the name of
     * the target the main entity is attacking
     */
    public static String getTargetName(LivingEntity mainEntity) {
        IEntityDataSaver target = (IEntityDataSaver)mainEntity;
        if (!target.getPersistentData().contains(NAME_ID)) {
            target.getPersistentData().putString(NAME_ID, "");
        }
        return target.getPersistentData().getString(NAME_ID);
    }

    /**
     * Gets the max target posture of the target saved on the main entity (player)
     */
    public static int getTargetsMaxPosture(LivingEntity mainEntity) {
        IEntityDataSaver target = (IEntityDataSaver)mainEntity;
        if (!target.getPersistentData().contains(MAX_POSTURE_ID)) {
            target.getPersistentData().putInt(MAX_POSTURE_ID, 200);
        }
        return target.getPersistentData().getInt(MAX_POSTURE_ID);
    }

    /**
     * Saves the name and posture of the target to the main entity
     */
    public static void updateTargetPosture(IEntityDataSaver mainEntity, LivingEntity target) {
        NbtCompound nbt = mainEntity.getPersistentData();
        int amount = PostureData.getPosture(target);
        String name = target.getName().getString();
        int max = EntityPosture.getMaxPostureLoss(target);
        nbt.putInt(POSTURE_ID, amount);
        nbt.putString(NAME_ID, name);
        nbt.putInt(MAX_POSTURE_ID, max);
        if (mainEntity instanceof ServerPlayerEntity) {
            syncData(amount, name, max, (ServerPlayerEntity) mainEntity);
        }
    }

    public static void resetValues(IEntityDataSaver mainEntity) {
        NbtCompound nbt = mainEntity.getPersistentData();
        nbt.putInt(POSTURE_ID, 0);
        nbt.putString(NAME_ID, "");
        nbt.putInt(MAX_POSTURE_ID, 200);
        if (mainEntity instanceof ServerPlayerEntity) {
            syncData(0, "", 200, (ServerPlayerEntity) mainEntity);
        }
    }

    public static void syncData(int data, String name, int maxPosture, ServerPlayerEntity entity) {
        ServerPlayNetworking.send(entity, new TargetPostureSyncS2C(data, name, maxPosture));
    }
}
