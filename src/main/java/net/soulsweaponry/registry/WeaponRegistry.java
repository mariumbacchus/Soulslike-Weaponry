package net.soulsweaponry.registry;

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
import net.soulsweaponry.items.dagger.MehrunesRazor;
import net.soulsweaponry.items.hammer.*;
import net.soulsweaponry.items.katana.Bloodlust;
import net.soulsweaponry.items.katana.DragonHuntersGreatKatana;
import net.soulsweaponry.items.katana.Dragonbane;
import net.soulsweaponry.items.katana.Moonveil;
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

    public static ToolItem BLUEMOON_SHORTSWORD = new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE));
    public static ToolItem BLUEMOON_GREATSWORD = new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE));
    public static ToolItem MOONLIGHT_SHORTSWORD = new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem MOONLIGHT_GREATSWORD = new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem PURE_MOONLIGHT_GREATSWORD = new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem BLOODTHIRSTER = new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem DARKIN_BLADE = new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem DRAGON_STAFF = new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem WITHERED_WABBAJACK = new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem WHIRLIGIG_SAWBLADE = new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem DRAGONSLAYER_SWORDSPEAR = new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem GUINSOOS_RAGEBLADE = new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem GUTS_SWORD = new HeapOfRawIron(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem NIGHTFALL = new Nightfall(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem COMET_SPEAR = new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem LICH_BANE = new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static BowItem GALEFORCE = new Galeforce(new Item.Settings().maxDamage(1300).rarity(Rarity.EPIC), () -> Ingredient.ofItems(ItemRegistry.VERGLAS, ItemRegistry.MOONSTONE));
    public static ToolItem TRANSLUCENT_SWORD = new TranslucentWeapon(ModToolMaterials.LOST_SOUL, (int) ConfigConstructor.translucent_sword_damage, ConfigConstructor.translucent_sword_attack_speed, new Item.Settings().rarity(Rarity.RARE));
    public static ToolItem TRANSLUCENT_GLAIVE = new TranslucentWeapon(ModToolMaterials.LOST_SOUL, (int) ConfigConstructor.translucent_glaive_damage, ConfigConstructor.translucent_glaive_attack_speed, new Item.Settings().rarity(Rarity.RARE));
    public static ToolItem TRANSLUCENT_DOUBLE_GREATSWORD = new TranslucentWeapon(ModToolMaterials.LOST_SOUL, (int) ConfigConstructor.translucent_double_edged_greatsword_damage, ConfigConstructor.translucent_double_edged_greatsword_attack_speed, new Item.Settings().rarity(Rarity.RARE));
    public static ToolItem DRAUGR = new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem DAWNBREAKER = new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem SOUL_REAPER = new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem FORLORN_SCYTHE = new ForlornScythe(ModToolMaterials.LOST_SOUL_DURABLE, new Item.Settings().rarity(Rarity.UNCOMMON));
    public static ToolItem LEVIATHAN_AXE = new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem SKOFNUNG = new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem MJOLNIR = new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem FREYR_SWORD = new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem STING = new Sting(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE));
    public static ToolItem FEATHERLIGHT = new Featherlight(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE));
    public static ToolItem CRUCIBLE_SWORD = new CrucibleSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem DARKIN_SCYTHE_PRE = new DarkinScythePre(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem DARKIN_SCYTHE_PRIME = new DarkinScythePrime(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem SHADOW_ASSASSIN_SCYTHE = new ShadowAssassinScythe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static TrickWeapon KIRKHAMMER = new Kirkhammmer(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.RARE));
    public static TrickWeapon SILVER_SWORD = new TrickWeapon(ModToolMaterials.IRON_BLOCK, (int) ConfigConstructor.kirkhammer_silver_sword_damage, ConfigConstructor.kirkhammer_silver_sword_attack_speed, new Item.Settings().rarity(Rarity.RARE), ConfigConstructor.disable_use_silver_sword, ConfigConstructor.kirkhammer_silver_sword_righteous_base_undead_bonus_damage, ConfigConstructor.kirkhammer_silver_sword_righteous_undead_bonus_damage_per_level);
    public static TrickWeapon HOLY_GREATSWORD = new TrickWeapon(ModToolMaterials.IRON_BLOCK, (int) ConfigConstructor.ludwigs_holy_greatsword_damage,  ConfigConstructor.ludwigs_holy_greatsword_attack_speed, new Item.Settings().rarity(Rarity.RARE), ConfigConstructor.disable_use_ludwigs_holy_greatsword, ConfigConstructor.ludwigs_holy_greatsword_righteous_base_undead_bonus_damage, ConfigConstructor.ludwigs_holy_greatsword_righteous_undead_bonus_damage_per_level);
    public static ToolItem DRAUPNIR_SPEAR = new DraupnirSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static HolyMoonlightGreatsword HOLY_MOONLIGHT_GREATSWORD = new HolyMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new Item.Settings().rarity(Rarity.EPIC));
    public static TrickWeapon HOLY_MOONLIGHT_SWORD = new HolyMoonlightSword(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new Item.Settings().rarity(Rarity.EPIC));
    public static Frostmourne FROSTMOURNE = new Frostmourne(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem MASTER_SWORD = new MasterSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem NIGHTS_EDGE_ITEM = new NightsEdgeItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem EMPOWERED_DAWNBREAKER = new EmpoweredDawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static BowItem KRAKEN_SLAYER = new KrakenSlayer(new Item.Settings().maxDamage(1258).rarity(Rarity.EPIC), () -> Ingredient.ofItems(Items.GOLD_INGOT));
    public static CrossbowItem KRAKEN_SLAYER_CROSSBOW = new KrakenSlayerCrossbow(new Item.Settings().maxDamage(1258).rarity(Rarity.EPIC), () -> Ingredient.ofItems(Items.GOLD_INGOT));
    public static BowItem DARKMOON_LONGBOW = new DarkmoonLongbow(new Item.Settings().maxDamage(1400).rarity(Rarity.EPIC), () -> Ingredient.ofItems(Items.GOLD_INGOT));
    public static ToolItem CHUNGUS_STAFF = new ChungusStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().maxDamage(1258).rarity(Rarity.EPIC));
    public static ToolItem DARK_MOON_GREATSWORD = new DarkMoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem GLAIVE_OF_HODIR = new GlaiveOfHodir(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem EXCALIBUR = new Excalibur(ModToolMaterials.ECHO_SHARD, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem MOONVEIL = new Moonveil(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static BowItem SIMONS_BOWBLADE = new SimonsBowblade(new Item.Settings().maxDamage(1354).rarity(Rarity.RARE), () -> Ingredient.ofItems(Items.IRON_BLOCK, ItemRegistry.SOUL_INGOT));
    public static TrickWeapon SIMONS_BLADE = new TrickWeapon(ModToolMaterials.IRON_BLOCK, (int) ConfigConstructor.simons_blade_damage,  ConfigConstructor.simons_blade_attack_speed, new Item.Settings().rarity(Rarity.RARE), ConfigConstructor.disable_use_simons_blade, ConfigConstructor.simons_blade_righteous_base_undead_bonus_damage, ConfigConstructor.simons_blade_righteous_undead_bonus_damage_per_level);
    public static ToolItem DRAGONBANE = new Dragonbane(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem SUPERNOVA = new Supernova(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem MEHRUNES_RAZOR = new MehrunesRazor(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem TONITRUS = new Tonitrus(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.RARE));
    public static ToolItem BLOODLUST = new Bloodlust(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.RARE));//TODO make 2d texture
    public static ToolItem NIGHTLORDS_SWORD = new NightlordsSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));
    public static ToolItem LARGE_MOONLIGHT_SWORD = new LargeMoonlightSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC));//TODO make 2d texture
    public static ToolItem DRAGON_HUNTERS_GREAT_KATANA = new DragonHuntersGreatKatana(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().rarity(Rarity.EPIC));//TODO make 2d texture

    public static void init() {
        ItemRegistry.registerLegendaryWeapon(BLUEMOON_SHORTSWORD, "bluemoon_shortsword", ConfigConstructor.disable_recipe_bluemoon_shortsword, ConfigConstructor.is_fireproof_bluemoon_shortsword);
        ItemRegistry.registerLegendaryWeapon(BLUEMOON_GREATSWORD, "bluemoon_greatsword", ConfigConstructor.disable_recipe_bluemoon_greatsword, ConfigConstructor.is_fireproof_bluemoon_greatsword);
        ItemRegistry.registerLegendaryWeapon(MOONLIGHT_SHORTSWORD, "moonlight_shortsword", ConfigConstructor.disable_recipe_moonlight_shortsword, ConfigConstructor.is_fireproof_moonlight_shortsword);
        ItemRegistry.registerLegendaryWeapon(MOONLIGHT_GREATSWORD, "moonlight_greatsword", ConfigConstructor.disable_recipe_moonlight_greatsword, ConfigConstructor.is_fireproof_moonlight_greatsword);
        ItemRegistry.registerLegendaryWeapon(PURE_MOONLIGHT_GREATSWORD, "pure_moonlight_greatsword", ConfigConstructor.disable_recipe_pure_moonlight_greatsword, ConfigConstructor.is_fireproof_pure_moonlight_greatsword);
        ItemRegistry.registerLegendaryWeapon(BLOODTHIRSTER, "bloodthirster", ConfigConstructor.disable_recipe_bloodthirster, ConfigConstructor.is_fireproof_bloodthirster);
        ItemRegistry.registerLegendaryWeapon(DARKIN_BLADE, "darkin_blade", ConfigConstructor.disable_recipe_darkin_blade, ConfigConstructor.is_fireproof_darkin_blade);
        ItemRegistry.registerLegendaryWeapon(DRAGON_STAFF, "dragon_staff", ConfigConstructor.disable_recipe_dragon_staff, ConfigConstructor.is_fireproof_dragon_staff);
        ItemRegistry.registerLegendaryWeapon(WITHERED_WABBAJACK, "withered_wabbajack", ConfigConstructor.disable_recipe_withered_wabbajack, ConfigConstructor.is_fireproof_withered_wabbajack);
        ItemRegistry.registerLegendaryWeapon(WHIRLIGIG_SAWBLADE, "whirligig_sawblade", ConfigConstructor.disable_recipe_whirligig_sawblade, ConfigConstructor.is_fireproof_whirligig_sawblade);
        ItemRegistry.registerLegendaryWeapon(DRAGONSLAYER_SWORDSPEAR, "dragonslayer_swordspear", ConfigConstructor.disable_recipe_dragonslayer_swordspear, ConfigConstructor.is_fireproof_dragonslayer_swordspear);
        ItemRegistry.registerLegendaryWeapon(GUINSOOS_RAGEBLADE, "rageblade", ConfigConstructor.disable_recipe_rageblade, ConfigConstructor.is_fireproof_rageblade);
        ItemRegistry.registerLegendaryWeapon(GUTS_SWORD, "guts_sword", ConfigConstructor.disable_recipe_heap_of_raw_iron, ConfigConstructor.is_fireproof_heap_of_raw_iron);
        ItemRegistry.registerLegendaryWeapon(NIGHTFALL, "nightfall", ConfigConstructor.disable_recipe_nightfall, ConfigConstructor.is_fireproof_nightfall);
        ItemRegistry.registerLegendaryWeapon(COMET_SPEAR, "comet_spear", ConfigConstructor.disable_recipe_comet_spear, ConfigConstructor.is_fireproof_comet_spear);
        ItemRegistry.registerLegendaryWeapon(LICH_BANE, "lich_bane", ConfigConstructor.disable_recipe_lich_bane, ConfigConstructor.is_fireproof_lich_bane);
        ItemRegistry.registerLegendaryWeapon(GALEFORCE, "galeforce", ConfigConstructor.disable_recipe_galeforce, ConfigConstructor.is_fireproof_galeforce);
        ItemRegistry.registerWeaponItem(TRANSLUCENT_SWORD, "translucent_sword", ConfigConstructor.disable_recipe_translucent_sword, ConfigConstructor.is_fireproof_translucent_weapons);
        ItemRegistry.registerWeaponItem(TRANSLUCENT_GLAIVE, "translucent_glaive", ConfigConstructor.disable_recipe_translucent_glaive, ConfigConstructor.is_fireproof_translucent_weapons);
        ItemRegistry.registerWeaponItem(TRANSLUCENT_DOUBLE_GREATSWORD, "translucent_double_greatsword", ConfigConstructor.disable_recipe_translucent_double_edged_greatsword, ConfigConstructor.is_fireproof_translucent_weapons);
        ItemRegistry.registerLegendaryWeapon(DRAUGR, "draugr", ConfigConstructor.disable_recipe_draugr, ConfigConstructor.is_fireproof_draugr);
        ItemRegistry.registerLegendaryWeapon(DAWNBREAKER, "dawnbreaker", ConfigConstructor.disable_recipe_dawnbreaker, ConfigConstructor.is_fireproof_dawnbreaker);
        ItemRegistry.registerLegendaryWeapon(SOUL_REAPER, "soul_reaper", ConfigConstructor.disable_recipe_soul_reaper, ConfigConstructor.is_fireproof_soul_reaper);
        ItemRegistry.registerLegendaryWeapon(FORLORN_SCYTHE, "forlorn_scythe", ConfigConstructor.disable_recipe_forlorn_scythe, ConfigConstructor.is_fireproof_forlorn_scythe);
        ItemRegistry.registerLegendaryItem(LEVIATHAN_AXE, "leviathan_axe", ConfigConstructor.is_fireproof_leviathan_axe); //Handled in RecipeHandler
        ItemRegistry.registerLegendaryWeapon(SKOFNUNG, "skofnung", ConfigConstructor.disable_recipe_skofnung, ConfigConstructor.is_fireproof_skofnung);
        ItemRegistry.registerLegendaryWeapon(MJOLNIR, "mjolnir", ConfigConstructor.disable_recipe_mjolnir, ConfigConstructor.is_fireproof_mjolnir);
        ItemRegistry.registerLegendaryWeapon(FREYR_SWORD, "freyr_sword", ConfigConstructor.disable_recipe_sword_of_freyr, ConfigConstructor.is_fireproof_sword_of_freyr);
        ItemRegistry.registerLegendaryWeapon(STING, "sting", ConfigConstructor.disable_recipe_sting, ConfigConstructor.is_fireproof_sting);
        ItemRegistry.registerLegendaryWeapon(FEATHERLIGHT, "featherlight", ConfigConstructor.disable_recipe_featherlight, ConfigConstructor.is_fireproof_featherlight);
        ItemRegistry.registerLegendaryWeapon(CRUCIBLE_SWORD, "crucible_sword", ConfigConstructor.disable_recipe_crucible_sword, ConfigConstructor.is_fireproof_crucible_sword);
        ItemRegistry.registerLegendaryWeapon(DARKIN_SCYTHE_PRE, "darkin_scythe_pre", ConfigConstructor.disable_recipe_darkin_scythe, ConfigConstructor.is_fireproof_darkin_scythe);
        ItemRegistry.registerLegendaryItem(DARKIN_SCYTHE_PRIME, "darkin_scythe", ConfigConstructor.is_fireproof_darkin_scythe_prime); // Gained by transforming DARKIN_SCYTHE_PRE
        ItemRegistry.registerLegendaryItem(SHADOW_ASSASSIN_SCYTHE, "shadow_assassin_scythe", ConfigConstructor.is_fireproof_shadow_assassin_scythe); // Gained by transforming DARKIN_SCYTHE_PRE
        ItemRegistry.registerWeaponItem(KIRKHAMMER, "kirkhammer", ConfigConstructor.disable_recipe_kirkhammer, ConfigConstructor.is_fireproof_kirkhammer);
        ItemRegistry.registerItem(SILVER_SWORD, "silver_sword", ConfigConstructor.is_fireproof_silver_sword); // Switched to by other trick weapons
        ItemRegistry.registerWeaponItem(HOLY_GREATSWORD, "holy_greatsword", ConfigConstructor.disable_recipe_ludwigs_holy_blade, ConfigConstructor.is_fireproof_ludwigs_holy_blade);
        ItemRegistry.registerLegendaryWeapon(DRAUPNIR_SPEAR, "draupnir_spear", ConfigConstructor.disable_recipe_draupnir_spear, ConfigConstructor.is_fireproof_draupnir_spear);
        ItemRegistry.registerLegendaryItem(HOLY_MOONLIGHT_GREATSWORD, "holy_moonlight_greatsword", ConfigConstructor.is_fireproof_holy_moonlight_greatsword); // Switched to by Holy Moonlight Sword
        ItemRegistry.registerLegendaryWeapon(HOLY_MOONLIGHT_SWORD, "holy_moonlight_sword", ConfigConstructor.disable_recipe_holy_moonlight_sword, ConfigConstructor.is_fireproof_holy_moonlight_sword);
        ItemRegistry.registerLegendaryWeapon(FROSTMOURNE, "frostmourne", ConfigConstructor.disable_recipe_frostmourne, ConfigConstructor.is_fireproof_frostmourne);
        ItemRegistry.registerLegendaryWeapon(MASTER_SWORD, "master_sword", ConfigConstructor.disable_recipe_master_sword, ConfigConstructor.is_fireproof_master_sword);
        ItemRegistry.registerLegendaryWeapon(NIGHTS_EDGE_ITEM, "nights_edge_item", ConfigConstructor.disable_recipe_nights_edge, ConfigConstructor.is_fireproof_nights_edge);
        ItemRegistry.registerLegendaryWeapon(EMPOWERED_DAWNBREAKER, "empowered_dawnbreaker", ConfigConstructor.disable_recipe_empowered_dawnbreaker, ConfigConstructor.is_fireproof_empowered_dawnbreaker);
        ItemRegistry.registerWeaponItem(KRAKEN_SLAYER, "kraken_slayer", ConfigConstructor.disable_recipe_kraken_slayer_bow, ConfigConstructor.is_fireproof_kraken_slayer_bow);
        ItemRegistry.registerLegendaryWeapon(KRAKEN_SLAYER_CROSSBOW, "kraken_slayer_crossbow", ConfigConstructor.disable_recipe_kraken_slayer_crossbow, ConfigConstructor.is_fireproof_kraken_slayer_crossbow);
        ItemRegistry.registerLegendaryWeapon(DARKMOON_LONGBOW, "darkmoon_longbow", ConfigConstructor.disable_recipe_darkmoon_longbow, ConfigConstructor.is_fireproof_darkmoon_longbow);
        ItemRegistry.registerLegendaryWeapon(CHUNGUS_STAFF, "chungus_staff", ConfigConstructor.disable_recipe_chungus_staff, ConfigConstructor.is_fireproof_chungus_staff);
        ItemRegistry.registerLegendaryWeapon(DARK_MOON_GREATSWORD, "dark_moon_greatsword", ConfigConstructor.disable_recipe_dark_moon_greatsword, ConfigConstructor.is_fireproof_dark_moon_greatsword);
        ItemRegistry.registerLegendaryWeapon(GLAIVE_OF_HODIR, "glaive_of_hodir", ConfigConstructor.disable_recipe_glaive_of_hodir, ConfigConstructor.is_fireproof_glaive_of_hodir);
        ItemRegistry.registerLegendaryWeapon(EXCALIBUR, "excalibur", ConfigConstructor.disable_recipe_excalibur, ConfigConstructor.is_fireproof_excalibur);
        ItemRegistry.registerLegendaryWeapon(MOONVEIL, "moonveil", ConfigConstructor.disable_recipe_moonveil, ConfigConstructor.is_fireproof_moonveil);
        ItemRegistry.registerLegendaryWeapon(SIMONS_BOWBLADE, "simons_bowblade", ConfigConstructor.disable_recipe_simons_bowblade, ConfigConstructor.is_fireproof_simons_bowblade);
        ItemRegistry.registerLegendaryItem(SIMONS_BLADE, "simons_blade", ConfigConstructor.is_fireproof_simons_blade); // Switched to by Simon's Bowblade
        ItemRegistry.registerLegendaryWeapon(DRAGONBANE, "dragonbane", ConfigConstructor.disable_recipe_dragonbane, ConfigConstructor.is_fireproof_dragonbane);
        ItemRegistry.registerLegendaryWeapon(SUPERNOVA, "supernova", ConfigConstructor.disable_recipe_supernova, ConfigConstructor.is_fireproof_supernova);
        ItemRegistry.registerLegendaryWeapon(MEHRUNES_RAZOR, "mehrunes_razor", ConfigConstructor.disable_recipe_mehrunes_razor, ConfigConstructor.is_fireproof_mehrunes_razor);
        ItemRegistry.registerLegendaryWeapon(TONITRUS, "tonitrus", ConfigConstructor.disable_recipe_tonitrus, ConfigConstructor.is_fireproof_tonitrus);
        ItemRegistry.registerLegendaryWeapon(BLOODLUST, "bloodlust", ConfigConstructor.disable_recipe_bloodlust, ConfigConstructor.is_fireproof_bloodlust);
        ItemRegistry.registerLegendaryWeapon(NIGHTLORDS_SWORD, "nightlords_sword", ConfigConstructor.disable_recipe_sword_of_the_nightlord, ConfigConstructor.is_fireproof_sword_of_the_nightlord);
        ItemRegistry.registerLegendaryWeapon(LARGE_MOONLIGHT_SWORD, "large_sword_of_moonlight", ConfigConstructor.disable_recipe_large_sword_of_moonlight, ConfigConstructor.is_fireproof_large_sword_of_moonlight);
        ItemRegistry.registerLegendaryWeapon(DRAGON_HUNTERS_GREAT_KATANA, "dragon_hunters_great_katana", ConfigConstructor.disable_recipe_dragon_hunters_great_katana, ConfigConstructor.is_fireproof_dragon_hunters_great_katana);
    }
}
