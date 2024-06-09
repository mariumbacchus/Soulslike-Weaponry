package net.soulsweaponry.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.soulsweaponry.registry.BlockRegistry;

import java.util.List;

public class OreFeatures {

    public static final List<OreFeatureConfig.Target> OVERWORLD_MOONSTONE_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, BlockRegistry.MOONSTONE_ORE.get().getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockRegistry.MOONSTONE_ORE_DEEPSLATE.get().getDefaultState())
    );
    public static final List<OreFeatureConfig.Target> OVERWORLD_VERGLAS_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, BlockRegistry.VERGLAS_ORE.get().getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockRegistry.VERGLAS_ORE_DEEPSLATE.get().getDefaultState())
    );

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> MOONSTONE_ORE = ConfiguredFeatures.register("overworld_moonstone_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_MOONSTONE_ORES, 5));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> VERGLAS_ORE = ConfiguredFeatures.register("overworld_verglas_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_VERGLAS_ORES, 3));
}
