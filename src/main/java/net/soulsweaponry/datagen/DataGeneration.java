package net.soulsweaponry.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.soulsweaponry.datagen.advancements.AdvancementsProvider;
import net.soulsweaponry.datagen.enchantment.EnchantmentProvider;
import net.soulsweaponry.datagen.loot_tables.BossLootTableProvider;
import net.soulsweaponry.datagen.recipe.WeaponRecipeProvider;
import net.soulsweaponry.datagen.tags.BlockTagsProvider;
import net.soulsweaponry.datagen.tags.EnchantmentTagsProvider;
import net.soulsweaponry.datagen.tags.EntityTagsProvider;
import net.soulsweaponry.datagen.tags.ItemTagsProvider;
import net.soulsweaponry.datagen.worldgen.ModWorldGenerator;
import net.soulsweaponry.world.feature.ConfiguredFeatures;
import net.soulsweaponry.world.feature.PlacedFeatures;

/**
 * NOTE:
 * Keep in mind that datagen only runs once in development and not during runtime.
 * Applying logic from config will not work.
 */
public class DataGeneration implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        //fabricDataGenerator.createPack().addProvider(ModelProvider::new);
        pack.addProvider(EnchantmentProvider::new);
        pack.addProvider(BossLootTableProvider::new);
        pack.addProvider(BlockTagsProvider::new);
        pack.addProvider(WeaponRecipeProvider::new);
        pack.addProvider(ModWorldGenerator::new);
        pack.addProvider(EnchantmentTagsProvider::new);
        pack.addProvider(EntityTagsProvider::new);
        pack.addProvider(ItemTagsProvider::new);
        pack.addProvider(AdvancementsProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, PlacedFeatures::bootstrap);
    }
}
