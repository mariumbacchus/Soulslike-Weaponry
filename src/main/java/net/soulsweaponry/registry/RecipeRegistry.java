package net.soulsweaponry.registry;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.JsonCreator;

public class RecipeRegistry {

    private static String ModId = SoulsWeaponry.ModId;
    
    public static JsonObject HUNTER_CANNON_RECIPE = null;
    public static JsonObject HUNTER_PISTOL_RECIPE = null;
    public static JsonObject GATLING_GUN_RECIPE = null;
    public static JsonObject BLUNDERBUSS_RECIPE = null;

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
    //public static JsonObject PURE_MOONLIGHT_SHORT_RECIPE = null;
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

    public static ArrayList<ArrayList<Object>> recipes = new ArrayList<>();

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

                registerRecipe(BLOODTHIRSTER_RECIPE, "bloodthirster", ConfigConstructor.disable_recipe_bloodthirster);
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

                registerRecipe(COMET_SPEAR_RECIPE, "comet_spear", ConfigConstructor.disable_recipe_comet_spear);
            }
            if (!ConfigConstructor.disable_recipe_darkin_blade) {
                DARKIN_BLADE_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier(ModId, "bloodthirster"), 
                    "tag", new Identifier(ModId, "lord_soul"), 
                    new Identifier(ModId, "darkin_blade"));

                registerRecipe(DARKIN_BLADE_RECIPE, "darkin_blade", ConfigConstructor.disable_recipe_darkin_blade);
            }
            if (!ConfigConstructor.disable_recipe_dawnbreaker) {
                DAWNBREAKER_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier("minecraft", "golden_sword"), 
                    "tag", new Identifier(ModId, "lord_soul"), 
                    new Identifier(ModId, "dawnbreaker"));

                registerRecipe(DAWNBREAKER_RECIPE, "dawnbreaker", ConfigConstructor.disable_recipe_dawnbreaker);
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

                registerRecipe(DRAGON_STAFF_RECIPE, "dragon_staff", ConfigConstructor.disable_recipe_dragon_staff);
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

                registerRecipe(GUTS_SWORD_RECIPE, "guts_sword", ConfigConstructor.disable_recipe_heap_of_raw_iron);
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

                registerRecipe(DRAGONSLAYER_SWORDSPEAR_RECIPE, "dragonslayer_swordspear", ConfigConstructor.disable_recipe_dragonslayer_swordspear);
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

                registerRecipe(GALEFORCE_RECIPE, "galeforce", ConfigConstructor.disable_recipe_galeforce);
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

                registerRecipe(LICH_BANE_RECIPE, "lich_bane", ConfigConstructor.disable_recipe_lich_bane);
            }
            if (!ConfigConstructor.disable_recipe_moonlight_greatsword) {
                MOONLIGHT_GREATSWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier(ModId, "bluemoon_greatsword"), 
                    "tag", new Identifier(ModId, "lord_soul"), 
                    new Identifier(ModId, "moonlight_greatsword"));

                registerRecipe(MOONLIGHT_GREATSWORD_RECIPE, "moonlight_greatsword", ConfigConstructor.disable_recipe_moonlight_greatsword);
            }
            if (!ConfigConstructor.disable_recipe_moonlight_shortsword) {
                MOONLIGHT_SHORTSWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier(ModId, "bluemoon_shortsword"), 
                    "tag", new Identifier(ModId, "lord_soul"), 
                    new Identifier(ModId, "moonlight_shortsword"));

                registerRecipe(MOONLIGHT_SHORTSWORD_RECIPE, "moonlight_shortsword", ConfigConstructor.disable_recipe_moonlight_shortsword);
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

                registerRecipe(NIGHTFALL_RECIPE, "nightfall", ConfigConstructor.disable_recipe_nightfall);
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

                registerRecipe(RAGEBLADE_RECIPE, "rageblade", ConfigConstructor.disable_recipe_rageblade);
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

                registerRecipe(SOUL_REAPER_RECIPE, "soul_reaper", ConfigConstructor.disable_recipe_soul_reaper);
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

                registerRecipe(WHIRLIGIG_SAWBLADE_RECIPE, "whirligig_sawblade", ConfigConstructor.disable_recipe_whirligig_sawblade);
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

                registerRecipe(WABBAJACK_RECIPE, "withered_wabbajack", ConfigConstructor.disable_recipe_withered_wabbajack);
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

                registerRecipe(LEVIATHAN_AXE_LEFT, "leviathan_axe_left", ConfigConstructor.disable_recipe_leviathan_axe);

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

                registerRecipe(SKOFNUNG_RECIPE, "skofnung", ConfigConstructor.disable_recipe_skofnung);
            }
            if (!ConfigConstructor.disable_recipe_pure_moonlight_greatsword) {
                PURE_MOONLIGHT_GREAT_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "tag", new Identifier(ModId, "moonlight_sword"), 
                    "item", new Identifier(ModId, "essence_of_luminescence"), 
                    new Identifier(ModId, "pure_moonlight_greatsword"));

                registerRecipe(PURE_MOONLIGHT_GREAT_RECIPE, "pure_moonlight_greatsword", ConfigConstructor.disable_recipe_pure_moonlight_greatsword);
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

                registerRecipe(MJOLNIR_RECIPE, "mjolnir", ConfigConstructor.disable_recipe_mjolnir);
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

                registerRecipe(FREYR_SWORD_RECIPE, "freyr_sword", ConfigConstructor.disable_recipe_sword_of_freyr);
            }
            if (!ConfigConstructor.disable_recipe_draugr) {
                DRAUGR_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier("minecraft", "iron_sword"), 
                    "item", new Identifier(ModId, "essence_of_eventide"), 
                    new Identifier(ModId, "draugr"));

                registerRecipe(DRAUGR_RECIPE, "draugr", ConfigConstructor.disable_recipe_draugr);
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

                registerRecipe(STING_RECIPE, "sting", ConfigConstructor.disable_recipe_sting);
            }
            if (!ConfigConstructor.disable_recipe_featherlight) {
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

                registerRecipe(FEATHERLIGHT_RECIPE, "featherlight", ConfigConstructor.disable_recipe_featherlight);
            }
            if (!ConfigConstructor.disable_recipe_crucible_sword) {
                CRUCIBLE_SWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier("minecraft", "netherite_sword"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "crucible_sword"));

                registerRecipe(CRUCIBLE_SWORD_RECIPE, "crucible_sword", ConfigConstructor.disable_recipe_crucible_sword);
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

                registerRecipe(DARKIN_SCYTHE_RECIPE, "darkin_scythe_pre", ConfigConstructor.disable_recipe_darkin_scythe);
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

                registerRecipe(KIRKHAMMER_RECIPE, "kirkhammer", ConfigConstructor.disable_recipe_kirkhammer);
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

                registerRecipe(HOLY_GREATSWORD_RECIPE, "holy_greatsword", ConfigConstructor.disable_recipe_ludwigs_holy_blade);
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

                registerRecipe(DRAUPNIR_SPEAR_RECIPE, "draupnir_spear", ConfigConstructor.disable_recipe_draupnir_spear);
            }
            if (!ConfigConstructor.disable_recipe_holy_moonlight_sword) {
                HOLY_MOONLIGHT_SWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                        "item", new Identifier(ModId, "sting"),
                        "tag", new Identifier(ModId, "lord_soul"),
                        new Identifier(ModId, "holy_moonlight_sword"));

                registerRecipe(HOLY_MOONLIGHT_SWORD_RECIPE, "holy_moonlight_sword", ConfigConstructor.disable_recipe_holy_moonlight_sword);
            }
        }
        if (FabricLoader.getInstance().isModLoaded("bewitchment")) {
            BEWITCHMENT_MOLTEN_DEMON_HEART = JsonCreator.createSmeltingRecipeJson(
                "item", new Identifier("bewitchment", "demon_heart"), 
            new Identifier(ModId, "molten_demon_heart"), 0.1, 200);

            ArrayList<ArrayList<Object>> list = new ArrayList<>();
            list.add(Lists.newArrayList("item", new Identifier("bewitchment", "silver_ingot")));
            list.add(Lists.newArrayList("item", new Identifier(ModId, "lost_soul")));
            BEWITCHMENT_SILVER_BULLET = JsonCreator.createShapelessRecipeJson(
                list, new Identifier(ModId, "silver_bullet"), 10);
        }
    }

    private static void registerRecipe(JsonObject recipe, String name, boolean configBoolean) {
        ArrayList<Object> register = new ArrayList<>();
        register.add(!configBoolean);
        register.add(name);
        register.add(recipe);
        recipes.add(register);
    }
}
