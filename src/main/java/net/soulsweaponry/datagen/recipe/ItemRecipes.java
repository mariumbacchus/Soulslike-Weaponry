package net.soulsweaponry.datagen.recipe;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.FoodRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;

import static net.minecraft.data.recipe.RecipeGenerator.conditionsFromItemPredicates;

public class ItemRecipes {

    public static void generateRecipes(RegistryEntryLookup<Item> itemLookup, RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.CHUNGUS_DISC)
                .input('#', ItemRegistry.CHUNGUS_EMERALD)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.CHUNGUS_EMERALD).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.FALLEN_ICON_DISC)
                .input('#', ItemRegistry.MOONSTONE)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.DRAUGR_BOSS_DISC)
                .input('#', ItemRegistry.MOONSTONE)
                .input('X', Items.BONE)
                .pattern("#X#")
                .pattern("X X")
                .pattern("#X#")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.IRON_SKULL)
                .input('Y', ModTags.Items.LOST_SOUL)
                .input('I', Items.BONE)
                .input('X', ConventionalItemTags.IRON_INGOTS)
                .pattern("XIX")
                .pattern("IYI")
                .pattern("XIX")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().tag(itemLookup, ModTags.Items.LOST_SOUL).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.DECORATIONS, Items.SOUL_LANTERN)
                .input('#', Items.IRON_NUGGET)
                .input('X', ModTags.Items.LOST_SOUL)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().tag(itemLookup, ModTags.Items.LOST_SOUL).build()
                ))
                .offerTo(recipeExporter, "soul_lantern_lost_soul");

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.SOUL_INGOT)
                .input('C', ConventionalItemTags.IRON_INGOTS)
                .input('#', ModTags.Items.LOST_SOUL)
                .pattern(" # ")
                .pattern("#C#")
                .pattern(" # ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().tag(itemLookup, ModTags.Items.LOST_SOUL).build()
                ))
                .offerTo(recipeExporter);

        // Moonstone tools
        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_AXE)
                .input('/', ConventionalItemTags.WOODEN_RODS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern("## ")
                .pattern("#i ")
                .pattern(" / ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter, "moonstone_axe_left");

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_AXE)
                .input('/', ConventionalItemTags.WOODEN_RODS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern(" ##")
                .pattern(" i#")
                .pattern(" / ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter, "moonstone_axe_right");

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_HOE)
                .input('/', ConventionalItemTags.WOODEN_RODS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern(" ##")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter, "moonstone_hoe_right");

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_HOE)
                .input('/', ConventionalItemTags.WOODEN_RODS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern("## ")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter, "moonstone_hoe_left");

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_PICKAXE)
                .input('/', ConventionalItemTags.WOODEN_RODS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern("###")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.TOOLS, ItemRegistry.MOONSTONE_SHOVEL)
                .input('/', ConventionalItemTags.WOODEN_RODS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('i', ItemRegistry.VERGLAS)
                .pattern(" # ")
                .pattern(" i ")
                .pattern(" / ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ItemRegistry.MOONSTONE_RING)
                .input('D', ItemRegistry.ARKENSTONE)
                .input('i', ConventionalItemTags.IRON_INGOTS)
                .pattern("Di ")
                .pattern("i i")
                .pattern(" i ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.ARKENSTONE).build()
                ))
                .offerTo(recipeExporter, "moonstone_ring_left");

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ItemRegistry.MOONSTONE_RING)
                .input('D', ItemRegistry.ARKENSTONE)
                .input('i', ConventionalItemTags.IRON_INGOTS)
                .pattern(" iD")
                .pattern("i i")
                .pattern(" i ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.ARKENSTONE).build()
                ))
                .offerTo(recipeExporter, "moonstone_ring_right");

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.TOOLS, ItemRegistry.BOSS_COMPASS)
                .input('#', ItemRegistry.MOONSTONE)
                .input('X', Items.COMPASS)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ItemRegistry.SKOFNUNG_STONE)
                .input('C', ItemRegistry.MOONSTONE)
                .input('#', ModTags.Items.LOST_SOUL)
                .input('D', ItemRegistry.VERGLAS)
                .pattern("#C#")
                .pattern("CDC")
                .pattern("#C#")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.VERGLAS).build()
                ))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ItemRegistry.CHAOS_ORB)
                .input(ItemRegistry.ESSENCE_OF_LUMINESCENCE)
                .input(ItemRegistry.WITHERED_DEMON_HEART)
                .input(ItemRegistry.ARKENSTONE)
                .input(ArmorRegistry.CHAOS_CROWN)
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.ESSENCE_OF_LUMINESCENCE).build()
                ))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.DEMON_CHUNK)
                .input(ItemRegistry.MOLTEN_DEMON_HEART, 4)
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, FoodRegistry.DEMON_HEART).build()
                ))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, Items.PURPLE_DYE)
                .input(BlockRegistry.HYDRANGEA)
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, BlockRegistry.HYDRANGEA).build()
                ))
                .offerTo(recipeExporter, "hydrangea_dye");

        ShapelessRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, Items.PURPLE_DYE, 2)
                .input(BlockRegistry.OLEANDER)
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, BlockRegistry.OLEANDER).build()
                ))
                .offerTo(recipeExporter, "oleander_dye");

        ShapelessRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.MOONSTONE, 9)
                .input(BlockRegistry.MOONSTONE_BLOCK)
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.MOONSTONE).build()
                ))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.VERGLAS, 9)
                .input(BlockRegistry.VERGLAS_BLOCK)
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.VERGLAS).build()
                ))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.CHUNGUS_EMERALD, 9)
                .input(BlockRegistry.CHUNGUS_EMERALD_BLOCK)
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.CHUNGUS_EMERALD).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.GLASS_VIAL, 5)
                .input('#', Items.GLASS)
                .pattern("# #")
                .pattern("# #")
                .pattern(" # ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, Items.GLASS).build()
                ))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, Items.PAPER, 1)
                .input(ItemRegistry.BLOOD_VIAL_RECIPE_PAGE)
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.BLOOD_VIAL_RECIPE_PAGE).build()
                ))
                .offerTo(recipeExporter, "special_page_to_paper");

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, ItemRegistry.TWINKLING_TITANITE, 1)
                .input('#', ItemRegistry.MOONSTONE)
                .input('S', ModTags.Items.LORD_SOUL)
                .input('V', ItemRegistry.VERGLAS)
                .pattern(" #V")
                .pattern("#S#")
                .pattern("V# ")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().tag(itemLookup, ModTags.Items.LORD_SOUL).build()
                ))
                .offerTo(recipeExporter);

        WeaponRecipeProvider.smeltingRecipe(
                Ingredient.ofItems(ItemRegistry.DEMON_CHUNK),
                ItemRegistry.CRIMSON_INGOT,
                0.1f,
                200,
                FoodRegistry.DEMON_HEART,
                recipeExporter,
                itemLookup
        );

        WeaponRecipeProvider.smeltingRecipe(
                Ingredient.ofItems(Items.SOUL_SAND),
                ItemRegistry.LOST_SOUL,
                0.1f,
                200,
                Items.SOUL_SAND,
                recipeExporter,
                itemLookup
        );

        WeaponRecipeProvider.smeltingRecipe(
                Ingredient.fromTag(itemLookup.getOrThrow(ModTags.Items.DEMON_HEARTS)),
                ItemRegistry.MOLTEN_DEMON_HEART,
                0.1f,
                200,
                ModTags.Items.DEMON_HEARTS,
                recipeExporter,
                itemLookup
        );

        WeaponRecipeProvider.smeltingRecipe(
                Ingredient.ofItems(ItemRegistry.WITHERED_DEMON_HEART),
                Items.NETHERITE_INGOT,
                10f,
                500,
                ItemRegistry.WITHERED_DEMON_HEART,
                recipeExporter,
                "smelt_withered_demon_heart",
                itemLookup
        );
    }
}