package net.soulsweaponry.compat;

import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;

public class SoulsWeaponryREICommonPlugin implements REICommonPlugin {

    public static final Identifier ITEM_UPGRADE_DISPLAY_ID = Identifier.of(SoulsWeaponry.ModId, "item_upgrade_display");

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(ItemUpgradeRecipe.class)
                .filterType(RecipeType.SMITHING)
                .fill(ItemUpgradeDisplay::new);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(ITEM_UPGRADE_DISPLAY_ID, ItemUpgradeDisplay.SERIALIZER);
    }
}