package net.soulsweaponry.registry;

import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.axe.LeviathanAxe;
import net.soulsweaponry.items.bow.DarkmoonLongbow;
import net.soulsweaponry.items.bow.Galeforce;
import net.soulsweaponry.items.bow.KrakenSlayer;
import net.soulsweaponry.items.bow.SimonsBowblade;
import net.soulsweaponry.items.crossbow.KrakenSlayerCrossbow;
import net.soulsweaponry.items.hammer.Mjolnir;
import net.soulsweaponry.items.hammer.Nightfall;
import net.soulsweaponry.items.material.ModToolMaterials;
import net.soulsweaponry.items.scythe.*;
import net.soulsweaponry.items.spear.CometSpear;
import net.soulsweaponry.items.spear.DragonslayerSwordspear;
import net.soulsweaponry.items.spear.DraupnirSpear;
import net.soulsweaponry.items.spear.GlaiveOfHodir;
import net.soulsweaponry.items.staff.ChungusStaff;
import net.soulsweaponry.items.staff.DragonStaff;
import net.soulsweaponry.items.staff.WitheredWabbajack;
import net.soulsweaponry.items.sword.*;

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
    public static RegistryObject<ToolItem> CHUNGUS_STAFF;
    public static RegistryObject<ToolItem> DARK_MOON_GREATSWORD;
    public static RegistryObject<ToolItem> GLAIVE_OF_HODIR;
    public static RegistryObject<ToolItem> EXCALIBUR;
    public static RegistryObject<ToolItem> MOONVEIL;
    public static RegistryObject<BowItem> SIMONS_BOWBLADE;
    public static RegistryObject<TrickWeapon> SIMONS_BLADE;

    //public static RegistryObject<Item> TEST_ITEM;

    public static void register() {
        BLUEMOON_SHORTSWORD = ItemRegistry.registerLegendaryWeapon("bluemoon_shortsword", () -> new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_bluemoon_shortsword);
        BLUEMOON_GREATSWORD = ItemRegistry.registerLegendaryWeapon("bluemoon_greatsword", () -> new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_bluemoon_greatsword);
        MOONLIGHT_SHORTSWORD = ItemRegistry.registerLegendaryWeapon("moonlight_shortsword", () -> new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_moonlight_shortsword);
        MOONLIGHT_GREATSWORD = ItemRegistry.registerLegendaryWeapon("moonlight_greatsword", () -> new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_moonlight_greatsword);
        PURE_MOONLIGHT_GREATSWORD = ItemRegistry.registerLegendaryWeapon("pure_moonlight_greatsword", () -> new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_pure_moonlight_greatsword);
        BLOODTHIRSTER = ItemRegistry.registerLegendaryWeapon("bloodthirster", () -> new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_bloodthirster);
        DARKIN_BLADE = ItemRegistry.registerLegendaryWeapon("darkin_blade", () -> new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_darkin_blade);
        DRAGON_STAFF = ItemRegistry.registerLegendaryWeapon("dragon_staff", () -> new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dragon_staff);
        WITHERED_WABBAJACK = ItemRegistry.registerLegendaryWeapon("withered_wabbajack", () -> new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_withered_wabbajack);
        WHIRLIGIG_SAWBLADE = ItemRegistry.registerLegendaryWeapon("whirligig_sawblade", () -> new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_whirligig_sawblade);
        DRAGONSLAYER_SWORDSPEAR = ItemRegistry.registerLegendaryWeapon("dragonslayer_swordspear", () -> new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dragonslayer_swordspear);
        GUINSOOS_RAGEBLADE = ItemRegistry.registerLegendaryWeapon("rageblade", () -> new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_rageblade);
        GUTS_SWORD = ItemRegistry.registerLegendaryWeapon("guts_sword", () -> new DragonslayerSwordBerserk(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_heap_of_raw_iron);
        NIGHTFALL = ItemRegistry.registerLegendaryWeapon("nightfall", () -> new Nightfall(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_nightfall);
        COMET_SPEAR = ItemRegistry.registerLegendaryWeapon("comet_spear", () -> new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_comet_spear);
        LICH_BANE = ItemRegistry.registerLegendaryWeapon("lich_bane", () -> new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_lich_bane);
        GALEFORCE = ItemRegistry.registerLegendaryWeapon("galeforce", () -> new Galeforce(new Item.Settings().maxDamage(1300).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_galeforce);
        TRANSLUCENT_SWORD = ItemRegistry.registerWeaponItem("translucent_sword", () -> new TranslucentWeapon(ModToolMaterials.LOST_SOUL, ConfigConstructor.translucent_sword_damage, ConfigConstructor.translucent_sword_attack_speed, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_translucent_sword);
        TRANSLUCENT_GLAIVE = ItemRegistry.registerWeaponItem("translucent_glaive", () -> new TranslucentWeapon(ModToolMaterials.LOST_SOUL, ConfigConstructor.translucent_glaive_damage, ConfigConstructor.translucent_glaive_attack_speed, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_translucent_glaive);
        TRANSLUCENT_DOUBLE_GREATSWORD = ItemRegistry.registerWeaponItem("translucent_double_greatsword", () -> new TranslucentWeapon(ModToolMaterials.LOST_SOUL, ConfigConstructor.translucent_double_edged_greatsword_damage, ConfigConstructor.translucent_double_edged_greatsword_attack_speed, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_translucent_double_edged_greatsword);
        DRAUGR = ItemRegistry.registerLegendaryWeapon("draugr", () -> new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_draugr);
        DAWNBREAKER = ItemRegistry.registerLegendaryWeapon("dawnbreaker", () -> new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dawnbreaker);
        SOUL_REAPER = ItemRegistry.registerLegendaryWeapon("soul_reaper", () -> new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_soul_reaper);
        FORLORN_SCYTHE = ItemRegistry.registerLegendaryWeapon("forlorn_scythe", () -> new ForlornScythe(ModToolMaterials.LOST_SOUL_DURABLE, new Item.Settings().rarity(Rarity.UNCOMMON)), ConfigConstructor.disable_recipe_forlorn_scythe);
        LEVIATHAN_AXE = ItemRegistry.registerLegendaryItem("leviathan_axe", () -> new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC))); //Handled in RecipeHandler
        SKOFNUNG = ItemRegistry.registerLegendaryWeapon("skofnung", () -> new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_skofnung);
        MJOLNIR = ItemRegistry.registerLegendaryWeapon("mjolnir", () -> new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_mjolnir);
        FREYR_SWORD = ItemRegistry.registerLegendaryWeapon("freyr_sword", () -> new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_sword_of_freyr);
        STING = ItemRegistry.registerLegendaryWeapon("sting", () -> new Sting(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_sting);
        FEATHERLIGHT = ItemRegistry.registerLegendaryWeapon("featherlight", () -> new Featherlight(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_featherlight);
        CRUCIBLE_SWORD = ItemRegistry.registerLegendaryWeapon("crucible_sword", () -> new CrucibleSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_crucible_sword);
        DARKIN_SCYTHE_PRE = ItemRegistry.registerLegendaryWeapon("darkin_scythe_pre", () -> new DarkinScythePre(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_darkin_scythe);
        DARKIN_SCYTHE_PRIME = ItemRegistry.registerLegendaryItem("darkin_scythe", () -> new DarkinScythePrime(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC))); // Gained by transforming DARKIN_SCYTHE_PRE
        SHADOW_ASSASSIN_SCYTHE = ItemRegistry.registerLegendaryItem("shadow_assassin_scythe", () -> new ShadowAssassinScythe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC))); // Gained by transforming DARKIN_SCYTHE_PRE
        KIRKHAMMER = ItemRegistry.registerWeaponItem("kirkhammer", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, ConfigConstructor.kirkhammer_damage, ConfigConstructor.kirkhammer_attack_speed, new Item.Settings().rarity(Rarity.RARE), true, 0f, ConfigConstructor.is_fireproof_kirkhammer, ConfigConstructor.disable_use_kirkhammer), ConfigConstructor.disable_recipe_kirkhammer);
        SILVER_SWORD = ItemRegistry.registerItem("silver_sword", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, ConfigConstructor.kirkhammer_silver_sword_damage, ConfigConstructor.kirkhammer_silver_sword_attack_speed, new Item.Settings().rarity(Rarity.RARE), false, ConfigConstructor.kirkhammer_silver_sword_righteous_undead_bonus_damage, ConfigConstructor.is_fireproof_silver_sword, ConfigConstructor.disable_use_silver_sword)); // Switched to by other trick weapons
        HOLY_GREATSWORD = ItemRegistry.registerWeaponItem("holy_greatsword", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, ConfigConstructor.ludwigs_holy_greatsword_damage,  ConfigConstructor.ludwigs_holy_greatsword_attack_speed, new Item.Settings().rarity(Rarity.RARE), false, ConfigConstructor.ludwigs_holy_greatsword_righteous_undead_bonus_damage, ConfigConstructor.is_fireproof_ludwigs_holy_blade, ConfigConstructor.disable_use_ludwigs_holy_greatsword), ConfigConstructor.disable_recipe_ludwigs_holy_blade);
        DRAUPNIR_SPEAR = ItemRegistry.registerLegendaryWeapon("draupnir_spear", () -> new DraupnirSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_draupnir_spear);
        HOLY_MOONLIGHT_GREATSWORD = ItemRegistry.registerLegendaryItem("holy_moonlight_greatsword", () -> new HolyMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC))); // Switched to by Holy Moonlight Sword
        HOLY_MOONLIGHT_SWORD = ItemRegistry.registerLegendaryWeapon("holy_moonlight_sword", () -> new HolyMoonlightSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_holy_moonlight_sword);
        FROSTMOURNE = ItemRegistry.registerLegendaryWeapon("frostmourne", () -> new Frostmourne(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_frostmourne);
        MASTER_SWORD = ItemRegistry.registerLegendaryWeapon("master_sword", () -> new MasterSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_master_sword);
        NIGHTS_EDGE_ITEM = ItemRegistry.registerLegendaryWeapon("nights_edge_item", () -> new NightsEdgeItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_nights_edge);
        EMPOWERED_DAWNBREAKER = ItemRegistry.registerLegendaryWeapon("empowered_dawnbreaker", () -> new EmpoweredDawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_empowered_dawnbreaker);
        KRAKEN_SLAYER = ItemRegistry.registerWeaponItem("kraken_slayer", () -> new KrakenSlayer(new Item.Settings().maxDamage(1258).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_kraken_slayer_bow);
        KRAKEN_SLAYER_CROSSBOW = ItemRegistry.registerLegendaryWeapon("kraken_slayer_crossbow", () -> new KrakenSlayerCrossbow(new Item.Settings().maxDamage(1258).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_kraken_slayer_crossbow);
        DARKMOON_LONGBOW = ItemRegistry.registerLegendaryWeapon("darkmoon_longbow", () -> new DarkmoonLongbow(new Item.Settings().maxDamage(1400).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_darkmoon_longbow);
        CHUNGUS_STAFF = ItemRegistry.registerLegendaryWeapon("chungus_staff", () -> new ChungusStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().maxDamage(1258).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_chungus_staff);
        DARK_MOON_GREATSWORD = ItemRegistry.registerLegendaryWeapon("dark_moon_greatsword", () -> new DarkMoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dark_moon_greatsword);
        GLAIVE_OF_HODIR = ItemRegistry.registerLegendaryWeapon("glaive_of_hodir", () -> new GlaiveOfHodir(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_glaive_of_hodir);
        EXCALIBUR = ItemRegistry.registerLegendaryWeapon("excalibur", () -> new Excalibur(ModToolMaterials.ECHO_SHARD, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_excalibur);
        MOONVEIL = ItemRegistry.registerLegendaryWeapon("moonveil", () -> new Moonveil(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_moonveil);
        SIMONS_BOWBLADE = ItemRegistry.registerLegendaryWeapon("simons_bowblade", () -> new SimonsBowblade(new Item.Settings().maxDamage(1354).rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_simons_bowblade);
        SIMONS_BLADE = ItemRegistry.registerLegendaryItem("simons_blade", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK,  ConfigConstructor.simons_blade_damage,  ConfigConstructor.simons_blade_attack_speed, new Item.Settings().rarity(Rarity.RARE), false, ConfigConstructor.simons_blade_righteous_undead_bonus_damage, ConfigConstructor.is_fireproof_simons_blade, ConfigConstructor.disable_use_simons_blade)); // Switched to by Simon's Bowblade

        //TEST_ITEM = ItemRegistry.registerItem("test_item", () -> new TestItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, 10, -2.4f, new Item.Settings().rarity(Rarity.EPIC)));
    }
}
