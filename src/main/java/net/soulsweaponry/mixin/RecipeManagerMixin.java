package net.soulsweaponry.mixin;

import java.util.ArrayList;
import java.util.Map;

import net.soulsweaponry.config.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.RecipeRegistry;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        if (!CommonConfig.DISABLE_WEAPON_RECIPES.get()) {
            ArrayList<ArrayList<Object>> recipes = RecipeRegistry.recipes;
            if (!CommonConfig.DISABLE_GUN_RECIPES.get()) {
                map.put(new Identifier(SoulsWeaponry.ModId, "hunter_cannon"), RecipeRegistry.HUNTER_CANNON_RECIPE);
                map.put(new Identifier(SoulsWeaponry.ModId, "hunter_pistol"), RecipeRegistry.HUNTER_PISTOL_RECIPE);
                map.put(new Identifier(SoulsWeaponry.ModId, "gatling_gun"), RecipeRegistry.GATLING_GUN_RECIPE);
                map.put(new Identifier(SoulsWeaponry.ModId, "blunderbuss"), RecipeRegistry.BLUNDERBUSS_RECIPE);
            }
            for (ArrayList<Object> recipe : recipes) {
                if ((boolean) recipe.get(0)) {
                    map.put(new Identifier(SoulsWeaponry.ModId, (String) recipe.get(1)), (JsonObject) recipe.get(2));
                }
            }
        }
        if (RecipeRegistry.BEWITCHMENT_MOLTEN_DEMON_HEART != null) {
            map.put(new Identifier(SoulsWeaponry.ModId, "bewitchment_molten_demon_heart"), RecipeRegistry.BEWITCHMENT_MOLTEN_DEMON_HEART);
        }
        if (RecipeRegistry.BEWITCHMENT_SILVER_BULLET != null) {
            map.put(new Identifier(SoulsWeaponry.ModId, "bewitchment_silver_bullet"), RecipeRegistry.BEWITCHMENT_SILVER_BULLET);
        }
    }
}
