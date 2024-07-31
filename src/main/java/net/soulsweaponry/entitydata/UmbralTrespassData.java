package net.soulsweaponry.entitydata;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.ShouldDamageRidingSyncS2C;
import net.soulsweaponry.networking.packets.S2C.TicksBeforeDismountSyncS2C;

public class UmbralTrespassData {

    public static final String DAMAGE_RIDING_ID = "should_damage_riding";
    public static final String TICKS_BEFORE_DISMOUNT_ID = "ticks_before_dismount";

    public static boolean shouldDamageRiding(LivingEntity entity) {
        if (!entity.getPersistentData().contains(DAMAGE_RIDING_ID)) {
            entity.getPersistentData().putBoolean(DAMAGE_RIDING_ID, false);
        }
        return entity.getPersistentData().getBoolean(DAMAGE_RIDING_ID);
    }

    public static boolean setShouldDamageRiding(LivingEntity entity, boolean bl) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putBoolean(DAMAGE_RIDING_ID, bl);
        if (entity instanceof ServerPlayerEntity) {
            syncDamageRidingData(bl, (ServerPlayerEntity) entity);
        }
        return bl;
    }

    public static int getTicksBeforeDismount(LivingEntity entity) {
        if (!entity.getPersistentData().contains(TICKS_BEFORE_DISMOUNT_ID)) {
            entity.getPersistentData().putInt(TICKS_BEFORE_DISMOUNT_ID, 40);
        }
        return entity.getPersistentData().getInt(TICKS_BEFORE_DISMOUNT_ID);
    }

    public static int setTicksBeforeDismount(LivingEntity entity, int ticksBeforeDismount) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putInt(TICKS_BEFORE_DISMOUNT_ID, ticksBeforeDismount);
        if (entity instanceof ServerPlayerEntity) {
            syncTicksBeforeDismountData(ticksBeforeDismount, (ServerPlayerEntity) entity);
        }
        return ticksBeforeDismount;
    }

    public static void syncDamageRidingData(boolean bl, ServerPlayerEntity entity) {
        ModMessages.sendToPlayer(new ShouldDamageRidingSyncS2C(bl), entity);
    }

    public static void syncTicksBeforeDismountData(int amount, ServerPlayerEntity entity) {
        ModMessages.sendToPlayer(new TicksBeforeDismountSyncS2C(amount), entity);
    }
}
