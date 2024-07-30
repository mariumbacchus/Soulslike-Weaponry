package net.soulsweaponry.entitydata;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.ParrySyncS2C;

public class ParryData {

    public static final String PARRY_FRAMES_ID = "parry_frames";
    public static final int MAX_PARRY_FRAMES = ConfigConstructor.shield_parry_max_animation_frames;

    public static int addParryFrames(PlayerEntity player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int frame = nbt.getInt(PARRY_FRAMES_ID);
        if (frame >= MAX_PARRY_FRAMES) {
            frame = 0;
        } else {
            frame += amount;
        }
        nbt.putInt(PARRY_FRAMES_ID, frame);
        if (player instanceof ServerPlayerEntity) {
            syncFrames(frame, (ServerPlayerEntity) player);
        }
        return frame;
    }

    public static int setParryFrames(PlayerEntity player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putInt(PARRY_FRAMES_ID, amount);
        if (player instanceof ServerPlayerEntity) {
            syncFrames(amount, (ServerPlayerEntity) player);
        }
        return amount;
    }

    public static int getParryFrames(PlayerEntity player) {
        return player.getPersistentData().getInt(PARRY_FRAMES_ID);
    }

    public static boolean successfulParry(PlayerEntity player, boolean checkIfCanBeParried, DamageSource source) {
        int frames = ParryData.getParryFrames(player);
        boolean bl = true;
        if (checkIfCanBeParried) {
            bl = !source.isUnblockable();
        }
        return frames >= 1 && frames <= ConfigConstructor.shield_parry_frames && bl;
    }

    public static void syncFrames(int frames, ServerPlayerEntity player) {
        ModMessages.sendToPlayer(new ParrySyncS2C(frames), player);
    }

    // Capabilities work fine, but why not use the getPersistentData method instead?
    // SummonData and ParryData still uses the persistentData method like in the fabric
    // version, while PostureData uses the capabilities instead.
    // Under are some remains after I tried to make ParryData use capabilities.
    /*private int parryFrames;

    public int getParryFrames() {
        return parryFrames;
    }

    public void setParryFrames(int parryFrames) {
        this.parryFrames = parryFrames;
    }

    public void addParryFrames(int amount) {
        if (this.parryFrames >= MAX_PARRY_FRAMES) {
            this.parryFrames = 0;
        } else {
            this.parryFrames += amount;
        }
    }

    public void copyFrom(ParryData data) {
        this.parryFrames = data.parryFrames;
    }

    public void saveNBTData(NbtCompound nbt) {
        nbt.putInt(PARRY_FRAMES_ID, this.parryFrames);
    }

    public void loadNBTData(NbtCompound nbt) {
        this.parryFrames = nbt.getInt(PARRY_FRAMES_ID);
    }

    public static boolean successfulParry(PlayerEntity player, boolean checkIfCanBeParried, DamageSource source) {
        return player.getCapability(ParryDataProvider.PARRY_DATA).map(data -> {
            int frames = data.getParryFrames();
            boolean bl = true;
            if (checkIfCanBeParried) {
                bl = !source.isUnblockable();
            }
            return frames >= 1 && frames <= CommonConfig.SHIELD_PARRY_FRAMES.get() && bl;
        }).orElse(false);
    }

    public static int getParryFrames(PlayerEntity player) {
        return player.getCapability(ParryDataProvider.PARRY_DATA).map(ParryData::getParryFrames).orElse(0);
    }

    public static void setParryFrames(PlayerEntity player, int amount) {
        player.getCapability(ParryDataProvider.PARRY_DATA).ifPresent(data -> {
            data.setParryFrames(amount);
            ModMessages.sendToPlayer(new ParrySyncS2C(data.getParryFrames()), (ServerPlayerEntity) player);
        });
    }

    public static void addParryFrames(PlayerEntity player, int amount) {
        player.getCapability(ParryDataProvider.PARRY_DATA).ifPresent(data -> {
            data.addParryFrames(amount);
            ModMessages.sendToPlayer(new ParrySyncS2C(data.getParryFrames()), (ServerPlayerEntity) player);
        });
    }*/
}