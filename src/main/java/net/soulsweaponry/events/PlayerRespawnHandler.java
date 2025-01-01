package net.soulsweaponry.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.ReturningProjectileData;

import java.util.UUID;

public class PlayerRespawnHandler implements ServerPlayerEvents.CopyFrom {

    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        NbtCompound oldData = ((IEntityDataSaver) oldPlayer).getPersistentData();
        NbtCompound newData = ((IEntityDataSaver) newPlayer).getPersistentData();

        if (oldData.contains(ReturningProjectileData.PROJECTILE_ID)) {
            UUID uuid = oldData.getUuid(ReturningProjectileData.PROJECTILE_ID);
            newData.putUuid(ReturningProjectileData.PROJECTILE_ID, uuid);
            ReturningProjectileData.syncData(uuid, newPlayer);
        }
    }
}
