package net.soulsweaponry.datagen.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.soulsweaponry.util.ModTags;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.server.recipe.RecipeGenerator.conditionsFromItemPredicates;
import static net.minecraft.data.server.recipe.RecipeGenerator.getRecipeName;

public class WeaponRecipeProvider extends FabricRecipeProvider {

    public WeaponRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                RegistryEntryLookup<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
                BlockRecipes.generateRecipes(itemLookup, exporter);
                ItemRecipes.generateRecipes(itemLookup, exporter);
                GunRecipes.generateRecipes(itemLookup, exporter);
                WeaponRecipes.generateRecipes(itemLookup, exporter);
                ArmorRecipes.generateRecipes(itemLookup, exporter);
                UpgradeRecipes.generateRecipes(itemLookup, exporter);
            }
        };
    }

    public static void smithingRecipe(Ingredient smithingTemplate, Ingredient base, Ingredient addition, Item output, RecipeCategory recipeCategory, Item itemCriterion, RecipeExporter recipeExporter, RegistryEntryLookup<Item> itemLookup) {
        SmithingTransformRecipeJsonBuilder.create(smithingTemplate, base, addition, recipeCategory, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemLookup, itemCriterion).build()))
                .offerTo(recipeExporter, getRecipeName(output));
    }

    public static void smithingRecipeCombat(Ingredient smithingTemplate, Ingredient base, Ingredient addition, Item output, Item itemCriterion, RecipeExporter recipeExporter, RegistryEntryLookup<Item> itemLookup) {
        SmithingTransformRecipeJsonBuilder.create(smithingTemplate, base, addition, RecipeCategory.COMBAT, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemLookup, itemCriterion).build()))
                .offerTo(recipeExporter, getRecipeName(output));
    }

    public static void smithingRecipeLordSoul(Ingredient base, Item output, RecipeCategory category, RecipeExporter recipeExporter, RegistryEntryLookup<Item> itemLookup) {
        RegistryEntryList.Named<Item> lordSoulEntries = itemLookup.getOrThrow(ModTags.Items.LORD_SOUL);
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), base, Ingredient.fromTag(lordSoulEntries), category, output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemLookup, ModTags.Items.LORD_SOUL).build()))
                .offerTo(recipeExporter, getRecipeName(output));
    }

    public static void smithingRecipeLordSoulCombat(Ingredient base, Item output, RecipeExporter recipeExporter, RegistryEntryLookup<Item> itemLookup) {
        RegistryEntryList.Named<Item> lordSoulEntries = itemLookup.getOrThrow(ModTags.Items.LORD_SOUL);
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), base, Ingredient.fromTag(lordSoulEntries), RecipeCategory.COMBAT, output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemLookup, ModTags.Items.LORD_SOUL).build()))
                .offerTo(recipeExporter, getRecipeName(output));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, Item itemCriterion, RecipeExporter recipeExporter, RegistryEntryLookup<Item> itemLookup) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemLookup, itemCriterion).build()))
                .offerTo(recipeExporter, getRecipeName(result));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, TagKey<Item> itemTag, RecipeExporter recipeExporter, RegistryEntryLookup<Item> itemLookup) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemLookup, itemTag).build()))
                .offerTo(recipeExporter, getRecipeName(result));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, Item itemCriterion, RecipeExporter recipeExporter, String fileId, RegistryEntryLookup<Item> itemLookup) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemLookup, itemCriterion).build()))
                .offerTo(recipeExporter, fileId);
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, TagKey<Item> itemTag, RecipeExporter recipeExporter, String fileId, RegistryEntryLookup<Item> itemLookup) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemLookup, itemTag).build()))
                .offerTo(recipeExporter, fileId);
    }

    @Override
    public String getName() {
        return "Soulslike Weaponry Recipes";
    }
}