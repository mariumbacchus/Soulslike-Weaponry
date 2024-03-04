package net.soulsweaponry.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.soulsweaponry.datagen.advancements.AdvancementsProvider;
import net.soulsweaponry.datagen.loot_tables.BossLootTableProvider;

public class DataGeneration implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(AdvancementsProvider::new);
        //fabricDataGenerator.createPack().addProvider(ModelProvider::new);
        pack.addProvider(BossLootTableProvider::new);
    }
}
