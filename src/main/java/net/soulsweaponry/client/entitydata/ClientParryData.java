package net.soulsweaponry.client.entitydata;

public class ClientParryData {

    private static int parryFrames;

    public static void setParryFrames(int parryFrames) {
        ClientParryData.parryFrames = parryFrames;
    }

    public static int getParryFrames() {
        return parryFrames;
    }
}
