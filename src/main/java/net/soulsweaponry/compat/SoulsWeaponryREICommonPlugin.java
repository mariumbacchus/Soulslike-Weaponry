package net.soulsweaponry.compat;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;

import java.util.List;
import java.util.Optional;

public class SoulsWeaponryREICommonPlugin implements REICommonPlugin {

    public static final Identifier ITEM_UPGRADE_DISPLAY_ID = Identifier.of(SoulsWeaponry.ModId, "item_upgrade_display");

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(ItemUpgradeRecipe.class)
                .filterType(RecipeType.SMITHING)
                .fill(entry -> {
                    try {
                        return new ItemUpgradeDisplay(entry);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        return new ItemUpgradeDisplay(
                                List.of(EntryIngredient.empty(), EntryIngredient.empty(), EntryIngredient.empty()),
                                List.of(EntryIngredients.of(Items.BARRIER)),
                                Optional.of(entry.id().getValue()),
                                0f, 0f
                        );
                    }
                });
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(ITEM_UPGRADE_DISPLAY_ID, ItemUpgradeDisplay.SERIALIZER);
    }
}