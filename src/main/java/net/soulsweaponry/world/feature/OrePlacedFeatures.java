package net.soulsweaponry.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;

public class OrePlacedFeatures {

    public static final RegistryEntry<PlacedFeature> MOONSTONE_PLACED = PlacedFeatures.register("moonstone_ore_placed",
            OreFeatures.MOONSTONE_ORE, OrePlacement.commonOrePlacement(4, HeightRangePlacementModifier.trapezoid(YOffset.fixed(-63), YOffset.fixed(16))));
    public static final RegistryEntry<PlacedFeature> VERGLAS_PLACED = PlacedFeatures.register("verglas_ore_placed",
            OreFeatures.MOONSTONE_ORE, OrePlacement.commonOrePlacement(7, HeightRangePlacementModifier.trapezoid(YOffset.fixed(-80), YOffset.fixed(120))));
}
