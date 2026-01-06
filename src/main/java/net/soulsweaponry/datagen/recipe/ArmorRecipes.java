package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.ItemRegistry;

import static net.minecraft.data.recipe.RecipeGenerator.conditionsFromItemPredicates;

public class ArmorRecipes {

    public static void generateRecipes(RegistryEntryLookup<Item> itemLookup, RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.SOUL_INGOT_HELMET)
                .input('#', ItemRegistry.SOUL_INGOT)
                .pattern("###")
                .pattern("# #")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.SOUL_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.SOUL_INGOT_CHESTPLATE)
                .input('#', ItemRegistry.SOUL_INGOT)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.SOUL_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.SOUL_INGOT_LEGGINGS)
                .input('#', ItemRegistry.SOUL_INGOT)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.SOUL_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.SOUL_INGOT_BOOTS)
                .input('#', ItemRegistry.SOUL_INGOT)
                .pattern("# #")
                .pattern("# #")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.SOUL_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.SOUL_ROBES_HELMET)
                .input('#', ItemRegistry.SOUL_INGOT)
                .input('L', Items.LEATHER)
                .pattern("#L#")
                .pattern("L L")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.SOUL_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.SOUL_ROBES_CHESTPLATE)
                .input('#', ItemRegistry.SOUL_INGOT)
                .input('L', Items.LEATHER)
                .pattern("# #")
                .pattern("L#L")
                .pattern("LLL")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.SOUL_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.SOUL_ROBES_LEGGINGS)
                .input('#', ItemRegistry.SOUL_INGOT)
                .input('L', Items.LEATHER)
                .pattern("#L#")
                .pattern("L L")
                .pattern("L L")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.SOUL_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.SOUL_ROBES_BOOTS)
                .input('#', ItemRegistry.SOUL_INGOT)
                .input('L', Items.LEATHER)
                .pattern("# #")
                .pattern("L L")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.SOUL_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.FORLORN_HELMET)
                .input('#', ItemRegistry.CRIMSON_INGOT)
                .input('L', ItemRegistry.SOUL_INGOT)
                .pattern("#L#")
                .pattern("L L")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.CRIMSON_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.FORLORN_CHESTPLATE)
                .input('#', ItemRegistry.CRIMSON_INGOT)
                .input('L', ItemRegistry.SOUL_INGOT)
                .pattern("# #")
                .pattern("L#L")
                .pattern("LLL")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.CRIMSON_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.FORLORN_LEGGINGS)
                .input('#', ItemRegistry.CRIMSON_INGOT)
                .input('L', ItemRegistry.SOUL_INGOT)
                .pattern("#L#")
                .pattern("L L")
                .pattern("L L")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.CRIMSON_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.FORLORN_BOOTS)
                .input('#', ItemRegistry.CRIMSON_INGOT)
                .input('L', ItemRegistry.SOUL_INGOT)
                .pattern("# #")
                .pattern("L L")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.CRIMSON_INGOT).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.ARKENPLATE)
                .input('#', Items.IRON_INGOT)
                .input('n', Items.NETHERITE_INGOT)
                .input('N', ItemRegistry.ARKENSTONE)
                .input('D', ItemRegistry.SOUL_INGOT)
                .pattern("# #")
                .pattern("nNn")
                .pattern("D#D")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.ARKENSTONE).build()
                ))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.COMBAT, ArmorRegistry.WITHERED_CHEST)
                .input('#', Items.NETHER_BRICK)
                .input('n', Items.NETHERITE_INGOT)
                .input('N', ItemRegistry.WITHERED_DEMON_HEART)
                .input('D', ItemRegistry.CRIMSON_INGOT)
                .pattern("# #")
                .pattern("nNn")
                .pattern("D#D")
                .criterion("has_item", conditionsFromItemPredicates(
                        ItemPredicate.Builder.create().items(itemLookup, ItemRegistry.WITHERED_DEMON_HEART).build()
                ))
                .offerTo(recipeExporter);

        WeaponRecipeProvider.smithingRecipeCombat(
                Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.ofItems(ArmorRegistry.ARKENPLATE),
                Ingredient.ofItems(ItemRegistry.LORD_SOUL_NIGHT_PROWLER),
                ArmorRegistry.ENHANCED_ARKENPLATE,
                ItemRegistry.LORD_SOUL_NIGHT_PROWLER,
                recipeExporter, itemLookup
        );

        WeaponRecipeProvider.smithingRecipeCombat(
                Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.ofItems(Items.NETHERITE_HELMET),
                Ingredient.ofItems(ArmorRegistry.CHAOS_CROWN),
                ArmorRegistry.CHAOS_HELMET,
                ArmorRegistry.CHAOS_CROWN,
                recipeExporter, itemLookup
        );

        WeaponRecipeProvider.smithingRecipeCombat(
                Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.ofItems(ArmorRegistry.WITHERED_CHEST),
                Ingredient.ofItems(ItemRegistry.LORD_SOUL_DAY_STALKER),
                ArmorRegistry.ENHANCED_WITHERED_CHEST,
                ItemRegistry.LORD_SOUL_DAY_STALKER,
                recipeExporter, itemLookup
        );
    }
}