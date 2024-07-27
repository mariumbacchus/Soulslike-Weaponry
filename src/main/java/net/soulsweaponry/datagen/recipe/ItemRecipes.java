package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.function.Consumer;

// Only extends RecipeProvider to access protected static methods
public class ItemRecipes extends RecipeProvider {

    public ItemRecipes(DataGenerator root) {
        super(root);
    }

    public static void generateRecipes(Consumer<RecipeJsonProvider> consumer) {
        // Misc. items
        ShapedRecipeJsonBuilder.create(ItemRegistry.CHUNGUS_DISC.get())
                .input('#', ItemRegistry.CHUNGUS_EMERALD.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.CHUNGUS_EMERALD.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(ItemRegistry.IRON_SKULL.get())
                .input('Y', ItemRegistry.LOST_SOUL.get())
                .input('I', Items.BONE)
                .input('X', ModTags.Items.IRON_INGOTS)
                .pattern("XIX")
                .pattern("IYI")
                .pattern("XIX")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(Items.SOUL_LANTERN)
                .input('#', Items.IRON_NUGGET)
                .input('X', ItemRegistry.LOST_SOUL.get())
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "soul_lantern_lost_soul"));
        ShapedRecipeJsonBuilder.create(ItemRegistry.SOUL_INGOT.get())
                .input('C', ModTags.Items.IRON_INGOTS)
                .input('#', ItemRegistry.LOST_SOUL.get())
                .pattern(" # ")
                .pattern("#C#")
                .pattern(" # ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);
        // Moonstone tools
        ShapedRecipeJsonBuilder.create(ItemRegistry.MOONSTONE_AXE.get())
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE.get())
                .input('i', ItemRegistry.VERGLAS.get())
                .pattern("## ")
                .pattern("#i ")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "moonstone_axe_left"));
        ShapedRecipeJsonBuilder.create(ItemRegistry.MOONSTONE_AXE.get())
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE.get())
                .input('i', ItemRegistry.VERGLAS.get())
                .pattern(" ##")
                .pattern(" i#")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "moonstone_axe_right"));
        ShapedRecipeJsonBuilder.create(ItemRegistry.MOONSTONE_HOE.get())
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE.get())
                .input('i', ItemRegistry.VERGLAS.get())
                .pattern(" ##")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "moonstone_hoe_right"));
        ShapedRecipeJsonBuilder.create(ItemRegistry.MOONSTONE_HOE.get())
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE.get())
                .input('i', ItemRegistry.VERGLAS.get())
                .pattern("## ")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "moonstone_hoe_left"));
        ShapedRecipeJsonBuilder.create(ItemRegistry.MOONSTONE_PICKAXE.get())
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE.get())
                .input('i', ItemRegistry.VERGLAS.get())
                .pattern("###")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(ItemRegistry.MOONSTONE_SHOVEL.get())
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE.get())
                .input('i', ItemRegistry.VERGLAS.get())
                .pattern(" # ")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(ItemRegistry.MOONSTONE_RING.get())
                .input('D', ItemRegistry.ARKENSTONE.get())
                .input('i', ModTags.Items.IRON_INGOTS)
                .pattern("Di ")
                .pattern("i i")
                .pattern(" i ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.ARKENSTONE.get()).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "moonstone_ring_left"));
        ShapedRecipeJsonBuilder.create(ItemRegistry.MOONSTONE_RING.get())
                .input('D', ItemRegistry.ARKENSTONE.get())
                .input('i', ModTags.Items.IRON_INGOTS)
                .pattern(" iD")
                .pattern("i i")
                .pattern(" i ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.ARKENSTONE.get()).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "moonstone_ring_right"));
        ShapedRecipeJsonBuilder.create(ItemRegistry.BOSS_COMPASS.get())
                .input('#', ItemRegistry.MOONSTONE.get())
                .input('X', Items.COMPASS)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(ItemRegistry.SKOFNUNG_STONE.get())
                .input('C', ItemRegistry.MOONSTONE.get())
                .input('#', ItemRegistry.LOST_SOUL.get())
                .input('D', ItemRegistry.VERGLAS.get())
                .pattern("#C#")
                .pattern("CDC")
                .pattern("#C#")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.VERGLAS.get()).build()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(ItemRegistry.CHAOS_ORB.get())
                .input(ItemRegistry.ESSENCE_OF_LUMINESCENCE.get())
                .input(ItemRegistry.WITHERED_DEMON_HEART.get())
                .input(ItemRegistry.ARKENSTONE.get())
                .input(ItemRegistry.CHAOS_CROWN.get())
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.ESSENCE_OF_LUMINESCENCE.get()).build()))
                .offerTo(consumer);
        ShapelessRecipeJsonBuilder.create(ItemRegistry.DEMON_CHUNK.get())
                .input(ItemRegistry.MOLTEN_DEMON_HEART.get(), 4)
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.DEMON_HEART.get()).build()))
                .offerTo(consumer);
        ShapelessRecipeJsonBuilder.create(Items.PURPLE_DYE)
                .input(BlockRegistry.HYDRANGEA.get())
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(BlockRegistry.HYDRANGEA.get()).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "hydrangea_dye"));
        ShapelessRecipeJsonBuilder.create(Items.PURPLE_DYE, 2)
                .input(BlockRegistry.OLEANDER.get())
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(BlockRegistry.OLEANDER.get()).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "oleander_dye"));
        ShapelessRecipeJsonBuilder.create(ItemRegistry.MOONSTONE.get(), 9)
                .input(BlockRegistry.MOONSTONE_BLOCK.get())
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer);
        ShapelessRecipeJsonBuilder.create(ItemRegistry.VERGLAS.get(), 9)
                .input(BlockRegistry.VERGLAS_BLOCK.get())
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.VERGLAS.get()).build()))
                .offerTo(consumer);

        WeaponRecipeProvider.smeltingRecipe(Ingredient.ofItems(ItemRegistry.DEMON_CHUNK.get()), ItemRegistry.CRIMSON_INGOT.get(), 0.1f, 200, ItemRegistry.DEMON_HEART.get(), consumer);
        WeaponRecipeProvider.smeltingRecipe(Ingredient.ofItems(Items.SOUL_SAND), ItemRegistry.LOST_SOUL.get(), 0.1f, 200, Items.SOUL_SAND, consumer);
        WeaponRecipeProvider.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DEMON_HEARTS), ItemRegistry.MOLTEN_DEMON_HEART.get(), 0.1f, 200, ModTags.Items.DEMON_HEARTS, consumer);
        WeaponRecipeProvider.smeltingRecipe(Ingredient.ofItems(ItemRegistry.WITHERED_DEMON_HEART.get()), Items.NETHERITE_INGOT, 10f, 500, ItemRegistry.WITHERED_DEMON_HEART.get(), consumer, "smelt_withered_demon_heart");
    }
}
