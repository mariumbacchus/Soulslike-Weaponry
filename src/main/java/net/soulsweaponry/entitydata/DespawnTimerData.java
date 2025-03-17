package net.soulsweaponry.entitydata;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class DespawnTimerData {

    public static final String DESPAWN_ID = "despawn_timer";

    public static int addDespawnTicks(Entity entity, int amount) {
        if (entity instanceof PlayerEntity) {
            return 0;
        }
        NbtCompound nbt = entity.getPersistentData();
        if (!nbt.contains(DESPAWN_ID)) {
            nbt.putInt(DESPAWN_ID, 0);
        }
        int ticks = nbt.getInt(DESPAWN_ID);
        if (ticks < 0) {
            ticks = 0;
        } else {
            ticks += amount;
        }
        nbt.putInt(DESPAWN_ID, ticks);
        return ticks;
    }

    public static int getDespawnTicks(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return 0;
        }
        if (!entity.getPersistentData().contains(DESPAWN_ID)) {
            entity.getPersistentData().putInt(DESPAWN_ID, 0);
        }
        return entity.getPersistentData().getInt(DESPAWN_ID);
    }

    public static int setDespawnTicks(Entity entity, int amount) {
        if (entity instanceof PlayerEntity) {
            return 0;
        }
        NbtCompound nbt = entity.getPersistentData();
        nbt.putInt(DESPAWN_ID, amount);
        return amount;
    }
}