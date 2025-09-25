package net.soulsweaponry.registry;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;

public class RecipeSerializerRegistry {

    public static final RecipeSerializer<ItemUpgradeRecipe> SMITHING_ITEM_UPGRADE = register("smithing_item_upgrade", new ItemUpgradeRecipe.Serializer());

    public static <T extends Recipe<?>, M extends RecipeSerializer<T>> RecipeSerializer<T> register(String id, M recipe) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(SoulsWeaponry.ModId, id), recipe);
    }

    public static void init() {}
}
