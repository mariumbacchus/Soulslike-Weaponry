package net.soulsweaponry.entitydata;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.EchoSyncS2C;

public class EchoDamageData {

    public static final String ECHO_DAMAGE_ID = "echo_damage";
    public static final String ECHO_DAMAGE_SAVED_MOD_ID = "echo_damage_saved_mod";

    public static void addEchoDamage(LivingEntity entity, float amount) {
        NbtCompound nbt = entity.getPersistentData();
        if (!nbt.contains(ECHO_DAMAGE_ID)) {
            nbt.putFloat(ECHO_DAMAGE_ID, 0);
        }
        float value = nbt.getFloat(ECHO_DAMAGE_ID);
        float savedDamageMod = getEchoDamageSavedMod(entity);
        if (savedDamageMod <= 0f) {
            savedDamageMod = 0.5f;
        }
        if (value < 0) {
            value = 0;
        } else {
            value += amount * savedDamageMod;
        }
        nbt.putFloat(ECHO_DAMAGE_ID, value);
        if (entity instanceof ServerPlayerEntity) {
            syncEchoDamage(value, savedDamageMod, (ServerPlayerEntity) entity);
        }
    }

    public static float getEchoDamage(LivingEntity target) {
        if (!target.getPersistentData().contains(ECHO_DAMAGE_ID)) {
            target.getPersistentData().putFloat(ECHO_DAMAGE_ID, 0);
        }
        return target.getPersistentData().getFloat(ECHO_DAMAGE_ID);
    }

    public static void setEchoDamage(LivingEntity entity, float amount) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putFloat(ECHO_DAMAGE_ID, amount);
        if (entity instanceof ServerPlayerEntity) {
            syncEchoDamage(amount, getEchoDamageSavedMod(entity), (ServerPlayerEntity) entity);
        }
    }

    /**
     * Storing damage multiplies the value by this value before saving.
     */
    public static float getEchoDamageSavedMod(LivingEntity target) {
        if (!target.getPersistentData().contains(ECHO_DAMAGE_SAVED_MOD_ID)) {
            target.getPersistentData().putFloat(ECHO_DAMAGE_SAVED_MOD_ID, 0);
        }
        return target.getPersistentData().getFloat(ECHO_DAMAGE_SAVED_MOD_ID);
    }

    /**
     * Storing damage multiplies the value by this value before saving.
     */
    public static void setEchoDamageSavedMod(LivingEntity entity, float amount) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putFloat(ECHO_DAMAGE_SAVED_MOD_ID, amount);
    }

    public static void syncEchoDamage(float damage, float savedDamageMod, ServerPlayerEntity entity) {
        ModMessages.sendToPlayer(new EchoSyncS2C(damage, savedDamageMod), entity);
    }
}