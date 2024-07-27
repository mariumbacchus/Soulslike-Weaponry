package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;

public class ItemRecipes {

    public static void generateRecipes(RecipeExporter exporter) {
        // Misc. items
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.CHUNGUS_DISC)
                .input('#', ItemRegistry.CHUNGUS_EMERALD)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.CHUNGUS_EMERALD).build()))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.IRON_SKULL)
                .input('Y', ItemRegistry.LOST_SOUL)
                .input('I', Items.BONE)
                .input('X', ModTags.Items.IRON_INGOTS)
                .pattern("XIX")
                .pattern("IYI")
                .pattern("XIX")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL).build()))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Items.SOUL_LANTERN)
                .input('#', Items.IRON_NUGGET)
                .input('X', ItemRegistry.LOST_SOUL)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, "soul_lantern_lost_soul"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.SOUL_INGOT)
                .input('C', ModTags.Items.IRON_INGOTS)
                .input('#', ItemRegistry.LOST_SOUL)
                .pattern(" # ")
                .pattern("#C#")
                .pattern(" # ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL).build()))
                .offerTo(exporter);
        // Moonstone tools
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_AXE)
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern("## ")
                .pattern("#i ")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, "moonstone_axe_left"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_AXE)
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern(" ##")
                .pattern(" i#")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, "moonstone_axe_right"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_HOE)
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern(" ##")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, "moonstone_hoe_right"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_HOE)
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern("## ")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, "moonstone_hoe_left"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_PICKAXE)
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern("###")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE).build()))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_SHOVEL)
                .input('/', ModTags.Items.STICKS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern(" # ")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE).build()))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemRegistry.MOONSTONE_RING)
                .input('D', ItemRegistry.ARKENSTONE)
                .input('i', ModTags.Items.IRON_INGOTS)
                .pattern("Di ")
                .pattern("i i")
                .pattern(" i ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.ARKENSTONE).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, "moonstone_ring_left"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemRegistry.MOONSTONE_RING)
                .input('D', ItemRegistry.ARKENSTONE)
                .input('i', ModTags.Items.IRON_INGOTS)
                .pattern(" iD")
                .pattern("i i")
                .pattern(" i ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.ARKENSTONE).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, "moonstone_ring_right"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRegistry.BOSS_COMPASS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('X', Items.COMPASS)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE).build()))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemRegistry.SKOFNUNG_STONE)
                .input('C', ItemRegistry.MOONSTONE)
                .input('#', ItemRegistry.LOST_SOUL)
                .input('D', ItemRegistry.VERGLAS)
                .pattern("#C#")
                .pattern("CDC")
                .pattern("#C#")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.VERGLAS).build()))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemRegistry.CHAOS_ORB)
                .input(ItemRegistry.ESSENCE_OF_LUMINESCENCE)
                .input(ItemRegistry.WITHERED_DEMON_HEART)
                .input(ItemRegistry.ARKENSTONE)
                .input(ItemRegistry.CHAOS_CROWN)
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.ESSENCE_OF_LUMINESCENCE).build()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.DEMON_CHUNK)
                .input(ItemRegistry.MOLTEN_DEMON_HEART, 4)
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.DEMON_HEART).build()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.PURPLE_DYE)
                .input(BlockRegistry.HYDRANGEA)
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(BlockRegistry.HYDRANGEA).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, "hydrangea_dye"));
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.PURPLE_DYE, 2)
                .input(BlockRegistry.OLEANDER)
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(BlockRegistry.OLEANDER).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, "oleander_dye"));
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.MOONSTONE, 9)
                .input(BlockRegistry.MOONSTONE_BLOCK)
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE).build()))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.VERGLAS, 9)
                .input(BlockRegistry.VERGLAS_BLOCK)
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.VERGLAS).build()))
                .offerTo(exporter);

        WeaponRecipeProvider.smeltingRecipe(Ingredient.ofItems(ItemRegistry.DEMON_CHUNK), ItemRegistry.CRIMSON_INGOT, 0.1f, 200, ItemRegistry.DEMON_HEART, exporter);
        WeaponRecipeProvider.smeltingRecipe(Ingredient.ofItems(Items.SOUL_SAND), ItemRegistry.LOST_SOUL, 0.1f, 200, Items.SOUL_SAND, exporter);
        WeaponRecipeProvider.smeltingRecipe(Ingredient.fromTag(ModTags.Items.DEMON_HEARTS), ItemRegistry.MOLTEN_DEMON_HEART, 0.1f, 200, ModTags.Items.DEMON_HEARTS, exporter);
        WeaponRecipeProvider.smeltingRecipe(Ingredient.ofItems(ItemRegistry.WITHERED_DEMON_HEART), Items.NETHERITE_INGOT, 10f, 500, ItemRegistry.WITHERED_DEMON_HEART, exporter, "smelt_withered_demon_heart");
    }
}