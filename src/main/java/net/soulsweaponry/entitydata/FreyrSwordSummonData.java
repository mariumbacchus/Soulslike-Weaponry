package net.soulsweaponry.entitydata;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.FreyrSwordSummonDataSyncS2C;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FreyrSwordSummonData {

    public static final String SUMMON_ID = "freyr_sword_summon_uuid";

    @Nullable
    public static UUID getSummonUuid(LivingEntity entity) {
        if (!entity.getPersistentData().contains(SUMMON_ID)) {
            return null;
        }
        return entity.getPersistentData().getUuid(SUMMON_ID);
    }

    public static UUID setSummonUuid(LivingEntity entity, UUID uuid) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putUuid(SUMMON_ID, uuid);
        if (entity instanceof ServerPlayerEntity) {
            syncData(uuid, (ServerPlayerEntity) entity);
        }
        return uuid;
    }

    public static void syncData(UUID uuid, ServerPlayerEntity entity) {
        ModMessages.sendToPlayer(new FreyrSwordSummonDataSyncS2C(uuid), entity);
    }
}
