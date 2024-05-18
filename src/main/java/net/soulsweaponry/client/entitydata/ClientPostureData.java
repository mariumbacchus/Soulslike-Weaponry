package net.soulsweaponry.client.entitydata;

public class ClientPostureData {

    private static int posture;

    public static void setPosture(int posture) {
        ClientPostureData.posture = posture;
    }

    public static int getPosture() {
        return posture;
    }
}
