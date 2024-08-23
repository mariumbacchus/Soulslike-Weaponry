package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.util.ModTags;

import java.util.function.Consumer;

public class WeaponRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public WeaponRecipeProvider(DataOutput output) {
        super(output);
    }

    @Override
    protected void generate(Consumer<RecipeJsonProvider> consumer) {
        BlockRecipes.generateRecipes(consumer);
        ItemRecipes.generateRecipes(consumer);
        GunRecipes.generateRecipes(consumer);
        WeaponRecipes.generateRecipes(consumer);
        ArmorRecipes.generateRecipes(consumer);
    }

    public static void smithingRecipe(Ingredient smithingTemplate, Ingredient base, Ingredient addition, Item output, RecipeCategory recipeCategory, Item itemCriterion, Consumer<RecipeJsonProvider> consumer) {
        SmithingTransformRecipeJsonBuilder.create(smithingTemplate, base, addition, recipeCategory, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, output.toString()));
    }

    public static void smithingRecipeCombat(Ingredient smithingTemplate, Ingredient base, Ingredient addition, Item output, Item itemCriterion, Consumer<RecipeJsonProvider> consumer) {
        SmithingTransformRecipeJsonBuilder.create(smithingTemplate, base, addition, RecipeCategory.COMBAT, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, output.toString()));
    }

    public static void smithingRecipeLordSoul(Ingredient base, Item output, RecipeCategory category, Consumer<RecipeJsonProvider> consumer) {
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), base, Ingredient.fromTag(ModTags.Items.LORD_SOUL), category, output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LORD_SOUL).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, output.toString()));
    }

    public static void smithingRecipeLordSoulCombat(Ingredient base, Item output, Consumer<RecipeJsonProvider> consumer) {
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), base, Ingredient.fromTag(ModTags.Items.LORD_SOUL), RecipeCategory.COMBAT, output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LORD_SOUL).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, output.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, Item itemCriterion, Consumer<RecipeJsonProvider> consumer) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, result.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, TagKey<Item> itemTag, Consumer<RecipeJsonProvider> consumer) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemTag).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, result.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, Item itemCriterion, Consumer<RecipeJsonProvider> consumer, String fileId) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, fileId));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, TagKey<Item> itemTag, Consumer<RecipeJsonProvider> consumer, String fileId) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, RecipeCategory.MISC, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemTag).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, fileId));
    }
}
