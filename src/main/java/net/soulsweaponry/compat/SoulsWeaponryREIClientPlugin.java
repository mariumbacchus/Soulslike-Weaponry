package net.soulsweaponry.compat;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.recipe.RecipeType;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;

public class SoulsWeaponryREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ItemUpgradeCategory());
        registry.addWorkstations(ItemUpgradeCategory.ITEM_UPGRADE, EntryStacks.of(Blocks.SMITHING_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(ItemUpgradeRecipe.class, RecipeType.SMITHING, ItemUpgradeDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(
                screen -> new Rectangle(134, 33, 18, 18),
                SmithingScreen.class,
                ItemUpgradeCategory.ITEM_UPGRADE
        );
    }
}