package net.soulsweaponry.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.soulsweaponry.SoulsWeaponry;
import net.minecraft.world.gen.GenerationStep;

public class OreGenerator {

    public static final RegistryKey<PlacedFeature> MOONSTONE_ORE_PLACED_KEY = registerKey("moonstone_ore");
    public static final RegistryKey<PlacedFeature> VERGLAS_ORE_PLACED_KEY = registerKey("verglas_ore");
    public static void generateOres() {

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, MOONSTONE_ORE_PLACED_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, VERGLAS_ORE_PLACED_KEY);
    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(SoulsWeaponry.ModId, name));
    }
}
