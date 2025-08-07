package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
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
    }
}
