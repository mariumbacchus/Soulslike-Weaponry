package net.soulsweaponry.client.entitydata;

public class ClientFrostData {

    private static int frostValue;
    private static boolean frostCoolingDown;

    public static int getFrostValue() {
        return frostValue;
    }

    public static void setFrostValue(int frostValue) {
        ClientFrostData.frostValue = frostValue;
    }

    public static boolean isFrostCoolingDown() {
        return frostCoolingDown;
    }

    public static void setFrostCoolingDown(boolean frostCoolingDown) {
        ClientFrostData.frostCoolingDown = frostCoolingDown;
    }
}
