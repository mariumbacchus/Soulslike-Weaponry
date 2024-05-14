package net.soulsweaponry.world.gen;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.soulsweaponry.world.feature.OrePlacedFeatures;

import java.util.List;

public class OreGeneration {

    public static void generateOres(final BiomeLoadingEvent event) {
        List<RegistryEntry<PlacedFeature>> base = event.getGeneration().getFeatures(GenerationStep.Feature.UNDERGROUND_ORES);
        base.add(OrePlacedFeatures.MOONSTONE_PLACED);
        base.add(OrePlacedFeatures.VERGLAS_PLACED);
    }
}
