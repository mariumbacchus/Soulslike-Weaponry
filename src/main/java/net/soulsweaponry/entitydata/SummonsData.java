package net.soulsweaponry.entitydata;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.S2C.SummonUUIDsSyncS2C;
import net.soulsweaponry.util.NbtHelper;

import java.util.UUID;

public class SummonsData {

    public static UUID[] addSummonUUID(IEntityDataSaver entity, UUID uuid, String listId) {
        NbtCompound nbt = entity.getPersistentData();
        NbtHelper.addUUIDToArr(nbt, uuid, listId);
        if (entity instanceof ServerPlayerEntity player) {
            syncUUIDArr(NbtHelper.getUUIDArr(nbt, listId), listId, player);
        }
        return NbtHelper.getUUIDArr(nbt, listId);
    }

    public static UUID[] getAliveSummons(LivingEntity entity, String listId) {
        return NbtHelper.getUUIDArr(((IEntityDataSaver)entity).getPersistentData(), listId);
    }

    public static UUID[] removeSummonUUID(IEntityDataSaver entity, UUID uuid, String listId) {
        NbtCompound nbt = entity.getPersistentData();
        NbtHelper.removeUUIDFromArr(nbt, uuid, listId);
        if (entity instanceof ServerPlayerEntity player) {
            syncUUIDArr(NbtHelper.getUUIDArr(nbt, listId), listId, player);
        }
        return NbtHelper.getUUIDArr(nbt, listId);
    }

    public static void syncUUIDArr(UUID[] uuids, String listId, ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new SummonUUIDsSyncS2C(listId, uuids));
    }
}