package net.soulsweaponry.client.entitydata;

public class ClientEchoData {

    private static float echoDamage;
    private static float savedDamageMod;

    public static void setEchoDamage(float echoDamage) {
        ClientEchoData.echoDamage = echoDamage;
    }

    public static float getEchoDamage() {
        return echoDamage;
    }

    public static void setSavedDamageMod(float savedDamageMod) {
        ClientEchoData.savedDamageMod = savedDamageMod;
    }

    public static float getSavedDamageMod() {
        return savedDamageMod;
    }
}
