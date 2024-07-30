package net.soulsweaponry.util;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;

import java.util.HashMap;
import java.util.Map;

public class RecipeHandler {

    /**
     * Map over item ids and a boolean determining whether the recipe (with the corresponding identifier) should
     * be removed during runtime or not. See {@link net.soulsweaponry.mixin.RecipeManagerMixin} for usage.
     */
    public static final Map<Identifier, Boolean> RECIPE_IDS = new HashMap<>();

    static {
        RECIPE_IDS.put(new Identifier(SoulsWeaponry.ModId, "silver_bullet_iron_ingot"), ConfigConstructor.disable_gun_recipes);
        RECIPE_IDS.put(new Identifier(SoulsWeaponry.ModId, "leviathan_axe_left"), ConfigConstructor.disable_recipe_leviathan_axe);
        RECIPE_IDS.put(new Identifier(SoulsWeaponry.ModId, "leviathan_axe_right"), ConfigConstructor.disable_recipe_leviathan_axe);
    }
}