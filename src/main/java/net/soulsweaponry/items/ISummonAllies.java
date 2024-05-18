package net.soulsweaponry.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.soulsweaponry.entitydata.summons.SummonsData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ISummonAllies {
    int getMaxSummons();
    String getSummonsListId();
    void saveSummonUuid(LivingEntity user, UUID summonUuid);
    default boolean canSummonEntity(ServerWorld world, LivingEntity user, String listId) {
        List<UUID> toRemove = new ArrayList<>();
        for (UUID prevUuid : SummonsData.getAliveSummons(user, listId)) {
            Entity entity = world.getEntity(prevUuid);
            if (entity == null || !entity.isAlive()) {
                toRemove.add(prevUuid);
            }
        }
        toRemove.forEach(uuid -> SummonsData.removeSummonUUID(user, uuid, listId));
        return SummonsData.getAliveSummons(user, listId).length < this.getMaxSummons();
    }
}