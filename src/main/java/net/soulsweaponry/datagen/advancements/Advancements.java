package net.soulsweaponry.datagen.advancements;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.RecipeRegistry;

import java.util.List;
import java.util.function.Consumer;

public class Advancements implements Consumer<Consumer<AdvancementEntry>> {

    private static final String MOD_ID = SoulsWeaponry.ModId;

    @Override
    public void accept(Consumer<AdvancementEntry> consumer) {
        AdvancementEntry recipeRoot = Advancement.Builder.createUntelemetered().criterion("impossible",
                Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions())).build(consumer, new Identifier(MOD_ID, "recipe_root").toString());
        for (Item[] items : RecipeRegistry.recipeAdvancements.keySet()) {
            Identifier id = RecipeRegistry.recipeAdvancements.get(items);
            Advancement.Builder.create()
                    .parent(recipeRoot)
                    .rewards(AdvancementRewards.Builder.recipe(id))
                    .criterion("has_item", InventoryChangedCriterion.Conditions.items(items))
                    .criterion("has_advancement", RecipeUnlockedCriterion.create(id))
                    .requirements(new AdvancementRequirements(List.of(List.of("has_item", "has_advancement"))))
                    .build(consumer, id.toString());
        }
    }
}
