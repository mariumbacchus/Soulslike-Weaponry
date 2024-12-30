package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.soulsweaponry.events.*;

public class EventRegistry {

    public static void init() {
        ServerTickEvents.START_SERVER_TICK.register(new PlayerTickHandler());
        LivingEntityTickCallback.EVENT.register(new LivingEntityTickHandler());
    }
}
