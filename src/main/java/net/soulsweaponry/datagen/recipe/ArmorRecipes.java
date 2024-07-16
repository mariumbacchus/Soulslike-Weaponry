package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.function.Consumer;

// Only extends RecipeProvider to access protected static methods
public class ArmorRecipes extends RecipeProvider {

    public ArmorRecipes(DataGenerator root) {
        super(root);
    }

    public static void generateRecipes(Consumer<RecipeJsonProvider> consumer) {
        if (!CommonConfig.RECIPE_ARKENPLATE.get()) {
            ShapedRecipeJsonBuilder.create(ItemRegistry.ARKENPLATE.get())
                    .input('#', Items.IRON_INGOT)
                    .input('n', Items.NETHERITE_INGOT)
                    .input('N', ItemRegistry.ARKENSTONE.get())
                    .input('D', ItemRegistry.SOUL_INGOT.get())
                    .pattern("# #")
                    .pattern("nNn")
                    .pattern("D#D")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(ItemRegistry.ARKENSTONE.get()).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_WITHERED_CHEST.get()) {
            ShapedRecipeJsonBuilder.create(ItemRegistry.WITHERED_CHEST.get())
                    .input('#', Items.NETHER_BRICK)
                    .input('n', Items.NETHERITE_INGOT)
                    .input('N', ItemRegistry.WITHERED_DEMON_HEART.get())
                    .input('D', ItemRegistry.CRIMSON_INGOT.get())
                    .pattern("# #")
                    .pattern("nNn")
                    .pattern("D#D")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(ItemRegistry.WITHERED_DEMON_HEART.get()).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_ENHANCED_ARKENPLATE.get()) {
            WeaponRecipeProvider.smithingRecipe(Ingredient.ofItems(ItemRegistry.ARKENPLATE.get()), Ingredient.ofItems(ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get()), ItemRegistry.ENHANCED_ARKENPLATE.get(), ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get(), "infused_arkenplate", consumer);
        }
        if (!CommonConfig.RECIPE_ENHANCED_WITHERED_CHEST.get()) {
            WeaponRecipeProvider.smithingRecipe(Ingredient.ofItems(ItemRegistry.WITHERED_CHEST.get()), Ingredient.ofItems(ItemRegistry.LORD_SOUL_DAY_STALKER.get()), ItemRegistry.ENHANCED_WITHERED_CHEST.get(), ItemRegistry.LORD_SOUL_DAY_STALKER.get(), "infused_withered_chest", consumer);
        }
    }
}
