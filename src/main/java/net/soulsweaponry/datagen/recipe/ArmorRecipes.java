package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.function.Consumer;

// Only extends RecipeProvider to access protected static methods
public class ArmorRecipes extends RecipeProvider {

    public ArmorRecipes(DataOutput output) {
        super(output);
    }

    @Override
    protected void generate(Consumer<RecipeJsonProvider> exporter) {

    }

    public static void generateRecipes(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.SOUL_INGOT_HELMET.get())
                .input('#', ItemRegistry.SOUL_INGOT.get())
                .pattern("###")
                .pattern("# #")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.SOUL_INGOT_CHESTPLATE.get())
                .input('#', ItemRegistry.SOUL_INGOT.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.SOUL_INGOT_LEGGINGS.get())
                .input('#', ItemRegistry.SOUL_INGOT.get())
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.SOUL_INGOT_BOOTS.get())
                .input('#', ItemRegistry.SOUL_INGOT.get())
                .pattern("# #")
                .pattern("# #")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.SOUL_ROBES_HELMET.get())
                .input('#', ItemRegistry.SOUL_INGOT.get())
                .input('L', Items.LEATHER)
                .pattern("#L#")
                .pattern("L L")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.SOUL_ROBES_CHESTPLATE.get())
                .input('#', ItemRegistry.SOUL_INGOT.get())
                .input('L', Items.LEATHER)
                .pattern("# #")
                .pattern("L#L")
                .pattern("LLL")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.SOUL_ROBES_LEGGINGS.get())
                .input('#', ItemRegistry.SOUL_INGOT.get())
                .input('L', Items.LEATHER)
                .pattern("#L#")
                .pattern("L L")
                .pattern("L L")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.SOUL_ROBES_BOOTS.get())
                .input('#', ItemRegistry.SOUL_INGOT.get())
                .input('L', Items.LEATHER)
                .pattern("# #")
                .pattern("L L")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.FORLORN_HELMET.get())
                .input('#', ItemRegistry.CRIMSON_INGOT.get())
                .input('L', ItemRegistry.SOUL_INGOT.get())
                .pattern("#L#")
                .pattern("L L")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.CRIMSON_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.FORLORN_CHESTPLATE.get())
                .input('#', ItemRegistry.CRIMSON_INGOT.get())
                .input('L', ItemRegistry.SOUL_INGOT.get())
                .pattern("# #")
                .pattern("L#L")
                .pattern("LLL")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.CRIMSON_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.FORLORN_LEGGINGS.get())
                .input('#', ItemRegistry.CRIMSON_INGOT.get())
                .input('L', ItemRegistry.SOUL_INGOT.get())
                .pattern("#L#")
                .pattern("L L")
                .pattern("L L")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.CRIMSON_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ArmorRegistry.FORLORN_BOOTS.get())
                .input('#', ItemRegistry.CRIMSON_INGOT.get())
                .input('L', ItemRegistry.SOUL_INGOT.get())
                .pattern("# #")
                .pattern("L L")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.CRIMSON_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemRegistry.ARKENPLATE.get())
                .input('#', Items.IRON_INGOT)
                .input('n', Items.NETHERITE_INGOT)
                .input('N', ItemRegistry.ARKENSTONE.get())
                .input('D', ItemRegistry.SOUL_INGOT.get())
                .pattern("# #")
                .pattern("nNn")
                .pattern("D#D")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.ARKENSTONE.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemRegistry.WITHERED_CHEST.get())
                .input('#', Items.NETHER_BRICK)
                .input('n', Items.NETHERITE_INGOT)
                .input('N', ItemRegistry.WITHERED_DEMON_HEART.get())
                .input('D', ItemRegistry.CRIMSON_INGOT.get())
                .pattern("# #")
                .pattern("nNn")
                .pattern("D#D")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.WITHERED_DEMON_HEART.get()).build()))
                .offerTo(consumer);
        WeaponRecipeProvider.smithingRecipeCombat(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.ofItems(ItemRegistry.ARKENPLATE.get()), Ingredient.ofItems(ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get()), ItemRegistry.ENHANCED_ARKENPLATE.get(), ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get(), consumer);
        WeaponRecipeProvider.smithingRecipeCombat(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.ofItems(Items.NETHERITE_HELMET), Ingredient.ofItems(ItemRegistry.CHAOS_CROWN.get()), ItemRegistry.CHAOS_HELMET.get(), ItemRegistry.CHAOS_CROWN.get(), consumer);
        WeaponRecipeProvider.smithingRecipeCombat(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.ofItems(ItemRegistry.WITHERED_CHEST.get()), Ingredient.ofItems(ItemRegistry.LORD_SOUL_DAY_STALKER.get()), ItemRegistry.ENHANCED_WITHERED_CHEST.get(), ItemRegistry.LORD_SOUL_DAY_STALKER.get(), consumer);
    }
}