package net.soulsweaponry.entitydata;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.PacketIds;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FreyrSwordSummonData {

    public static final String SUMMON_ID = "freyr_sword_summon_uuid";

    @Nullable
    public static UUID getSummonUuid(LivingEntity entity) {
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();
        if (!nbt.contains(SUMMON_ID)) {
            return null;
        }
        return nbt.getUuid(SUMMON_ID);
    }

    public static UUID setSummonUuid(LivingEntity entity, UUID uuid) {
        NbtCompound nbt = ((IEntityDataSaver)entity).getPersistentData();
        nbt.putUuid(SUMMON_ID, uuid);
        if (entity instanceof ServerPlayerEntity) {
            syncData(uuid, (ServerPlayerEntity) entity);
        }
        return uuid;
    }

    public static void syncData(UUID uuid, ServerPlayerEntity entity) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        ServerPlayNetworking.send(entity, PacketIds.SYNC_FREYR_SWORD_SUMMON_DATA, buf);
    }
}