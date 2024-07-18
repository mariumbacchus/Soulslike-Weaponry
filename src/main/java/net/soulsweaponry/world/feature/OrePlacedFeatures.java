package net.soulsweaponry.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.soulsweaponry.config.ConfigConstructor;

public class OrePlacedFeatures {

    public static final RegistryEntry<PlacedFeature> MOONSTONE_PLACED = PlacedFeatures.register("moonstone_ore_placed",
            OreFeatures.MOONSTONE_ORE, OrePlacement.commonOrePlacement(ConfigConstructor.moonstone_ore_count_per_chunk, HeightRangePlacementModifier.trapezoid(YOffset.fixed(ConfigConstructor.moonstone_ore_min_height), YOffset.fixed(ConfigConstructor.moonstone_ore_max_height))));
    public static final RegistryEntry<PlacedFeature> VERGLAS_PLACED = PlacedFeatures.register("verglas_ore_placed",
            OreFeatures.VERGLAS_ORE, OrePlacement.commonOrePlacement(ConfigConstructor.verglas_ore_count_per_chunk, HeightRangePlacementModifier.trapezoid(YOffset.fixed(ConfigConstructor.verglas_ore_min_height), YOffset.fixed(ConfigConstructor.verglas_ore_max_height))));
}