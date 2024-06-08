package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.function.Consumer;

// Only extends RecipeProvider to access protected static methods
public class ArmorRecipes extends RecipeProvider {

    public ArmorRecipes(DataGenerator root) {
        super(root);
    }

    public static void generateRecipes(Consumer<RecipeJsonProvider> consumer) {
        if (!CommonConfig.RECIPE_ENHANCED_ARKENPLATE.get()) {
            WeaponRecipeProvider.smithingRecipe(Ingredient.ofItems(ItemRegistry.ARKENPLATE.get()), Ingredient.ofItems(ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get()), ItemRegistry.ENHANCED_ARKENPLATE.get(), ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get(), "infused_arkenplate", consumer);
        }
        if (!CommonConfig.RECIPE_ENHANCED_WITHERED_CHEST.get()) {
            WeaponRecipeProvider.smithingRecipe(Ingredient.ofItems(ItemRegistry.WITHERED_CHEST.get()), Ingredient.ofItems(ItemRegistry.LORD_SOUL_DAY_STALKER.get()), ItemRegistry.ENHANCED_WITHERED_CHEST.get(), ItemRegistry.LORD_SOUL_DAY_STALKER.get(), "infused_withered_chest", consumer);
        }
    }
}
