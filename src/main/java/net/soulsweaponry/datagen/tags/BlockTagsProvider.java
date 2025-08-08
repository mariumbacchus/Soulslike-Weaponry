package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagsProvider extends FabricTagProvider.BlockTagProvider {

    public BlockTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        //TODO when fabric adds remove from tag when using addTag (since we dont want everything in one tag),
        // add datagen instead of manual jsons regarding INCORRECT_FOR_X_TOOL

        //TODO test all mining levels if they work or not
        this.getOrCreateTagBuilder(ModTags.Blocks.NEEDS_IRON_BLOCK_TOOL)
                .addOptionalTag(BlockTags.NEEDS_IRON_TOOL);
        this.getOrCreateTagBuilder(ModTags.Blocks.NEEDS_LOST_SOUL_TOOL)
                .addOptionalTag(BlockTags.NEEDS_IRON_TOOL);
        this.getOrCreateTagBuilder(ModTags.Blocks.NEEDS_LOST_SOUL_DURABLE_TOOL)
                .addOptionalTag(BlockTags.NEEDS_IRON_TOOL);
        this.getOrCreateTagBuilder(ModTags.Blocks.NEEDS_MOONSTONE_OR_VERGLAS_TOOL)
                .addOptionalTag(BlockTags.NEEDS_DIAMOND_TOOL);
        this.getOrCreateTagBuilder(ModTags.Blocks.NEEDS_CRIMSON_INGOT_TOOL)
                .addOptionalTag(BlockTags.NEEDS_DIAMOND_TOOL);
        this.getOrCreateTagBuilder(ModTags.Blocks.NEEDS_MOONSTONE_TOOL)
                .addOptionalTag(BlockTags.NEEDS_DIAMOND_TOOL);
        this.getOrCreateTagBuilder(ModTags.Blocks.NEEDS_ECHO_SHARD_TOOL)
                .addOptionalTag(BlockTags.NEEDS_DIAMOND_TOOL);

        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(BlockRegistry.MOONSTONE_ORE)
                .add(BlockRegistry.MOONSTONE_ORE_DEEPSLATE)
                .add(BlockRegistry.MOONSTONE_BLOCK)
                .add(BlockRegistry.CRACKED_INFUSED_BLACKSTONE)
                .add(BlockRegistry.INFUSED_BLACKSTONE)
                .add(BlockRegistry.CRIMSON_OBSIDIAN)
                .add(BlockRegistry.ALTAR_BLOCK)
                .add(BlockRegistry.BLACKSTONE_PEDESTAL)
                .add(BlockRegistry.VERGLAS_BLOCK)
                .add(BlockRegistry.VERGLAS_ORE)
                .add(BlockRegistry.VERGLAS_ORE_DEEPSLATE)
                .add(BlockRegistry.SOULFIRE_STAIN)
                .add(BlockRegistry.CHUNGUS_EMERALD_BLOCK)
                .add(BlockRegistry.CHUNGUS_MONOLITH);

        this.getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
                .add(BlockRegistry.WITHERED_DIRT)
                .add(BlockRegistry.WITHERED_GRASS_BLOCK);

        this.getOrCreateTagBuilder(BlockTags.DIRT)
                .add(BlockRegistry.WITHERED_DIRT)
                .add(BlockRegistry.WITHERED_GRASS_BLOCK);

        this.getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(BlockRegistry.CRIMSON_OBSIDIAN)
                .add(BlockRegistry.ALTAR_BLOCK)
                .add(BlockRegistry.BLACKSTONE_PEDESTAL);

        this.getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(BlockRegistry.MOONSTONE_ORE)
                .add(BlockRegistry.MOONSTONE_ORE_DEEPSLATE)
                .add(BlockRegistry.MOONSTONE_BLOCK)
                .add(BlockRegistry.VERGLAS_ORE)
                .add(BlockRegistry.VERGLAS_ORE_DEEPSLATE)
                .add(BlockRegistry.VERGLAS_BLOCK)
                .add(BlockRegistry.CHUNGUS_EMERALD_BLOCK);

        this.getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(BlockRegistry.CRACKED_INFUSED_BLACKSTONE)
                .add(BlockRegistry.INFUSED_BLACKSTONE)
                .add(BlockRegistry.SOULFIRE_STAIN)
                .add(BlockRegistry.CHUNGUS_MONOLITH);
    }
}
