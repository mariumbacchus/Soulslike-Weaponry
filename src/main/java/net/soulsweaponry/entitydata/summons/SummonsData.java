package net.soulsweaponry.entitydata.summons;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.SummonUUIDsSyncS2C;
import net.soulsweaponry.util.NbtHelper;

import java.util.UUID;

public class SummonsData {

    /*public static final String SUMMONS_DATA_ID = "summons_data";
    private UUID[] summons;

    public UUID[] getAliveSummons() {
        return summons;
    }

    public void addSummonUUID(UUID uuid) {
        this.summons = NbtHelper.addUUIDToArr(this.summons, uuid);
    }

    public void copyFrom(SummonsData data) {
        this.summons = data.summons;
    }

    public void saveNBTData(NbtCompound nbt) {
        NbtHelper.saveUUIDArr(nbt, this.summons, SUMMONS_DATA_ID);
    }

    public void loadNBTData(NbtCompound nbt) {
        this.summons = NbtHelper.getUUIDArr(nbt, SUMMONS_DATA_ID);
    }*/

    public static UUID[] addSummonUUID(LivingEntity entity, UUID uuid, String listId) {
        NbtCompound nbt = entity.getPersistentData();
        NbtHelper.addUUIDToArr(nbt, uuid, listId);
        if (entity instanceof ServerPlayerEntity player) {
            syncUUIDArr(NbtHelper.getUUIDArr(nbt, listId), listId, player);
        }
        return NbtHelper.getUUIDArr(nbt, listId);
    }

    public static UUID[] getAliveSummons(LivingEntity entity, String listId) {
        return NbtHelper.getUUIDArr((entity).getPersistentData(), listId);
    }

    public static UUID[] removeSummonUUID(LivingEntity entity, UUID uuid, String listId) {
        NbtCompound nbt = entity.getPersistentData();
        NbtHelper.removeUUIDFromArr(nbt, uuid, listId);
        return NbtHelper.getUUIDArr(nbt, listId);
    }

    public static void syncUUIDArr(UUID[] uuids, String listId, ServerPlayerEntity player) {
        ModMessages.sendToPlayer(new SummonUUIDsSyncS2C(uuids.length, listId, uuids), player);
    }
}