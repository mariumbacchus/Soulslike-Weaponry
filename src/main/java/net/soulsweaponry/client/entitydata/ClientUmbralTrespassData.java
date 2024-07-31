package net.soulsweaponry.client.entitydata;

public class ClientUmbralTrespassData {

    private static boolean shouldDamageRiding;
    private static int ticksBeforeDismount;

    public static void setShouldDamageRiding(boolean bl) {
        shouldDamageRiding = bl;
    }

    public static void setTicksBeforeDismount(int amount) {
        ticksBeforeDismount = amount;
    }

    public static boolean shouldDamageRiding() {
        return shouldDamageRiding;
    }

    public static int getTicksBeforeDismount() {
        return ticksBeforeDismount;
    }
}
