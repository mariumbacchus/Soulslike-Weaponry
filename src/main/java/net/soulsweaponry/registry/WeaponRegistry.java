package net.soulsweaponry.registry;

import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.material.ModToolMaterials;

public class WeaponRegistry {

    public static final ItemGroup MAIN_GROUP = SoulsWeaponry.MAIN_GROUP;

    public static ToolItem BLUEMOON_SHORTSWORD = new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem BLUEMOON_GREATSWORD = new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem MOONLIGHT_SHORTSWORD = new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem MOONLIGHT_GREATSWORD = new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem PURE_MOONLIGHT_GREATSWORD = new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem BLOODTHIRSTER = new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static DetonateGroundItem DARKIN_BLADE = new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem DRAGON_STAFF = new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem WITHERED_WABBAJACK = new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem WHIRLIGIG_SAWBLADE = new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem DRAGONSLAYER_SWORDSPEAR = new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem GUINSOOS_RAGEBLADE = new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem GUTS_SWORD = new DragonslayerSwordBerserk(ModToolMaterials.IRON_BLOCK, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem NIGHTFALL = new Nightfall(ModToolMaterials.IRON_BLOCK, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static DetonateGroundItem COMET_SPEAR = new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem LICH_BANE = new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static BowItem GALEFORCE = new Galeforce(new Item.Settings().group(MAIN_GROUP).maxDamage(1300).rarity(Rarity.EPIC));
    public static ToolItem TRANSLUCENT_SWORD = new SwordItem(ModToolMaterials.LOST_SOUL, 6, -2.4F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem TRANSLUCENT_GLAIVE = new SwordItem(ModToolMaterials.LOST_SOUL, 7, -2.6F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem TRANSLUCENT_DOUBLE_GREATSWORD = new SwordItem(ModToolMaterials.LOST_SOUL, 8, -2.8F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem DRAUGR = new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem DAWNBREAKER = new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem SOUL_REAPER = new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem FORLORN_SCYTHE = new ForlornScythe(ModToolMaterials.LOST_SOUL, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.UNCOMMON));
    public static ToolItem LEVIATHAN_AXE = new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem SKOFNUNG = new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem MJOLNIR = new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem FREYR_SWORD = new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem STING = new Sting(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem FEATHERLIGHT = new Featherlight(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem CRUCIBLE_SWORD = new CrucibleSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem DARKIN_SCYTHE_PRE = new DarkinScythePre(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem DARKIN_SCYTHE_PRIME = new DarkinScythePrime(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem SHADOW_ASSASSIN_SCYTHE = new ShadowAssassinScythe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static TrickWeapon KIRKHAMMER = new TrickWeapon(ModToolMaterials.IRON_BLOCK, 0, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE), 1, 0, true, false);
    public static TrickWeapon SILVER_SWORD = new TrickWeapon(ModToolMaterials.IRON_BLOCK, 1, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE), 0, 1, false, true);
    public static TrickWeapon HOLY_GREATSWORD = new TrickWeapon(ModToolMaterials.IRON_BLOCK, 2, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE), 1, 2, false, true);
    public static ToolItem DRAUPNIR_SPEAR = new DraupnirSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static HolyMoonlightGreatsword HOLY_MOONLIGHT_GREATSWORD = new HolyMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC), 4);
    public static TrickWeapon HOLY_MOONLIGHT_SWORD = new HolyMoonlightSword(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static Frostmourne FROSTMOURNE = new Frostmourne(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem MASTER_SWORD = new MasterSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem NIGHTS_EDGE_ITEM = new NightsEdgeItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem EMPOWERED_DAWNBREAKER = new EmpoweredDawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static BowItem KRAKEN_SLAYER = new KrakenSlayer(new Item.Settings().group(MAIN_GROUP).maxDamage(1258).rarity(Rarity.EPIC));
    public static CrossbowItem KRAKEN_SLAYER_CROSSBOW = new KrakenSlayerCrossbow(new Item.Settings().group(MAIN_GROUP).maxDamage(1258).rarity(Rarity.EPIC));
    public static BowItem DARKMOON_LONGBOW = new DarkmoonLongbow(new Item.Settings().group(MAIN_GROUP).maxDamage(1400).rarity(Rarity.EPIC));

    public static void init() {
        ItemRegistry.registerWeaponItem(BLUEMOON_SHORTSWORD, "bluemoon_shortsword", ConfigConstructor.disable_recipe_bluemoon_shortsword);
        ItemRegistry.registerWeaponItem(BLUEMOON_GREATSWORD, "bluemoon_greatsword", ConfigConstructor.disable_recipe_bluemoon_greatsword);
        ItemRegistry.registerWeaponItem(MOONLIGHT_SHORTSWORD, "moonlight_shortsword", ConfigConstructor.disable_recipe_moonlight_shortsword);
        ItemRegistry.registerWeaponItem(MOONLIGHT_GREATSWORD, "moonlight_greatsword", ConfigConstructor.disable_recipe_moonlight_greatsword);
        ItemRegistry.registerWeaponItem(PURE_MOONLIGHT_GREATSWORD, "pure_moonlight_greatsword", ConfigConstructor.disable_recipe_pure_moonlight_greatsword);
        ItemRegistry.registerWeaponItem(BLOODTHIRSTER, "bloodthirster", ConfigConstructor.disable_recipe_bloodthirster);
        ItemRegistry.registerWeaponItem(DARKIN_BLADE, "darkin_blade", ConfigConstructor.disable_recipe_darkin_blade);
        ItemRegistry.registerWeaponItem(DRAGON_STAFF, "dragon_staff", ConfigConstructor.disable_recipe_dragon_staff);
        ItemRegistry.registerWeaponItem(WITHERED_WABBAJACK, "withered_wabbajack", ConfigConstructor.disable_recipe_withered_wabbajack);
        ItemRegistry.registerWeaponItem(WHIRLIGIG_SAWBLADE, "whirligig_sawblade", ConfigConstructor.disable_recipe_whirligig_sawblade);
        ItemRegistry.registerWeaponItem(DRAGONSLAYER_SWORDSPEAR, "dragonslayer_swordspear", ConfigConstructor.disable_recipe_dragonslayer_swordspear);
        ItemRegistry.registerWeaponItem(GUINSOOS_RAGEBLADE, "rageblade", ConfigConstructor.disable_recipe_rageblade);
        ItemRegistry.registerWeaponItem(GUTS_SWORD, "guts_sword", ConfigConstructor.disable_recipe_heap_of_raw_iron);
        ItemRegistry.registerWeaponItem(NIGHTFALL, "nightfall", ConfigConstructor.disable_recipe_nightfall);
        ItemRegistry.registerWeaponItem(COMET_SPEAR, "comet_spear", ConfigConstructor.disable_recipe_comet_spear);
        ItemRegistry.registerWeaponItem(LICH_BANE, "lich_bane", ConfigConstructor.disable_recipe_lich_bane);
        ItemRegistry.registerWeaponItem(GALEFORCE, "galeforce", ConfigConstructor.disable_recipe_galeforce);
        ItemRegistry.registerItem(TRANSLUCENT_SWORD, "translucent_sword");
        ItemRegistry.registerItem(TRANSLUCENT_GLAIVE, "translucent_glaive");
        ItemRegistry.registerItem(TRANSLUCENT_DOUBLE_GREATSWORD, "translucent_double_greatsword");
        ItemRegistry.registerWeaponItem(DRAUGR, "draugr", ConfigConstructor.disable_recipe_draugr);
        ItemRegistry.registerWeaponItem(DAWNBREAKER, "dawnbreaker", ConfigConstructor.disable_recipe_dawnbreaker);
        ItemRegistry.registerWeaponItem(SOUL_REAPER, "soul_reaper", ConfigConstructor.disable_recipe_soul_reaper);
        ItemRegistry.registerWeaponItem(FORLORN_SCYTHE, "forlorn_scythe", ConfigConstructor.disable_recipe_forlorn_scythe);
        ItemRegistry.registerItem(LEVIATHAN_AXE, "leviathan_axe"); //Handled in RecipeHandler
        ItemRegistry.registerWeaponItem(SKOFNUNG, "skofnung", ConfigConstructor.disable_recipe_skofnung);
        ItemRegistry.registerWeaponItem(MJOLNIR, "mjolnir", ConfigConstructor.disable_recipe_mjolnir);
        ItemRegistry.registerWeaponItem(FREYR_SWORD, "freyr_sword", ConfigConstructor.disable_recipe_sword_of_freyr);
        ItemRegistry.registerWeaponItem(STING, "sting", ConfigConstructor.disable_recipe_sting);
        ItemRegistry.registerWeaponItem(FEATHERLIGHT, "featherlight", ConfigConstructor.disable_recipe_featherlight);
        ItemRegistry.registerWeaponItem(CRUCIBLE_SWORD, "crucible_sword", ConfigConstructor.disable_recipe_crucible_sword);
        ItemRegistry.registerWeaponItem(DARKIN_SCYTHE_PRE, "darkin_scythe_pre", ConfigConstructor.disable_recipe_darkin_scythe);
        ItemRegistry.registerItem(DARKIN_SCYTHE_PRIME, "darkin_scythe"); // Gained by transforming DARKIN_SCYTHE_PRE
        ItemRegistry.registerItem(SHADOW_ASSASSIN_SCYTHE, "shadow_assassin_scythe"); // Gained by transforming DARKIN_SCYTHE_PRE
        ItemRegistry.registerWeaponItem(KIRKHAMMER, "kirkhammer", ConfigConstructor.disable_recipe_kirkhammer);
        ItemRegistry.registerItem(SILVER_SWORD, "silver_sword"); // Switched to by other trick weapons
        ItemRegistry.registerWeaponItem(HOLY_GREATSWORD, "holy_greatsword", ConfigConstructor.disable_recipe_ludwigs_holy_blade);
        ItemRegistry.registerWeaponItem(DRAUPNIR_SPEAR, "draupnir_spear", ConfigConstructor.disable_recipe_draupnir_spear);
        ItemRegistry.registerItem(HOLY_MOONLIGHT_GREATSWORD, "holy_moonlight_greatsword"); // Switched to by Holy Moonlight Sword
        ItemRegistry.registerWeaponItem(HOLY_MOONLIGHT_SWORD, "holy_moonlight_sword", ConfigConstructor.disable_recipe_holy_moonlight_sword);
        ItemRegistry.registerWeaponItem(FROSTMOURNE, "frostmourne", ConfigConstructor.disable_recipe_frostmourne);
        ItemRegistry.registerWeaponItem(MASTER_SWORD, "master_sword", ConfigConstructor.disable_recipe_master_sword);
        ItemRegistry.registerWeaponItem(NIGHTS_EDGE_ITEM, "nights_edge_item", ConfigConstructor.disable_recipe_nights_edge);
        ItemRegistry.registerWeaponItem(EMPOWERED_DAWNBREAKER, "empowered_dawnbreaker", ConfigConstructor.disable_recipe_empowered_dawnbreaker);
        ItemRegistry.registerWeaponItem(KRAKEN_SLAYER, "kraken_slayer", ConfigConstructor.disable_recipe_kraken_slayer_bow);
        ItemRegistry.registerWeaponItem(KRAKEN_SLAYER_CROSSBOW, "kraken_slayer_crossbow", ConfigConstructor.disable_recipe_kraken_slayer_crossbow);
        ItemRegistry.registerWeaponItem(DARKMOON_LONGBOW, "darkmoon_longbow", ConfigConstructor.disable_recipe_darkmoon_longbow);
    }
}
