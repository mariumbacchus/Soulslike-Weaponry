package net.soulsweaponry.datagen.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.SmithingRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.util.ModTags;

import java.util.function.Consumer;

public class WeaponRecipeProvider extends FabricRecipeProvider {

    public WeaponRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> consumer) {
        BlockRecipes.generateRecipes(consumer);
        ItemRecipes.generateRecipes(consumer);
        GunRecipes.generateRecipes(consumer);
        WeaponRecipes.generateRecipes(consumer);
        ArmorRecipes.generateRecipes(consumer);
    }

    public static void smithingRecipe(Ingredient base, Ingredient addition, Item output, Item itemCriterion, Consumer<RecipeJsonProvider> consumer) {
        SmithingRecipeJsonBuilder.create(base, addition, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, output.toString()));
    }

    public static void smithingRecipeLordSoul(Ingredient base, Item output, Consumer<RecipeJsonProvider> consumer) {
        SmithingRecipeJsonBuilder.create(base, Ingredient.fromTag(ModTags.Items.LORD_SOUL), output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LORD_SOUL).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, output.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, Item itemCriterion, Consumer<RecipeJsonProvider> consumer) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, result.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, TagKey<Item> itemTag, Consumer<RecipeJsonProvider> consumer) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemTag).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, result.toString()));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, Item itemCriterion, Consumer<RecipeJsonProvider> consumer, String fileId) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, fileId));
    }

    public static void smeltingRecipe(Ingredient ingredient, Item result, float expGain, int cookingTime, TagKey<Item> itemTag, Consumer<RecipeJsonProvider> consumer, String fileId) {
        CookingRecipeJsonBuilder.createSmelting(ingredient, result, expGain, cookingTime)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(itemTag).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, fileId));
    }
}