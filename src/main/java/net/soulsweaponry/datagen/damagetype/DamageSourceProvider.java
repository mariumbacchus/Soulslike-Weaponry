package net.soulsweaponry.datagen.damagetype;

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

public class DamageSourceProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistryBuilder BUILDER = new RegistryBuilder()
            .addRegistry(RegistryKeys.DAMAGE_TYPE, DamageSourceRegistry::bootstrap);

    public DamageSourceProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider) {
        super(output, lookupProvider, BUILDER, Set.of(SoulsWeaponry.ModId));
    }
}
