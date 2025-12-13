package net.soulsweaponry.entitydata;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.S2C.packets.ParrySyncS2C;

public class ParryData {

    public static final String PARRY_TICKS_ID = "parry_ticks";
    public static final String MAX_PARRY_TICKS_ID = "max_parry_ticks";
    public static final String PARRY_FRAMES_ID = "parry_frames";

    public static void addParryTicks(PlayerEntity player, int ticks) {
        IEntityDataSaver saver = (IEntityDataSaver) player;
        NbtCompound nbt = saver.getPersistentData();
        int maxTicks = getMaxParryTicks(player);
        if (!nbt.contains(PARRY_TICKS_ID)) {
            nbt.putInt(PARRY_TICKS_ID, 0);
        }
        int tick = nbt.getInt(PARRY_TICKS_ID);
        if (tick >= maxTicks) {
            tick = 0;
        } else {
            tick += ticks;
        }
        nbt.putInt(PARRY_TICKS_ID, tick);
        syncTicks(tick, getParryFrames(player), maxTicks, (ServerPlayerEntity) player);
    }

    public static void setParryTicks(IEntityDataSaver player, int ticks, int frames, int maxTicks) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putInt(PARRY_TICKS_ID, ticks);
        nbt.putInt(PARRY_FRAMES_ID, frames);
        nbt.putInt(MAX_PARRY_TICKS_ID, maxTicks);
        syncTicks(ticks, frames, maxTicks, (ServerPlayerEntity) player);
    }

    public static int getParryTicks(PlayerEntity player) {
        IEntityDataSaver entity = (IEntityDataSaver)player;
        if (!entity.getPersistentData().contains(PARRY_TICKS_ID)) {
            entity.getPersistentData().putInt(PARRY_TICKS_ID, 0);
        }
        return entity.getPersistentData().getInt(PARRY_TICKS_ID);
    }

    public static boolean successfulParry(PlayerEntity player, boolean checkIfCanBeParried, DamageSource source) {
        int ticks = ParryData.getParryTicks(player);
        int frames = ParryData.getParryFrames(player);
        boolean bl = true;
        if (checkIfCanBeParried) {
            bl = !source.isIn(DamageTypeTags.BYPASSES_SHIELD);
        }
        return ticks >= 1 && ticks <= frames && bl;
    }

    /**
     * Actual frames the player can counter opponents.
     */
    public static int getParryFrames(PlayerEntity player) {
        IEntityDataSaver entity = (IEntityDataSaver)player;
        if (!entity.getPersistentData().contains(PARRY_FRAMES_ID)) {
            entity.getPersistentData().putInt(PARRY_FRAMES_ID, 0);
        }
        return entity.getPersistentData().getInt(PARRY_FRAMES_ID);
    }

    /**
     * How long the parry animation will last.
     */
    public static int getMaxParryTicks(PlayerEntity player) {
        IEntityDataSaver entity = (IEntityDataSaver)player;
        if (!entity.getPersistentData().contains(MAX_PARRY_TICKS_ID)) {
            entity.getPersistentData().putInt(MAX_PARRY_TICKS_ID, 0);
        }
        return entity.getPersistentData().getInt(MAX_PARRY_TICKS_ID);
    }

    public static void syncTicks(int ticks, int frames, int maxTicks, ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new ParrySyncS2C(ticks, frames, maxTicks));
    }
}