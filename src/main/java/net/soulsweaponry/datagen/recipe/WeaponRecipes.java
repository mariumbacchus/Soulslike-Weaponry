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
        // Regular weapons
        ShapedRecipeJsonBuilder.create(WeaponRegistry.TRANSLUCENT_SWORD.get())
                .input('#', ItemRegistry.LOST_SOUL.get())
                .input('X', ItemRegistry.SOUL_INGOT.get())
                .pattern(" # ")
                .pattern(" # ")
                .pattern(" X ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(WeaponRegistry.TRANSLUCENT_GLAIVE.get())
                .input('#', ItemRegistry.LOST_SOUL.get())
                .input('X', ItemRegistry.SOUL_INGOT.get())
                .pattern(" #")
                .pattern("##")
                .pattern("#X")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(WeaponRegistry.TRANSLUCENT_DOUBLE_GREATSWORD.get())
                .input('#', ItemRegistry.LOST_SOUL.get())
                .input('X', ItemRegistry.SOUL_INGOT.get())
                .pattern(" ##")
                .pattern(" X ")
                .pattern("## ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.SOUL_INGOT.get()).build()))
                .offerTo(consumer);

        // Legendary weapons
        ShapedRecipeJsonBuilder.create(WeaponRegistry.BLUEMOON_GREATSWORD.get())
                .input('#', ItemRegistry.MOONSTONE.get())
                .input('X', ModTags.Items.STICKS)
                .pattern(" # ")
                .pattern("###")
                .pattern("#X#")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(WeaponRegistry.BLUEMOON_SHORTSWORD.get())
                .input('#', ItemRegistry.MOONSTONE.get())
                .input('X', ModTags.Items.STICKS)
                .pattern(" # ")
                .pattern(" # ")
                .pattern(" X ")
                .criterion("has_item", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.MOONSTONE.get()).build()))
                .offerTo(consumer);
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
        ShapedRecipeJsonBuilder.create(WeaponRegistry.DRAGON_STAFF.get())
                .input('#', Items.BLAZE_ROD)
                .input('X', Items.DRAGON_HEAD)
                .pattern("X")
                .pattern("#")
                .pattern("#")
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(Items.BLAZE_ROD).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(WeaponRegistry.GUTS_SWORD.get())
                .input('#', Items.IRON_BLOCK)
                .input('X', ModTags.Items.STICKS)
                .pattern(" # ")
                .pattern("###")
                .pattern("#X#")
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(Items.IRON_BLOCK).build()))
                .offerTo(consumer);
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
        ShapedRecipeJsonBuilder.create(WeaponRegistry.WITHERED_WABBAJACK.get())
                .input('#', ItemRegistry.CRIMSON_INGOT.get())
                .input('X', Items.WITHER_SKELETON_SKULL)
                .pattern("X")
                .pattern("#")
                .pattern("#")
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(Items.WITHER_SKELETON_SKULL).build()))
                .offerTo(consumer);
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
        ShapedRecipeJsonBuilder.create(WeaponRegistry.STING.get())
                .input('X', ModTags.Items.STICKS)
                .input('#', ItemRegistry.VERGLAS.get())
                .pattern("#")
                .pattern("#")
                .pattern("X")
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(ItemRegistry.VERGLAS.get()).build()))
                .offerTo(consumer);
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
        ShapedRecipeJsonBuilder.create(WeaponRegistry.KIRKHAMMER.get())
                .input('X', Items.IRON_SWORD)
                .input('#', Items.STONE)
                .pattern("###")
                .pattern("###")
                .pattern(" X ")
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(Items.IRON_SWORD).build()))
                .offerTo(consumer);
        ShapedRecipeJsonBuilder.create(WeaponRegistry.HOLY_GREATSWORD.get())
                .input('X', Items.IRON_SWORD)
                .input('#', Items.IRON_INGOT)
                .pattern(" # ")
                .pattern("###")
                .pattern("#X#")
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .items(Items.IRON_INGOT).build()))
                .offerTo(consumer);
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
        ShapedRecipeJsonBuilder.create(WeaponRegistry.FROSTMOURNE.get())
                .input('#', ItemRegistry.SOUL_INGOT.get())
                .input('X', ItemRegistry.VERGLAS.get())
                .input('D', ItemRegistry.LORD_SOUL_DAY_STALKER.get())
                .input('N', ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get())
                .pattern(" #X")
                .pattern("DX#")
                .pattern("#N ")
                .criterion("has_item", conditionsFromItemPredicates(ItemPredicate.Builder.create()
                        .tag(ModTags.Items.LORD_SOUL).build()))
                .offerTo(consumer);
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

        WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(WeaponRegistry.BLOODTHIRSTER.get()), WeaponRegistry.DARKIN_BLADE.get(), consumer);
        WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(Items.GOLDEN_SWORD), WeaponRegistry.DAWNBREAKER.get(), consumer);
        WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(WeaponRegistry.BLUEMOON_GREATSWORD.get()), WeaponRegistry.MOONLIGHT_GREATSWORD.get(), consumer);
        WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(WeaponRegistry.BLUEMOON_SHORTSWORD.get()), WeaponRegistry.MOONLIGHT_SHORTSWORD.get(), consumer);
        WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(Items.IRON_SWORD), WeaponRegistry.SKOFNUNG.get(), consumer);
        WeaponRecipeProvider.smithingRecipe(Ingredient.fromTag(ModTags.Items.MOONLIGHT_SWORD), Ingredient.ofItems(ItemRegistry.ESSENCE_OF_LUMINESCENCE.get()), WeaponRegistry.PURE_MOONLIGHT_GREATSWORD.get(), ItemRegistry.ESSENCE_OF_LUMINESCENCE.get(), consumer);
        WeaponRecipeProvider.smithingRecipe(Ingredient.ofItems(Items.IRON_SWORD), Ingredient.ofItems(ItemRegistry.ESSENCE_OF_EVENTIDE.get()), WeaponRegistry.DRAUGR.get(), ItemRegistry.ESSENCE_OF_EVENTIDE.get(), consumer);
        WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(Items.NETHERITE_SWORD), WeaponRegistry.CRUCIBLE_SWORD.get(), consumer);
        WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(WeaponRegistry.STING.get()), WeaponRegistry.HOLY_MOONLIGHT_SWORD.get(), consumer);
        WeaponRecipeProvider.smithingRecipeLordSoul(Ingredient.ofItems(Items.DIAMOND_SWORD), WeaponRegistry.MASTER_SWORD.get(), consumer);
        WeaponRecipeProvider.smithingRecipe(Ingredient.ofItems(WeaponRegistry.DAWNBREAKER.get()), Ingredient.ofItems(ItemRegistry.LORD_SOUL_DAY_STALKER.get()), WeaponRegistry.EMPOWERED_DAWNBREAKER.get(), ItemRegistry.LORD_SOUL_DAY_STALKER.get(), consumer);
    }
}
