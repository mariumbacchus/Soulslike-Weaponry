package net.soulsweaponry.client.entitydata;

import java.util.UUID;

public class ClientSummonsData {

    private static UUID[] summons;

    public static void setSummons(UUID[] summons) {
        ClientSummonsData.summons = summons;
    }

    public static UUID[] getSummons() {
        return summons;
    }
}
