package net.soulsweaponry.util;

import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.soulsweaponry.registry.RecipeSerializerRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    private final Map<String, CriterionConditions> criteria = new LinkedHashMap<>();

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

    public ItemUpgradeRecipeJsonBuilder criterion(String name, CriterionConditions criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public void offerTo(Consumer<RecipeJsonProvider> exporter, String id) {
        this.offerTo(exporter, new Identifier(id));
    }

    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier id) {
        validate(id);

        Advancement.Builder adv = Advancement.Builder.create()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .criteriaMerger(CriterionMerger.OR);

        this.criteria.forEach(adv::criterion);

        exporter.accept(new ItemUpgradeRecipeJsonProvider(
                id,
                this.template,
                this.base,
                this.addition,
                this.primaryBonus,
                this.secondaryBonus,
                this.fallback,
                adv,
                id.withPrefixedPath("recipes/" + this.category.getName() + "/")
        ));
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

    static class ItemUpgradeRecipeJsonProvider implements RecipeJsonProvider {
        private final Identifier id;
        private final Ingredient template;
        private final Ingredient base;
        private final Ingredient addition;
        private final float primaryBonus;
        private final float secondaryBonus;
        private final boolean fallback;
        private final Advancement.Builder advancement;
        private final Identifier advancementId;

        ItemUpgradeRecipeJsonProvider(Identifier id,
                                      Ingredient template,
                                      Ingredient base,
                                      Ingredient addition,
                                      float primaryBonus,
                                      float secondaryBonus,
                                      boolean fallback,
                                      Advancement.Builder advancement,
                                      Identifier advancementId) {
            this.id = id;
            this.template = template;
            this.base = base;
            this.addition = addition;
            this.primaryBonus = primaryBonus;
            this.secondaryBonus = secondaryBonus;
            this.fallback = fallback;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize(JsonObject json) {
            json.add("template", this.template.toJson());
            json.add("base", this.base.toJson());
            json.add("addition", this.addition.toJson());
            json.addProperty("primaryBonus", this.primaryBonus);
            json.addProperty("secondaryBonus", this.secondaryBonus);
            json.addProperty("fallback", this.fallback);
        }

        @Override
        public Identifier getRecipeId() {
            return this.id;
        }

        @Override
        public net.minecraft.recipe.RecipeSerializer<?> getSerializer() {
            return RecipeSerializerRegistry.SMITHING_ITEM_UPGRADE.get();
        }

        @Override
        public @Nullable JsonObject toAdvancementJson() {
            return this.advancement.toJson();
        }

        @Override
        public @Nullable Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}