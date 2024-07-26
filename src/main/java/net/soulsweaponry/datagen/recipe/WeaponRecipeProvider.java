package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.SmithingRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.function.Consumer;

public class WeaponRecipeProvider extends RecipeProvider {

    public WeaponRecipeProvider(DataGenerator root) {
        super(root);
    }

    @Override
    protected void generate(Consumer<RecipeJsonProvider> consumer) {
        GunRecipes.generateRecipes(consumer);
        WeaponRecipes.generateRecipes(consumer);
        ArmorRecipes.generateRecipes(consumer);
        CookingRecipeJsonBuilder.createSmelting(
                Ingredient.fromTag(ModTags.Items.DEMON_HEARTS), ItemRegistry.MOLTEN_DEMON_HEART.get(), 0.1f, 200)
                .criterion("has_demon_heart", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                                .tag(ModTags.Items.DEMON_HEARTS).build()))
                        .offerTo(consumer);
    }

    public static void smithingRecipe(Ingredient base, Ingredient addition, Item output, Item itemCriterion, Consumer<RecipeJsonProvider> consumer) {
        SmithingRecipeJsonBuilder.create(base, addition, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(consumer, output.toString());
    }

    public static void smithingRecipeLordSoul(Ingredient base, Item output, Consumer<RecipeJsonProvider> consumer) {
        SmithingRecipeJsonBuilder.create(base, Ingredient.fromTag(ModTags.Items.LORD_SOUL), output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LORD_SOUL).build()))
                .offerTo(consumer, output.toString());
    }
}
