package net.soulsweaponry.entitydata.parry;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.ParrySyncS2C;

public class ParryData {

    public static final String PARRY_FRAMES_ID = "parry_frames";
    public static final int MAX_PARRY_FRAMES = CommonConfig.SHIELD_PARRY_MAX_ANIMATION_FRAMES.get();
    private int parryFrames;

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
    }
}