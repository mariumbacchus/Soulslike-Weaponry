package net.soulsweaponry.client.entitydata;

public class ClientUmbralTrespassData {

    private static boolean shouldDamageRiding;
    private static float damage;
    private static int cooldown;
    private static float healMod;
    private static double maxHealthDamage;

    public static void setShouldDamageRiding(boolean bl) {
        shouldDamageRiding = bl;
    }

    public static void setDamage(float amount) {
        damage = amount;
    }

    public static void setCooldown(int amount) {
        cooldown = amount;
    }

    public static void setHealMod(float healMod) {
        ClientUmbralTrespassData.healMod = healMod;
    }

    public static void setMaxHealthDamage(double maxHealthDamage) {
        ClientUmbralTrespassData.maxHealthDamage = maxHealthDamage;
    }

    public static boolean shouldDamageRiding() {
        return shouldDamageRiding;
    }

    public static float getDamage() {
        return damage;
    }

    public static int getCooldown() {
        return cooldown;
    }

    public static float getHealMod() {
        return healMod;
    }

    public static double getMaxHealthDamage() {
        return maxHealthDamage;
    }
}
