package net.soulsweaponry.datagen;

import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.world.feature.ConfiguredFeatures;
import net.soulsweaponry.world.feature.PlacedFeatures;
import net.soulsweaponry.world.gen.BiomeModifiers;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModRegistryProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistryBuilder BUILDER = new RegistryBuilder()
            .addRegistry(RegistryKeys.DAMAGE_TYPE, DamageSourceRegistry::bootstrap)
            .addRegistry(RegistryKeys.CONFIGURED_FEATURE, ConfiguredFeatures::bootstrap)
            .addRegistry(RegistryKeys.PLACED_FEATURE, PlacedFeatures::bootstrap)
            .addRegistry(ForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiers::bootstrap);

    public ModRegistryProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries, BUILDER, Set.of(SoulsWeaponry.ModId));
    }
}