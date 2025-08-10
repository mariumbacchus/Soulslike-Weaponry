package net.soulsweaponry.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.soulsweaponry.datagen.advancements.AdvancementsProvider;
import net.soulsweaponry.datagen.loot_tables.BlockLootTableProvider;
import net.soulsweaponry.datagen.loot_tables.ChestLootTableProvider;
import net.soulsweaponry.datagen.loot_tables.EntityLootTablesProvider;
import net.soulsweaponry.datagen.loot_tables.ChungusBarterLootTableProvider;
import net.soulsweaponry.datagen.models.ModelProvider;
import net.soulsweaponry.datagen.recipe.WeaponRecipeProvider;
import net.soulsweaponry.datagen.tags.*;
import net.soulsweaponry.datagen.worldgen.ModWorldGenerator;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.registry.EnchantRegistry;
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
        pack.addProvider(RegistryDataGen::new);
        pack.addProvider(ChungusBarterLootTableProvider::new);
        pack.addProvider(ChestLootTableProvider::new);
        pack.addProvider(EntityLootTablesProvider::new);
        pack.addProvider(BlockLootTableProvider::new);
        pack.addProvider(BlockTagsProvider::new);
        pack.addProvider(WeaponRecipeProvider::new);
        pack.addProvider(ModWorldGenerator::new);
        pack.addProvider(EnchantmentTagsProvider::new);
        pack.addProvider(StructureTagProvider::new);
        pack.addProvider(FluidTagsProvider::new);
        pack.addProvider(DamageTypeTagsProvider::new);
        pack.addProvider(EntityTagsProvider::new);
        pack.addProvider(ItemTagsProvider::new);
        pack.addProvider(AdvancementsProvider::new);
        pack.addProvider(StatusEffectTagProvider::new);
        pack.addProvider(ModelProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, PlacedFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, EnchantRegistry::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, DamageSourceRegistry::bootstrap);
    }
}
