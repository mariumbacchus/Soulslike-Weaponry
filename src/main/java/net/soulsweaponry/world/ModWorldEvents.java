package net.soulsweaponry.world;

import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.world.gen.OreGeneration;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId)
public class ModWorldEvents {
    //TODO test the ore generation, keep in mind that data packs can be used in 1.20.1 and up
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        // Be wary of calling generation in the order! See GenerationStep.Feature to set up the right order.
        OreGeneration.generateOres(event);
    }
}
