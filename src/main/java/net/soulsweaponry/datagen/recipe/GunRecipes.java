package net.soulsweaponry.datagen.recipe;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.function.Consumer;

// Only extends RecipeProvider to access protected static methods
public class GunRecipes extends RecipeProvider {

    public GunRecipes(DataGenerator root) {
        super(root);
    }

    public static void generateRecipes(Consumer<RecipeJsonProvider> consumer) {
        if (CommonConfig.DISABLE_GUN_RECIPES.get()) {
            return;
        }
        ShapedRecipeJsonBuilder.create(GunRegistry.HUNTER_CANNON.get())
                .input('#', Items.IRON_INGOT)
                .input('G', ItemRegistry.LOST_SOUL.get())
                .input('M', Blocks.IRON_BLOCK)
                .input('S', ModTags.Items.STICKS)
                .pattern("S M")
                .pattern("SG#")
                .pattern(" MM")
                .criterion("has_lost_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(GunRegistry.HUNTER_PISTOL.get())
                .input('#', Items.IRON_INGOT)
                .input('G', ItemRegistry.LOST_SOUL.get())
                .input('S', ModTags.Items.STICKS)
                .pattern(" ##")
                .pattern("SG#")
                .pattern("S  ")
                .criterion("has_lost_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(GunRegistry.GATLING_GUN.get())
                .input('#', Items.IRON_INGOT)
                .input('G', ItemRegistry.LOST_SOUL.get())
                .input('M', Blocks.IRON_BLOCK)
                .input('S', ModTags.Items.STICKS)
                .pattern("S #")
                .pattern("SG#")
                .pattern(" #M")
                .criterion("has_lost_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(GunRegistry.BLUNDERBUSS.get())
                .input('#', Blocks.IRON_BLOCK)
                .input('G', ItemRegistry.LOST_SOUL.get())
                .input('S', ModTags.Items.STICKS)
                .input('i', Items.IRON_INGOT)
                .pattern(" i#")
                .pattern("SGi")
                .pattern("S i")
                .criterion("has_lost_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(ItemRegistry.SILVER_BULLET.get(), 10)
                .input(ModTags.Items.SILVER_INGOTS)
                .input(ItemRegistry.LOST_SOUL.get())
                .input(Items.GUNPOWDER)
                .criterion("has_lost_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(ItemRegistry.SILVER_BULLET.get(), 3)
                .input(Items.IRON_INGOT)
                .input(ItemRegistry.LOST_SOUL.get())
                .input(Items.GUNPOWDER)
                .criterion("has_lost_soul", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.LOST_SOUL.get()).build()))
                .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "silver_bullet_iron_ingot"));
    }
}
