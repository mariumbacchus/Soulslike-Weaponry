package net.soulsweaponry.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Lists;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.EffectRegistry;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeMixin {
    
    @Shadow
    private static final List<Recipe<Potion>> POTION_RECIPES = Lists.newArrayList();
    
    @Shadow
    private static void registerPotionRecipe(Potion input, Item item, Potion output) {
        POTION_RECIPES.add(new Recipe<Potion>(input, Ingredient.ofItems(item), output));
    }

    static class Recipe<T> {
        final T input;
        final Ingredient ingredient;
        final T output;

        public Recipe(T input, Ingredient ingredient, T output) {
            this.input = input;
            this.ingredient = ingredient;
            this.output = output;
        }
    }

    static {
        registerPotionRecipe(Potions.AWKWARD, BlockRegistry.HYDRANGEA.asItem(), EffectRegistry.WARDING);
        registerPotionRecipe(Potions.AWKWARD, BlockRegistry.OLEANDER.asItem(), EffectRegistry.WARDING);
        registerPotionRecipe(EffectRegistry.WARDING, Items.GLOWSTONE_DUST, EffectRegistry.STRONG_WARDING);
        registerPotionRecipe(EffectRegistry.WARDING, Items.REDSTONE, EffectRegistry.LONG_WARDING);
    }
}
