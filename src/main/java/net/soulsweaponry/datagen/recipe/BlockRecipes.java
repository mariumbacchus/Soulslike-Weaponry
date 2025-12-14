package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;

import static net.minecraft.data.server.recipe.RecipeGenerator.conditionsFromItemPredicates;

public class BlockRecipes {

    public static void generateRecipes(RegistryEntryLookup<Item> itemLookup, RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.DECORATIONS, BlockRegistry.ALTAR_BLOCK)
                .input('#', ItemRegistry.IRON_SKULL)
                .input('D', ItemRegistry.MOONSTONE)
                .input('O', Items.OBSIDIAN)
                .pattern(" # ")
                .pattern("DOD")
                .pattern("OOO")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.DECORATIONS, BlockRegistry.BLACKSTONE_PEDESTAL)
                .input('X', Items.ENDER_PEARL)
                .input('O', Items.POLISHED_BLACKSTONE_BRICKS)
                .input('I', ItemRegistry.MOONSTONE)
                .input('Y', Items.OBSIDIAN)
                .pattern("I I")
                .pattern("XYX")
                .pattern("OOO")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.DECORATIONS, BlockRegistry.CHUNGUS_MONOLITH)
                .input('O', Items.GOLDEN_CARROT)
                .input('D', Items.DEEPSLATE)
                .input('S', Items.STONE)
                .pattern(" S ")
                .pattern("SOS")
                .pattern("SDS")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, Items.GOLDEN_CARROT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CRACKED_INFUSED_BLACKSTONE)
                .input('#', ModTags.Items.LOST_SOUL) // TagKey overload is fine (builder resolves it using itemLookup)
                .input('C', Items.CRACKED_POLISHED_BLACKSTONE_BRICKS)
                .pattern(" # ")
                .pattern("#C#")
                .pattern(" # ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().tag(itemLookup, ModTags.Items.LOST_SOUL).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.BUILDING_BLOCKS, BlockRegistry.INFUSED_BLACKSTONE)
                .input('#', ModTags.Items.LOST_SOUL)
                .input('C', Items.POLISHED_BLACKSTONE_BRICKS)
                .pattern(" # ")
                .pattern("#C#")
                .pattern(" # ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().tag(itemLookup, ModTags.Items.LOST_SOUL).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.BUILDING_BLOCKS, BlockRegistry.MOONSTONE_BLOCK)
                .input('#', ItemRegistry.MOONSTONE)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.BUILDING_BLOCKS, BlockRegistry.VERGLAS_BLOCK)
                .input('#', ItemRegistry.VERGLAS)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.VERGLAS).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.BUILDING_BLOCKS, BlockRegistry.SOULFIRE_STAIN)
                .input('#', ModTags.Items.LOST_SOUL)
                .pattern("##")
                .pattern("##")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().tag(itemLookup, ModTags.Items.LOST_SOUL).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.DECORATIONS, BlockRegistry.SOUL_LAMP)
                .input('#', ModTags.Items.LOST_SOUL)
                .input('C', Items.REDSTONE_LAMP)
                .pattern(" # ")
                .pattern("#C#")
                .pattern(" # ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().tag(itemLookup, ModTags.Items.LOST_SOUL).build()
                ))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(itemLookup, RecipeCategory.DECORATIONS, BlockRegistry.CRIMSON_OBSIDIAN)
                .input(ItemRegistry.CRIMSON_INGOT)
                .input(Items.OBSIDIAN)
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.DEMON_HEART).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.BUILDING_BLOCKS, BlockRegistry.CHUNGUS_EMERALD_BLOCK)
                .input('#', ItemRegistry.CHUNGUS_EMERALD)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.CHUNGUS_EMERALD).build()
                ))
                .offerTo(recipeExporter);
    }
}