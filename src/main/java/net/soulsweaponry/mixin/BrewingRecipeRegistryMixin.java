package net.soulsweaponry.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.EffectRegistry;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {

    static {
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(Potions.AWKWARD, BlockRegistry.HYDRANGEA.asItem(), EffectRegistry.WARDING);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(Potions.AWKWARD, BlockRegistry.OLEANDER.asItem(), EffectRegistry.WARDING);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(EffectRegistry.WARDING, Items.GLOWSTONE_DUST, EffectRegistry.STRONG_WARDING);
        BrewingRecipeRegistryInvoker.invokeRegisterPotionRecipe(EffectRegistry.WARDING, Items.REDSTONE, EffectRegistry.LONG_WARDING);
    }
}
