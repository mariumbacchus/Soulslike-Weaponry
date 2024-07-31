package net.soulsweaponry.entitydata;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.ReturningProjectileDataSyncS2C;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ReturningProjectileData {

    public static final String PROJECTILE_ID = "returning_projectile_uuid";

    @Nullable
    public static UUID getReturningProjectileUuid(LivingEntity entity) {
        if (!entity.getPersistentData().contains(PROJECTILE_ID)) {
            return null;
        }
        return entity.getPersistentData().getUuid(PROJECTILE_ID);
    }

    public static UUID setReturningProjectileUuid(LivingEntity entity, UUID uuid) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putUuid(PROJECTILE_ID, uuid);
        if (entity instanceof ServerPlayerEntity) {
            syncData(uuid, (ServerPlayerEntity) entity);
        }
        return uuid;
    }

    public static void syncData(UUID uuid, ServerPlayerEntity entity) {
        ModMessages.sendToPlayer(new ReturningProjectileDataSyncS2C(uuid), entity);
    }
}
