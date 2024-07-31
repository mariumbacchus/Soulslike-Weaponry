package net.soulsweaponry.client.entitydata;

import java.util.UUID;

public class ClientFreyrSwordSummonData {

    private static UUID uuid;

    public static void setUUID(UUID uuid) {
        ClientFreyrSwordSummonData.uuid = uuid;
    }

    public static UUID getUUID() {
        return uuid;
    }
}
