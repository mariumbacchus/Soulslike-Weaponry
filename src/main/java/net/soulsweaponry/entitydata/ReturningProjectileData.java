package net.soulsweaponry.entitydata;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.S2C.packets.ReturningProjectileDataSyncS2C;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ReturningProjectileData {

    public static final String PROJECTILE_ID = "returning_projectile_uuid";

    @Nullable
    public static UUID getReturningProjectileUuid(LivingEntity entity) {
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();
        if (!nbt.contains(PROJECTILE_ID)) {
            return null;
        }
        return nbt.getUuid(PROJECTILE_ID);
    }

    public static UUID setReturningProjectileUuid(LivingEntity entity, UUID uuid) {
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();
        nbt.putUuid(PROJECTILE_ID, uuid);
        if (entity instanceof ServerPlayerEntity) {
            syncData(uuid, (ServerPlayerEntity) entity);
        }
        return uuid;
    }

    public static void syncData(UUID uuid, ServerPlayerEntity entity) {
        ServerPlayNetworking.send(entity, new ReturningProjectileDataSyncS2C(uuid));
    }
}