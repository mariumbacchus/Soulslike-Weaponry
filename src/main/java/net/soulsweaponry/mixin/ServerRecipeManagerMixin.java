package net.soulsweaponry.mixin;

import net.minecraft.recipe.PreparedRecipes;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.util.RecipeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(ServerRecipeManager.class)
public class ServerRecipeManagerMixin {

    @ModifyVariable(
            method = "apply(Lnet/minecraft/recipe/PreparedRecipes;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private PreparedRecipes interceptApply(PreparedRecipes preparedRecipes) {
        // The "map" variable will contain every single recipe generated and/or are in resources folder
        // This removes every recipe from being registered into the game on runtime if they should be based on config

        // In 1.21.2+ recipes are already deserialized at this point, so we filter PreparedRecipes instead
        Collection<RecipeEntry<?>> all = preparedRecipes.recipes();
        List<RecipeEntry<?>> kept = new ArrayList<>(all.size());

        for (RecipeEntry<?> entry : all) {
            Identifier id = entry.id().getValue(); // RegistryKey<Recipe<?>> -> Identifier
            boolean removeRecipe = RecipeHandler.RECIPE_IDS.getOrDefault(id, false);
            if (!removeRecipe) {
                kept.add(entry);
            }
        }

        return PreparedRecipes.of(kept);
    }
}