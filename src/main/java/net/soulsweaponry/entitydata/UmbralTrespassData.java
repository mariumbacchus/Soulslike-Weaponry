package net.soulsweaponry.entitydata;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.ShouldDamageRidingSyncS2C;
import net.soulsweaponry.networking.packets.S2C.UTDamageCooldownSyncS2C;

public class UmbralTrespassData {

    public static final String DAMAGE_RIDING_ID = "should_damage_riding";
    public static final String UMBRAL_DAMAGE_ID = "umbral_trespass_damage";
    public static final String COOLDOWN_ID = "umbral_trespass_cooldown";
    public static final String HEAL_ID = "umbral_trespass_should_heal";

    public static boolean shouldDamageRiding(LivingEntity entity) {
        if (!entity.getPersistentData().contains(DAMAGE_RIDING_ID)) {
            entity.getPersistentData().putBoolean(DAMAGE_RIDING_ID, false);
        }
        return entity.getPersistentData().getBoolean(DAMAGE_RIDING_ID);
    }

    public static void setShouldDamageRiding(LivingEntity entity, boolean bl) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putBoolean(DAMAGE_RIDING_ID, bl);
        if (entity instanceof ServerPlayerEntity) {
            syncDamageRidingData(bl, (ServerPlayerEntity) entity);
        }
    }

    public static void setOtherStats(LivingEntity entity, float damage, int cooldown, boolean shouldHeal) {
        NbtCompound nbt = entity.getPersistentData();
        nbt.putFloat(UMBRAL_DAMAGE_ID, damage);
        nbt.putInt(COOLDOWN_ID, cooldown);
        nbt.putBoolean(HEAL_ID, shouldHeal);
        if (entity instanceof ServerPlayerEntity) {
            syncOtherStats(damage, cooldown, shouldHeal, (ServerPlayerEntity) entity);
        }
    }

    public static float getAbilityDamage(LivingEntity entity) {
        NbtCompound nbt = entity.getPersistentData();
        if (!nbt.contains(UMBRAL_DAMAGE_ID)) {
            nbt.putFloat(UMBRAL_DAMAGE_ID, 20f);
        }
        return nbt.getFloat(UMBRAL_DAMAGE_ID);
    }

    public static int getAbilityCooldown(LivingEntity entity) {
        NbtCompound nbt = entity.getPersistentData();
        if (!nbt.contains(COOLDOWN_ID)) {
            nbt.putInt(COOLDOWN_ID, 350);
        }
        return nbt.getInt(COOLDOWN_ID);
    }

    public static boolean shouldAbilityHeal(LivingEntity entity) {
        NbtCompound nbt = entity.getPersistentData();
        if (!nbt.contains(HEAL_ID)) {
            nbt.putBoolean(HEAL_ID, false);
        }
        return nbt.getBoolean(HEAL_ID);
    }

    public static void syncDamageRidingData(boolean bl, ServerPlayerEntity entity) {
        ModMessages.sendToPlayer(new ShouldDamageRidingSyncS2C(bl), entity);
    }

    public static void syncOtherStats(float damage, int cooldown, boolean shouldHeal, ServerPlayerEntity entity) {
        ModMessages.sendToPlayer(new UTDamageCooldownSyncS2C(damage, cooldown, shouldHeal), entity);
    }
}
