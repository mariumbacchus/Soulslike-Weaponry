package net.soulsweaponry.datagen.advancements;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.RecipeRegistry;

import java.util.Set;
import java.util.function.Consumer;

public class Advancements implements Consumer<Consumer<Advancement>> {

    private static final String MOD_ID = SoulsWeaponry.ModId;

    @Override
    public void accept(Consumer<Advancement> consumer) {
        Advancement recipeRoot = Advancement.Builder.create().criterion("impossible",
                new ImpossibleCriterion.Conditions()).build(consumer, new Identifier(MOD_ID, "recipe_root").toString());
        for (Item[] items : RecipeRegistry.recipeAdvancements.keySet()) {
            Identifier id = RecipeRegistry.recipeAdvancements.get(items);
            Advancement.Builder.create()
                    .parent(recipeRoot)
                    .rewards(AdvancementRewards.Builder.recipe(id))
                    .criterion("has_item", items(items))
                    .criterion("has_advancement", RecipeUnlockedCriterion.create(id))
                    .requirements(new String[][]{{"has_item", "has_advancement"}})
                    .build(consumer, id.toString());
        }
    }

    public static InventoryChangedCriterion.Conditions items(Item... items) {
        ItemPredicate itemPredicates = new ItemPredicate(null, Set.of(items), NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, EnchantmentPredicate.ARRAY_OF_ANY, EnchantmentPredicate.ARRAY_OF_ANY, (Potion) null, NbtPredicate.ANY);
        return InventoryChangedCriterion.Conditions.items(itemPredicates);
    }
}