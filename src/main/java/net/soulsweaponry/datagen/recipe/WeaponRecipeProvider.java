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
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.util.ModTags;

public class WeaponRecipeProvider extends FabricRecipeProvider {

    public WeaponRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        BlockRecipes.generateRecipes(exporter);
        ItemRecipes.generateRecipes(exporter);
        GunRecipes.generateRecipes(exporter);
        WeaponRecipes.generateRecipes(exporter);
        ArmorRecipes.generateRecipes(exporter);
    }

    public static void smithingRecipe(Ingredient smithingTemplate, Ingredient base, Ingredient addition, Item output, RecipeCategory recipeCategory, Item itemCriterion, RecipeExporter exporter) {
        SmithingTransformRecipeJsonBuilder.create(smithingTemplate, base, addition, recipeCategory, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, output.toString()));
    }

    public static void smithingRecipeCombat(Ingredient smithingTemplate, Ingredient base, Ingredient addition, Item output, Item itemCriterion, RecipeExporter exporter) {
        SmithingTransformRecipeJsonBuilder.create(smithingTemplate, base, addition, RecipeCategory.COMBAT, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, output.toString()));
    }

    public static void smithingRecipeLordSoul(Ingredient base, Item output, RecipeCategory category, RecipeExporter exporter) {
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), base, Ingredient.fromTag(ModTags.Items.LORD_SOUL), category, output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LORD_SOUL).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, output.toString()));
    }

    public static void smithingRecipeLordSoulCombat(Ingredient base, Item output, RecipeExporter exporter) {
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), base, Ingredient.fromTag(ModTags.Items.LORD_SOUL), RecipeCategory.COMBAT, output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LORD_SOUL).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, output.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, Item itemCriterion, RecipeExporter exporter) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, result.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, TagKey<Item> itemTag, RecipeExporter exporter) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemTag).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, result.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, Item itemCriterion, RecipeExporter exporter, String fileId) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, fileId));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, TagKey<Item> itemTag, RecipeExporter exporter, String fileId) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemTag).build()))
                .offerTo(exporter, new Identifier(SoulsWeaponry.ModId, fileId));
    }
}