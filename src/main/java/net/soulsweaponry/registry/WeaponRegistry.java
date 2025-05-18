package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Rarity;
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

    public static ToolItem BLUEMOON_SHORTSWORD = new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem BLUEMOON_GREATSWORD = new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem MOONLIGHT_SHORTSWORD = new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem MOONLIGHT_GREATSWORD = new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem PURE_MOONLIGHT_GREATSWORD = new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem BLOODTHIRSTER = new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, new FabricItemSettings().rarity(Rarity.EPIC));
    public static DetonateGroundItem DARKIN_BLADE = new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem DRAGON_STAFF = new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem WITHERED_WABBAJACK = new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem WHIRLIGIG_SAWBLADE = new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem DRAGONSLAYER_SWORDSPEAR = new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem GUINSOOS_RAGEBLADE = new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem GUTS_SWORD = new DragonslayerSwordBerserk(ModToolMaterials.IRON_BLOCK, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem NIGHTFALL = new Nightfall(ModToolMaterials.IRON_BLOCK, new FabricItemSettings().rarity(Rarity.EPIC));
    public static DetonateGroundItem COMET_SPEAR = new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem LICH_BANE = new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static BowItem GALEFORCE = new Galeforce(new FabricItemSettings().maxDamage(1300).rarity(Rarity.EPIC), () -> Ingredient.ofItems(ItemRegistry.VERGLAS, ItemRegistry.MOONSTONE));
    public static ToolItem TRANSLUCENT_SWORD = new TranslucentWeapon(ModToolMaterials.LOST_SOUL, (int) ConfigConstructor.translucent_sword_damage, ConfigConstructor.translucent_sword_attack_speed, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem TRANSLUCENT_GLAIVE = new TranslucentWeapon(ModToolMaterials.LOST_SOUL, (int) ConfigConstructor.translucent_glaive_damage, ConfigConstructor.translucent_glaive_attack_speed, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem TRANSLUCENT_DOUBLE_GREATSWORD = new TranslucentWeapon(ModToolMaterials.LOST_SOUL, (int) ConfigConstructor.translucent_double_edged_greatsword_damage, ConfigConstructor.translucent_double_edged_greatsword_attack_speed, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem DRAUGR = new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem DAWNBREAKER = new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem SOUL_REAPER = new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem FORLORN_SCYTHE = new ForlornScythe(ModToolMaterials.LOST_SOUL_DURABLE, new FabricItemSettings().rarity(Rarity.UNCOMMON));
    public static ToolItem LEVIATHAN_AXE = new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem SKOFNUNG = new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem MJOLNIR = new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem FREYR_SWORD = new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem STING = new Sting(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem FEATHERLIGHT = new Featherlight(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem CRUCIBLE_SWORD = new CrucibleSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem DARKIN_SCYTHE_PRE = new DarkinScythePre(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem DARKIN_SCYTHE_PRIME = new DarkinScythePrime(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem SHADOW_ASSASSIN_SCYTHE = new ShadowAssassinScythe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static TrickWeapon KIRKHAMMER = new TrickWeapon(ModToolMaterials.IRON_BLOCK, (int) ConfigConstructor.kirkhammer_damage, ConfigConstructor.kirkhammer_attack_speed, new FabricItemSettings().rarity(Rarity.RARE), true, 0f, ConfigConstructor.is_fireproof_kirkhammer, ConfigConstructor.disable_use_kirkhammer);
    public static TrickWeapon SILVER_SWORD = new TrickWeapon(ModToolMaterials.IRON_BLOCK, (int) ConfigConstructor.kirkhammer_silver_sword_damage, ConfigConstructor.kirkhammer_silver_sword_attack_speed, new FabricItemSettings().rarity(Rarity.RARE), false, ConfigConstructor.kirkhammer_silver_sword_righteous_undead_bonus_damage, ConfigConstructor.is_fireproof_silver_sword, ConfigConstructor.disable_use_silver_sword);
    public static TrickWeapon HOLY_GREATSWORD = new TrickWeapon(ModToolMaterials.IRON_BLOCK, (int) ConfigConstructor.ludwigs_holy_greatsword_damage,  ConfigConstructor.ludwigs_holy_greatsword_attack_speed, new FabricItemSettings().rarity(Rarity.RARE), false, ConfigConstructor.ludwigs_holy_greatsword_righteous_undead_bonus_damage, ConfigConstructor.is_fireproof_ludwigs_holy_blade, ConfigConstructor.disable_use_ludwigs_holy_greatsword);
    public static ToolItem DRAUPNIR_SPEAR = new DraupnirSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static HolyMoonlightGreatsword HOLY_MOONLIGHT_GREATSWORD = new HolyMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new FabricItemSettings().rarity(Rarity.EPIC));
    public static TrickWeapon HOLY_MOONLIGHT_SWORD = new HolyMoonlightSword(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new FabricItemSettings().rarity(Rarity.EPIC));
    public static Frostmourne FROSTMOURNE = new Frostmourne(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem MASTER_SWORD = new MasterSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem NIGHTS_EDGE_ITEM = new NightsEdgeItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem EMPOWERED_DAWNBREAKER = new EmpoweredDawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static BowItem KRAKEN_SLAYER = new KrakenSlayer(new FabricItemSettings().maxDamage(1258).rarity(Rarity.EPIC), () -> Ingredient.ofItems(Items.GOLD_INGOT));
    public static CrossbowItem KRAKEN_SLAYER_CROSSBOW = new KrakenSlayerCrossbow(new FabricItemSettings().maxDamage(1258).rarity(Rarity.EPIC), () -> Ingredient.ofItems(Items.GOLD_INGOT));
    public static BowItem DARKMOON_LONGBOW = new DarkmoonLongbow(new FabricItemSettings().maxDamage(1400).rarity(Rarity.EPIC), () -> Ingredient.ofItems(Items.GOLD_INGOT));
    public static ToolItem CHUNGUS_STAFF = new ChungusStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().maxDamage(1258).rarity(Rarity.EPIC));
    public static ToolItem DARK_MOON_GREATSWORD = new DarkMoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem GLAIVE_OF_HODIR = new GlaiveOfHodir(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem EXCALIBUR = new Excalibur(ModToolMaterials.ECHO_SHARD, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem MOONVEIL = new Moonveil(ModToolMaterials.MOONSTONE_OR_VERGLAS, new FabricItemSettings().rarity(Rarity.EPIC));
    public static BowItem SIMONS_BOWBLADE = new SimonsBowblade(new FabricItemSettings().maxDamage(1354).rarity(Rarity.RARE), () -> Ingredient.ofItems(Items.IRON_BLOCK, ItemRegistry.SOUL_INGOT));
    public static TrickWeapon SIMONS_BLADE = new TrickWeapon(ModToolMaterials.IRON_BLOCK, (int) ConfigConstructor.simons_blade_damage,  ConfigConstructor.simons_blade_attack_speed, new FabricItemSettings().rarity(Rarity.RARE), false, ConfigConstructor.simons_blade_righteous_undead_bonus_damage, ConfigConstructor.is_fireproof_simons_blade, ConfigConstructor.disable_use_simons_blade);

    public static void init() {
        ItemRegistry.registerLegendaryWeapon(BLUEMOON_SHORTSWORD, "bluemoon_shortsword", ConfigConstructor.disable_recipe_bluemoon_shortsword);
        ItemRegistry.registerLegendaryWeapon(BLUEMOON_GREATSWORD, "bluemoon_greatsword", ConfigConstructor.disable_recipe_bluemoon_greatsword);
        ItemRegistry.registerLegendaryWeapon(MOONLIGHT_SHORTSWORD, "moonlight_shortsword", ConfigConstructor.disable_recipe_moonlight_shortsword);
        ItemRegistry.registerLegendaryWeapon(MOONLIGHT_GREATSWORD, "moonlight_greatsword", ConfigConstructor.disable_recipe_moonlight_greatsword);
        ItemRegistry.registerLegendaryWeapon(PURE_MOONLIGHT_GREATSWORD, "pure_moonlight_greatsword", ConfigConstructor.disable_recipe_pure_moonlight_greatsword);
        ItemRegistry.registerLegendaryWeapon(BLOODTHIRSTER, "bloodthirster", ConfigConstructor.disable_recipe_bloodthirster);
        ItemRegistry.registerLegendaryWeapon(DARKIN_BLADE, "darkin_blade", ConfigConstructor.disable_recipe_darkin_blade);
        ItemRegistry.registerLegendaryWeapon(DRAGON_STAFF, "dragon_staff", ConfigConstructor.disable_recipe_dragon_staff);
        ItemRegistry.registerLegendaryWeapon(WITHERED_WABBAJACK, "withered_wabbajack", ConfigConstructor.disable_recipe_withered_wabbajack);
        ItemRegistry.registerLegendaryWeapon(WHIRLIGIG_SAWBLADE, "whirligig_sawblade", ConfigConstructor.disable_recipe_whirligig_sawblade);
        ItemRegistry.registerLegendaryWeapon(DRAGONSLAYER_SWORDSPEAR, "dragonslayer_swordspear", ConfigConstructor.disable_recipe_dragonslayer_swordspear);
        ItemRegistry.registerLegendaryWeapon(GUINSOOS_RAGEBLADE, "rageblade", ConfigConstructor.disable_recipe_rageblade);
        ItemRegistry.registerLegendaryWeapon(GUTS_SWORD, "guts_sword", ConfigConstructor.disable_recipe_heap_of_raw_iron);
        ItemRegistry.registerLegendaryWeapon(NIGHTFALL, "nightfall", ConfigConstructor.disable_recipe_nightfall);
        ItemRegistry.registerLegendaryWeapon(COMET_SPEAR, "comet_spear", ConfigConstructor.disable_recipe_comet_spear);
        ItemRegistry.registerLegendaryWeapon(LICH_BANE, "lich_bane", ConfigConstructor.disable_recipe_lich_bane);
        ItemRegistry.registerLegendaryWeapon(GALEFORCE, "galeforce", ConfigConstructor.disable_recipe_galeforce);
        ItemRegistry.registerWeaponItem(TRANSLUCENT_SWORD, "translucent_sword", ConfigConstructor.disable_recipe_translucent_sword);
        ItemRegistry.registerWeaponItem(TRANSLUCENT_GLAIVE, "translucent_glaive", ConfigConstructor.disable_recipe_translucent_glaive);
        ItemRegistry.registerWeaponItem(TRANSLUCENT_DOUBLE_GREATSWORD, "translucent_double_greatsword", ConfigConstructor.disable_recipe_translucent_double_edged_greatsword);
        ItemRegistry.registerLegendaryWeapon(DRAUGR, "draugr", ConfigConstructor.disable_recipe_draugr);
        ItemRegistry.registerLegendaryWeapon(DAWNBREAKER, "dawnbreaker", ConfigConstructor.disable_recipe_dawnbreaker);
        ItemRegistry.registerLegendaryWeapon(SOUL_REAPER, "soul_reaper", ConfigConstructor.disable_recipe_soul_reaper);
        ItemRegistry.registerLegendaryWeapon(FORLORN_SCYTHE, "forlorn_scythe", ConfigConstructor.disable_recipe_forlorn_scythe);
        ItemRegistry.registerLegendaryItem(LEVIATHAN_AXE, "leviathan_axe"); //Handled in RecipeHandler
        ItemRegistry.registerLegendaryWeapon(SKOFNUNG, "skofnung", ConfigConstructor.disable_recipe_skofnung);
        ItemRegistry.registerLegendaryWeapon(MJOLNIR, "mjolnir", ConfigConstructor.disable_recipe_mjolnir);
        ItemRegistry.registerLegendaryWeapon(FREYR_SWORD, "freyr_sword", ConfigConstructor.disable_recipe_sword_of_freyr);
        ItemRegistry.registerLegendaryWeapon(STING, "sting", ConfigConstructor.disable_recipe_sting);
        ItemRegistry.registerLegendaryWeapon(FEATHERLIGHT, "featherlight", ConfigConstructor.disable_recipe_featherlight);
        ItemRegistry.registerLegendaryWeapon(CRUCIBLE_SWORD, "crucible_sword", ConfigConstructor.disable_recipe_crucible_sword);
        ItemRegistry.registerLegendaryWeapon(DARKIN_SCYTHE_PRE, "darkin_scythe_pre", ConfigConstructor.disable_recipe_darkin_scythe);
        ItemRegistry.registerLegendaryItem(DARKIN_SCYTHE_PRIME, "darkin_scythe"); // Gained by transforming DARKIN_SCYTHE_PRE
        ItemRegistry.registerLegendaryItem(SHADOW_ASSASSIN_SCYTHE, "shadow_assassin_scythe"); // Gained by transforming DARKIN_SCYTHE_PRE
        ItemRegistry.registerWeaponItem(KIRKHAMMER, "kirkhammer", ConfigConstructor.disable_recipe_kirkhammer);
        ItemRegistry.registerItem(SILVER_SWORD, "silver_sword"); // Switched to by other trick weapons
        ItemRegistry.registerWeaponItem(HOLY_GREATSWORD, "holy_greatsword", ConfigConstructor.disable_recipe_ludwigs_holy_blade);
        ItemRegistry.registerLegendaryWeapon(DRAUPNIR_SPEAR, "draupnir_spear", ConfigConstructor.disable_recipe_draupnir_spear);
        ItemRegistry.registerLegendaryItem(HOLY_MOONLIGHT_GREATSWORD, "holy_moonlight_greatsword"); // Switched to by Holy Moonlight Sword
        ItemRegistry.registerLegendaryWeapon(HOLY_MOONLIGHT_SWORD, "holy_moonlight_sword", ConfigConstructor.disable_recipe_holy_moonlight_sword);
        ItemRegistry.registerLegendaryWeapon(FROSTMOURNE, "frostmourne", ConfigConstructor.disable_recipe_frostmourne);
        ItemRegistry.registerLegendaryWeapon(MASTER_SWORD, "master_sword", ConfigConstructor.disable_recipe_master_sword);
        ItemRegistry.registerLegendaryWeapon(NIGHTS_EDGE_ITEM, "nights_edge_item", ConfigConstructor.disable_recipe_nights_edge);
        ItemRegistry.registerLegendaryWeapon(EMPOWERED_DAWNBREAKER, "empowered_dawnbreaker", ConfigConstructor.disable_recipe_empowered_dawnbreaker);
        ItemRegistry.registerWeaponItem(KRAKEN_SLAYER, "kraken_slayer", ConfigConstructor.disable_recipe_kraken_slayer_bow);
        ItemRegistry.registerLegendaryWeapon(KRAKEN_SLAYER_CROSSBOW, "kraken_slayer_crossbow", ConfigConstructor.disable_recipe_kraken_slayer_crossbow);
        ItemRegistry.registerLegendaryWeapon(DARKMOON_LONGBOW, "darkmoon_longbow", ConfigConstructor.disable_recipe_darkmoon_longbow);
        ItemRegistry.registerLegendaryWeapon(CHUNGUS_STAFF, "chungus_staff", ConfigConstructor.disable_recipe_chungus_staff);
        ItemRegistry.registerLegendaryWeapon(DARK_MOON_GREATSWORD, "dark_moon_greatsword", ConfigConstructor.disable_recipe_dark_moon_greatsword);
        ItemRegistry.registerLegendaryWeapon(GLAIVE_OF_HODIR, "glaive_of_hodir", ConfigConstructor.disable_recipe_glaive_of_hodir);
        ItemRegistry.registerLegendaryWeapon(EXCALIBUR, "excalibur", ConfigConstructor.disable_recipe_excalibur);
        ItemRegistry.registerLegendaryWeapon(MOONVEIL, "moonveil", ConfigConstructor.disable_recipe_moonveil);
        ItemRegistry.registerLegendaryWeapon(SIMONS_BOWBLADE, "simons_bowblade", ConfigConstructor.disable_recipe_simons_bowblade);
        ItemRegistry.registerLegendaryItem(SIMONS_BLADE, "simons_blade"); // Switched to by Simon's Bowblade
    }
}
