package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ItemRegistry;

public class ArmorRecipes {

    public static void generateRecipes(RecipeExporter exporter) {
        if (!ConfigConstructor.disable_recipe_arkenplate) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemRegistry.ARKENPLATE)
                    .input('#', Items.IRON_INGOT)
                    .input('n', Items.NETHERITE_INGOT)
                    .input('N', ItemRegistry.ARKENSTONE)
                    .input('D', ItemRegistry.SOUL_INGOT)
                    .pattern("# #")
                    .pattern("nNn")
                    .pattern("D#D")
                    .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(ItemRegistry.ARKENSTONE).build()))
                    .offerTo(exporter);
        }
        if (!ConfigConstructor.disable_recipe_withered_chest) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemRegistry.WITHERED_CHEST)
                    .input('#', Items.NETHER_BRICK)
                    .input('n', Items.NETHERITE_INGOT)
                    .input('N', ItemRegistry.WITHERED_DEMON_HEART)
                    .input('D', ItemRegistry.CRIMSON_INGOT)
                    .pattern("# #")
                    .pattern("nNn")
                    .pattern("D#D")
                    .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(ItemRegistry.WITHERED_DEMON_HEART).build()))
                    .offerTo(exporter);
        }
        if (!ConfigConstructor.disable_recipe_enhanced_arkenplate) {
            WeaponRecipeProvider.smithingRecipeCombat(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.ofItems(ItemRegistry.ARKENPLATE), Ingredient.ofItems(ItemRegistry.LORD_SOUL_NIGHT_PROWLER), ItemRegistry.ENHANCED_ARKENPLATE, ItemRegistry.LORD_SOUL_NIGHT_PROWLER, "infused_arkenplate", exporter);
        }
        if (!ConfigConstructor.disable_recipe_enhanced_withered_chest) {
            WeaponRecipeProvider.smithingRecipeCombat(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.ofItems(ItemRegistry.WITHERED_CHEST), Ingredient.ofItems(ItemRegistry.LORD_SOUL_DAY_STALKER), ItemRegistry.ENHANCED_WITHERED_CHEST, ItemRegistry.LORD_SOUL_DAY_STALKER, "infused_withered_chest", exporter);
        }
    }
}