package net.soulsweaponry.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.loottable.LootTableProvider;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.datagen.advancements.AdvancementsProvider;
import net.soulsweaponry.datagen.loot_tables.BlockLootTableProvider;
import net.soulsweaponry.datagen.loot_tables.ChestLootTableProvider;
import net.soulsweaponry.datagen.loot_tables.ChungusBarterLootTables;
import net.soulsweaponry.datagen.loot_tables.EntityLootTablesProvider;
import net.soulsweaponry.datagen.recipe.WeaponRecipeProvider;
import net.soulsweaponry.datagen.tags.*;

import java.util.Collections;
import java.util.List;
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

        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(),
                List.of(
                        new LootTableProvider.LootTypeGenerator(EntityLootTablesProvider::new, LootContextTypes.ENTITY),
                        new LootTableProvider.LootTypeGenerator(ChungusBarterLootTables::new, LootContextTypes.BARTER),
                        new LootTableProvider.LootTypeGenerator(BlockLootTableProvider::new, LootContextTypes.BLOCK),
                        new LootTableProvider.LootTypeGenerator(ChestLootTableProvider::new, LootContextTypes.CHEST)
                )));
        generator.addProvider(event.includeServer(), new WeaponRecipeProvider(output));

        generator.addProvider(event.includeServer(), new ForgeAdvancementProvider(output, event.getLookupProvider(), event.getExistingFileHelper(),
                List.of(new AdvancementsProvider())
        ));

        BlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, lookupProvider, fileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, lookupProvider, blockTagsProvider.getTagLookupFuture(), fileHelper));
        generator.addProvider(event.includeServer(), new DamageTypeTagsProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new EntityTagsProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new FluidTagsProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new StatusEffectTagProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new StructureTagProvider(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new EnchantmentTagsProvider(output, lookupProvider, fileHelper));

        generator.addProvider(event.includeServer(), new ModRegistryProvider(output, lookupProvider));
    }
}
