package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.util.ItemUpgradeRecipeJsonBuilder;
import net.soulsweaponry.util.ModTags;

import static net.minecraft.data.server.recipe.RecipeProvider.conditionsFromItemPredicates;

public class UpgradeRecipes {

    public static void generateRecipes(RecipeExporter recipeExporter) {
        // Upgrading recipes
        // TODO make the addition into twinkling titanite instead of sticks
        // TODO check different weapons if theyre balanced (damage and attack speed addition)
        // TODO maybe change the color of the bonus attribute to green or smth to indicate the bonus (since always a new attribute line/row comes up)
        // TODO update changelog to reflect the addition of upgrading system
        // TODO remove scaling off of enchants & replace with scaling off of this item level instead
        createUpgrade(recipeExporter, ModTags.Items.MELEE_ITEM_UPGRADABLES, 1f, 0.05f);
        createUpgrade(recipeExporter, ModTags.Items.RANGED_ITEM_UPGRADABLES, 0.4f, 0.1f);

        // Armor items (head to feet via tags)
        createUpgrade(recipeExporter, ItemTags.HEAD_ARMOR, 0.6f, 0.6f);
        createUpgrade(recipeExporter, ItemTags.CHEST_ARMOR, 1f, 0.6f);
        createUpgrade(recipeExporter, ItemTags.LEG_ARMOR, 0.8f, 0.6f);
        createUpgrade(recipeExporter, ItemTags.FOOT_ARMOR, 0.6f, 0.6f);

        // Guns
        createUpgrade(recipeExporter, GunRegistry.GATLING_GUN, 0.4f, 0f);
        createUpgrade(recipeExporter, GunRegistry.HUNTER_CANNON, 3, 0f);
        createUpgrade(recipeExporter, GunRegistry.BLUNDERBUSS, 1.5f, 0f);
        createUpgrade(recipeExporter, GunRegistry.HUNTER_PISTOL, 1, 0f);
    }

    public static void createUpgrade(RecipeExporter recipeExporter, TagKey<Item> tag, float primaryBonus, float secondaryBonus) {
        ItemUpgradeRecipeJsonBuilder.create(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, tag, Items.STICK, RecipeCategory.COMBAT, primaryBonus, secondaryBonus)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(Items.STICK).build()))
                .offerTo(recipeExporter, Identifier.of(SoulsWeaponry.ModId, tag.id().getPath() + "_upgrade"));
    }

    public static void createUpgrade(RecipeExporter recipeExporter, Item item, float primaryBonus, float secondaryBonus) {
        ItemUpgradeRecipeJsonBuilder.create(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, item, Items.STICK, RecipeCategory.COMBAT, primaryBonus, secondaryBonus)
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(Items.STICK).build()))
                .offerTo(recipeExporter, Identifier.of(item.toString() + "_upgrade"));
    }
}
