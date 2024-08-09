package net.soulsweaponry.registry;

import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.material.ModToolMaterials;

import static net.soulsweaponry.SoulsWeaponry.MAIN_GROUP;

public class WeaponRegistry {

    public static RegistryObject<ToolItem> BLUEMOON_SHORTSWORD;
    public static RegistryObject<ToolItem> BLUEMOON_GREATSWORD;
    public static RegistryObject<ToolItem> MOONLIGHT_SHORTSWORD;
    public static RegistryObject<ToolItem> MOONLIGHT_GREATSWORD;
    public static RegistryObject<ToolItem> PURE_MOONLIGHT_GREATSWORD;
    public static RegistryObject<ToolItem> BLOODTHIRSTER;
    public static RegistryObject<DetonateGroundItem> DARKIN_BLADE;
    public static RegistryObject<ToolItem> DRAGON_STAFF;
    public static RegistryObject<ToolItem> WITHERED_WABBAJACK;
    public static RegistryObject<ToolItem> WHIRLIGIG_SAWBLADE;
    public static RegistryObject<ToolItem> DRAGONSLAYER_SWORDSPEAR;
    public static RegistryObject<ToolItem> GUINSOOS_RAGEBLADE;
    public static RegistryObject<ToolItem> GUTS_SWORD;
    public static RegistryObject<ToolItem> NIGHTFALL;
    public static RegistryObject<DetonateGroundItem> COMET_SPEAR;
    public static RegistryObject<ToolItem> LICH_BANE;
    public static RegistryObject<BowItem> GALEFORCE;
    public static RegistryObject<ToolItem> TRANSLUCENT_SWORD;
    public static RegistryObject<ToolItem> TRANSLUCENT_GLAIVE;
    public static RegistryObject<ToolItem> TRANSLUCENT_DOUBLE_GREATSWORD;
    public static RegistryObject<ToolItem> DRAUGR;
    public static RegistryObject<ToolItem> DAWNBREAKER;
    public static RegistryObject<ToolItem> SOUL_REAPER;
    public static RegistryObject<ToolItem> FORLORN_SCYTHE;
    public static RegistryObject<ToolItem> LEVIATHAN_AXE;
    public static RegistryObject<ToolItem> SKOFNUNG;
    public static RegistryObject<ToolItem> MJOLNIR;
    public static RegistryObject<ToolItem> FREYR_SWORD;
    public static RegistryObject<ToolItem> STING;
    public static RegistryObject<ToolItem> FEATHERLIGHT;
    public static RegistryObject<ToolItem> CRUCIBLE_SWORD;
    public static RegistryObject<ToolItem> DARKIN_SCYTHE_PRE;
    public static RegistryObject<ToolItem> DARKIN_SCYTHE_PRIME;
    public static RegistryObject<ToolItem> SHADOW_ASSASSIN_SCYTHE;
    public static RegistryObject<TrickWeapon> KIRKHAMMER;
    public static RegistryObject<TrickWeapon> SILVER_SWORD;
    public static RegistryObject<TrickWeapon> HOLY_GREATSWORD;
    public static RegistryObject<ToolItem> DRAUPNIR_SPEAR;
    public static RegistryObject<HolyMoonlightGreatsword> HOLY_MOONLIGHT_GREATSWORD;
    public static RegistryObject<TrickWeapon> HOLY_MOONLIGHT_SWORD;
    public static RegistryObject<Frostmourne> FROSTMOURNE;
    public static RegistryObject<ToolItem> MASTER_SWORD;
    public static RegistryObject<ToolItem> NIGHTS_EDGE_ITEM;
    public static RegistryObject<ToolItem> EMPOWERED_DAWNBREAKER;
    public static RegistryObject<BowItem> KRAKEN_SLAYER;
    public static RegistryObject<CrossbowItem> KRAKEN_SLAYER_CROSSBOW;
    public static RegistryObject<BowItem> DARKMOON_LONGBOW;

    //public static RegistryObject<Item> TEST_ITEM;

