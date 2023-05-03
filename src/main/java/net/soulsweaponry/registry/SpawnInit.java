package net.soulsweaponry.registry;

import java.util.function.Predicate;

import com.google.common.base.Preconditions;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.SpawnSettings;
import net.soulsweaponry.config.ConfigConstructor;

public class SpawnInit {
    
    public static int spawnRateChungus = ConfigConstructor.moderatly_sized_chungus_spawnrate;
    public static int spawnRateDemon = ConfigConstructor.withered_demon_spawnrate;
    public static int spawnRateForlorn = ConfigConstructor.evil_forlorn_spawnrate;
    
    public static void addSpawn(Predicate<BiomeSelectionContext> biomeSelector, SpawnGroup spawnGroup, SpawnSettings.SpawnEntry se) {
        Preconditions.checkArgument(se.type.getSpawnGroup() != SpawnGroup.MISC, "MISC spawns pigs");

        Identifier id = Registries.ENTITY_TYPE.getId(se.type);
        Preconditions.checkState(id != Registries.ENTITY_TYPE.getDefaultId(), "Unregistererd entity type: %s", se.type);

        BiomeModifications.create(id).add(ModificationPhase.ADDITIONS, biomeSelector, context -> {
            context.getSpawnSettings().addSpawn(spawnGroup, se);
        });
    }

    private static void normalSpawn() {
        Predicate<BiomeSelectionContext> biomeSelectorChungus = (context) -> {
            RegistryKey<Biome> category = context.getBiomeKey();
            return category == BiomeKeys.FOREST;
        };

        Predicate<BiomeSelectionContext> biomeSelectorDemon = (context) -> {
            RegistryKey<Biome> category = context.getBiomeKey();
            return category == BiomeKeys.CRIMSON_FOREST;
        };

        Predicate<BiomeSelectionContext> biomeSelectorForlorn = (context) -> {
            RegistryKey<Biome> category = context.getBiomeKey();
            return category == BiomeKeys.SOUL_SAND_VALLEY;
        };

        addSpawn(biomeSelectorChungus, EntityRegistry.BIG_CHUNGUS.getSpawnGroup(),
            new SpawnSettings.SpawnEntry(EntityRegistry.BIG_CHUNGUS, spawnRateChungus, 4, 8));

        addSpawn(biomeSelectorDemon, EntityRegistry.WITHERED_DEMON.getSpawnGroup(),
            new SpawnSettings.SpawnEntry(EntityRegistry.WITHERED_DEMON, spawnRateDemon, 1, 1));

        addSpawn(biomeSelectorForlorn, EntityRegistry.EVIL_FORLORN.getSpawnGroup(),
            new SpawnSettings.SpawnEntry(EntityRegistry.EVIL_FORLORN, spawnRateForlorn, 1, 1));
    }

    public static void init() {
        normalSpawn();
    }

}
