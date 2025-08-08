package net.soulsweaponry.datagen.loot_tables;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {

    public BlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);

        addDrop(BlockRegistry.MOONSTONE_ORE, oreDrops(BlockRegistry.MOONSTONE_ORE, ItemRegistry.MOONSTONE));
        addDrop(BlockRegistry.MOONSTONE_ORE_DEEPSLATE, oreDrops(BlockRegistry.MOONSTONE_ORE_DEEPSLATE, ItemRegistry.MOONSTONE));

        addDrop(BlockRegistry.VERGLAS_ORE, oreDrops(BlockRegistry.VERGLAS_ORE, ItemRegistry.VERGLAS));
        addDrop(BlockRegistry.VERGLAS_ORE_DEEPSLATE, oreDrops(BlockRegistry.VERGLAS_ORE_DEEPSLATE, ItemRegistry.VERGLAS));

        addDrop(BlockRegistry.ALTAR_BLOCK);
        addDrop(BlockRegistry.CHUNGUS_EMERALD_BLOCK);
        addDrop(BlockRegistry.CHUNGUS_MONOLITH);
        addDrop(BlockRegistry.CRACKED_INFUSED_BLACKSTONE);
        addDrop(BlockRegistry.CRIMSON_OBSIDIAN);
        addDrop(BlockRegistry.HYDRANGEA);
        addDrop(BlockRegistry.INFUSED_BLACKSTONE);
        addDrop(BlockRegistry.MOONSTONE_BLOCK);
        addDrop(BlockRegistry.SOUL_LAMP);
        addDrop(BlockRegistry.SOULFIRE_STAIN);
        addDrop(BlockRegistry.VERGLAS_BLOCK);
        addDrop(BlockRegistry.BLACKSTONE_PEDESTAL);

        addDrop(BlockRegistry.WITHERED_DIRT);
        addDrop(BlockRegistry.WITHERED_GRASS_BLOCK, drops(BlockRegistry.WITHERED_GRASS_BLOCK, BlockRegistry.WITHERED_DIRT));

        addDrop(BlockRegistry.OLEANDER, doorDrops(BlockRegistry.OLEANDER));
    }
}
