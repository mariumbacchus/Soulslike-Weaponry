package net.soulsweaponry.registry;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.JsonCreator;

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
    public static JsonObject KRAKEN_SLAYER_RECIPE = null;
    public static JsonObject DARKMOON_LONGBOW_RECIPE = null;

    public static ArrayList<ArrayList<Object>> recipes = new ArrayList<>();
    public static HashMap<Item[], Identifier> recipeAdvancements = new HashMap<>();

    public static JsonObject BEWITCHMENT_MOLTEN_DEMON_HEART = null;
    public static JsonObject BEWITCHMENT_SILVER_BULLET = null;

    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("soulsweapons") && !ConfigConstructor.disable_all_legendary_recipes) {
            if (!ConfigConstructor.disable_gun_recipes) {
                HUNTER_CANNON_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'G', 'M', 'S'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_ingot"),
                                new Identifier(ModId, "lost_soul"),
                                new Identifier("minecraft", "iron_block"),
                                new Identifier("minecraft", "stick")
                        ),
                        Lists.newArrayList("item", "item", "item", "item"),
                        Lists.newArrayList(
                                "S M",
                                "SG#",
                                " MM"
                        ), new Identifier(ModId, "hunter_cannon"));

                HUNTER_PISTOL_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'G', 'S'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_ingot"),
                                new Identifier(ModId, "lost_soul"),
                                new Identifier("minecraft", "stick")
                        ),
                        Lists.newArrayList("item", "item", "item"),
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
                                new Identifier("minecraft", "stick")
                        ),
                        Lists.newArrayList("item", "item", "item", "item"),
                        Lists.newArrayList(
                                "S #",
                                "SG#",
                                " #M"
                        ), new Identifier(ModId, "gatling_gun"));

                BLUNDERBUSS_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'G', 'S', 'i'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_block"),
                                new Identifier(ModId, "lost_soul"),
                                new Identifier("minecraft", "stick"),
                                new Identifier("minecraft", "iron_ingot")
                        ),
                        Lists.newArrayList("item", "item", "item", "item"),
                        Lists.newArrayList(
                                " i#",
                                "SGi",
                                "S i"
                        ), new Identifier(ModId, "blunderbuss"));
            }
            if (!ConfigConstructor.disable_recipe_bloodthirster) {
                BLOODTHIRSTER_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('i', 'S', '/'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_ingot"),
                                new Identifier(ModId, "crimson_ingot"),
                                new Identifier("minecraft", "stick")
                        ),
                        Lists.newArrayList("item", "item", "item"),
                        Lists.newArrayList(
                                "iSi",
                                "iSi",
                                " / "
                        ), new Identifier(ModId, "bloodthirster"));

                registerAndAddToBook(BLOODTHIRSTER_RECIPE, "bloodthirster", ConfigConstructor.disable_recipe_bloodthirster, ItemRegistry.CRIMSON_INGOT);
            }
            if (!ConfigConstructor.disable_recipe_comet_spear) {
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

                registerAndBookLordSoul(COMET_SPEAR_RECIPE, "comet_spear", ConfigConstructor.disable_recipe_comet_spear);
            }
            if (!ConfigConstructor.disable_recipe_darkin_blade) {
                DARKIN_BLADE_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "bloodthirster"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "darkin_blade"));

                registerAndBookLordSoul(DARKIN_BLADE_RECIPE, "darkin_blade", ConfigConstructor.disable_recipe_darkin_blade);
            }
            if (!ConfigConstructor.disable_recipe_dawnbreaker) {
                DAWNBREAKER_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "golden_sword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "dawnbreaker"));

                registerAndBookLordSoul(DAWNBREAKER_RECIPE, "dawnbreaker", ConfigConstructor.disable_recipe_dawnbreaker);
            }
            if (!ConfigConstructor.disable_recipe_dragon_staff) {
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

                registerAndAddToBook(DRAGON_STAFF_RECIPE, "dragon_staff", ConfigConstructor.disable_recipe_dragon_staff, Items.BLAZE_ROD);
            }
            if (!ConfigConstructor.disable_recipe_heap_of_raw_iron) {
                GUTS_SWORD_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X'),
                        Lists.newArrayList(new Identifier("minecraft", "iron_block"),
                                new Identifier("minecraft", "stick")
                        ),
                        Lists.newArrayList("item", "item"),
                        Lists.newArrayList(
                                " # ",
                                "###",
                                "#X#"
                        ), new Identifier(ModId, "guts_sword"));

                registerAndAddToBook(GUTS_SWORD_RECIPE, "guts_sword", ConfigConstructor.disable_recipe_heap_of_raw_iron, Items.IRON_BLOCK);
            }
            if (!ConfigConstructor.disable_recipe_dragonslayer_swordspear) {
                DRAGONSLAYER_SWORDSPEAR_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('S', 'G', '/'),
                        Lists.newArrayList(new Identifier(ModId, "lord_soul"),
                                new Identifier("minecraft", "gold_ingot"),
                                new Identifier("minecraft", "stick")
                        ),
                        Lists.newArrayList("tag", "item", "item"),
                        Lists.newArrayList(
                                " G ",
                                "GSG",
                                "G/G"
                        ), new Identifier(ModId, "dragonslayer_swordspear"));

                registerAndBookLordSoul(DRAGONSLAYER_SWORDSPEAR_RECIPE, "dragonslayer_swordspear", ConfigConstructor.disable_recipe_dragonslayer_swordspear);
            }
            if (!ConfigConstructor.disable_recipe_galeforce) {
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

                registerAndBookLordSoul(GALEFORCE_RECIPE, "galeforce", ConfigConstructor.disable_recipe_galeforce);
            }
            if (!ConfigConstructor.disable_recipe_lich_bane) {
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

                registerAndBookLordSoul(LICH_BANE_RECIPE, "lich_bane", ConfigConstructor.disable_recipe_lich_bane);
            }
            if (!ConfigConstructor.disable_recipe_moonlight_greatsword) {
                MOONLIGHT_GREATSWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "bluemoon_greatsword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "moonlight_greatsword"));

                registerAndBookLordSoul(MOONLIGHT_GREATSWORD_RECIPE, "moonlight_greatsword", ConfigConstructor.disable_recipe_moonlight_greatsword);
            }
            if (!ConfigConstructor.disable_recipe_moonlight_shortsword) {
                MOONLIGHT_SHORTSWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "bluemoon_shortsword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "moonlight_shortsword"));

                registerAndBookLordSoul(MOONLIGHT_SHORTSWORD_RECIPE, "moonlight_shortsword", ConfigConstructor.disable_recipe_moonlight_shortsword);
            }
            if (!ConfigConstructor.disable_recipe_nightfall) {
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

                registerAndBookLordSoul(NIGHTFALL_RECIPE, "nightfall", ConfigConstructor.disable_recipe_nightfall);
            }
            if (!ConfigConstructor.disable_recipe_rageblade) {
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

                registerAndBookLordSoul(RAGEBLADE_RECIPE, "rageblade", ConfigConstructor.disable_recipe_rageblade);
            }
            if (!ConfigConstructor.disable_recipe_soul_reaper) {
                SOUL_REAPER_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('i', '/', 'S'),
                        Lists.newArrayList(new Identifier(ModId, "soul_ingot"),
                                new Identifier("minecraft", "stick"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "item", "tag"),
                        Lists.newArrayList(
                                " ii",
                                "S/ ",
                                " / "
                        ), new Identifier(ModId, "soul_reaper"));

                registerAndBookLordSoul(SOUL_REAPER_RECIPE, "soul_reaper", ConfigConstructor.disable_recipe_soul_reaper);
            }
            if (!ConfigConstructor.disable_recipe_whirligig_sawblade) {
                WHIRLIGIG_SAWBLADE_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('/', 'i', 'S'),
                        Lists.newArrayList(new Identifier("minecraft", "stick"),
                                new Identifier("minecraft", "iron_ingot"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "item", "tag"),
                        Lists.newArrayList(
                                " i ",
                                "iSi",
                                "/i "
                        ), new Identifier(ModId, "whirligig_sawblade"));

                registerAndBookLordSoul(WHIRLIGIG_SAWBLADE_RECIPE, "whirligig_sawblade", ConfigConstructor.disable_recipe_whirligig_sawblade);
            }
            if (!ConfigConstructor.disable_recipe_withered_wabbajack) {
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

                registerAndAddToBook(WABBAJACK_RECIPE, "withered_wabbajack", ConfigConstructor.disable_recipe_withered_wabbajack, Items.WITHER_SKELETON_SKULL);
            }
            if (!ConfigConstructor.disable_recipe_leviathan_axe) {
                LEVIATHAN_AXE_LEFT = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('S', '#', '/'),
                        Lists.newArrayList(new Identifier(ModId, "lord_soul"),
                                new Identifier(ModId, "verglas"),
                                new Identifier("minecraft", "stick")
                        ),
                        Lists.newArrayList("tag", "item", "item"),
                        Lists.newArrayList(
                                "#S",
                                "#/",
                                " /"
                        ), new Identifier(ModId, "leviathan_axe"));

                registerAndBookLordSoul(LEVIATHAN_AXE_LEFT, "leviathan_axe_left", ConfigConstructor.disable_recipe_leviathan_axe);

                LEVIATHAN_AXE_RIGHT = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('S', '#', '/'),
                        Lists.newArrayList(new Identifier(ModId, "lord_soul"),
                                new Identifier(ModId, "verglas"),
                                new Identifier("minecraft", "stick")
                        ),
                        Lists.newArrayList("tag", "item", "item"),
                        Lists.newArrayList(
                                "S#",
                                "/#",
                                "/ "
                        ), new Identifier(ModId, "leviathan_axe"));

                registerRecipe(LEVIATHAN_AXE_RIGHT, "leviathan_axe_right", ConfigConstructor.disable_recipe_leviathan_axe);
            }
            if (!ConfigConstructor.disable_recipe_skofnung) {
                SKOFNUNG_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "iron_sword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "skofnung"));

                registerAndBookLordSoul(SKOFNUNG_RECIPE, "skofnung", ConfigConstructor.disable_recipe_skofnung);
            }
            if (!ConfigConstructor.disable_recipe_pure_moonlight_greatsword) {
                PURE_MOONLIGHT_GREAT_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "tag", new Identifier(ModId, "moonlight_sword"),
                        "item", new Identifier(ModId, "essence_of_luminescence"),
                        new Identifier(ModId, "pure_moonlight_greatsword"));

                registerAndAddToBook(PURE_MOONLIGHT_GREAT_RECIPE, "pure_moonlight_greatsword", ConfigConstructor.disable_recipe_pure_moonlight_greatsword, ItemRegistry.ESSENCE_OF_LUMINESCENCE);
            }
            if (!ConfigConstructor.disable_recipe_mjolnir) {
                MJOLNIR_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'O', 'Y'),
                        Lists.newArrayList(new Identifier("minecraft", "stick"),
                                new Identifier("minecraft", "iron_block"),
                                new Identifier(ModId, "verglas"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "item", "item", "tag"),
                        Lists.newArrayList(
                                "XOX",
                                "XYX",
                                " # "
                        ), new Identifier(ModId, "mjolnir"));

                registerAndBookLordSoul(MJOLNIR_RECIPE, "mjolnir", ConfigConstructor.disable_recipe_mjolnir);
            }
            if (!ConfigConstructor.disable_recipe_sword_of_freyr) {
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

                registerAndBookLordSoul(FREYR_SWORD_RECIPE, "freyr_sword", ConfigConstructor.disable_recipe_sword_of_freyr);
            }
            if (!ConfigConstructor.disable_recipe_draugr) {
                DRAUGR_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "iron_sword"),
                        "item", new Identifier(ModId, "essence_of_eventide"),
                        new Identifier(ModId, "draugr"));

                registerAndAddToBook(DRAUGR_RECIPE, "draugr", ConfigConstructor.disable_recipe_draugr, ItemRegistry.ESSENCE_OF_EVENTIDE);
            }
            if (!ConfigConstructor.disable_recipe_sting) {
                STING_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X'),
                        Lists.newArrayList(
                                new Identifier(ModId, "verglas"),
                                new Identifier("minecraft", "stick")
                        ),
                        Lists.newArrayList("item", "item"),
                        Lists.newArrayList(
                                "#",
                                "#",
                                "X"
                        ), new Identifier(ModId, "sting"));

                registerAndAddToBook(STING_RECIPE, "sting", ConfigConstructor.disable_recipe_sting, ItemRegistry.VERGLAS);
            }if (!ConfigConstructor.disable_recipe_featherlight) {
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

                registerAndAddToBook(FEATHERLIGHT_RECIPE, "featherlight", ConfigConstructor.disable_recipe_featherlight, Items.AMETHYST_SHARD);
            }
            if (!ConfigConstructor.disable_recipe_crucible_sword) {
                CRUCIBLE_SWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "netherite_sword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "crucible_sword"));

                registerAndBookLordSoul(CRUCIBLE_SWORD_RECIPE, "crucible_sword", ConfigConstructor.disable_recipe_crucible_sword);
            }
            if (!ConfigConstructor.disable_recipe_darkin_scythe) {
                DARKIN_SCYTHE_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'O'),
                        Lists.newArrayList(
                                new Identifier(ModId, "crimson_ingot"),
                                new Identifier("minecraft", "stick"),
                                new Identifier(ModId, "lord_soul")
                        ),
                        Lists.newArrayList("item", "item", "tag"),
                        Lists.newArrayList(
                                " ##",
                                "OX ",
                                " X "
                        ), new Identifier(ModId, "darkin_scythe_pre"));

                registerAndBookLordSoul(DARKIN_SCYTHE_RECIPE, "darkin_scythe_pre", ConfigConstructor.disable_recipe_darkin_scythe);
            }
            if (!ConfigConstructor.disable_recipe_kirkhammer) {
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

                registerAndAddToBook(KIRKHAMMER_RECIPE, "kirkhammer", ConfigConstructor.disable_recipe_kirkhammer, Items.IRON_SWORD);
            }
            if (!ConfigConstructor.disable_recipe_ludwigs_holy_blade) {
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

                registerAndAddToBook(HOLY_GREATSWORD_RECIPE, "holy_greatsword", ConfigConstructor.disable_recipe_ludwigs_holy_blade, Items.IRON_INGOT);
            }
            if (!ConfigConstructor.disable_recipe_draupnir_spear) {
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
                registerAndBookLordSoul(DRAUPNIR_SPEAR_RECIPE, "draupnir_spear", ConfigConstructor.disable_recipe_draupnir_spear);
            }
            if (!ConfigConstructor.disable_recipe_holy_moonlight_sword) {
                HOLY_MOONLIGHT_SWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "sting"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "holy_moonlight_sword"));

                registerAndBookLordSoul(HOLY_MOONLIGHT_SWORD_RECIPE, "holy_moonlight_sword", ConfigConstructor.disable_recipe_holy_moonlight_sword);
            }
            if (!ConfigConstructor.disable_recipe_master_sword) {
                MASTER_SWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "diamond_sword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "master_sword"));

                registerAndBookLordSoul(MASTER_SWORD_RECIPE, "master_sword", ConfigConstructor.disable_recipe_master_sword);
            }
            if (!ConfigConstructor.disable_recipe_frostmourne) {
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
                registerAndBookLordSoul(FROSTMOURNE_RECIPE, "frostmourne", ConfigConstructor.disable_recipe_frostmourne);
            }
            if (!ConfigConstructor.disable_recipe_nights_edge) {
                NIGHTS_EDGE_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('/', 'X', 'Y', '#'),
                        Lists.newArrayList(
                                new Identifier("stick"),
                                new Identifier(ModId, "moonstone"),
                                new Identifier(ModId, "lord_soul_night_prowler"),
                                new Identifier(ModId, "soul_ingot")
                        ),
                        Lists.newArrayList("item", "item", "item", "item"),
                        Lists.newArrayList(
                                " XX",
                                "#YX",
                                "/# "
                        ), new Identifier(ModId, "nights_edge_item"));
                registerAndAddToBook(NIGHTS_EDGE_RECIPE, "nights_edge_item", ConfigConstructor.disable_recipe_nights_edge, ItemRegistry.LORD_SOUL_NIGHT_PROWLER);
            }
            if (!ConfigConstructor.disable_recipe_empowered_dawnbreaker) {
                EMPOWERED_DAWNBREAKER_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "dawnbreaker"),
                        "item", new Identifier(ModId, "lord_soul_day_stalker"),
                        new Identifier(ModId, "empowered_dawnbreaker"));
                registerAndAddToBook(EMPOWERED_DAWNBREAKER_RECIPE, "empowered_dawnbreaker", ConfigConstructor.disable_recipe_empowered_dawnbreaker, ItemRegistry.LORD_SOUL_DAY_STALKER);
            }
            if (!ConfigConstructor.disable_recipe_kraken_slayer) {
                KRAKEN_SLAYER_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'X', 'Y', 'A'),
                        Lists.newArrayList(
                                new Identifier("iron_ingot"),
                                new Identifier("gold_block"),
                                new Identifier(ModId, "lord_soul"),
                                new Identifier("arrow")
                        ),
                        Lists.newArrayList("item", "item", "tag", "item"),
                        Lists.newArrayList(
                                "A# ",
                                "#Y#",
                                " #X"
                        ), new Identifier(ModId, "kraken_slayer"));
                registerAndBookLordSoul(KRAKEN_SLAYER_RECIPE, "kraken_slayer", ConfigConstructor.disable_recipe_kraken_slayer);
            }
            if (!ConfigConstructor.disable_recipe_darkmoon_longbow) {
                DARKMOON_LONGBOW_RECIPE = JsonCreator.createShapedRecipeJson(
                        Lists.newArrayList('#', 'Y', 'A', 'X'),
                        Lists.newArrayList(
                                new Identifier("gold_ingot"),
                                new Identifier(ModId, "essence_of_eventide"),
                                new Identifier("string"),
                                new Identifier("stick")
                        ),
                        Lists.newArrayList("item", "item", "item", "item"),
                        Lists.newArrayList(
                                " #A",
                                "XYA",
                                " #A"
                        ), new Identifier(ModId, "darkmoon_longbow"));
                registerAndAddToBook(DARKMOON_LONGBOW_RECIPE, "darkmoon_longbow", ConfigConstructor.disable_recipe_darkmoon_longbow, ItemRegistry.ESSENCE_OF_EVENTIDE);
            }
        }
        if (FabricLoader.getInstance().isModLoaded("bewitchment")) {
            BEWITCHMENT_MOLTEN_DEMON_HEART = JsonCreator.createSmeltingRecipeJson(
                    "item", new Identifier("bewitchment", "demon_heart"),
                    new Identifier(ModId, "molten_demon_heart"), 0.1, 200);

            if (!ConfigConstructor.disable_gun_recipes) {
                ArrayList<ArrayList<Object>> list = new ArrayList<>();
                list.add(Lists.newArrayList("tag", new Identifier("c", "silver_ingots")));
                list.add(Lists.newArrayList("item", new Identifier(ModId, "lost_soul")));
                list.add(Lists.newArrayList("item", new Identifier("gunpowder")));
                BEWITCHMENT_SILVER_BULLET = JsonCreator.createShapelessRecipeJson(
                        list, new Identifier(ModId, "silver_bullet"), 10);
            }
        } else if (!ConfigConstructor.disable_gun_recipes) {
            ArrayList<ArrayList<Object>> list = new ArrayList<>();
            list.add(Lists.newArrayList("item", new Identifier("iron_ingot")));
            list.add(Lists.newArrayList("item", new Identifier(ModId, "lost_soul")));
            list.add(Lists.newArrayList("item", new Identifier("gunpowder")));
            SILVER_BULLET_RECIPE = JsonCreator.createShapelessRecipeJson(
                    list, new Identifier(ModId, "silver_bullet"), 10);
            registerAndAddToBook(SILVER_BULLET_RECIPE, "silver_bullet", ConfigConstructor.disable_gun_recipes, ItemRegistry.LOST_SOUL);
        }
    }

    private static void registerAndAddToBook(JsonObject recipe, String name, boolean configBoolean, Item... items) {
        registerRecipe(recipe, name, configBoolean);
        addToRecipeBook(new Identifier(ModId, name + "_recipe"), items);
    }

    private static void registerAndBookLordSoul(JsonObject recipe, String name, boolean configBoolean) {
        registerRecipe(recipe, name, configBoolean);
        addToRecipeBook(new Identifier(ModId, name + "_recipe"), ItemRegistry.LORD_SOUL_DARK, ItemRegistry.LORD_SOUL_PURPLE, ItemRegistry.LORD_SOUL_RED,
                ItemRegistry.LORD_SOUL_ROSE, ItemRegistry.LORD_SOUL_VOID, ItemRegistry.LORD_SOUL_WHITE, ItemRegistry.LORD_SOUL_DAY_STALKER,
                ItemRegistry.LORD_SOUL_NIGHT_PROWLER);
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