package net.soulsweaponry.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.soulsweaponry.datagen.advancements.AdvancementsProvider;
import net.soulsweaponry.datagen.loot_tables.BossLootTableProvider;
import net.soulsweaponry.datagen.recipe.WeaponRecipeProvider;

public class DataGeneration implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        //fabricDataGenerator.addProvider(AdvancementsProvider::new); Recipes automatically add themselves to the recipe book so this is no longer needed
        //fabricDataGenerator.addProvider(ModelProvider::new);
        fabricDataGenerator.addProvider(BossLootTableProvider::new);
        fabricDataGenerator.addProvider(WeaponRecipeProvider::new);
    }
}