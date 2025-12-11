package net.soulsweaponry.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.soulsweaponry.mixin.BrewingRecipeRegistryAccessor;

public class BetterBrewingRecipe {

    /**
     * Add a potion recipe via forge hooks. This will not automatically creat throwable ones.
     * @param inputPotion potion crafting ingredient
     * @param inputItem item to mix with the potion
     * @param outputPotion output
     */
    public static void addRecipe(Potion inputPotion, Item inputItem, Potion outputPotion) {
        ItemStack input = PotionUtil.setPotion(new ItemStack(Items.POTION), inputPotion);
        ItemStack output = PotionUtil.setPotion(new ItemStack(Items.POTION), outputPotion);
        Ingredient ingredient = Ingredient.ofItems(inputItem);
        BrewingRecipeRegistry.addRecipe(Ingredient.ofStacks(input), ingredient, output);
    }

    /**
     * Register potion recipes and automatically creates throwable recipes
     * (with gunpowder and dragon breath) like in fabric.
     * @param basePotion potion crafting ingredient
     * @param ingredient item crafting ingredient
     * @param resultPotion output
     */
    public static void addPotionRecipe(Potion basePotion, Item ingredient, Potion resultPotion) {
        BrewingRecipeRegistryAccessor.invokeRegisterPotionRecipe(basePotion, ingredient, resultPotion);
    }

    /**
     * Register potion recipes and automatically creates throwable recipes
     * (with gunpowder and dragon breath) like in fabric.
     * Input potion is automatically AWKWARD.
     * @param ingredient item crafting ingredient
     * @param resultPotion output
     */
    public static void addAwkwardRecipe(Item ingredient, Potion resultPotion) {
        addPotionRecipe(Potions.AWKWARD, ingredient, resultPotion);
    }
}