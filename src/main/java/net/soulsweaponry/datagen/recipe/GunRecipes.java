package net.soulsweaponry.datagen.recipe;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;

public class GunRecipes {

    public static void generateRecipes(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, GunRegistry.HUNTER_CANNON)
                .input('#', Items.IRON_INGOT)
                .input('G', ModTags.Items.LOST_SOUL)
                .input('M', Blocks.IRON_BLOCK)
                .input('S', ConventionalItemTags.WOODEN_RODS)
                .pattern("S M")
                .pattern("SG#")
                .pattern(" MM")
                .criterion("has_lost_soul", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LOST_SOUL).build()))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, GunRegistry.HUNTER_PISTOL)
                .input('#', Items.IRON_INGOT)
                .input('G', ModTags.Items.LOST_SOUL)
                .input('S', ConventionalItemTags.WOODEN_RODS)
                .pattern(" ##")
                .pattern("SG#")
                .pattern("S  ")
                .criterion("has_lost_soul", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LOST_SOUL).build()))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, GunRegistry.GATLING_GUN)
                .input('#', Items.IRON_INGOT)
                .input('G', ModTags.Items.LOST_SOUL)
                .input('M', Blocks.IRON_BLOCK)
                .input('S', ConventionalItemTags.WOODEN_RODS)
                .pattern("S #")
                .pattern("SG#")
                .pattern(" #M")
                .criterion("has_lost_soul", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LOST_SOUL).build()))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, GunRegistry.BLUNDERBUSS)
                .input('#', Blocks.IRON_BLOCK)
                .input('G', ModTags.Items.LOST_SOUL)
                .input('S', ConventionalItemTags.WOODEN_RODS)
                .input('i', Items.IRON_INGOT)
                .pattern(" i#")
                .pattern("SGi")
                .pattern("S i")
                .criterion("has_lost_soul", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LOST_SOUL).build()))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemRegistry.SILVER_BULLET, 10)
                .input(ModTags.Items.SILVER_INGOTS)
                .input(ModTags.Items.LOST_SOUL)
                .input(Items.GUNPOWDER)
                .criterion("has_lost_soul", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LOST_SOUL).build()))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemRegistry.SILVER_BULLET, 3)
                .input(Items.IRON_INGOT)
                .input(ModTags.Items.LOST_SOUL)
                .input(Items.GUNPOWDER)
                .criterion("has_lost_soul", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LOST_SOUL).build()))
                .offerTo(recipeExporter, Identifier.of(SoulsWeaponry.ModId, "silver_bullet_iron_ingot"));
    }
}