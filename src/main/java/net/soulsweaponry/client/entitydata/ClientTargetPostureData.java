package net.soulsweaponry.client.entitydata;

public class ClientTargetPostureData {

    private static int targetPosture;
    private static String name;
    private static int maxPosture;

    public static void setTargetPosture(int targetPosture) {
        ClientTargetPostureData.targetPosture = targetPosture;
    }

    public static void setName(String name) {
        ClientTargetPostureData.name = name;
    }

    public static void setMaxPosture(int maxPosture) {
        ClientTargetPostureData.maxPosture = maxPosture;
    }

    public static int getTargetPosture() {
        return targetPosture;
    }

    public static String getName() {
        return name;
    }

    public static int getMaxPosture() {
        return maxPosture;
    }
}
