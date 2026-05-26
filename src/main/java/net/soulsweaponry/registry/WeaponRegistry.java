package net.soulsweaponry.registry;

import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.datagen.tags.ItemDatagenType;
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

    public static RegistryObject<ToolItem> BLUEMOON_SHORTSWORD;
    public static RegistryObject<ToolItem> BLUEMOON_GREATSWORD;
    public static RegistryObject<ToolItem> MOONLIGHT_SHORTSWORD;
    public static RegistryObject<ToolItem> MOONLIGHT_GREATSWORD;
    public static RegistryObject<ToolItem> PURE_MOONLIGHT_GREATSWORD;
    public static RegistryObject<ToolItem> BLOODTHIRSTER;
    public static RegistryObject<ToolItem> DARKIN_BLADE;
    public static RegistryObject<ToolItem> DRAGON_STAFF;
    public static RegistryObject<ToolItem> WITHERED_WABBAJACK;
    public static RegistryObject<ToolItem> WHIRLIGIG_SAWBLADE;
    public static RegistryObject<ToolItem> DRAGONSLAYER_SWORDSPEAR;
    public static RegistryObject<ToolItem> GUINSOOS_RAGEBLADE;
    public static RegistryObject<ToolItem> GUTS_SWORD;
    public static RegistryObject<ToolItem> NIGHTFALL;
    public static RegistryObject<ToolItem> COMET_SPEAR;
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
    public static RegistryObject<ToolItem> DRAGONBANE;
    public static RegistryObject<ToolItem> SUPERNOVA;
    public static RegistryObject<ToolItem> MEHRUNES_RAZOR;
    public static RegistryObject<ToolItem> TONITRUS;
    public static RegistryObject<ToolItem> BLOODLUST;
    public static RegistryObject<ToolItem> NIGHTLORDS_SWORD;

    //public static RegistryObject<Item> TEST_ITEM;

    public static void register() {
        BLUEMOON_SHORTSWORD = ItemRegistry.registerLegendarySword("bluemoon_shortsword", () -> new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_bluemoon_shortsword);
        BLUEMOON_GREATSWORD = ItemRegistry.registerLegendarySword("bluemoon_greatsword", () -> new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_bluemoon_greatsword);
        MOONLIGHT_SHORTSWORD = ItemRegistry.registerLegendarySword("moonlight_shortsword", () -> new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_moonlight_shortsword);
        MOONLIGHT_GREATSWORD = ItemRegistry.registerLegendarySword("moonlight_greatsword", () -> new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_moonlight_greatsword);
        PURE_MOONLIGHT_GREATSWORD = ItemRegistry.registerLegendarySword("pure_moonlight_greatsword", () -> new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_pure_moonlight_greatsword);
        BLOODTHIRSTER = ItemRegistry.registerLegendarySword("bloodthirster", () -> new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_bloodthirster);
        DARKIN_BLADE = ItemRegistry.registerLegendaryHeavySword("darkin_blade", () -> new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_darkin_blade);
        DRAGON_STAFF = ItemRegistry.registerLegendarySword("dragon_staff", () -> new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dragon_staff);
        WITHERED_WABBAJACK = ItemRegistry.registerLegendarySword("withered_wabbajack", () -> new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_withered_wabbajack);
        WHIRLIGIG_SAWBLADE = ItemRegistry.registerLegendarySword("whirligig_sawblade", () -> new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_whirligig_sawblade);
        DRAGONSLAYER_SWORDSPEAR = ItemRegistry.registerLegendarySword("dragonslayer_swordspear", () -> new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dragonslayer_swordspear);
        GUINSOOS_RAGEBLADE = ItemRegistry.registerLegendarySword("rageblade", () -> new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_rageblade);
        GUTS_SWORD = ItemRegistry.registerLegendaryHeavySword("guts_sword", () -> new HeapOfRawIron(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_heap_of_raw_iron);
        NIGHTFALL = ItemRegistry.registerLegendaryHeavySword("nightfall", () -> new Nightfall(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_nightfall);
        COMET_SPEAR = ItemRegistry.registerLegendarySword("comet_spear", () -> new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_comet_spear);
        LICH_BANE = ItemRegistry.registerLegendarySword("lich_bane", () -> new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_lich_bane);
        GALEFORCE = ItemRegistry.registerLegendaryBow("galeforce", () -> new Galeforce(new Item.Settings().maxDamage(1300).rarity(Rarity.EPIC), () -> Ingredient.ofItems(ItemRegistry.VERGLAS.get(), ItemRegistry.MOONSTONE.get())), ConfigConstructor.disable_recipe_galeforce);
        TRANSLUCENT_SWORD = ItemRegistry.registerWeaponItem("translucent_sword", () -> new TranslucentWeapon(ModToolMaterials.LOST_SOUL, (int) ConfigConstructor.translucent_sword_damage, ConfigConstructor.translucent_sword_attack_speed, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_translucent_sword, ItemDatagenType.SWORD);
        TRANSLUCENT_GLAIVE = ItemRegistry.registerWeaponItem("translucent_glaive", () -> new TranslucentWeapon(ModToolMaterials.LOST_SOUL, (int) ConfigConstructor.translucent_glaive_damage, ConfigConstructor.translucent_glaive_attack_speed, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_translucent_glaive, ItemDatagenType.SWORD);
        TRANSLUCENT_DOUBLE_GREATSWORD = ItemRegistry.registerWeaponItem("translucent_double_greatsword", () -> new TranslucentWeapon(ModToolMaterials.LOST_SOUL, (int) ConfigConstructor.translucent_double_edged_greatsword_damage, ConfigConstructor.translucent_double_edged_greatsword_attack_speed, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_translucent_double_edged_greatsword, ItemDatagenType.SWORD);
        DRAUGR = ItemRegistry.registerLegendarySword("draugr", () -> new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_draugr);
        DAWNBREAKER = ItemRegistry.registerLegendarySword("dawnbreaker", () -> new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dawnbreaker);
        SOUL_REAPER = ItemRegistry.registerLegendarySoulHarvestingSword("soul_reaper", () -> new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_soul_reaper);
        FORLORN_SCYTHE = ItemRegistry.registerLegendarySoulHarvestingSword("forlorn_scythe", () -> new ForlornScythe(ModToolMaterials.LOST_SOUL_DURABLE, new Item.Settings().rarity(Rarity.UNCOMMON)), ConfigConstructor.disable_recipe_forlorn_scythe);
        LEVIATHAN_AXE = ItemRegistry.registerLegendaryItem("leviathan_axe", () -> new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ItemDatagenType.AXE); //Handled in RecipeHandler
        SKOFNUNG = ItemRegistry.registerLegendarySword("skofnung", () -> new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_skofnung);
        MJOLNIR = ItemRegistry.registerLegendarySword("mjolnir", () -> new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_mjolnir);
        FREYR_SWORD = ItemRegistry.registerLegendarySword("freyr_sword", () -> new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_sword_of_freyr);
        STING = ItemRegistry.registerLegendarySword("sting", () -> new Sting(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_sting);
        FEATHERLIGHT = ItemRegistry.registerLegendaryHeavySword("featherlight", () -> new Featherlight(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_featherlight);
        CRUCIBLE_SWORD = ItemRegistry.registerLegendarySword("crucible_sword", () -> new CrucibleSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_crucible_sword);
        DARKIN_SCYTHE_PRE = ItemRegistry.registerLegendarySword("darkin_scythe_pre", () -> new DarkinScythePre(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_darkin_scythe);
        DARKIN_SCYTHE_PRIME = ItemRegistry.registerLegendaryItem("darkin_scythe", () -> new DarkinScythePrime(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ItemDatagenType.SWORD); // Gained by transforming DARKIN_SCYTHE_PRE
        SHADOW_ASSASSIN_SCYTHE = ItemRegistry.registerLegendaryItem("shadow_assassin_scythe", () -> new ShadowAssassinScythe(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ItemDatagenType.SWORD); // Gained by transforming DARKIN_SCYTHE_PRE
        KIRKHAMMER = ItemRegistry.registerWeaponItem("kirkhammer", () -> new Kirkhammmer(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_kirkhammer, ItemDatagenType.SWORD, ItemDatagenType.HEAVY_WEAPON);
        SILVER_SWORD = ItemRegistry.registerItem("silver_sword", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, (int) ConfigConstructor.kirkhammer_silver_sword_damage, ConfigConstructor.kirkhammer_silver_sword_attack_speed, new Item.Settings().rarity(Rarity.RARE), ConfigConstructor.is_fireproof_silver_sword, ConfigConstructor.disable_use_silver_sword, ConfigConstructor.kirkhammer_silver_sword_righteous_base_undead_bonus_damage, ConfigConstructor.kirkhammer_silver_sword_righteous_undead_bonus_damage_per_level), ItemDatagenType.SWORD); // Switched to by other trick weapons
        HOLY_GREATSWORD = ItemRegistry.registerWeaponItem("holy_greatsword", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, (int) ConfigConstructor.ludwigs_holy_greatsword_damage,  ConfigConstructor.ludwigs_holy_greatsword_attack_speed, new Item.Settings().rarity(Rarity.RARE), ConfigConstructor.is_fireproof_ludwigs_holy_blade, ConfigConstructor.disable_use_ludwigs_holy_greatsword, ConfigConstructor.ludwigs_holy_greatsword_righteous_base_undead_bonus_damage, ConfigConstructor.ludwigs_holy_greatsword_righteous_undead_bonus_damage_per_level), ConfigConstructor.disable_recipe_ludwigs_holy_blade, ItemDatagenType.SWORD);
        DRAUPNIR_SPEAR = ItemRegistry.registerLegendarySword("draupnir_spear", () -> new DraupnirSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_draupnir_spear);
        HOLY_MOONLIGHT_GREATSWORD = ItemRegistry.registerLegendaryItem("holy_moonlight_greatsword", () -> new HolyMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ItemDatagenType.SWORD); // Switched to by Holy Moonlight Sword
        HOLY_MOONLIGHT_SWORD = ItemRegistry.registerLegendarySword("holy_moonlight_sword", () -> new HolyMoonlightSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_holy_moonlight_sword);
        FROSTMOURNE = ItemRegistry.registerLegendarySoulHarvestingSword("frostmourne", () -> new Frostmourne(ModToolMaterials.MOONSTONE_OR_VERGLAS,  new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_frostmourne);
        MASTER_SWORD = ItemRegistry.registerLegendarySword("master_sword", () -> new MasterSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_master_sword);
        NIGHTS_EDGE_ITEM = ItemRegistry.registerLegendarySword("nights_edge_item", () -> new NightsEdgeItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_nights_edge);
        EMPOWERED_DAWNBREAKER = ItemRegistry.registerLegendarySword("empowered_dawnbreaker", () -> new EmpoweredDawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_empowered_dawnbreaker);
        KRAKEN_SLAYER = ItemRegistry.registerWeaponItem("kraken_slayer", () -> new KrakenSlayer(new Item.Settings().maxDamage(1258).rarity(Rarity.EPIC), () -> Ingredient.ofItems(Items.GOLD_INGOT)), ConfigConstructor.disable_recipe_kraken_slayer_bow, ItemDatagenType.BOW);
        KRAKEN_SLAYER_CROSSBOW = ItemRegistry.registerLegendaryCrossbow("kraken_slayer_crossbow", () -> new KrakenSlayerCrossbow(new Item.Settings().maxDamage(1258).rarity(Rarity.EPIC), () -> Ingredient.ofItems(Items.GOLD_INGOT)), ConfigConstructor.disable_recipe_kraken_slayer_crossbow);
        DARKMOON_LONGBOW = ItemRegistry.registerLegendaryBow("darkmoon_longbow", () -> new DarkmoonLongbow(new Item.Settings().maxDamage(1400).rarity(Rarity.EPIC), () -> Ingredient.ofItems(Items.GOLD_INGOT)), ConfigConstructor.disable_recipe_darkmoon_longbow);
        CHUNGUS_STAFF = ItemRegistry.registerLegendarySword("chungus_staff", () -> new ChungusStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().maxDamage(1258).rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_chungus_staff);
        DARK_MOON_GREATSWORD = ItemRegistry.registerLegendarySword("dark_moon_greatsword", () -> new DarkMoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dark_moon_greatsword);
        GLAIVE_OF_HODIR = ItemRegistry.registerLegendarySword("glaive_of_hodir", () -> new GlaiveOfHodir(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_glaive_of_hodir);
        EXCALIBUR = ItemRegistry.registerLegendarySword("excalibur", () -> new Excalibur(ModToolMaterials.ECHO_SHARD, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_excalibur);
        MOONVEIL = ItemRegistry.registerLegendarySword("moonveil", () -> new Moonveil(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_moonveil);
        SIMONS_BOWBLADE = ItemRegistry.registerLegendaryBow("simons_bowblade", () -> new SimonsBowblade(new Item.Settings().maxDamage(1354).rarity(Rarity.RARE), () -> Ingredient.ofItems(Items.IRON_BLOCK, ItemRegistry.SOUL_INGOT.get())), ConfigConstructor.disable_recipe_simons_bowblade);
        SIMONS_BLADE = ItemRegistry.registerLegendaryItem("simons_blade", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, (int) ConfigConstructor.simons_blade_damage,  ConfigConstructor.simons_blade_attack_speed, new Item.Settings().rarity(Rarity.RARE), ConfigConstructor.is_fireproof_simons_blade, ConfigConstructor.disable_use_simons_blade, ConfigConstructor.simons_blade_righteous_base_undead_bonus_damage, ConfigConstructor.simons_blade_righteous_undead_bonus_damage_per_level), ItemDatagenType.SWORD); // Switched to by Simon's Bowblade
        DRAGONBANE = ItemRegistry.registerLegendarySword("dragonbane", () -> new Dragonbane(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_dragonbane);
        SUPERNOVA = ItemRegistry.registerLegendaryHeavySword("supernova", () -> new Supernova(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_supernova);
        MEHRUNES_RAZOR = ItemRegistry.registerLegendarySword("mehrunes_razor", () -> new MehrunesRazor(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_mehrunes_razor);
        TONITRUS = ItemRegistry.registerLegendarySword("tonitrus", () -> new Tonitrus(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_tonitrus);
        BLOODLUST = ItemRegistry.registerLegendarySword("bloodlust", () -> new Bloodlust(ModToolMaterials.IRON_BLOCK, new Item.Settings().rarity(Rarity.RARE)), ConfigConstructor.disable_recipe_bloodlust);
        NIGHTLORDS_SWORD = ItemRegistry.registerLegendarySword("nightlords_sword", () -> new NightlordsSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_sword_of_the_nightlord);

        //TEST_ITEM = ItemRegistry.registerItem("test_item", () -> new TestItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, 10, -2.4f, new Item.Settings().rarity(Rarity.EPIC)));
    }
}
