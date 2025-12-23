package net.soulsweaponry.compat;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;
import net.soulsweaponry.registry.ComponentRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemUpgradeDisplay extends BasicDisplay {

    //TODO fix compat
    public ItemUpgradeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public ItemUpgradeDisplay(RecipeEntry<ItemUpgradeRecipe> recipe) {
        super(getInputList(recipe.value()),
                List.of(EntryIngredient.of(EntryStacks.of(buildPreview(recipe.value())))));
    }

    private static List<EntryIngredient> getInputList(ItemUpgradeRecipe r) {
        if (r == null) return Collections.emptyList();
        List<EntryIngredient> inputs = new ArrayList<>(3);
        inputs.add(EntryIngredients.ofIngredient(r.template()));
        inputs.add(EntryIngredients.ofIngredient(r.base()));
        inputs.add(EntryIngredients.ofIngredient(r.addition()));
        return inputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ItemUpgradeCategory.ITEM_UPGRADE;
    }

    private static ItemStack buildPreview(ItemUpgradeRecipe r) {
        ItemStack[] matches = r.base().getMatchingStacks();
        if (matches.length == 0) {
            return ItemStack.EMPTY;
        }
        ItemStack out = matches[0].copy();
        int prev = out.getOrDefault(ComponentRegistry.ITEM_UPGRADE_LEVEL, 0);
        int next = Math.min(prev + 1, 5);
        r.applyUpgrades(out, next);
        return out;
    }
}