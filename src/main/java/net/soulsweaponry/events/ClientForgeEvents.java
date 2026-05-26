package net.soulsweaponry.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.registry.KeyBindRegistry;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            KeyBindRegistry.registerKeyInputs();
        }
    }
}
