package net.soulsweaponry.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import net.soulsweaponry.world.feature.OrePlacedFeatures;

public class OreGeneration {

    public static void generateOres() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, OrePlacedFeatures.MOONSTONE_PLACED.getKey().get());
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, OrePlacedFeatures.VERGLAS_PLACED.getKey().get());
    }
}
