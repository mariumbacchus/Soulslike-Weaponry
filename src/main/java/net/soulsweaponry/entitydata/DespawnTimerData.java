package net.soulsweaponry.entitydata;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class DespawnTimerData {

    public static final String DESPAWN_ID = "despawn_timer";

    public static int addDespawnTicks(IEntityDataSaver entity, int amount) {
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
        IEntityDataSaver target = (IEntityDataSaver)entity;
        if (!target.getPersistentData().contains(DESPAWN_ID)) {
            target.getPersistentData().putInt(DESPAWN_ID, 0);
        }
        return target.getPersistentData().getInt(DESPAWN_ID);
    }

    public static int setDespawnTicks(IEntityDataSaver entity, int amount) {
        if (entity instanceof PlayerEntity) {
            return 0;
        }
        NbtCompound nbt = entity.getPersistentData();
        nbt.putInt(DESPAWN_ID, amount);
        return amount;
    }
}
