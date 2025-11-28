package net.soulsweaponry.util;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.item.Item;

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
    public static ItemUpgradeRecipeJsonBuilder create(ItemConvertible template, TagKey<Item> baseTag, ItemConvertible addition, RecipeCategory category, float primaryBonus, float secondaryBonus, boolean fallback) {
        return create(Ingredient.ofItems(template), Ingredient.fromTag(baseTag), Ingredient.ofItems(addition), category, primaryBonus, secondaryBonus, fallback);
    }

    /** Convenience: tags for base/addition */
    public static ItemUpgradeRecipeJsonBuilder create(ItemConvertible template, TagKey<Item> baseTag, TagKey<Item> additionTag, RecipeCategory category, float primaryBonus, float secondaryBonus, boolean fallback) {
        return create(Ingredient.ofItems(template), Ingredient.fromTag(baseTag), Ingredient.fromTag(additionTag), category, primaryBonus, secondaryBonus, fallback);
    }

    public ItemUpgradeRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public void offerTo(RecipeExporter exporter, String id) {
        this.offerTo(exporter, Identifier.of(id));
    }

    public void offerTo(RecipeExporter exporter, Identifier id) {
        validate(id);

        Advancement.Builder adv = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);

        this.criteria.forEach(adv::criterion);

        ItemUpgradeRecipe recipe = new ItemUpgradeRecipe(this.template, this.base, this.addition, this.primaryBonus, this.secondaryBonus, this.fallback);
        exporter.accept(id, recipe, adv.build(id.withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private void validate(Identifier id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
        if (Float.isNaN(this.primaryBonus) || Float.isInfinite(this.primaryBonus)) {
            throw new IllegalStateException("Invalid primaryBonus for recipe " + id + ": " + this.primaryBonus);
        }
        if (Float.isNaN(this.secondaryBonus) || Float.isInfinite(this.secondaryBonus)) {
            throw new IllegalStateException("Invalid secondaryBonus for recipe " + id + ": " + this.secondaryBonus);
        }
    }
}
