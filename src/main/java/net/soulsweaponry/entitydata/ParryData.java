package net.soulsweaponry.entitydata;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.networking.S2C.ParrySyncS2C;

public class ParryData {

    public static final String PARRY_FRAMES_ID = "parry_frames";
    public static int MAX_PARRY_FRAMES = (int) ConfigConstructor.shield_parry_max_animation_frames;

    public static int addParryFrames(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        if (!nbt.contains(PARRY_FRAMES_ID)) {
            nbt.putInt(PARRY_FRAMES_ID, 0);
        }
        int frame = nbt.getInt(PARRY_FRAMES_ID);
        if (frame >= MAX_PARRY_FRAMES) {
            frame = 0;
        } else {
            frame += amount;
        }
        nbt.putInt(PARRY_FRAMES_ID, frame);
        syncFrames(frame, (ServerPlayerEntity) player);
        return frame;
    }

    public static int setParryFrames(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putInt(PARRY_FRAMES_ID, amount);
        syncFrames(amount, (ServerPlayerEntity) player);
        return amount;
    }

    public static int getParryFrames(PlayerEntity player) {
        IEntityDataSaver entity = (IEntityDataSaver)player;
        if (!entity.getPersistentData().contains(PARRY_FRAMES_ID)) {
            entity.getPersistentData().putInt(PARRY_FRAMES_ID, 0);
        }
        return entity.getPersistentData().getInt(PARRY_FRAMES_ID);
    }

    public static boolean successfulParry(PlayerEntity player, boolean checkIfCanBeParried, DamageSource source) {
        int frames = ParryData.getParryFrames(player);
        boolean bl = true;
        if (checkIfCanBeParried) {
            bl = !source.isIn(DamageTypeTags.BYPASSES_SHIELD);
        }
        return frames >= 1 && frames <= ConfigConstructor.shield_parry_frames && bl;
    }

    public static void syncFrames(int frames, ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new ParrySyncS2C(frames));
    }
}
