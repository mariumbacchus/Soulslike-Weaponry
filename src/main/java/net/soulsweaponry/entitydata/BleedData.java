package net.soulsweaponry.entitydata;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.api.entitystats.EntityBleed;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.BleedSyncS2C;

public class BleedData {

    public static final String BLEED_ID = "bleed";

    public static int addBleed(LivingEntity entity, int amount) {
        if (EntityBleed.isBleedDisabled(entity)
                || entity.isDead()
                || entity.getWorld().isClient) {
            return 0;
        }

        int newAmount = EntityBleed.getBleedBuildup(entity, amount);
        NbtCompound nbt = entity.getPersistentData();
        if (!nbt.contains(BLEED_ID)) {
            nbt.putInt(BLEED_ID, 0);
        }
        int value = nbt.getInt(BLEED_ID);
        if (value < 0) {
            value = 0;
        } else {
            value += newAmount;
        }
        nbt.putInt(BLEED_ID, value);
        if (entity instanceof ServerPlayerEntity serverPlayer) {
            syncData(value, serverPlayer);
        }
        return value;
    }

    public static int getBleed(LivingEntity target) {
        if (!target.getPersistentData().contains(BLEED_ID)) {
            target.getPersistentData().putInt(BLEED_ID, 0);
        }
        return target.getPersistentData().getInt(BLEED_ID);
    }

    public static int reduceBleed(LivingEntity entity, int amount) {
        return addBleed(entity, -amount);
    }

    public static int setBleed(LivingEntity entity, int amount) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putInt(BLEED_ID, amount);
        if (entity instanceof ServerPlayerEntity) {
            syncData(amount, (ServerPlayerEntity) entity);
        }
        return amount;
    }

    public static void syncData(int data, ServerPlayerEntity entity) {
        ModMessages.sendToPlayer(new BleedSyncS2C(data), entity);
    }
}