    public static void register() {
        BLUEMOON_SHORTSWORD = ItemRegistry.registerWeaponItem("bluemoon_shortsword", () -> new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_bluemoon_shortsword);
        BLUEMOON_GREATSWORD = ItemRegistry.registerWeaponItem("bluemoon_greatsword", () -> new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_bluemoon_greatsword);
        MOONLIGHT_SHORTSWORD = ItemRegistry.registerWeaponItem("moonlight_shortsword", () -> new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_moonlight_shortsword);
        MOONLIGHT_GREATSWORD = ItemRegistry.registerWeaponItem("moonlight_greatsword", () -> new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_moonlight_greatsword);
        PURE_MOONLIGHT_GREATSWORD = ItemRegistry.registerWeaponItem("pure_moonlight_greatsword", () -> new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_pure_moonlight_greatsword);
        BLOODTHIRSTER = ItemRegistry.registerWeaponItem("bloodthirster", () -> new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_bloodthirster);
        DARKIN_BLADE = ItemRegistry.registerWeaponItem("darkin_blade", () -> new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_darkin_blade);
        DRAGON_STAFF = ItemRegistry.registerWeaponItem("dragon_staff", () -> new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dragon_staff);
        WITHERED_WABBAJACK = ItemRegistry.registerWeaponItem("withered_wabbajack", () -> new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_withered_wabbajack);
        WHIRLIGIG_SAWBLADE = ItemRegistry.registerWeaponItem("whirligig_sawblade", () -> new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_whirligig_sawblade);
        DRAGONSLAYER_SWORDSPEAR = ItemRegistry.registerWeaponItem("dragonslayer_swordspear", () -> new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dragonslayer_swordspear);
        GUINSOOS_RAGEBLADE = ItemRegistry.registerWeaponItem("rageblade", () -> new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_rageblade);
        GUTS_SWORD = ItemRegistry.registerWeaponItem("guts_sword", () -> new DragonslayerSwordBerserk(ModToolMaterials.IRON_BLOCK, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_heap_of_raw_iron);
        NIGHTFALL = ItemRegistry.registerWeaponItem("nightfall", () -> new Nightfall(ModToolMaterials.IRON_BLOCK, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_nightfall);
        COMET_SPEAR = ItemRegistry.registerWeaponItem("comet_spear", () -> new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_comet_spear);
        LICH_BANE = ItemRegistry.registerWeaponItem("lich_bane", () -> new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_lich_bane);
        GALEFORCE = ItemRegistry.registerWeaponItem("galeforce", () -> new Galeforce(new Item.Settings().group(MAIN_GROUP).maxDamage(1300).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_galeforce);
        TRANSLUCENT_SWORD = ItemRegistry.registerItem("translucent_sword", () -> new SwordItem(ModToolMaterials.LOST_SOUL, 6, -2.4F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
        TRANSLUCENT_GLAIVE = ItemRegistry.registerItem("translucent_glaive", () -> new SwordItem(ModToolMaterials.LOST_SOUL, 7, -2.6F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
        TRANSLUCENT_DOUBLE_GREATSWORD = ItemRegistry.registerItem("translucent_double_greatsword", () -> new SwordItem(ModToolMaterials.LOST_SOUL, 8, -2.8F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
        DRAUGR = ItemRegistry.registerWeaponItem("draugr", () -> new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_draugr);
        DAWNBREAKER = ItemRegistry.registerWeaponItem("dawnbreaker", () -> new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dawnbreaker);
        SOUL_REAPER = ItemRegistry.registerWeaponItem("soul_reaper", () -> new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_soul_reaper);
        FORLORN_SCYTHE = ItemRegistry.registerWeaponItem("forlorn_scythe", () -> new ForlornScythe(ModToolMaterials.LOST_SOUL, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.UNCOMMON)), ConfigConstructor.disable_recipe_forlorn_scythe);
        LEVIATHAN_AXE = ItemRegistry.registerItem("leviathan_axe", () -> new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC))); //Handled in RecipeHandler
        SKOFNUNG = ItemRegistry.registerWeaponItem("skofnung", () -> new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_skofnung);
        MJOLNIR = ItemRegistry.registerWeaponItem("mjolnir", () -> new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_mjolnir);
        FREYR_SWORD = ItemRegistry.registerWeaponItem("freyr_sword", () -> new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_sword_of_freyr);
        STING = ItemRegistry.registerWeaponItem("sting", () -> new Sting(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_sting);
        FEATHERLIGHT = ItemRegistry.registerWeaponItem("featherlight", () -> new Featherlight(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_featherlight);
        CRUCIBLE_SWORD = ItemRegistry.registerWeaponItem("crucible_sword", () -> new CrucibleSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_crucible_sword);
        DARKIN_SCYTHE_PRE = ItemRegistry.registerWeaponItem("darkin_scythe_pre", () -> new DarkinScythePre(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_darkin_scythe);
        DARKIN_SCYTHE_PRIME = ItemRegistry.registerItem("darkin_scythe", () -> new DarkinScythePrime(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC))); // Gained by transforming DARKIN_SCYTHE_PRE
        SHADOW_ASSASSIN_SCYTHE = ItemRegistry.registerItem("shadow_assassin_scythe", () -> new ShadowAssassinScythe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC))); // Gained by transforming DARKIN_SCYTHE_PRE
        KIRKHAMMER = ItemRegistry.registerWeaponItem("kirkhammer", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, 0, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE), 1, 0, true, false), ConfigConstructor.disable_recipe_kirkhammer);
        SILVER_SWORD = ItemRegistry.registerItem("silver_sword", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, 1, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE), 0, 1, false, true)); // Switched to by other trick weapons
        HOLY_GREATSWORD = ItemRegistry.registerWeaponItem("holy_greatsword", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, 2, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE), 1, 2, false, true), ConfigConstructor.disable_recipe_ludwigs_holy_blade);
        DRAUPNIR_SPEAR = ItemRegistry.registerWeaponItem("draupnir_spear", () -> new DraupnirSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_draupnir_spear);
        HOLY_MOONLIGHT_GREATSWORD = ItemRegistry.registerItem("holy_moonlight_greatsword", () -> new HolyMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC), 4)); // Switched to by Holy Moonlight Sword
        HOLY_MOONLIGHT_SWORD = ItemRegistry.registerWeaponItem("holy_moonlight_sword", () -> new HolyMoonlightSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_holy_moonlight_sword);
        FROSTMOURNE = ItemRegistry.registerWeaponItem("frostmourne", () -> new Frostmourne(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_frostmourne);
        MASTER_SWORD = ItemRegistry.registerWeaponItem("master_sword", () -> new MasterSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_master_sword);
        NIGHTS_EDGE_ITEM = ItemRegistry.registerWeaponItem("nights_edge_item", () -> new NightsEdgeItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_nights_edge);
        EMPOWERED_DAWNBREAKER = ItemRegistry.registerWeaponItem("empowered_dawnbreaker", () -> new EmpoweredDawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_empowered_dawnbreaker);
        KRAKEN_SLAYER = ItemRegistry.registerWeaponItem("kraken_slayer", () -> new KrakenSlayer(new Item.Settings().group(MAIN_GROUP).maxDamage(1258).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_kraken_slayer_bow);
        KRAKEN_SLAYER_CROSSBOW = ItemRegistry.registerWeaponItem("kraken_slayer_crossbow", () -> new KrakenSlayerCrossbow(new Item.Settings().group(MAIN_GROUP).maxDamage(1258).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_kraken_slayer_crossbow);
        DARKMOON_LONGBOW = ItemRegistry.registerWeaponItem("darkmoon_longbow", () -> new DarkmoonLongbow(new Item.Settings().group(MAIN_GROUP).maxDamage(1400).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_darkmoon_longbow);

        //TEST_ITEM = ItemRegistry.registerItem("test_item", () -> new TestItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, 10, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)));
    }
}
