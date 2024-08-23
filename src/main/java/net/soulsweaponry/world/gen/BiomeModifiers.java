package net.soulsweaponry.world.gen;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.world.feature.PlacedFeatures;

public class BiomeModifiers {

    public static final RegistryKey<BiomeModifier> ADD_MOONSTONE_ORE = registerKey("add_moonstone_ore");
    public static final RegistryKey<BiomeModifier> ADD_VERGLAS_ORE = registerKey("add_verglas_ore");

    public static void bootstrap(Registerable<BiomeModifier> context) {
        var placedFeatures = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        var biomes = context.getRegistryLookup(RegistryKeys.BIOME);

        context.register(ADD_MOONSTONE_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                RegistryEntryList.of(placedFeatures.getOrThrow(PlacedFeatures.MOONSTONE_ORE_PLACED_KEY)),
                GenerationStep.Feature.UNDERGROUND_ORES));

        context.register(ADD_VERGLAS_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                RegistryEntryList.of(placedFeatures.getOrThrow(PlacedFeatures.VERGLAS_ORE_PLACED_KEY)),
                GenerationStep.Feature.UNDERGROUND_ORES));
    }

    public static RegistryKey<BiomeModifier> registerKey(String name) {
        return RegistryKey.of(ForgeRegistries.Keys.BIOME_MODIFIERS, new Identifier(SoulsWeaponry.ModId, name));
    }
}
