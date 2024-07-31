package net.soulsweaponry.entitydata;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.PacketIds;

public class UmbralTrespassData {

    public static final String DAMAGE_RIDING_ID = "should_damage_riding";
    public static final String TICKS_BEFORE_DISMOUNT_ID = "ticks_before_dismount";

    public static boolean shouldDamageRiding(LivingEntity entity) {
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();
        if (!nbt.contains(DAMAGE_RIDING_ID)) {
            nbt.putBoolean(DAMAGE_RIDING_ID, false);
        }
        return nbt.getBoolean(DAMAGE_RIDING_ID);
    }

    public static boolean setShouldDamageRiding(LivingEntity entity, boolean bl) {
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();
        nbt.putBoolean(DAMAGE_RIDING_ID, bl);
        if (entity instanceof ServerPlayerEntity) {
            syncDamageRidingData(bl, (ServerPlayerEntity) entity);
        }
        return bl;
    }

    public static int getTicksBeforeDismount(LivingEntity entity) {
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();
        if (!nbt.contains(TICKS_BEFORE_DISMOUNT_ID)) {
            nbt.putInt(TICKS_BEFORE_DISMOUNT_ID, 40);
        }
        return nbt.getInt(TICKS_BEFORE_DISMOUNT_ID);
    }

    public static int setTicksBeforeDismount(LivingEntity entity, int ticksBeforeDismount) {
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();
        nbt.putInt(TICKS_BEFORE_DISMOUNT_ID, ticksBeforeDismount);
        if (entity instanceof ServerPlayerEntity) {
            syncTicksBeforeDismountData(ticksBeforeDismount, (ServerPlayerEntity) entity);
        }
        return ticksBeforeDismount;
    }

    public static void syncDamageRidingData(boolean bl, ServerPlayerEntity entity) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(bl);
        ServerPlayNetworking.send(entity, PacketIds.SYNC_DAMAGE_RIDING_DATA, buf);
    }

    public static void syncTicksBeforeDismountData(int amount, ServerPlayerEntity entity) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(amount);
        ServerPlayNetworking.send(entity, PacketIds.SYNC_TICKS_BEFORE_DISMOUNT_DATA, buf);
    }
}