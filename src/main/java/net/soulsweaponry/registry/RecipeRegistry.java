package net.soulsweaponry.registry;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.JsonCreator;
import net.soulsweaponry.util.WeaponUtil;

public class RecipeRegistry {

    private static final String ModId = SoulsWeaponry.ModId;

    public static JsonObject HUNTER_CANNON_RECIPE = null;
    public static JsonObject HUNTER_PISTOL_RECIPE = null;
    public static JsonObject GATLING_GUN_RECIPE = null;
    public static JsonObject BLUNDERBUSS_RECIPE = null;
    public static JsonObject SILVER_BULLET_RECIPE = null;

    public static JsonObject BLOODTHIRSTER_RECIPE = null;
    public static JsonObject COMET_SPEAR_RECIPE = null;
    public static JsonObject DARKIN_BLADE_RECIPE = null;
    public static JsonObject DAWNBREAKER_RECIPE = null;
    public static JsonObject GUTS_SWORD_RECIPE = null;
    public static JsonObject DRAGONSLAYER_SWORDSPEAR_RECIPE = null;
    public static JsonObject DRAGON_STAFF_RECIPE = null;
    public static JsonObject GALEFORCE_RECIPE = null;
    public static JsonObject RAGEBLADE_RECIPE = null;
    public static JsonObject LICH_BANE_RECIPE = null;
    public static JsonObject MOONLIGHT_GREATSWORD_RECIPE = null;
    public static JsonObject MOONLIGHT_SHORTSWORD_RECIPE = null;
    public static JsonObject NIGHTFALL_RECIPE = null;
    public static JsonObject SOUL_REAPER_RECIPE = null;
    public static JsonObject WHIRLIGIG_SAWBLADE_RECIPE = null;
    public static JsonObject WABBAJACK_RECIPE = null;
    public static JsonObject LEVIATHAN_AXE_LEFT = null;
    public static JsonObject LEVIATHAN_AXE_RIGHT = null;
    public static JsonObject SKOFNUNG_RECIPE = null;
    public static JsonObject PURE_MOONLIGHT_GREAT_RECIPE = null;
    public static JsonObject MJOLNIR_RECIPE = null;
    public static JsonObject FREYR_SWORD_RECIPE = null;
    public static JsonObject DRAUGR_RECIPE = null;
    public static JsonObject STING_RECIPE = null;
    public static JsonObject FEATHERLIGHT_RECIPE = null;
    public static JsonObject CRUCIBLE_SWORD_RECIPE = null;
    public static JsonObject DARKIN_SCYTHE_RECIPE = null;
    public static JsonObject KIRKHAMMER_RECIPE = null;
    public static JsonObject HOLY_GREATSWORD_RECIPE = null;
    public static JsonObject DRAUPNIR_SPEAR_RECIPE = null;
    public static JsonObject HOLY_MOONLIGHT_SWORD_RECIPE = null;
    public static JsonObject MASTER_SWORD_RECIPE = null;
    public static JsonObject FROSTMOURNE_RECIPE = null;
    public static JsonObject NIGHTS_EDGE_RECIPE = null;
    public static JsonObject EMPOWERED_DAWNBREAKER_RECIPE = null;
    public static JsonObject KRAKEN_SLAYER_BOW_RECIPE = null;
    public static JsonObject KRAKEN_SLAYER_CROSSBOW_RECIPE = null;
    public static JsonObject DARKMOON_LONGBOW_RECIPE = null;
    public static JsonObject ENHANCED_ARKENPLATE_RECIPE = null;
    public static JsonObject ENHANCED_WITHERED_CHEST_RECIPE = null;


    public static ArrayList<ArrayList<Object>> recipes = new ArrayList<>();
    public static HashMap<Item[], Identifier> recipeAdvancements = new HashMap<>();

    public static JsonObject BEWITCHMENT_MOLTEN_DEMON_HEART = null;
    public static JsonObject BEWITCHMENT_SILVER_BULLET = null;

