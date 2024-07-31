package net.soulsweaponry.client.entitydata;

import java.util.UUID;

public class ClientReturningProjectileData {

    private static UUID uuid;

    public static void setUUID(UUID uuid) {
        ClientReturningProjectileData.uuid = uuid;
    }

    public static UUID getUUID() {
        return uuid;
    }
}
