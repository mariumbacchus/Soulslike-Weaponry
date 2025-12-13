package net.soulsweaponry.entitydata;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.api.entitystats.EntityFrost;
import net.soulsweaponry.networking.S2C.packets.FrostSyncS2C;

import java.util.UUID;

public class FrostData {

    public static final String FROST_VALUE_ID = "frost_status";
    public static final String FROST_COOLING_DOWN_ID = "frost_cooling_down";
    public static final String FROST_SOURCE_ID = "frost_source_uuid_id";

    public static final UUID NIL_UUID = new UUID(0L, 0L);

    /**
     * Adds frost buildup to entities that are NOT on fire and have NOT triggered frost yet (meaning frost
     * buildup is NOT on cooldown)
     */
    public static void addFrost(LivingEntity entity, int amount) {
        if (!EntityFrost.isFrostBuildupDisabled(entity) && !entity.isOnFire() && !isFrostCoolingDown(entity) && !entity.isDead() && !entity.getWorld().isClient) {
            int newAmount = EntityFrost.getFrostBuildup(entity, amount);
            addFrost((IEntityDataSaver) entity, newAmount, EntityFrost.getMaxFrostBuildup(entity));
        }
    }

    private static int addFrost(IEntityDataSaver entity, int amount, int max) {
        NbtCompound nbt = entity.getPersistentData();
        if (!nbt.contains(FROST_VALUE_ID)) {
            nbt.putInt(FROST_VALUE_ID, 0);
        }
        int value = nbt.getInt(FROST_VALUE_ID);
        if (value < 0) {
            value = 0;
        } else {
            value = Math.min(value + amount, max);
        }
        nbt.putInt(FROST_VALUE_ID, value);
        if (entity instanceof ServerPlayerEntity) {
            syncFrostData(value, isFrostCoolingDown(entity), (ServerPlayerEntity) entity);
        }
        return value;
    }

    public static int getFrost(LivingEntity entity) {
        IEntityDataSaver target = (IEntityDataSaver)entity;
        return getFrost(target);
    }

    public static int getFrost(IEntityDataSaver entity) {
        if (!entity.getPersistentData().contains(FROST_VALUE_ID)) {
            entity.getPersistentData().putInt(FROST_VALUE_ID, 0);
        }
        return entity.getPersistentData().getInt(FROST_VALUE_ID);
    }

    /**
     * This bypasses the check for if frost is cooling down so it successfully reduces the value
     */
    public static int reduceFrost(IEntityDataSaver entity, int amount, int max) {
        return addFrost(entity, -amount, max);
    }

    public static int setFrost(IEntityDataSaver entity, int amount, boolean frostCoolingDown) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putInt(FROST_VALUE_ID, amount);
        nbt.putBoolean(FROST_COOLING_DOWN_ID, frostCoolingDown);
        if (entity instanceof ServerPlayerEntity) {
            syncFrostData(amount, frostCoolingDown, (ServerPlayerEntity) entity);
        }
        return amount;
    }

    public static boolean isFrostCoolingDown(LivingEntity entity) {
        IEntityDataSaver target = (IEntityDataSaver)entity;
        return isFrostCoolingDown(target);
    }

    public static boolean isFrostCoolingDown(IEntityDataSaver entity) {
        if (!entity.getPersistentData().contains(FROST_COOLING_DOWN_ID)) {
            entity.getPersistentData().putBoolean(FROST_COOLING_DOWN_ID, false);
        }
        return entity.getPersistentData().getBoolean(FROST_COOLING_DOWN_ID);
    }

    public static boolean setFrostCoolingDown(IEntityDataSaver entity, boolean coolingDown) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putBoolean(FROST_COOLING_DOWN_ID, coolingDown);
        if (entity instanceof ServerPlayerEntity) {
            syncFrostData(getFrost(entity), coolingDown, (ServerPlayerEntity) entity);
        }
        return coolingDown;
    }

    public static void syncFrostData(int value, boolean frostCoolingDown, ServerPlayerEntity entity) {
        ServerPlayNetworking.send(entity, new FrostSyncS2C(value, frostCoolingDown));
    }

    /**
     * Set the inflicter of the frost buildup or Permafrost effect.
     * <p>
     * Mainly used when the player attacks a target, inflicting it with Permafrost,
     * and to avoid the Permafrost from spreading back to the main attacker (player)
     * via another mob, the attacker is saved to each mob so that before the
     * damage and AOE is triggered, it checks whether the attacker is being
     * targeted or not.
     */
    public static UUID setFrostSource(Entity entity, Entity source) {
        return setFrostSource(entity, source.getUuid());
    }

    public static UUID setFrostSource(Entity entity, UUID uuid) {
        IEntityDataSaver saver = (IEntityDataSaver) entity;
        NbtCompound nbt = saver.getPersistentData();
        nbt.putUuid(FROST_SOURCE_ID, uuid);
        // Only handled server side so no need to sync
        return uuid;
    }

    /**
     * Get the inflicter of the frost buildup or Permafrost effect.
     * <p>
     * Mainly used when the player attacks a target, inflicting it with Permafrost,
     * and to avoid the Permafrost from spreading back to the main attacker (player)
     * via another mob, the attacker is saved to each mob so that before the
     * damage and AOE is triggered, it checks whether the attacker is being
     * targeted or not.
     */
    public static UUID getFrostSource(Entity entity) {
        IEntityDataSaver target = (IEntityDataSaver)entity;
        if (!target.getPersistentData().contains(FROST_SOURCE_ID)) {
            target.getPersistentData().putUuid(FROST_SOURCE_ID, NIL_UUID);
        }
        return target.getPersistentData().getUuid(FROST_SOURCE_ID);
    }
}