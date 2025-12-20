package net.soulsweaponry.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import net.soulsweaponry.recipe.ItemUpgradeRecipe;

/**
 * Builder that serializes to:
 * {
 *   "type": "soulsweapons:smithing_item_upgrade",
 *   "template": { ... },
 *   "base": { ... },
 *   "addition": { ... },
 *   "primaryBonus": <float>, // Often used as melee damage, ranged damage, armor bonus, etc.
 *   "secondaryBonus": <float> // Often used as attack speed, draw haste, armor toughness, mining efficiency, etc.
 *   "fallback": <boolean> // True if other upgrade recipes should be prioritized over this one
 *      // (i.e. upgrade for diamond sword specifically should be chosen over generic tag recipes)
 * }
 */
public class ItemUpgradeRecipeJsonBuilder {
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final RecipeCategory category;
    private final float primaryBonus;
    private final float secondaryBonus;
    private final boolean fallback;

    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();

    private ItemUpgradeRecipeJsonBuilder(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, float primaryBonus, float secondaryBonus, boolean fallback) {
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.category = category;
        this.primaryBonus = primaryBonus;
        this.secondaryBonus = secondaryBonus;
        this.fallback = fallback;
    }

    public static ItemUpgradeRecipeJsonBuilder create(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, float primaryBonus, float secondaryBonus, boolean fallback) {
        return new ItemUpgradeRecipeJsonBuilder(template, base, addition, category, primaryBonus, secondaryBonus, fallback);
    }

    /** Convenience: items everywhere */
    public static ItemUpgradeRecipeJsonBuilder create(ItemConvertible template, ItemConvertible base, ItemConvertible addition, RecipeCategory category, float primaryBonus, float secondaryBonus) {
        return create(Ingredient.ofItems(template), Ingredient.ofItems(base), Ingredient.ofItems(addition), category, primaryBonus, secondaryBonus, false);
    }

    /** Convenience: items everywhere */
    public static ItemUpgradeRecipeJsonBuilder create(ItemConvertible template, ItemConvertible base, ItemConvertible addition, RecipeCategory category, float primaryBonus, float secondaryBonus, boolean fallback) {
        return create(Ingredient.ofItems(template), Ingredient.ofItems(base), Ingredient.ofItems(addition), category, primaryBonus, secondaryBonus, fallback);
    }

    /** Convenience: tag for base */
    public static ItemUpgradeRecipeJsonBuilder create(RegistryEntryLookup<Item> itemLookup, ItemConvertible template, TagKey<Item> baseTag, ItemConvertible addition, RecipeCategory category, float primaryBonus, float secondaryBonus, boolean fallback) {
        return create(Ingredient.ofItems(template), Ingredient.fromTag(itemLookup.getOrThrow(baseTag)), Ingredient.ofItems(addition), category, primaryBonus, secondaryBonus, fallback);
    }

    /** Convenience: tags for base/addition */
    public static ItemUpgradeRecipeJsonBuilder create(RegistryEntryLookup<Item> itemLookup, ItemConvertible template, TagKey<Item> baseTag, TagKey<Item> additionTag, RecipeCategory category, float primaryBonus, float secondaryBonus, boolean fallback) {
        return create(Ingredient.ofItems(template), Ingredient.fromTag(itemLookup.getOrThrow(baseTag)), Ingredient.fromTag(itemLookup.getOrThrow(additionTag)), category, primaryBonus, secondaryBonus, fallback);
    }

    public ItemUpgradeRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public void offerTo(RecipeExporter exporter, String id) {
        this.offerTo(exporter, Identifier.of(id));
    }

    public void offerTo(RecipeExporter exporter, Identifier id) {
        RegistryKey<Recipe<?>> key = RegistryKey.of(RegistryKeys.RECIPE, id);
        validate(key);

        Advancement.Builder adv = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(key))
                .rewards(AdvancementRewards.Builder.recipe(key))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);

        this.criteria.forEach(adv::criterion);

        ItemUpgradeRecipe recipe = new ItemUpgradeRecipe(Optional.of(this.template), Optional.of(this.base), Optional.of(this.addition), this.primaryBonus, this.secondaryBonus, this.fallback);
        exporter.accept(key, recipe, adv.build(id.withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private void validate(RegistryKey<Recipe<?>> key) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + key.getValue());
        }
        if (Float.isNaN(this.primaryBonus) || Float.isInfinite(this.primaryBonus)) {
            throw new IllegalStateException("Invalid primaryBonus for recipe " + key.getValue() + ": " + this.primaryBonus);
        }
        if (Float.isNaN(this.secondaryBonus) || Float.isInfinite(this.secondaryBonus)) {
            throw new IllegalStateException("Invalid secondaryBonus for recipe " + key.getValue() + ": " + this.secondaryBonus);
        }
    }
}