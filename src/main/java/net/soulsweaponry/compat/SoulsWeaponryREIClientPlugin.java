package net.soulsweaponry.compat;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;

public class SoulsWeaponryREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ItemUpgradeCategory());
        registry.addWorkstations(ItemUpgradeREIIds.ITEM_UPGRADE, EntryStacks.of(Blocks.SMITHING_TABLE));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(
                screen -> new Rectangle(134, 33, 18, 18),
                SmithingScreen.class,
                ItemUpgradeREIIds.ITEM_UPGRADE
        );
    }
}