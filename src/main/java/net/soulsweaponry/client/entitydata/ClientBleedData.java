package net.soulsweaponry.client.entitydata;

public class ClientBleedData {

    private static int bleed;

    public static void setBleed(int bleed) {
        ClientBleedData.bleed = bleed;
    }

    public static int getBleed() {
        return bleed;
    }
}