    public static void init() {
        if (WeaponUtil.isModLoaded("soulsweapons") && !CommonConfig.DISABLE_WEAPON_RECIPES.get()) {
            if (!CommonConfig.DISABLE_GUN_RECIPES.get()) {
                HUNTER_CANNON_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'G', 'M', 'S'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_ingot"),
                                new Identifier(ModId, "lost_soul"),
                                new Identifier("minecraft", "iron_block"),
                                new Identifier("c", "wood_sticks")
                        ),
                        Lists.newArrayList("item", "item", "item", "tag"),
                        Lists.newArrayList(
                                "S M",
                                "SG#",
                                " MM"
                        ), new Identifier(ModId, "hunter_cannon"));

                HUNTER_PISTOL_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'G', 'S'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_ingot"),
                                new Identifier(ModId, "lost_soul"),
                                new Identifier("c", "wood_sticks")
                        ),
                        Lists.newArrayList("item", "item", "tag"),
                        Lists.newArrayList(
                                " ##",
                                "SG#",
                                "S  "
                        ), new Identifier(ModId, "hunter_pistol"));

                GATLING_GUN_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'G', 'M', 'S'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_ingot"),
                                new Identifier(ModId, "lost_soul"),
                                new Identifier("minecraft", "iron_block"),
                                new Identifier("c", "wood_sticks")
                        ),
                        Lists.newArrayList("item", "item", "item", "tag"),
                        Lists.newArrayList(
                                "S #",
                                "SG#",
                                " #M"
                        ), new Identifier(ModId, "gatling_gun"));

                BLUNDERBUSS_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'G', 'S', 'i'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_block"),
                                new Identifier(ModId, "lost_soul"),
                                new Identifier("c", "wood_sticks"),
                                new Identifier("minecraft", "iron_ingot")
                        ),
                        Lists.newArrayList("item", "item", "tag", "item"),
                        Lists.newArrayList(
                                " i#",
                                "SGi",
                                "S i"
                        ), new Identifier(ModId, "blunderbuss"));

                ArrayList<ArrayList<Object>> list = new ArrayList<>();
                list.add(Lists.newArrayList("tag", new Identifier("c", "silver_ingots")));
                list.add(Lists.newArrayList("item", new Identifier(ModId, "lost_soul")));
                list.add(Lists.newArrayList("item", new Identifier("gunpowder")));
                BEWITCHMENT_SILVER_BULLET = JsonCreator.createShapelessRecipeJson(
                        list, new Identifier(ModId, "silver_bullet"), 10);
                registerAndAddToBook(BEWITCHMENT_SILVER_BULLET, "silver_bullet_1", CommonConfig.DISABLE_GUN_RECIPES.get(), ItemRegistry.LOST_SOUL.get());

                list = new ArrayList<>();
                list.add(Lists.newArrayList("item", new Identifier("iron_ingot")));
                list.add(Lists.newArrayList("item", new Identifier(ModId, "lost_soul")));
                list.add(Lists.newArrayList("item", new Identifier("gunpowder")));
                SILVER_BULLET_RECIPE = JsonCreator.createShapelessRecipeJson(
                        list, new Identifier(ModId, "silver_bullet"), 3);
                registerAndAddToBook(SILVER_BULLET_RECIPE, "silver_bullet_2", CommonConfig.DISABLE_GUN_RECIPES.get(), ItemRegistry.LOST_SOUL.get());
            }
            if (!CommonConfig.RECIPE_BLOODTHIRSTER.get()) {
                BLOODTHIRSTER_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('i', 'S', '/'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_ingot"),
                                new Identifier(ModId, "crimson_ingot"),
                                new Identifier("c", "wood_sticks")
                        ),
                        Lists.newArrayList("item", "item", "tag"),
                        Lists.newArrayList(
                                "iSi",
                                "iSi",
                                " / "
                        ), new Identifier(ModId, "bloodthirster"));

                registerAndAddToBook(BLOODTHIRSTER_RECIPE, "bloodthirster", CommonConfig.RECIPE_BLOODTHIRSTER.get(), ItemRegistry.CRIMSON_INGOT.get());
            }
            if (!CommonConfig.RECIPE_COMET_SPEAR.get()) {
                COMET_SPEAR_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'G', 'S'),
                        Lists.newArrayList(new Identifier("minecraft", "gold_ingot"),
                                new Identifier(ModId, "lord_soul"),
                                new Identifier(ModId, "moonstone")
                        ),
                        Lists.newArrayList("item", "tag", "item"),
                        Lists.newArrayList(
                                " ##",
                                "SG#",
                                "#S "
                        ), new Identifier(ModId, "comet_spear"));

                registerAndBookLordSoul(COMET_SPEAR_RECIPE, "comet_spear", CommonConfig.RECIPE_COMET_SPEAR.get());
            }
            if (!CommonConfig.RECIPE_DARKIN_BLADE.get()) {
                DARKIN_BLADE_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "bloodthirster"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "darkin_blade"));

                registerAndBookLordSoul(DARKIN_BLADE_RECIPE, "darkin_blade", CommonConfig.RECIPE_DARKIN_BLADE.get());
            }
            if (!CommonConfig.RECIPE_DAWNBREAKER.get()) {
                DAWNBREAKER_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "golden_sword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "dawnbreaker"));

                registerAndBookLordSoul(DAWNBREAKER_RECIPE, "dawnbreaker", CommonConfig.RECIPE_DAWNBREAKER.get());
            }
            if (!CommonConfig.RECIPE_DRAGON_STAFF.get()) {
                DRAGON_STAFF_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X'),
                        Lists.newArrayList(new Identifier("minecraft", "blaze_rod"),
                                new Identifier("minecraft", "dragon_head")
                        ),
                        Lists.newArrayList("item", "item"),
                        Lists.newArrayList(
                                "X",
                                "#",
                                "#"
                        ), new Identifier(ModId, "dragon_staff"));

                registerAndAddToBook(DRAGON_STAFF_RECIPE, "dragon_staff", CommonConfig.RECIPE_DRAGON_STAFF.get(), Items.BLAZE_ROD);
            }
            if (!CommonConfig.RECIPE_GUTS_SWORD.get()) {
                GUTS_SWORD_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_block"),
                                new Identifier("c", "wood_sticks")
                        ),
                        Lists.newArrayList("item", "tag"),
                        Lists.newArrayList(
                                " # ",
                                "###",
                                "#X#"
                        ), new Identifier(ModId, "guts_sword"));

                registerAndAddToBook(GUTS_SWORD_RECIPE, "guts_sword", CommonConfig.RECIPE_GUTS_SWORD.get(), Items.IRON_BLOCK);
            }
            if (!CommonConfig.RECIPE_DRAGON_SWORDSPEAR.get()) {
                DRAGONSLAYER_SWORDSPEAR_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('S', 'G', '/'),
                        Lists.newArrayList(new Identifier(ModId, "lord_soul"),
                                new Identifier("minecraft", "gold_ingot"),
                                new Identifier("c", "wood_sticks")
                        ),
                        Lists.newArrayList("tag", "item", "tag"),
                        Lists.newArrayList(
                                " G ",
                                "GSG",
                                "G/G"
                        ), new Identifier(ModId, "dragonslayer_swordspear"));

                registerAndBookLordSoul(DRAGONSLAYER_SWORDSPEAR_RECIPE, "dragonslayer_swordspear", CommonConfig.RECIPE_DRAGON_SWORDSPEAR.get());
            }
            if (!CommonConfig.RECIPE_GALEFORCE.get()) {
                GALEFORCE_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'S', 'G'),
                        Lists.newArrayList(new Identifier(ModId, "moonstone"),
                                new Identifier("minecraft", "string"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "item", "tag"),
                        Lists.newArrayList(
                                " #S",
                                "#GS",
                                " #S"
                        ), new Identifier(ModId, "galeforce"));

                registerAndBookLordSoul(GALEFORCE_RECIPE, "galeforce", CommonConfig.RECIPE_GALEFORCE.get());
            }
            if (!CommonConfig.RECIPE_LICH_BANE.get()) {
                LICH_BANE_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('S', 'g', '/', 'F'),
                        Lists.newArrayList(new Identifier(ModId, "lord_soul"),
                                new Identifier("minecraft", "diamond"),
                                new Identifier("minecraft", "blaze_rod"),
                                new Identifier("minecraft", "copper_ingot")
                        ),
                        Lists.newArrayList("tag", "item", "item", "item"),
                        Lists.newArrayList(
                                "  /",
                                "gS ",
                                "Fg "
                        ), new Identifier(ModId, "lich_bane"));

                registerAndBookLordSoul(LICH_BANE_RECIPE, "lich_bane", CommonConfig.RECIPE_LICH_BANE.get());
            }
            if (!CommonConfig.RECIPE_MOONLIGHT_GREATSWORD.get()) {
                MOONLIGHT_GREATSWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "bluemoon_greatsword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "moonlight_greatsword"));

                registerAndBookLordSoul(MOONLIGHT_GREATSWORD_RECIPE, "moonlight_greatsword", CommonConfig.RECIPE_MOONLIGHT_GREATSWORD.get());
            }
            if (!CommonConfig.RECIPE_MOONLIGHT_SHORTSWORD.get()) {
                MOONLIGHT_SHORTSWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "bluemoon_shortsword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "moonlight_shortsword"));

                registerAndBookLordSoul(MOONLIGHT_SHORTSWORD_RECIPE, "moonlight_shortsword", CommonConfig.RECIPE_MOONLIGHT_SHORTSWORD.get());
            }
            if (!CommonConfig.RECIPE_NIGHTFALL.get()) {
                NIGHTFALL_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('S', 'X', 'Y', '#'),
                        Lists.newArrayList(new Identifier(ModId, "lord_soul"),
                                new Identifier(ModId, "lost_soul"),
                                new Identifier("minecraft", "iron_block"),
                                new Identifier(ModId, "soul_ingot")
                        ),
                        Lists.newArrayList("tag", "item", "item", "item"),
                        Lists.newArrayList(
                                "YYY",
                                "XSX",
                                " # "
                        ), new Identifier(ModId, "nightfall"));

                registerAndBookLordSoul(NIGHTFALL_RECIPE, "nightfall", CommonConfig.RECIPE_NIGHTFALL.get());
            }
            if (!CommonConfig.RECIPE_RAGEBLADE.get()) {
                RAGEBLADE_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('/', 'g', 'S'),
                        Lists.newArrayList(new Identifier("minecraft", "blaze_rod"),
                                new Identifier("minecraft", "gold_ingot"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "item", "tag"),
                        Lists.newArrayList(
                                " /g",
                                "/S ",
                                "/  "
                        ), new Identifier(ModId, "rageblade"));

                registerAndBookLordSoul(RAGEBLADE_RECIPE, "rageblade", CommonConfig.RECIPE_RAGEBLADE.get());
            }
            if (!CommonConfig.RECIPE_SOUL_REAPER.get()) {
                SOUL_REAPER_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('i', '/', 'S'),
                        Lists.newArrayList(new Identifier(ModId, "soul_ingot"),
                                new Identifier("c", "wood_sticks"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "tag", "tag"),
                        Lists.newArrayList(
                                " ii",
                                "S/ ",
                                " / "
                        ), new Identifier(ModId, "soul_reaper"));

                registerAndBookLordSoul(SOUL_REAPER_RECIPE, "soul_reaper", CommonConfig.RECIPE_SOUL_REAPER.get());
            }
            if (!CommonConfig.RECIPE_SAWBLADE.get()) {
                WHIRLIGIG_SAWBLADE_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('/', 'i', 'S'),
                        Lists.newArrayList(new Identifier("c", "wood_sticks"),
                                new Identifier("minecraft", "iron_ingot"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("tag", "item", "tag"),
                        Lists.newArrayList(
                                " i ",
                                "iSi",
                                "/i "
                        ), new Identifier(ModId, "whirligig_sawblade"));

                registerAndBookLordSoul(WHIRLIGIG_SAWBLADE_RECIPE, "whirligig_sawblade", CommonConfig.RECIPE_SAWBLADE.get());
            }
            if (!CommonConfig.RECIPE_WABBAJACK.get()) {
                WABBAJACK_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X'),
                        Lists.newArrayList(new Identifier(ModId, "crimson_ingot"),
                                new Identifier("minecraft", "wither_skeleton_skull")
                        ),
                        Lists.newArrayList("item", "item"),
                        Lists.newArrayList(
                                "X",
                                "#",
                                "#"
                        ), new Identifier(ModId, "withered_wabbajack"));

                registerAndAddToBook(WABBAJACK_RECIPE, "withered_wabbajack", CommonConfig.RECIPE_WABBAJACK.get(), Items.WITHER_SKELETON_SKULL);
            }
            if (!CommonConfig.RECIPE_LEVIATHAN_AXE.get()) {
                LEVIATHAN_AXE_LEFT = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('S', '#', '/'),
                        Lists.newArrayList(new Identifier(ModId, "lord_soul"),
                                new Identifier(ModId, "verglas"),
                                new Identifier("c", "wood_sticks")
                        ),
                        Lists.newArrayList("tag", "item", "tag"),
                        Lists.newArrayList(
                                "#S",
                                "#/",
                                " /"
                        ), new Identifier(ModId, "leviathan_axe"));

                registerAndBookLordSoul(LEVIATHAN_AXE_LEFT, "leviathan_axe_left", CommonConfig.RECIPE_LEVIATHAN_AXE.get());

                LEVIATHAN_AXE_RIGHT = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('S', '#', '/'),
                        Lists.newArrayList(new Identifier(ModId, "lord_soul"),
                                new Identifier(ModId, "verglas"),
                                new Identifier("c", "wood_sticks")
                        ),
                        Lists.newArrayList("tag", "item", "tag"),
                        Lists.newArrayList(
                                "S#",
                                "/#",
                                "/ "
                        ), new Identifier(ModId, "leviathan_axe"));

                registerRecipe(LEVIATHAN_AXE_RIGHT, "leviathan_axe_right", CommonConfig.RECIPE_LEVIATHAN_AXE.get());
            }
            if (!CommonConfig.RECIPE_SKOFNUNG.get()) {
                SKOFNUNG_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "iron_sword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "skofnung"));

                registerAndBookLordSoul(SKOFNUNG_RECIPE, "skofnung", CommonConfig.RECIPE_SKOFNUNG.get());
            }
            if (!CommonConfig.RECIPE_PURE_MOONLIGHT.get()) {
                PURE_MOONLIGHT_GREAT_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "tag", new Identifier(ModId, "moonlight_sword"),
                        "item", new Identifier(ModId, "essence_of_luminescence"),
                        new Identifier(ModId, "pure_moonlight_greatsword"));

                registerAndAddToBook(PURE_MOONLIGHT_GREAT_RECIPE, "pure_moonlight_greatsword", CommonConfig.RECIPE_PURE_MOONLIGHT.get(), ItemRegistry.ESSENCE_OF_LUMINESCENCE.get());
            }
            if (!CommonConfig.RECIPE_MJOLNIR.get()) {
                MJOLNIR_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'O', 'Y'),
                        Lists.newArrayList(new Identifier("c", "wood_sticks"),
                                new Identifier("minecraft", "iron_block"),
                                new Identifier(ModId, "verglas"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("tag", "item", "item", "tag"),
                        Lists.newArrayList(
                                "XOX",
                                "XYX",
                                " # "
                        ), new Identifier(ModId, "mjolnir"));

                registerAndBookLordSoul(MJOLNIR_RECIPE, "mjolnir", CommonConfig.RECIPE_MJOLNIR.get());
            }
            if (!CommonConfig.RECIPE_FREYR_SWORD.get()) {
                FREYR_SWORD_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'Y', 'O'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_ingot"),
                                new Identifier(ModId, "verglas"),
                                new Identifier(ModId, "moonstone"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "item", "item", "tag"),
                        Lists.newArrayList(
                                " X ",
                                " Y ",
                                "#O#"
                        ), new Identifier(ModId, "freyr_sword"));

                registerAndBookLordSoul(FREYR_SWORD_RECIPE, "freyr_sword", CommonConfig.RECIPE_FREYR_SWORD.get());
            }
            if (!CommonConfig.RECIPE_DRAUGR.get()) {
                DRAUGR_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "iron_sword"),
                        "item", new Identifier(ModId, "essence_of_eventide"),
                        new Identifier(ModId, "draugr"));

                registerAndAddToBook(DRAUGR_RECIPE, "draugr", CommonConfig.RECIPE_DRAUGR.get(), ItemRegistry.ESSENCE_OF_EVENTIDE.get());
            }
            if (!CommonConfig.RECIPE_STING.get()) {
                STING_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X'),
                        Lists.newArrayList(
                                new Identifier(ModId, "verglas"),
                                new Identifier("c", "wood_sticks")
                        ),
                        Lists.newArrayList("item", "tag"),
                        Lists.newArrayList(
                                "#",
                                "#",
                                "X"
                        ), new Identifier(ModId, "sting"));

                registerAndAddToBook(STING_RECIPE, "sting", CommonConfig.RECIPE_STING.get(), ItemRegistry.VERGLAS.get());
            }if (!CommonConfig.RECIPE_FEATHERLIGHT.get()) {
                FEATHERLIGHT_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'O'),
                        Lists.newArrayList(
                                new Identifier("minecraft", "obsidian"),
                                new Identifier("minecraft", "diamond_pickaxe"),
                                new Identifier("minecraft", "amethyst_shard")
                        ),
                        Lists.newArrayList("item", "item", "item"),
                        Lists.newArrayList(
                                "#O#",
                                "#O#",
                                " X "
                        ), new Identifier(ModId, "featherlight"));

                registerAndAddToBook(FEATHERLIGHT_RECIPE, "featherlight", CommonConfig.RECIPE_FEATHERLIGHT.get(), Items.AMETHYST_SHARD);
            }
            if (!CommonConfig.RECIPE_CRUCIBLE_SWORD.get()) {
                CRUCIBLE_SWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "netherite_sword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "crucible_sword"));

                registerAndBookLordSoul(CRUCIBLE_SWORD_RECIPE, "crucible_sword", CommonConfig.RECIPE_CRUCIBLE_SWORD.get());
            }
            if (!CommonConfig.RECIPE_DARKIN_SCYTHE.get()) {
                DARKIN_SCYTHE_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'O'),
                        Lists.newArrayList(
                                new Identifier(ModId, "crimson_ingot"),
                                new Identifier("c", "wood_sticks"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "tag", "tag"),
                        Lists.newArrayList(
                                " ##",
                                "OX ",
                                " X "
                        ), new Identifier(ModId, "darkin_scythe_pre"));

                registerAndBookLordSoul(DARKIN_SCYTHE_RECIPE, "darkin_scythe_pre", CommonConfig.RECIPE_DARKIN_SCYTHE.get());
            }
            if (!CommonConfig.RECIPE_KIRKHAMMER.get()) {
                KIRKHAMMER_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X'),
                        Lists.newArrayList(
                                new Identifier("minecraft", "stone"),
                                new Identifier("minecraft", "iron_sword")
                        ),
                        Lists.newArrayList("item", "item"),
                        Lists.newArrayList(
                                "###",
                                "###",
                                " X "
                        ), new Identifier(ModId, "kirkhammer"));

                registerAndAddToBook(KIRKHAMMER_RECIPE, "kirkhammer", CommonConfig.RECIPE_KIRKHAMMER.get(), Items.IRON_SWORD);
            }
            if (!CommonConfig.RECIPE_LUDWIGS_HOLY_BLADE.get()) {
                HOLY_GREATSWORD_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X'),
                        Lists.newArrayList(
                                new Identifier("minecraft", "iron_ingot"),
                                new Identifier("minecraft", "iron_sword")
                        ),
                        Lists.newArrayList("item", "item"),
                        Lists.newArrayList(
                                " # ",
                                "###",
                                "#X#"
                        ), new Identifier(ModId, "holy_greatsword"));

                registerAndAddToBook(HOLY_GREATSWORD_RECIPE, "holy_greatsword", CommonConfig.RECIPE_LUDWIGS_HOLY_BLADE.get(), Items.IRON_INGOT);
            }
            if (!CommonConfig.RECIPE_DRAUPNIR_SPEAR.get()) {
                DRAUPNIR_SPEAR_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'Y'),
                        Lists.newArrayList(
                                new Identifier("minecraft", "blaze_rod"),
                                new Identifier("minecraft", "netherite_ingot"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "item", "tag"),
                        Lists.newArrayList(
                                "X",
                                "Y",
                                "#"
                        ), new Identifier(ModId, "draupnir_spear"));
                registerAndBookLordSoul(DRAUPNIR_SPEAR_RECIPE, "draupnir_spear", CommonConfig.RECIPE_DRAUPNIR_SPEAR.get());
            }
            if (!CommonConfig.RECIPE_HOLY_MOONLIGHT.get()) {
                HOLY_MOONLIGHT_SWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "sting"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "holy_moonlight_sword"));

                registerAndBookLordSoul(HOLY_MOONLIGHT_SWORD_RECIPE, "holy_moonlight_sword", CommonConfig.RECIPE_HOLY_MOONLIGHT.get());
            }
            if (!CommonConfig.RECIPE_MASTER_SWORD.get()) {
                MASTER_SWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "diamond_sword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "master_sword"));

                registerAndBookLordSoul(MASTER_SWORD_RECIPE, "master_sword", CommonConfig.RECIPE_MASTER_SWORD.get());
            }
            if (!CommonConfig.RECIPE_FROSTMOURNE.get()) {
                FROSTMOURNE_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'Y'),
                        Lists.newArrayList(
                                new Identifier(ModId, "soul_ingot"),
                                new Identifier(ModId, "verglas"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "item", "tag"),
                        Lists.newArrayList(
                                "  X",
                                "#X ",
                                "Y# "
                        ), new Identifier(ModId, "frostmourne"));
                registerAndBookLordSoul(FROSTMOURNE_RECIPE, "frostmourne", CommonConfig.RECIPE_FROSTMOURNE.get());
            }
            if (!CommonConfig.RECIPE_NIGHTS_EDGE.get()) {
                NIGHTS_EDGE_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('/', 'X', 'Y', '#'),
                        Lists.newArrayList(
                                new Identifier("c", "wood_sticks"),
                                new Identifier(ModId, "moonstone"),
                                new Identifier(ModId, "lord_soul_night_prowler"),
                                new Identifier(ModId, "soul_ingot")
                        ),
                        Lists.newArrayList("tag", "item", "item", "item"),
                        Lists.newArrayList(
                                " XX",
                                "#YX",
                                "/# "
                        ), new Identifier(ModId, "nights_edge_item"));
                registerAndAddToBook(NIGHTS_EDGE_RECIPE, "nights_edge_item", CommonConfig.RECIPE_NIGHTS_EDGE.get(), ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get());
            }
            if (!CommonConfig.RECIPE_EMPOWERED_DAWNBREAKER.get()) {
                EMPOWERED_DAWNBREAKER_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "dawnbreaker"),
                        "item", new Identifier(ModId, "lord_soul_day_stalker"),
                        new Identifier(ModId, "empowered_dawnbreaker"));
                registerAndAddToBook(EMPOWERED_DAWNBREAKER_RECIPE, "empowered_dawnbreaker", CommonConfig.RECIPE_EMPOWERED_DAWNBREAKER.get(), ItemRegistry.LORD_SOUL_DAY_STALKER.get());
            }
            if (!CommonConfig.RECIPE_KRAKEN_SLAYER_BOW.get()) {
                KRAKEN_SLAYER_BOW_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'Y', 'A'),
                        Lists.newArrayList(
                                new Identifier("iron_ingot"),
                                new Identifier("gold_block"),
                                new Identifier(ModId, "lord_soul"),
                                new Identifier("bow")
                        ),
                        Lists.newArrayList("item", "item", "tag", "item"),
                        Lists.newArrayList(
                                "A# ",
                                "#Y#",
                                " #X"
                        ), new Identifier(ModId, "kraken_slayer"));
                registerAndBookLordSoul(KRAKEN_SLAYER_BOW_RECIPE, "kraken_slayer_bow", CommonConfig.RECIPE_KRAKEN_SLAYER_BOW.get());
            }
            if (!CommonConfig.RECIPE_KRAKEN_SLAYER_CROSSBOW.get()) {
                KRAKEN_SLAYER_CROSSBOW_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'Y', 'A'),
                        Lists.newArrayList(
                                new Identifier("iron_ingot"),
                                new Identifier("gold_block"),
                                new Identifier(ModId, "lord_soul"),
                                new Identifier("crossbow")
                        ),
                        Lists.newArrayList("item", "item", "tag", "item"),
                        Lists.newArrayList(
                                "A# ",
                                "#Y#",
                                " #X"
                        ), new Identifier(ModId, "kraken_slayer_crossbow"));
                registerAndBookLordSoul(KRAKEN_SLAYER_CROSSBOW_RECIPE, "kraken_slayer_crossbow", CommonConfig.RECIPE_KRAKEN_SLAYER_CROSSBOW.get());
            }
            if (!CommonConfig.RECIPE_DARKMOON_LONGBOW.get()) {
                DARKMOON_LONGBOW_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'Y', 'A', 'X'),
                        Lists.newArrayList(
                                new Identifier("gold_ingot"),
                                new Identifier(ModId, "essence_of_eventide"),
                                new Identifier("string"),
                                new Identifier("c", "wood_sticks")
                        ),
                        Lists.newArrayList("item", "item", "item", "tag"),
                        Lists.newArrayList(
                                " #A",
                                "XYA",
                                " #A"
                        ), new Identifier(ModId, "darkmoon_longbow"));
                registerAndAddToBook(DARKMOON_LONGBOW_RECIPE, "darkmoon_longbow", CommonConfig.RECIPE_DARKMOON_LONGBOW.get(), ItemRegistry.ESSENCE_OF_EVENTIDE.get());
            }
            if (!CommonConfig.RECIPE_ENHANCED_ARKENPLATE.get()) {
                ENHANCED_ARKENPLATE_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "arkenplate"),
                        "item", new Identifier(ModId, "lord_soul_night_prowler"),
                        new Identifier(ModId, "enhanced_arkenplate"));
                registerAndAddToBook(ENHANCED_ARKENPLATE_RECIPE, "enhanced_arkenplate", CommonConfig.RECIPE_ENHANCED_ARKENPLATE.get(), ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get());
            }
            if (!CommonConfig.RECIPE_ENHANCED_WITHERED_CHEST.get()) {
                ENHANCED_WITHERED_CHEST_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "withered_chest"),
                        "item", new Identifier(ModId, "lord_soul_day_stalker"),
                        new Identifier(ModId, "enhanced_withered_chest"));
                registerAndAddToBook(ENHANCED_WITHERED_CHEST_RECIPE, "enhanced_withered_chest", CommonConfig.RECIPE_ENHANCED_WITHERED_CHEST.get(), ItemRegistry.LORD_SOUL_DAY_STALKER.get());
            }
        }
        if (WeaponUtil.isModLoaded("bewitchment")) {
            BEWITCHMENT_MOLTEN_DEMON_HEART = JsonCreator.createSmeltingRecipeJson(
                    "item", new Identifier("bewitchment", "demon_heart"),
                    new Identifier(ModId, "molten_demon_heart"), 0.1, 200);
        }
    }

    private static void registerAndAddToBook(JsonObject recipe, String name, boolean configBoolean, Item... items) {
        registerRecipe(recipe, name, configBoolean);
        addToRecipeBook(new Identifier(ModId, name + "_recipe"), items);
    }

    private static void registerAndBookLordSoul(JsonObject recipe, String name, boolean configBoolean) {
        registerRecipe(recipe, name, configBoolean);
        addToRecipeBook(new Identifier(ModId, name + "_recipe"), ItemRegistry.LORD_SOUL_DARK.get(), ItemRegistry.LORD_SOUL_PURPLE.get(), ItemRegistry.LORD_SOUL_RED.get(),
                ItemRegistry.LORD_SOUL_ROSE.get(), ItemRegistry.LORD_SOUL_VOID.get(), ItemRegistry.LORD_SOUL_WHITE.get(), ItemRegistry.LORD_SOUL_DAY_STALKER.get(),
                ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get());
    }

    private static void registerRecipe(JsonObject recipe, String name, boolean configBoolean) {
        ArrayList<Object> register = new ArrayList<>();
        register.add(!configBoolean);
        register.add(name + "_recipe");
        register.add(recipe);
        recipes.add(register);
    }

    private static void addToRecipeBook(Identifier recipeId, Item... items) {
        recipeAdvancements.put(items, recipeId);
    }
}