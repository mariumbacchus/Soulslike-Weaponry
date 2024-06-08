package net.soulsweaponry.datagen.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.function.Consumer;

// Only extends RecipeProvider to access protected static methods
public class WeaponRecipes extends RecipeProvider {

    public WeaponRecipes(DataGenerator root) {
        super(root);
    }

    public static void generateRecipes(Consumer<RecipeJsonProvider> consumer) {
        if (!CommonConfig.RECIPE_BLOODTHIRSTER.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.BLOODTHIRSTER.get())
                    .input('i', Items.IRON_INGOT)
                    .input('S', ItemRegistry.CRIMSON_INGOT.get())
                    .input('/', ModTags.Items.STICKS)
                    .pattern("iSi")
                    .pattern("iSi")
                    .pattern(" / ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(ItemRegistry.CRIMSON_INGOT.get()).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_COMET_SPEAR.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.COMET_SPEAR.get())
                    .input('#', Items.GOLD_INGOT)
                    .input('G', ModTags.Items.LORD_SOUL)
                    .input('S', ItemRegistry.MOONSTONE.get())
                    .pattern(" ##")
                    .pattern("SG#")
                    .pattern("#S ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_DARKIN_BLADE.get()) {
            WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(WeaponRegistry.BLOODTHIRSTER.get()), WeaponRegistry.DARKIN_BLADE.get(), "darkin_blade", consumer);
        }
        if (!CommonConfig.RECIPE_DAWNBREAKER.get()) {
            WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(Items.GOLDEN_SWORD), WeaponRegistry.DAWNBREAKER.get(), "dawnbreaker", consumer);
        }
        if (!CommonConfig.RECIPE_DRAGON_STAFF.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.DRAGON_STAFF.get())
                    .input('#', Items.BLAZE_ROD)
                    .input('X', Items.DRAGON_HEAD)
                    .pattern("X")
                    .pattern("#")
                    .pattern("#")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(Items.BLAZE_ROD).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_GUTS_SWORD.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.GUTS_SWORD.get())
                    .input('#', Items.IRON_BLOCK)
                    .input('X', ModTags.Items.STICKS)
                    .pattern(" # ")
                    .pattern("###")
                    .pattern("#X#")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(Items.IRON_BLOCK).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_DRAGON_SWORDSPEAR.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.DRAGONSLAYER_SWORDSPEAR.get())
                    .input('S', ModTags.Items.LORD_SOUL)
                    .input('G', Items.GOLD_INGOT)
                    .input('/', ModTags.Items.STICKS)
                    .pattern(" G ")
                    .pattern("GSG")
                    .pattern("G/G")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_GALEFORCE.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.GALEFORCE.get())
                    .input('#', ItemRegistry.MOONSTONE.get())
                    .input('S', Items.STRING)
                    .input('G', ModTags.Items.LORD_SOUL)
                    .pattern(" #S")
                    .pattern("#GS")
                    .pattern(" #S")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_LICH_BANE.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.LICH_BANE.get())
                    .input('S', ModTags.Items.LORD_SOUL)
                    .input('g', Items.DIAMOND)
                    .input('/', Items.BLAZE_ROD)
                    .input('F', Items.COPPER_INGOT)
                    .pattern("  /")
                    .pattern("gS ")
                    .pattern("Fg ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_MOONLIGHT_GREATSWORD.get()) {
            WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(WeaponRegistry.BLUEMOON_GREATSWORD.get()), WeaponRegistry.MOONLIGHT_GREATSWORD.get(), "moonlight_greatsword", consumer);
        }
        if (!CommonConfig.RECIPE_MOONLIGHT_SHORTSWORD.get()) {
            WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(WeaponRegistry.BLUEMOON_SHORTSWORD.get()), WeaponRegistry.MOONLIGHT_SHORTSWORD.get(), "moonlight_shortsword", consumer);
        }
        if (!CommonConfig.RECIPE_NIGHTFALL.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.NIGHTFALL.get())
                    .input('S', ModTags.Items.LORD_SOUL)
                    .input('X', ItemRegistry.LOST_SOUL.get())
                    .input('Y', Items.IRON_BLOCK)
                    .input('#', ItemRegistry.SOUL_INGOT.get())
                    .pattern("YYY")
                    .pattern("XSX")
                    .pattern(" # ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_RAGEBLADE.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.GUINSOOS_RAGEBLADE.get())
                    .input('/', Items.BLAZE_ROD)
                    .input('g', Items.GOLD_INGOT)
                    .input('S', ModTags.Items.LORD_SOUL)
                    .pattern(" /g")
                    .pattern("/S ")
                    .pattern("/  ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_SOUL_REAPER.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.SOUL_REAPER.get())
                    .input('i', ItemRegistry.SOUL_INGOT.get())
                    .input('/', ModTags.Items.STICKS)
                    .input('S', ModTags.Items.LORD_SOUL)
                    .pattern(" ii")
                    .pattern("S/ ")
                    .pattern(" / ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_SAWBLADE.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.WHIRLIGIG_SAWBLADE.get())
                    .input('i', Items.IRON_INGOT)
                    .input('/', ModTags.Items.STICKS)
                    .input('S', ModTags.Items.LORD_SOUL)
                    .pattern(" i ")
                    .pattern("iSi")
                    .pattern("/i ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_WABBAJACK.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.WITHERED_WABBAJACK.get())
                    .input('#', ItemRegistry.CRIMSON_INGOT.get())
                    .input('X', Items.WITHER_SKELETON_SKULL)
                    .pattern("X")
                    .pattern("#")
                    .pattern("#")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(Items.WITHER_SKELETON_SKULL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_LEVIATHAN_AXE.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.LEVIATHAN_AXE.get())
                    .input('#', ItemRegistry.VERGLAS.get())
                    .input('/', ModTags.Items.STICKS)
                    .input('S', ModTags.Items.LORD_SOUL)
                    .pattern("#S")
                    .pattern("#/")
                    .pattern(" /")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "leviathan_axe_left"));

            ShapedRecipeJsonBuilder.create(WeaponRegistry.LEVIATHAN_AXE.get())
                    .input('#', ItemRegistry.VERGLAS.get())
                    .input('/', ModTags.Items.STICKS)
                    .input('S', ModTags.Items.LORD_SOUL)
                    .pattern("S#")
                    .pattern("/#")
                    .pattern("/ ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer, new Identifier(SoulsWeaponry.ModId, "leviathan_axe_right"));
        }
        if (!CommonConfig.RECIPE_SKOFNUNG.get()) {
            WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(Items.IRON_SWORD), WeaponRegistry.SKOFNUNG.get(), "skofnung", consumer);
        }
        if (!CommonConfig.RECIPE_PURE_MOONLIGHT.get()) {
            WeaponRecipeProvider.smithingRecipe(Ingredient.fromTag(ModTags.Items.MOONLIGHT_SWORD), Ingredient.ofItems(ItemRegistry.ESSENCE_OF_LUMINESCENCE.get()), WeaponRegistry.PURE_MOONLIGHT_GREATSWORD.get(), ItemRegistry.ESSENCE_OF_LUMINESCENCE.get(), "pure_moonlight_greatsword", consumer);
        }
        if (!CommonConfig.RECIPE_MJOLNIR.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.MJOLNIR.get())
                    .input('#', ModTags.Items.STICKS)
                    .input('X', Items.IRON_BLOCK)
                    .input('O', ItemRegistry.VERGLAS.get())
                    .input('Y', ModTags.Items.LORD_SOUL)
                    .pattern("XOX")
                    .pattern("XYX")
                    .pattern(" # ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_FREYR_SWORD.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.FREYR_SWORD.get())
                    .input('#', Items.IRON_INGOT)
                    .input('Y', ItemRegistry.MOONSTONE.get())
                    .input('X', ItemRegistry.VERGLAS.get())
                    .input('O', ModTags.Items.LORD_SOUL)
                    .pattern(" X ")
                    .pattern(" Y ")
                    .pattern("#O#")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_DRAUGR.get()) {
            WeaponRecipeProvider.smithingRecipe(Ingredient.ofItems(Items.IRON_SWORD), Ingredient.ofItems(ItemRegistry.ESSENCE_OF_EVENTIDE.get()), WeaponRegistry.DRAUGR.get(), ItemRegistry.ESSENCE_OF_EVENTIDE.get(), "draugr", consumer);
        }
        if (!CommonConfig.RECIPE_STING.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.STING.get())
                    .input('X', ModTags.Items.STICKS)
                    .input('#', ItemRegistry.VERGLAS.get())
                    .pattern("#")
                    .pattern("#")
                    .pattern("X")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(ItemRegistry.VERGLAS.get()).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_FEATHERLIGHT.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.FEATHERLIGHT.get())
                    .input('X', Items.DIAMOND_PICKAXE)
                    .input('#', Items.OBSIDIAN)
                    .input('O', Items.AMETHYST_SHARD)
                    .pattern("#O#")
                    .pattern("#O#")
                    .pattern(" X ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(Items.AMETHYST_SHARD).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_CRUCIBLE_SWORD.get()) {
            WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(Items.NETHERITE_SWORD), WeaponRegistry.CRUCIBLE_SWORD.get(), "crucible_sword", consumer);
        }
        if (!CommonConfig.RECIPE_DARKIN_SCYTHE.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.DARKIN_SCYTHE_PRE.get())
                    .input('X', ModTags.Items.STICKS)
                    .input('#', ItemRegistry.CRIMSON_INGOT.get())
                    .input('O', ModTags.Items.LORD_SOUL)
                    .pattern(" ##")
                    .pattern("OX ")
                    .pattern(" X ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_KIRKHAMMER.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.KIRKHAMMER.get())
                    .input('X', Items.IRON_SWORD)
                    .input('#', Items.STONE)
                    .pattern("###")
                    .pattern("###")
                    .pattern(" X ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(Items.IRON_SWORD).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_LUDWIGS_HOLY_BLADE.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.HOLY_GREATSWORD.get())
                    .input('X', Items.IRON_SWORD)
                    .input('#', Items.IRON_INGOT)
                    .pattern(" # ")
                    .pattern("###")
                    .pattern("#X#")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(Items.IRON_INGOT).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_DRAUPNIR_SPEAR.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.DRAUPNIR_SPEAR.get())
                    .input('X', Items.BLAZE_ROD)
                    .input('#', Items.NETHERITE_INGOT)
                    .input('Y', ModTags.Items.LORD_SOUL)
                    .pattern("#")
                    .pattern("Y")
                    .pattern("X")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_HOLY_MOONLIGHT.get()) {
            WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(WeaponRegistry.STING.get()), WeaponRegistry.HOLY_MOONLIGHT_SWORD.get(), "holy_moonlight_sword", consumer);
        }
        if (!CommonConfig.RECIPE_MASTER_SWORD.get()) {
            WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(Items.DIAMOND_SWORD), WeaponRegistry.MASTER_SWORD.get(), "master_sword", consumer);
        }
        if (!CommonConfig.RECIPE_FROSTMOURNE.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.FROSTMOURNE.get())
                    .input('#', ItemRegistry.SOUL_INGOT.get())
                    .input('X', ItemRegistry.VERGLAS.get())
                    .input('Y', ModTags.Items.LORD_SOUL)
                    .pattern("  X")
                    .pattern("#X ")
                    .pattern("Y# ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_NIGHTS_EDGE.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.NIGHTS_EDGE_ITEM.get())
                    .input('/', ModTags.Items.STICKS)
                    .input('X', ItemRegistry.MOONSTONE.get())
                    .input('Y', ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get())
                    .input('#', ItemRegistry.SOUL_INGOT.get())
                    .pattern(" XX")
                    .pattern("#YX")
                    .pattern("/# ")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get()).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_EMPOWERED_DAWNBREAKER.get()) {
            WeaponRecipeProvider.smithingRecipe(Ingredient.ofItems(WeaponRegistry.DAWNBREAKER.get()), Ingredient.ofItems(ItemRegistry.LORD_SOUL_DAY_STALKER.get()), WeaponRegistry.EMPOWERED_DAWNBREAKER.get(), ItemRegistry.LORD_SOUL_DAY_STALKER.get(), "emp_dawnbreaker", consumer);
        }
        if (!CommonConfig.RECIPE_KRAKEN_SLAYER_BOW.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.KRAKEN_SLAYER.get())
                    .input('#', Items.IRON_INGOT)
                    .input('X', Items.GOLD_BLOCK)
                    .input('Y', ModTags.Items.LORD_SOUL)
                    .input('A', Items.BOW)
                    .pattern("A# ")
                    .pattern("#Y#")
                    .pattern(" #X")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_KRAKEN_SLAYER_CROSSBOW.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.KRAKEN_SLAYER_CROSSBOW.get())
                    .input('#', Items.IRON_INGOT)
                    .input('X', Items.GOLD_BLOCK)
                    .input('Y', ModTags.Items.LORD_SOUL)
                    .input('A', Items.CROSSBOW)
                    .pattern("A# ")
                    .pattern("#Y#")
                    .pattern(" #X")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .tag(ModTags.Items.LORD_SOUL).build()))
                    .offerTo(consumer);
        }
        if (!CommonConfig.RECIPE_DARKMOON_LONGBOW.get()) {
            ShapedRecipeJsonBuilder.create(WeaponRegistry.DARKMOON_LONGBOW.get())
                    .input('#', Items.GOLD_INGOT)
                    .input('Y', ItemRegistry.ESSENCE_OF_EVENTIDE.get())
                    .input('A', Items.STRING)
                    .input('X', ModTags.Items.STICKS)
                    .pattern(" #A")
                    .pattern("XYA")
                    .pattern(" #A")
                    .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                            .items(ItemRegistry.ESSENCE_OF_EVENTIDE.get()).build()))
                    .offerTo(consumer);
        }
    }
}
