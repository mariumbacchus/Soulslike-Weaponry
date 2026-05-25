package net.soulsweaponry.datagen.recipe;

import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ItemUpgradeRecipeJsonBuilder;
import net.soulsweaponry.util.ModTags;

import java.util.function.Consumer;

public class UpgradeRecipes {

    public static void generateRecipes(Consumer<RecipeJsonProvider> recipeExporter) {
        // General upgrading recipes
        createUpgrade(recipeExporter, ModTags.Items.MELEE_ITEM_UPGRADABLES, 1f, 0.05f, true);
        createUpgrade(recipeExporter, ModTags.Items.RANGED_ITEM_UPGRADABLES, 0.4f, 0.1f, true);

        // Armor items (head to feet via tags)
        createUpgrade(recipeExporter, ModTags.Items.HEAD_ARMOR, 0.6f, 0.6f, true);
        createUpgrade(recipeExporter, ModTags.Items.CHEST_ARMOR, 1f, 0.6f, true);
        createUpgrade(recipeExporter, ModTags.Items.LEG_ARMOR, 0.8f, 0.6f, true);
        createUpgrade(recipeExporter, ModTags.Items.FOOT_ARMOR, 0.6f, 0.6f, true);

        // Guns
        createUpgrade(recipeExporter, GunRegistry.GATLING_GUN.get(), 1, 0f);
        createUpgrade(recipeExporter, GunRegistry.HUNTER_CANNON.get(), 9, 0f);
        createUpgrade(recipeExporter, GunRegistry.BLUNDERBUSS.get(), 2f, 0f);
        createUpgrade(recipeExporter, GunRegistry.HUNTER_PISTOL.get(), 3f, 0f);

        // Mining items
        createUpgrade(recipeExporter, ItemTags.SHOVELS, 1f, 5, true);
        createUpgrade(recipeExporter, ItemTags.AXES, 1.25f, 5, true);
        createUpgrade(recipeExporter, ItemTags.PICKAXES, 1f, 5, true);
        createUpgrade(recipeExporter, ItemTags.HOES, 1f, 5, true);
    }

    /**
     * Create tag recipe. Tag recipes should be marked with fallback = true so that other recipes meant for that item
     * with fallback = false will be prioritized.
     * <p></p>
     * An example of this is with the Gatling Gun item, it is within the RANGED_ITEM_UPGRADABLES tag, but it should
     * prioritize using the upgrade json file meant for itself rather than always defaulting to the tag recipe.
     */
    public static void createUpgrade(Consumer<RecipeJsonProvider> recipeExporter, TagKey<Item> tag, float primaryBonus, float secondaryBonus, boolean fallback) {
        ItemUpgradeRecipeJsonBuilder.create(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, tag, ItemRegistry.TWINKLING_TITANITE.get(), RecipeCategory.COMBAT, primaryBonus, secondaryBonus, fallback)
                .criterion("has_item", InventoryChangedCriterion.Conditions.items(Items.STICK))
                .offerTo(recipeExporter, Identifier.of(SoulsWeaponry.ModId, tag.id().getPath() + "_upgrade"));
    }

    public static void createUpgrade(Consumer<RecipeJsonProvider> recipeExporter, Item item, float primaryBonus, float secondaryBonus) {
        ItemUpgradeRecipeJsonBuilder.create(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, item, ItemRegistry.TWINKLING_TITANITE.get(), RecipeCategory.COMBAT, primaryBonus, secondaryBonus)
                .criterion("has_item", InventoryChangedCriterion.Conditions.items(Items.STICK))
                .offerTo(recipeExporter, new Identifier(item.toString() + "_upgrade"));
    }
}