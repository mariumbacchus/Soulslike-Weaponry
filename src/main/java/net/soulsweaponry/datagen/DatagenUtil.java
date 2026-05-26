package net.soulsweaponry.datagen;

public class DatagenUtil {

    private static final boolean DATAGEN = Boolean.getBoolean("forge.datagen") || Boolean.getBoolean("fabric-api.datagen");

    public static boolean isDatagenRunning() {
        return DATAGEN;
    }
}