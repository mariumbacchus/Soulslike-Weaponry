package net.soulsweaponry.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.soulsweaponry.config.CommonConfig;

public class OrePlacedFeatures {

    public static final RegistryEntry<PlacedFeature> MOONSTONE_PLACED = PlacedFeatures.register("moonstone_ore_placed",
            OreFeatures.MOONSTONE_ORE, OrePlacement.commonOrePlacement(CommonConfig.MOONSTONE_ORE_COUNT_PER_CHUNK.get(), HeightRangePlacementModifier.trapezoid(YOffset.fixed(CommonConfig.MOONSTONE_ORE_MIN_HEIGHT.get()), YOffset.fixed(CommonConfig.MOONSTONE_ORE_MAX_HEIGHT.get()))));
    public static final RegistryEntry<PlacedFeature> VERGLAS_PLACED = PlacedFeatures.register("verglas_ore_placed",
            OreFeatures.VERGLAS_ORE, OrePlacement.commonOrePlacement(CommonConfig.VERGLAS_ORE_COUNT_PER_CHUNK.get(), HeightRangePlacementModifier.trapezoid(YOffset.fixed(CommonConfig.VERGLAS_ORE_MIN_HEIGHT.get()), YOffset.fixed(CommonConfig.VERGLAS_ORE_MAX_HEIGHT.get()))));
}
