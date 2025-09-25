package net.soulsweaponry.datagen.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class WeaponRecipeProvider extends FabricRecipeProvider {

    public WeaponRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        BlockRecipes.generateRecipes(recipeExporter);
        ItemRecipes.generateRecipes(recipeExporter);
        GunRecipes.generateRecipes(recipeExporter);
        WeaponRecipes.generateRecipes(recipeExporter);
        ArmorRecipes.generateRecipes(recipeExporter);
        UpgradeRecipes.generateRecipes(recipeExporter);
    }

    public static void smithingRecipe(Ingredient smithingTemplate, Ingredient base, Ingredient addition, Item output, RecipeCategory recipeCategory, Item itemCriterion, RecipeExporter recipeExporter) {
        SmithingTransformRecipeJsonBuilder.create(smithingTemplate, base, addition, recipeCategory, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(recipeExporter, Identifier.of(output.toString()));
    }

    public static void smithingRecipeCombat(Ingredient smithingTemplate, Ingredient base, Ingredient addition, Item output, Item itemCriterion, RecipeExporter recipeExporter) {
        SmithingTransformRecipeJsonBuilder.create(smithingTemplate, base, addition, RecipeCategory.COMBAT, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(recipeExporter, Identifier.of(output.toString()));
    }

    public static void smithingRecipeLordSoul(Ingredient base, Item output, RecipeCategory category, RecipeExporter recipeExporter) {
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), base, Ingredient.fromTag(ModTags.Items.LORD_SOUL), category, output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LORD_SOUL).build()))
                .offerTo(recipeExporter, Identifier.of(output.toString()));
    }

    public static void smithingRecipeLordSoulCombat(Ingredient base, Item output, RecipeExporter recipeExporter) {
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), base, Ingredient.fromTag(ModTags.Items.LORD_SOUL), RecipeCategory.COMBAT, output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LORD_SOUL).build()))
                .offerTo(recipeExporter, Identifier.of(output.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, Item itemCriterion, RecipeExporter recipeExporter) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(recipeExporter, Identifier.of(result.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, TagKey<Item> itemTag, RecipeExporter recipeExporter) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemTag).build()))
                .offerTo(recipeExporter, Identifier.of(result.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, Item itemCriterion, RecipeExporter recipeExporter, String fileId) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(recipeExporter, Identifier.of(SoulsWeaponry.ModId, fileId));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, TagKey<Item> itemTag, RecipeExporter recipeExporter, String fileId) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemTag).build()))
                .offerTo(recipeExporter, Identifier.of(SoulsWeaponry.ModId, fileId));
    }
}