package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.soulsweaponry.SoulsWeaponry;

import java.util.Arrays;

/**
 * Old ore spawn logic, consider this deprecated.
 */
public class OreSpawnRegistry {

    private static final ConfiguredFeature<?, ?> OVERWORLD_MOONSTONE_ORE_CONFIGURED_FEATURE = new ConfiguredFeature<>(
        Feature.ORE, new OreFeatureConfig(
            OreConfiguredFeatures.STONE_ORE_REPLACEABLES, 
            BlockRegistry.MOONSTONE_ORE.getDefaultState(),
            3
    ));
    private static final PlacedFeature OVERWORLD_MOONSTONE_ORE_PLACED_FEATURE = new PlacedFeature(
        RegistryEntry.of(OVERWORLD_MOONSTONE_ORE_CONFIGURED_FEATURE), 
        Arrays.asList(
            CountPlacementModifier.of(1), //veins per chunk
            SquarePlacementModifier.of(),
            HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(16))
    ));

    private static final ConfiguredFeature<?, ?> OVERWORLD_DEEPSLATE_MOONSTONE_ORE_CONFIGURED_FEATURE = new ConfiguredFeature<>(
        Feature.ORE, new OreFeatureConfig(
            OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, 
            BlockRegistry.MOONSTONE_ORE_DEEPSLATE.getDefaultState(),
            5
    ));
    private static final PlacedFeature OVERWORLD_DEEPSLATE_MOONSTONE_ORE_PLACED_FEATURE = new PlacedFeature(
        RegistryEntry.of(OVERWORLD_DEEPSLATE_MOONSTONE_ORE_CONFIGURED_FEATURE), 
        Arrays.asList(
            CountPlacementModifier.of(3),
            HeightRangePlacementModifier.uniform(YOffset.fixed(-63), YOffset.fixed(0))
    ));

    private static final ConfiguredFeature<?, ?> OVERWORLD_VERGLAS_ORE_CONFIGURED_FEATURE = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, BlockRegistry.VERGLAS_ORE.getDefaultState(),3));
    private static final PlacedFeature OVERWORLD_VERGLAS_ORE_PLACED_FEATURE = new PlacedFeature(RegistryEntry.of(OVERWORLD_VERGLAS_ORE_CONFIGURED_FEATURE), Arrays.asList(CountPlacementModifier.of(8), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(120))));

    private static final ConfiguredFeature<?, ?> OVERWORLD_DEEPSLATE_VERGLAS_ORE_CONFIGURED_FEATURE = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockRegistry.VERGLAS_ORE_DEEPSLATE.getDefaultState(), 3));
    private static final PlacedFeature OVERWORLD_DEEPSLATE_VERGLAS_ORE_PLACED_FEATURE = new PlacedFeature(RegistryEntry.of(OVERWORLD_DEEPSLATE_VERGLAS_ORE_CONFIGURED_FEATURE), Arrays.asList(CountPlacementModifier.of(6), HeightRangePlacementModifier.uniform(YOffset.fixed(-63), YOffset.fixed(0))));

    public static void init() {
        registerConfigured(OVERWORLD_MOONSTONE_ORE_CONFIGURED_FEATURE, "overworld_moonstone_ore");
        registerConfigured(OVERWORLD_DEEPSLATE_MOONSTONE_ORE_CONFIGURED_FEATURE, "overworld_deepslate_moonstone_ore");
        registerConfigured(OVERWORLD_VERGLAS_ORE_CONFIGURED_FEATURE, "overworld_verglas_ore");
        registerConfigured(OVERWORLD_DEEPSLATE_VERGLAS_ORE_CONFIGURED_FEATURE, "overworld_deepslate_verglas_ore");

        registerPlaced(OVERWORLD_MOONSTONE_ORE_PLACED_FEATURE, "overworld_moonstone_ore");
        registerPlaced(OVERWORLD_DEEPSLATE_MOONSTONE_ORE_PLACED_FEATURE, "overworld_deepslate_moonstone_ore");
        registerPlaced(OVERWORLD_VERGLAS_ORE_PLACED_FEATURE, "overworld_verglas_ore");
        registerPlaced(OVERWORLD_DEEPSLATE_VERGLAS_ORE_PLACED_FEATURE, "overworld_deepslate_verglas_ore");

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(SoulsWeaponry.ModId, "overworld_moonstone_ore")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(SoulsWeaponry.ModId, "overworld_deepslate_moonstone_ore")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(SoulsWeaponry.ModId, "overworld_verglas_ore")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(SoulsWeaponry.ModId, "overworld_deepslate_verglas_ore")));
    }

    public static ConfiguredFeature<?, ?> registerConfigured(ConfiguredFeature<?,?> configFeature, String name) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SoulsWeaponry.ModId, name), configFeature);
    }

    public static PlacedFeature registerPlaced(PlacedFeature placedFeature, String name) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(SoulsWeaponry.ModId, name), placedFeature);
    }
}
