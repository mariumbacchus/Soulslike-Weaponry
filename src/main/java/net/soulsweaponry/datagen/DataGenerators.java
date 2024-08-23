package net.soulsweaponry.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.datagen.recipe.WeaponRecipeProvider;
import net.soulsweaponry.datagen.tags.EntityTagsProvider;
import net.soulsweaponry.datagen.worldgen.ModWorldGenProvider;

import java.util.concurrent.CompletableFuture;

/**
 * NOTE:
 * Keep in mind that datagen only runs once in development and not during runtime.
 * Applying logic from config will not work.
 */
@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        DataOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider = event.getLookupProvider();
        /* TODO:
        For some reason forge crashes whenever generating entity loot tables, regardless of what is in the generate
        method. This simple line that registers the provider crashes it and I have 0 idea why. All it says in the
        crash log is that minecraft's own entity loot table files don't generate, like the allay, which I
        have no control over.

        For now I simply "hard-coded" the files in since they would not generate.
         */
        //generator.addProvider(event.includeServer(), new BossLootTableProvider(output));
        generator.addProvider(event.includeServer(), new WeaponRecipeProvider(output));
        generator.addProvider(event.includeServer(), new EntityTagsProvider(output, lookupProvider, fileHelper));

        generator.addProvider(event.includeServer(), new ModWorldGenProvider(output, lookupProvider));
    }
}
