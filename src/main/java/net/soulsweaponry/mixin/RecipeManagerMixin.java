package net.soulsweaponry.mixin;

import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.soulsweaponry.util.RecipeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        // The "map" variable will contain every single recipe generated and/or are in resources folder
        // This removes every recipe from being registered into the game on runtime if they should be based on config
        for (Identifier id : RecipeHandler.RECIPE_IDS.keySet()) {
            boolean removeRecipe = RecipeHandler.RECIPE_IDS.get(id);
            if (removeRecipe) {
                map.remove(id);
            }
        }
    }
}