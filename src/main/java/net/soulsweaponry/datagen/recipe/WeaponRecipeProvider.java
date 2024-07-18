package net.soulsweaponry.datagen.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.SmithingRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.function.Consumer;

public class WeaponRecipeProvider extends FabricRecipeProvider {

    public WeaponRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> consumer) {
        if (FabricLoader.getInstance().isModLoaded("soulsweapons") && !ConfigConstructor.disable_weapon_recipes) {
            GunRecipes.generateRecipes(consumer);
            WeaponRecipes.generateRecipes(consumer);
        }
        if (!ConfigConstructor.disable_armor_recipes) {
            ArmorRecipes.generateRecipes(consumer);
        }
        CookingRecipeJsonBuilder.createSmelting(
                        Ingredient.fromTag(ModTags.Items.DEMON_HEARTS), ItemRegistry.MOLTEN_DEMON_HEART, 0.1f, 200)
                .criterion("has_demon_heart", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.DEMON_HEARTS).build()))
                .offerTo(consumer);
    }

    public static void smithingRecipe(Ingredient base, Ingredient addition, Item output, Item itemCriterion, String id, Consumer<RecipeJsonProvider> consumer) {
        SmithingRecipeJsonBuilder.create(base, addition, output)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemCriterion).build()))
                .offerTo(consumer, id + "_smithing");
    }

    public static void smithingRecipeLordSoul(Ingredient base, Item output, String id, Consumer<RecipeJsonProvider> consumer) {
        SmithingRecipeJsonBuilder.create(base, Ingredient.fromTag(ModTags.Items.LORD_SOUL), output)
                .criterion("has_lord_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LORD_SOUL).build()))
                .offerTo(consumer, id + "_smithing");
    }
}