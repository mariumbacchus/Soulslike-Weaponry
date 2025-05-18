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

import java.util.List;

public class PlacedFeatures {

    public static final int VERGLAS_ORE_COUNT_PER_CHUNK = 50;
    public static final int VERGLAS_ORE_MIN_HEIGHT = -80;
    public static int VERGLAS_ORE_MAX_HEIGHT = 300;

    public static final int MOONSTONE_ORE_COUNT_PER_CHUNK = 4;
    public static final int MOONSTONE_ORE_MIN_HEIGHT = -80;
    public static final int MOONSTONE_ORE_MAX_HEIGHT = 16;

    public static final RegistryKey<PlacedFeature> MOONSTONE_ORE_PLACED_KEY = registerKey("moonstone_ore_placed");
    public static final RegistryKey<PlacedFeature> VERGLAS_ORE_PLACED_KEY = registerKey("verglas_ore_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var featureLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, MOONSTONE_ORE_PLACED_KEY, featureLookup.getOrThrow(ConfiguredFeatures.MOONSTONE_ORE_KEY), OrePlacement.modifiersWithCount(
                MOONSTONE_ORE_COUNT_PER_CHUNK,
                HeightRangePlacementModifier.trapezoid(
                        YOffset.fixed(MOONSTONE_ORE_MIN_HEIGHT),
                        YOffset.fixed(MOONSTONE_ORE_MAX_HEIGHT)
                )));
        register(context, VERGLAS_ORE_PLACED_KEY, featureLookup.getOrThrow(ConfiguredFeatures.VERGLAS_ORE_KEY), OrePlacement.modifiersWithCount(
                VERGLAS_ORE_COUNT_PER_CHUNK,
                HeightRangePlacementModifier.trapezoid(
                        YOffset.fixed(VERGLAS_ORE_MIN_HEIGHT),
                        YOffset.fixed(VERGLAS_ORE_MAX_HEIGHT)
                )));
    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(SoulsWeaponry.ModId, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> config, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }
}
