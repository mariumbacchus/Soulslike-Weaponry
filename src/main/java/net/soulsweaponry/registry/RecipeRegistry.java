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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_bloodthirster);
                register.add("bloodthirster");
                register.add(BLOODTHIRSTER_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_comet_spear);
                register.add("comet_spear");
                register.add(COMET_SPEAR_RECIPE);
                recipes.add(register);
            }
            if (!ConfigConstructor.disable_recipe_darkin_blade) {
                DARKIN_BLADE_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier(ModId, "bloodthirster"), 
                    "tag", new Identifier(ModId, "lord_soul"), 
                    new Identifier(ModId, "darkin_blade"));
                
                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_darkin_blade);
                register.add("darkin_blade");
                register.add(DARKIN_BLADE_RECIPE);
                recipes.add(register);
            }
            if (!ConfigConstructor.disable_recipe_dawnbreaker) {
                DAWNBREAKER_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier("minecraft", "golden_sword"), 
                    "tag", new Identifier(ModId, "lord_soul"), 
                    new Identifier(ModId, "dawnbreaker"));
                
                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_dawnbreaker);
                register.add("dawnbreaker");
                register.add(DAWNBREAKER_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_dragon_staff);
                register.add("dragon_staff");
                register.add(DRAGON_STAFF_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_heap_of_raw_iron);
                register.add("guts_sword");
                register.add(GUTS_SWORD_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_dragonslayer_swordspear);
                register.add("dragonslayer_swordspear");
                register.add(DRAGONSLAYER_SWORDSPEAR_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_galeforce);
                register.add("galeforce");
                register.add(GALEFORCE_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_lich_bane);
                register.add("lich_bane");
                register.add(LICH_BANE_RECIPE);
                recipes.add(register);
            }
            if (!ConfigConstructor.disable_recipe_moonlight_greatsword) {
                MOONLIGHT_GREATSWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier(ModId, "bluemoon_greatsword"), 
                    "tag", new Identifier(ModId, "lord_soul"), 
                    new Identifier(ModId, "moonlight_greatsword"));
                
                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_moonlight_greatsword);
                register.add("moonlight_greatsword");
                register.add(MOONLIGHT_GREATSWORD_RECIPE);
                recipes.add(register);
            }
            if (!ConfigConstructor.disable_recipe_moonlight_shortsword) {
                MOONLIGHT_SHORTSWORD_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier(ModId, "bluemoon_shortsword"), 
                    "tag", new Identifier(ModId, "lord_soul"), 
                    new Identifier(ModId, "moonlight_shortsword"));

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_moonlight_shortsword);
                register.add("moonlight_shortsword");
                register.add(MOONLIGHT_SHORTSWORD_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_nightfall);
                register.add("nightfall");
                register.add(NIGHTFALL_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_rageblade);
                register.add("rageblade");
                register.add(RAGEBLADE_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_soul_reaper);
                register.add("soul_reaper");
                register.add(SOUL_REAPER_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_whirligig_sawblade);
                register.add("whirligig_sawblade");
                register.add(WHIRLIGIG_SAWBLADE_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_withered_wabbajack);
                register.add("withered_wabbajack");
                register.add(WABBAJACK_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register_left = new ArrayList<>();
                register_left.add(!ConfigConstructor.disable_recipe_leviathan_axe);
                register_left.add("leviathan_axe_left");
                register_left.add(LEVIATHAN_AXE_LEFT);
                recipes.add(register_left);

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

                ArrayList<Object> register_right = new ArrayList<>();
                register_right.add(!ConfigConstructor.disable_recipe_leviathan_axe);
                register_right.add("leviathan_axe_right");
                register_right.add(LEVIATHAN_AXE_RIGHT);
                recipes.add(register_right);
            }
            if (!ConfigConstructor.disable_recipe_skofnung) {
                SKOFNUNG_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier("minecraft", "iron_sword"), 
                    "tag", new Identifier(ModId, "lord_soul"), 
                    new Identifier(ModId, "skofnung"));
                
                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_skofnung);
                register.add("skofnung");
                register.add(SKOFNUNG_RECIPE);
                recipes.add(register);
            }
            if (!ConfigConstructor.disable_recipe_pure_moonlight_greatsword) {
                PURE_MOONLIGHT_GREAT_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "tag", new Identifier(ModId, "moonlight_sword"), 
                    "item", new Identifier(ModId, "essence_of_luminescence"), 
                    new Identifier(ModId, "pure_moonlight_greatsword"));
                
                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_pure_moonlight_greatsword);
                register.add("pure_moonlight_greatsword");
                register.add(PURE_MOONLIGHT_GREAT_RECIPE);
                recipes.add(register);
            }
            /* if (!ConfigConstructor.disable_recipe_pure_moonlight_shortsword) {
                PURE_MOONLIGHT_SHORT_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier(ModId, "moonlight_shortsword"), 
                    "item", new Identifier(ModId, "arkenstone"), 
                    new Identifier(ModId, "pure_moonlight_shortsword"));
                
                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_pure_moonlight_shortsword);
                register.add("pure_moonlight_shortsword");
                register.add(PURE_MOONLIGHT_SHORT_RECIPE);
                recipes.add(register);
            } */
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_mjolnir);
                register.add("mjolnir");
                register.add(MJOLNIR_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_sword_of_freyr);
                register.add("freyr_sword");
                register.add(FREYR_SWORD_RECIPE);
                recipes.add(register);
            }
            if (!ConfigConstructor.disable_recipe_draugr) {
                DRAUGR_RECIPE = JsonCreator.createSmithingRecipeJson(
                    "item", new Identifier("minecraft", "iron_sword"), 
                    "item", new Identifier(ModId, "essence_of_eventide"), 
                    new Identifier(ModId, "draugr"));
                
                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_draugr);
                register.add("draugr");
                register.add(DRAUGR_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_sting);
                register.add("sting");
                register.add(STING_RECIPE);
                recipes.add(register);
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

                ArrayList<Object> register = new ArrayList<>();
                register.add(!ConfigConstructor.disable_recipe_featherlight);
                register.add("featherlight");
                register.add(FEATHERLIGHT_RECIPE);
                recipes.add(register);
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
}
