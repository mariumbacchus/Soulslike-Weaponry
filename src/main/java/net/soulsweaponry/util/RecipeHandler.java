package net.soulsweaponry.util;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.config.GunConfig;

import java.util.HashMap;
import java.util.Map;

public class RecipeHandler {

    /**
     * Map over item ids and a boolean determining whether the recipe (with the corresponding identifier) should
     * be removed during runtime or not. See {@link net.soulsweaponry.mixin.RecipeManagerMixin} for usage.
     */
    public static final Map<Identifier, Boolean> RECIPE_IDS = new HashMap<>();

    static {
        RECIPE_IDS.put(Identifier.of(SoulsWeaponry.ModId, "silver_bullet_iron_ingot"), GunConfig.disable_gun_recipes);
        RECIPE_IDS.put(Identifier.of(SoulsWeaponry.ModId, "leviathan_axe_left"), WeaponConfig.disable_recipe_leviathan_axe);
        RECIPE_IDS.put(Identifier.of(SoulsWeaponry.ModId, "leviathan_axe_right"), WeaponConfig.disable_recipe_leviathan_axe);
    }
}