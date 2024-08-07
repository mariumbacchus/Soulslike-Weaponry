package net.soulsweaponry.client.entitydata;

public class ClientUmbralTrespassData {

    private static boolean shouldDamageRiding;
    private static float damage;
    private static int cooldown;
    private static boolean shouldHeal;

    public static void setShouldDamageRiding(boolean bl) {
        shouldDamageRiding = bl;
    }

    public static void setDamage(float amount) {
        damage = amount;
    }

    public static void setCooldown(int amount) {
        cooldown = amount;
    }

    public static void setShouldHeal(boolean bl) {
        shouldHeal = bl;
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

    public static boolean shouldHeal() {
        return shouldHeal;
    }
}
