package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ItemUpgradeRecipeJsonBuilder;
import net.soulsweaponry.util.ModTags;

import static net.minecraft.data.server.recipe.RecipeGenerator.conditionsFromItemPredicates;

public class UpgradeRecipes {

    public static void generateRecipes(RegistryEntryLookup<Item> itemLookup, RecipeExporter recipeExporter) {
        // General upgrading recipes
        createUpgrade(recipeExporter, itemLookup, ModTags.Items.MELEE_ITEM_UPGRADABLES, 1f, 0.05f, true);
        createUpgrade(recipeExporter, itemLookup, ModTags.Items.RANGED_ITEM_UPGRADABLES, 0.4f, 0.1f, true);

        // Armor items (head to feet via tags)
        createUpgrade(recipeExporter, itemLookup, ItemTags.HEAD_ARMOR, 0.6f, 0.6f, true);
        createUpgrade(recipeExporter, itemLookup, ItemTags.CHEST_ARMOR, 1f, 0.6f, true);
        createUpgrade(recipeExporter, itemLookup, ItemTags.LEG_ARMOR, 0.8f, 0.6f, true);
        createUpgrade(recipeExporter, itemLookup, ItemTags.FOOT_ARMOR, 0.6f, 0.6f, true);

        // Guns
        createUpgrade(recipeExporter, itemLookup, GunRegistry.GATLING_GUN, 1, 0f);
        createUpgrade(recipeExporter, itemLookup, GunRegistry.HUNTER_CANNON, 6, 0f);
        createUpgrade(recipeExporter, itemLookup, GunRegistry.BLUNDERBUSS, 3.5f, 0f);
        createUpgrade(recipeExporter, itemLookup, GunRegistry.HUNTER_PISTOL, 2.5f, 0f);

        // Mining items
        createUpgrade(recipeExporter, itemLookup, ItemTags.SHOVELS, 1f, 5, true);
        createUpgrade(recipeExporter, itemLookup, ItemTags.AXES, 1.25f, 5, true);
        createUpgrade(recipeExporter, itemLookup, ItemTags.PICKAXES, 1f, 5, true);
        createUpgrade(recipeExporter, itemLookup, ItemTags.HOES, 1f, 5, true);
    }

    /**
     * Create tag recipe. Tag recipes should be marked with fallback = true so that other recipes meant for that item
     * with fallback = false will be prioritized.
     * <p></p>
     * An example of this is with the Gatling Gun item, it is within the RANGED_ITEM_UPGRADABLES tag, but it should
     * prioritize using the upgrade json file meant for itself rather than always defaulting to the tag recipe.
     */
    public static void createUpgrade(RecipeExporter recipeExporter, RegistryEntryLookup<Item> itemLookup, TagKey<Item> tag, float primaryBonus, float secondaryBonus, boolean fallback) {
        ItemUpgradeRecipeJsonBuilder.create(itemLookup, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, tag, ItemRegistry.TWINKLING_TITANITE, RecipeCategory.COMBAT, primaryBonus, secondaryBonus, fallback)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemLookup, Items.STICK).build()))
                .offerTo(recipeExporter, Identifier.of(SoulsWeaponry.ModId, tag.id().getPath() + "_upgrade"));
    }

    public static void createUpgrade(RecipeExporter recipeExporter, RegistryEntryLookup<Item> itemLookup, Item item, float primaryBonus, float secondaryBonus) {
        ItemUpgradeRecipeJsonBuilder.create(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, item, ItemRegistry.TWINKLING_TITANITE, RecipeCategory.COMBAT, primaryBonus, secondaryBonus)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(itemLookup, Items.STICK).build()))
                .offerTo(recipeExporter, Identifier.of(item.toString() + "_upgrade"));
    }
}