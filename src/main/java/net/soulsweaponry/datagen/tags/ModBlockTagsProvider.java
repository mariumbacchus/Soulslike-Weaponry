package net.soulsweaponry.datagen.tags;

import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SoulsWeaponry.ModId, existingFileHelper);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
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

        this.getOrCreateTagBuilder(Tags.Blocks.ORES)
                .add(BlockRegistry.MOONSTONE_ORE.get())
                .add(BlockRegistry.MOONSTONE_ORE_DEEPSLATE.get())
                .add(BlockRegistry.VERGLAS_ORE.get())
                .add(BlockRegistry.VERGLAS_ORE_DEEPSLATE.get());

        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(BlockRegistry.MOONSTONE_ORE.get())
                .add(BlockRegistry.MOONSTONE_ORE_DEEPSLATE.get())
                .add(BlockRegistry.MOONSTONE_BLOCK.get())
                .add(BlockRegistry.CRACKED_INFUSED_BLACKSTONE.get())
                .add(BlockRegistry.INFUSED_BLACKSTONE.get())
                .add(BlockRegistry.CRIMSON_OBSIDIAN.get())
                .add(BlockRegistry.ALTAR_BLOCK.get())
                .add(BlockRegistry.BLACKSTONE_PEDESTAL.get())
                .add(BlockRegistry.VERGLAS_BLOCK.get())
                .add(BlockRegistry.VERGLAS_ORE.get())
                .add(BlockRegistry.VERGLAS_ORE_DEEPSLATE.get())
                .add(BlockRegistry.SOULFIRE_STAIN.get())
                .add(BlockRegistry.CHUNGUS_EMERALD_BLOCK.get())
                .add(BlockRegistry.CHUNGUS_MONOLITH.get());


        this.getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
                .add(BlockRegistry.WITHERED_DIRT.get())
                .add(BlockRegistry.WITHERED_GRASS_BLOCK.get());


        this.getOrCreateTagBuilder(BlockTags.DIRT)
                .add(BlockRegistry.WITHERED_DIRT.get())
                .add(BlockRegistry.WITHERED_GRASS_BLOCK.get());


        this.getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(BlockRegistry.CRIMSON_OBSIDIAN.get())
                .add(BlockRegistry.ALTAR_BLOCK.get())
                .add(BlockRegistry.BLACKSTONE_PEDESTAL.get());


        this.getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(BlockRegistry.MOONSTONE_ORE.get())
                .add(BlockRegistry.MOONSTONE_ORE_DEEPSLATE.get())
                .add(BlockRegistry.MOONSTONE_BLOCK.get())
                .add(BlockRegistry.VERGLAS_ORE.get())
                .add(BlockRegistry.VERGLAS_ORE_DEEPSLATE.get())
                .add(BlockRegistry.VERGLAS_BLOCK.get())
                .add(BlockRegistry.CHUNGUS_EMERALD_BLOCK.get());


        this.getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(BlockRegistry.CRACKED_INFUSED_BLACKSTONE.get())
                .add(BlockRegistry.INFUSED_BLACKSTONE.get())
                .add(BlockRegistry.SOULFIRE_STAIN.get())
                .add(BlockRegistry.CHUNGUS_MONOLITH.get());
    }
}
