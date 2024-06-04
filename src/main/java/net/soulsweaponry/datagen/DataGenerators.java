package net.soulsweaponry.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.datagen.advancements.RecipeBookAdvancementProvider;
import net.soulsweaponry.datagen.loot_tables.BossLootTableProvider;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        generator.addProvider(new BossLootTableProvider(generator));
        generator.addProvider(new RecipeBookAdvancementProvider(generator, fileHelper));
    }
}
