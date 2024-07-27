package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.function.Consumer;

// Only extends RecipeProvider to access protected static methods
public class BlockRecipes extends RecipeProvider {

    public BlockRecipes(DataGenerator root) {
        super(root);
    }

    public static void generateRecipes(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(BlockRegistry.ALTAR_BLOCK.get())
                .input('#', ItemRegistry.IRON_SKULL.get())
                .input('D', ItemRegistry.MOONSTONE.get())
                .input('O', Items.OBSIDIAN)
                .pattern(" # ")
                .pattern("DOD")
                .pattern("OOO")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(BlockRegistry.BLACKSTONE_PEDESTAL.get())
                .input('X', Items.ENDER_PEARL)
                .input('O', Items.POLISHED_BLACKSTONE_BRICKS)
                .input('I', ItemRegistry.MOONSTONE.get())
                .input('Y', Items.OBSIDIAN)
                .pattern("I I")
                .pattern("XYX")
                .pattern("OOO")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(BlockRegistry.CHUNGUS_MONOLITH.get())
                .input('O', Items.GOLDEN_CARROT)
                .input('D', Items.DEEPSLATE)
                .input('S', Items.STONE)
                .pattern(" S ")
                .pattern("SOS")
                .pattern("SDS")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(Items.GOLDEN_CARROT).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(BlockRegistry.CRACKED_INFUSED_BLACKSTONE.get())
                .input('#', ItemRegistry.LOST_SOUL.get())
                .input('C', Items.CRACKED_POLISHED_BLACKSTONE_BRICKS)
                .pattern(" # ")
                .pattern("#C#")
                .pattern(" # ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(BlockRegistry.INFUSED_BLACKSTONE.get())
                .input('#', ItemRegistry.LOST_SOUL.get())
                .input('C', Items.POLISHED_BLACKSTONE_BRICKS)
                .pattern(" # ")
                .pattern("#C#")
                .pattern(" # ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(BlockRegistry.MOONSTONE_BLOCK.get())
                .input('#', ItemRegistry.MOONSTONE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(BlockRegistry.VERGLAS_BLOCK.get())
                .input('#', ItemRegistry.VERGLAS.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.VERGLAS.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(BlockRegistry.SOULFIRE_STAIN.get())
                .input('#', ItemRegistry.LOST_SOUL.get())
                .pattern("##")
                .pattern("##")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(BlockRegistry.SOUL_LAMP.get())
                .input('#', ItemRegistry.LOST_SOUL.get())
                .input('C', Items.REDSTONE_LAMP)
                .pattern(" # ")
                .pattern("#C#")
                .pattern(" # ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(BlockRegistry.CRIMSON_OBSIDIAN.get())
                .input(ItemRegistry.CRIMSON_INGOT.get())
                .input(Items.OBSIDIAN)
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.DEMON_HEART.get()).build()))
                .offerTo(consumer);
    }
}
