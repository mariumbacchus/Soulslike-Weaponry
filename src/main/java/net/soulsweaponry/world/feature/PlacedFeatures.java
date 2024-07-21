package net.soulsweaponry.world.feature;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;

import java.util.List;

public class PlacedFeatures {

    public static final RegistryKey<PlacedFeature> MOONSTONE_ORE_PLACED_KEY = registerKey("moonstone_ore_placed");
    public static final RegistryKey<PlacedFeature> VERGLAS_ORE_PLACED_KEY = registerKey("verglas_ore_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var featureLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, MOONSTONE_ORE_PLACED_KEY, featureLookup.getOrThrow(ConfiguredFeatures.MOONSTONE_ORE_KEY), OrePlacement.modifiersWithCount(
                ConfigConstructor.moonstone_ore_count_per_chunk,
                HeightRangePlacementModifier.trapezoid(
                        YOffset.fixed(ConfigConstructor.moonstone_ore_min_height),
                        YOffset.fixed(ConfigConstructor.moonstone_ore_max_height)
                )));
        register(context, VERGLAS_ORE_PLACED_KEY, featureLookup.getOrThrow(ConfiguredFeatures.VERGLAS_ORE_KEY), OrePlacement.modifiersWithCount(
                ConfigConstructor.verglas_ore_count_per_chunk,
                HeightRangePlacementModifier.trapezoid(
                        YOffset.fixed(ConfigConstructor.verglas_ore_min_height),
                        YOffset.fixed(ConfigConstructor.verglas_ore_max_height)
                )));
    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(SoulsWeaponry.ModId, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> config, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }
}