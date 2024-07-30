package net.soulsweaponry.world.gen;

import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EntityRegistry;

import java.util.Arrays;
import java.util.List;

public class EntityGeneration {

    public static void onEntitySpawn(final BiomeLoadingEvent event) {
        addEntityToSpecificBiomes(event, EntityRegistry.BIG_CHUNGUS.get(), ConfigConstructor.moderatly_sized_chungus_spawnweight, 4, 8, BiomeKeys.FOREST);
        addEntityToAllNetherBiomes(event, EntityRegistry.WITHERED_DEMON.get(), ConfigConstructor.withered_demon_spawnweight, 1, 1);
        addEntityToAllNetherBiomes(event, EntityRegistry.EVIL_FORLORN.get(), ConfigConstructor.evil_forlorn_spawnweight, 1, 1);
    }

    private static void addEntityToAllBiomesExceptThese(BiomeLoadingEvent event, EntityType<?> type,
                                                        int weight, int minCount, int maxCount, RegistryKey<Biome>... biomes) {
        // Goes through each entry in the biomes and sees if it matches the current biome we are loading
        boolean isBiomeSelected = Arrays.stream(biomes).map(RegistryKey::getValue)
                .map(Object::toString).anyMatch(s -> s.equals(event.getName().toString()));

        if(!isBiomeSelected) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    @SafeVarargs
    private static void addEntityToSpecificBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                                  int weight, int minCount, int maxCount, RegistryKey<Biome>... biomes) {
        // Goes through each entry in the biomes and sees if it matches the current biome we are loading
        boolean isBiomeSelected = Arrays.stream(biomes).map(RegistryKey::getValue)
                .map(Object::toString).anyMatch(s -> s.equals(event.getName().toString()));

        if (isBiomeSelected) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    private static void addEntityToAllOverworldBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                                      int weight, int minCount, int maxCount) {
        if (!event.getCategory().equals(Biome.Category.THEEND) && !event.getCategory().equals(Biome.Category.NETHER)) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    private static void addEntityToAllNetherBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                                      int weight, int minCount, int maxCount) {
        if (event.getCategory().equals(Biome.Category.NETHER)) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    private static void addEntityToAllBiomesNoNether(BiomeLoadingEvent event, EntityType<?> type,
                                                     int weight, int minCount, int maxCount) {
        if (!event.getCategory().equals(Biome.Category.NETHER)) {
            List<SpawnSettings.SpawnEntry> base = event.getSpawns().getSpawner(type.getSpawnGroup());
            base.add(new SpawnSettings.SpawnEntry(type,weight, minCount, maxCount));
        }
    }

    private static void addEntityToAllBiomesNoEnd(BiomeLoadingEvent event, EntityType<?> type,
                                                  int weight, int minCount, int maxCount) {
        if (!event.getCategory().equals(Biome.Category.THEEND)) {
            List<SpawnSettings.SpawnEntry> base = event.getSpawns().getSpawner(type.getSpawnGroup());
            base.add(new SpawnSettings.SpawnEntry(type,weight, minCount, maxCount));
        }
    }

    private static void addEntityToAllBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                             int weight, int minCount, int maxCount) {
        List<SpawnSettings.SpawnEntry> base = event.getSpawns().getSpawner(type.getSpawnGroup());
        base.add(new SpawnSettings.SpawnEntry(type,weight, minCount, maxCount));
    }
}
