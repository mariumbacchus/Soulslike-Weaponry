package net.soulsweaponry.client.entitydata;

public class ClientMaxPostureData {

    private static int maxPosture;

    public static void setMaxPosture(int posture) {
        ClientMaxPostureData.maxPosture = posture;
    }

    public static int getMaxPosture() {
        return maxPosture;
    }
}
