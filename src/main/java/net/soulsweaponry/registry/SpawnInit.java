package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.DarkSorcerer;
import net.soulsweaponry.entity.mobs.EvilForlorn;

public class SpawnInit {
    
    public static int spawnRateChungus = ConfigConstructor.moderatly_sized_chungus_spawnrate;
    public static int spawnRateDemon = ConfigConstructor.withered_demon_spawnrate;
    public static int spawnRateForlorn = ConfigConstructor.evil_forlorn_spawnrate;

    public static void init() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.CRIMSON_FOREST), SpawnGroup.MONSTER, EntityRegistry.WITHERED_DEMON, spawnRateDemon, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.FOREST), SpawnGroup.MONSTER, EntityRegistry.BIG_CHUNGUS, spawnRateChungus, 4, 8);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.NETHER_WASTES), SpawnGroup.MONSTER, EntityRegistry.EVIL_FORLORN, spawnRateForlorn, 1, 1);

        SpawnRestrictionAccessor.callRegister(EntityRegistry.WITHERED_DEMON, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestrictionAccessor.callRegister(EntityRegistry.BIG_CHUNGUS, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestrictionAccessor.callRegister(EntityRegistry.EVIL_FORLORN, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EvilForlorn::canSpawn);
        SpawnRestrictionAccessor.callRegister(EntityRegistry.DARK_SORCERER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DarkSorcerer::canSpawn);
    }
}
