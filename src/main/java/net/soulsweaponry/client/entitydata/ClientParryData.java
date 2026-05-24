package net.soulsweaponry.client.entitydata;

public class ClientParryData {

    private static int parryTicks;
    private static int parryFrames;
    private static int maxParryTicks;

    public static int getParryFrames() {
        return parryFrames;
    }

    public static int getMaxParryTicks() {
        return maxParryTicks;
    }

    public static int getParryTicks() {
        return parryTicks;
    }

    public static void setMaxParryTicks(int maxParryTicks) {
        ClientParryData.maxParryTicks = maxParryTicks;
    }

    public static void setParryFrames(int parryFrames) {
        ClientParryData.parryFrames = parryFrames;
    }

    public static void setParryTicks(int parryTicks) {
        ClientParryData.parryTicks = parryTicks;
    }
}

