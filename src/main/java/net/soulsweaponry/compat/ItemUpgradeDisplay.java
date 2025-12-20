package net.soulsweaponry.compat;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;
import net.soulsweaponry.registry.ComponentRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ItemUpgradeDisplay extends BasicDisplay {

    public ItemUpgradeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public ItemUpgradeDisplay(RecipeEntry<ItemUpgradeRecipe> recipe) {
        super(getInputList(recipe.value()),
                List.of(EntryIngredient.of(EntryStacks.of(buildPreview(recipe.value())))));
    }

    public ItemUpgradeDisplay(ItemUpgradeRecipe recipe) {
        super(getInputList(recipe),
                List.of(EntryIngredient.of(EntryStacks.of(buildPreview(recipe)))));
    }

    private static EntryIngredient ofOptionalIngredient(Optional<Ingredient> ingredient) {
        return ingredient.map(EntryIngredients::ofIngredient).orElse(EntryIngredient.empty());
    }

    private static List<EntryIngredient> getInputList(ItemUpgradeRecipe r) {
        if (r == null) return Collections.emptyList();
        List<EntryIngredient> inputs = new ArrayList<>(3);
        inputs.add(ofOptionalIngredient(r.template()));
        inputs.add(ofOptionalIngredient(r.base()));
        inputs.add(ofOptionalIngredient(r.addition()));
        return inputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ItemUpgradeCategory.ITEM_UPGRADE;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return null;
    }

    private static ItemStack buildPreview(ItemUpgradeRecipe r) {
        Ingredient base = r.base().orElse(null);
        if (base == null) {
            return ItemStack.EMPTY;
        }

        var matches = base.getMatchingItems();
        if (matches.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack out = matches.getFirst().value().getDefaultStack().copy();
        int prev = out.getOrDefault(ComponentRegistry.ITEM_UPGRADE_LEVEL, 0);
        int next = Math.min(prev + 1, 5);
        r.applyUpgrades(out, next);
        return out;
    }
}