package net.soulsweaponry.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.networking.PacketIds;

public class ParryData {

    public static final String PARRY_FRAMES_ID = "parry_frames";
    public static int MAX_PARRY_FRAMES = ConfigConstructor.shield_parry_max_animation_frames;

    public static int addParryFrames(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
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
        return ((IEntityDataSaver)player).getPersistentData().getInt(PARRY_FRAMES_ID);
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
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(frames);
        ServerPlayNetworking.send(player, PacketIds.PARRY, buf);
    }
}
