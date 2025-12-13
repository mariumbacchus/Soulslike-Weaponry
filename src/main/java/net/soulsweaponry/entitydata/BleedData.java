package net.soulsweaponry.entitydata;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.api.entitystats.EntityBleed;
import net.soulsweaponry.networking.S2C.packets.BleedSyncS2C;

public class BleedData {

    public static final String BLEED_ID = "bleed";

    public static void addBleed(LivingEntity entity, int amount) {
        if (!EntityBleed.isBleedDisabled(entity) && !entity.isDead() && !entity.getWorld().isClient) {
            int newAmount = EntityBleed.getBleedBuildup(entity, amount);
            addBleed((IEntityDataSaver) entity, newAmount, EntityBleed.getMaxBleed(entity));
        }
    }

    private static int addBleed(IEntityDataSaver entity, int amount, int max) {
        NbtCompound nbt = entity.getPersistentData();
        if (!nbt.contains(BLEED_ID)) {
            nbt.putInt(BLEED_ID, 0);
        }
        int value = nbt.getInt(BLEED_ID);
        if (value < 0) {
            value = 0;
        } else {
            value = Math.min(value + amount, max);
        }
        nbt.putInt(BLEED_ID, value);
        if (entity instanceof ServerPlayerEntity) {
            syncData(value, (ServerPlayerEntity) entity);
        }
        return value;
    }

    public static int getBleed(LivingEntity entity) {
        IEntityDataSaver target = (IEntityDataSaver)entity;
        if (!target.getPersistentData().contains(BLEED_ID)) {
            target.getPersistentData().putInt(BLEED_ID, 0);
        }
        return target.getPersistentData().getInt(BLEED_ID);
    }

    public static int reduceBleed(IEntityDataSaver entity, int amount, int max) {
        return addBleed(entity, -amount, max);
    }

    public static int setBleed(IEntityDataSaver entity, int amount) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putInt(BLEED_ID, amount);
        if (entity instanceof ServerPlayerEntity) {
            syncData(amount, (ServerPlayerEntity) entity);
        }
        return amount;
    }

    public static void syncData(int data, ServerPlayerEntity entity) {
        ServerPlayNetworking.send(entity, new BleedSyncS2C(data));
    }
}