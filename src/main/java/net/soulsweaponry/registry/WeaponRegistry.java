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

import java.util.function.UnaryOperator;

public class WeaponRegistry {

    public static Item BLUEMOON_SHORTSWORD;
    public static Item BLUEMOON_GREATSWORD;
    public static Item MOONLIGHT_SHORTSWORD;
    public static Item MOONLIGHT_GREATSWORD;
    public static Item PURE_MOONLIGHT_GREATSWORD;
    public static Item BLOODTHIRSTER;
    public static Item DARKIN_BLADE;
    public static Item DRAGON_STAFF;
    public static Item WITHERED_WABBAJACK;
    public static Item WHIRLIGIG_SAWBLADE;
    public static Item DRAGONSLAYER_SWORDSPEAR;
    public static Item GUINSOOS_RAGEBLADE;
    public static Item GUTS_SWORD;
    public static Item NIGHTFALL;
    public static Item COMET_SPEAR;
    public static Item LICH_BANE;
    public static BowItem GALEFORCE;
    public static Item TRANSLUCENT_SWORD;
    public static Item TRANSLUCENT_GLAIVE;
    public static Item TRANSLUCENT_DOUBLE_GREATSWORD;
    public static Item DRAUGR;
    public static Item DAWNBREAKER;
    public static Item SOUL_REAPER;
    public static Item FORLORN_SCYTHE;
    public static Item LEVIATHAN_AXE;
    public static Item SKOFNUNG;
    public static Item MJOLNIR;
    public static Item FREYR_SWORD;
    public static Item STING;
    public static Item FEATHERLIGHT;
    public static Item CRUCIBLE_SWORD;
    public static Item DARKIN_SCYTHE_PRE;
    public static Item DARKIN_SCYTHE_PRIME;
    public static Item SHADOW_ASSASSIN_SCYTHE;
    public static TrickWeapon KIRKHAMMER;
    public static TrickWeapon SILVER_SWORD;
    public static TrickWeapon HOLY_GREATSWORD;
    public static Item DRAUPNIR_SPEAR;
    public static HolyMoonlightGreatsword HOLY_MOONLIGHT_GREATSWORD;
    public static TrickWeapon HOLY_MOONLIGHT_SWORD;
    public static Frostmourne FROSTMOURNE;
    public static Item MASTER_SWORD;
    public static Item NIGHTS_EDGE_ITEM;
    public static Item EMPOWERED_DAWNBREAKER;
    public static BowItem KRAKEN_SLAYER;
    public static CrossbowItem KRAKEN_SLAYER_CROSSBOW;
    public static BowItem DARKMOON_LONGBOW;
    public static Item CHUNGUS_STAFF;
    public static Item DARK_MOON_GREATSWORD;
    public static Item GLAIVE_OF_HODIR;
    public static Item EXCALIBUR;
    public static Item MOONVEIL;
    public static BowItem SIMONS_BOWBLADE;
    public static TrickWeapon SIMONS_BLADE;
    public static Item DRAGONBANE;
    public static Item SUPERNOVA;
    public static Item MEHRUNES_RAZOR;
    public static Item TONITRUS;
    public static Item BLOODLUST;
    public static Item NIGHTLORDS_SWORD;

    public static void init() {
        // Small helper so we can pass "no extra settings" cleanly
        UnaryOperator<Item.Settings> ID = UnaryOperator.identity();

        BLUEMOON_SHORTSWORD = ItemRegistry.registerLegendaryWeapon(
                "bluemoon_shortsword",
                s -> new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_bluemoon_shortsword,
                ConfigConstructor.is_fireproof_bluemoon_shortsword
        );
        BLUEMOON_GREATSWORD = ItemRegistry.registerLegendaryWeapon(
                "bluemoon_greatsword",
                s -> new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_bluemoon_greatsword,
                ConfigConstructor.is_fireproof_bluemoon_greatsword
        );
        MOONLIGHT_SHORTSWORD = ItemRegistry.registerLegendaryWeapon(
                "moonlight_shortsword",
                s -> new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_moonlight_shortsword,
                ConfigConstructor.is_fireproof_moonlight_shortsword
        );
        MOONLIGHT_GREATSWORD = ItemRegistry.registerLegendaryWeapon(
                "moonlight_greatsword",
                s -> new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_moonlight_greatsword,
                ConfigConstructor.is_fireproof_moonlight_greatsword
        );
        PURE_MOONLIGHT_GREATSWORD = ItemRegistry.registerLegendaryWeapon(
                "pure_moonlight_greatsword",
                s -> new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_pure_moonlight_greatsword,
                ConfigConstructor.is_fireproof_pure_moonlight_greatsword
        );
        BLOODTHIRSTER = ItemRegistry.registerLegendaryWeapon(
                "bloodthirster",
                s -> new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_bloodthirster,
                ConfigConstructor.is_fireproof_bloodthirster
        );
        DARKIN_BLADE = ItemRegistry.registerLegendaryWeapon(
                "darkin_blade",
                s -> new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_darkin_blade,
                ConfigConstructor.is_fireproof_darkin_blade
        );
        DRAGON_STAFF = ItemRegistry.registerLegendaryWeapon(
                "dragon_staff",
                s -> new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_dragon_staff,
                ConfigConstructor.is_fireproof_dragon_staff
        );
        WITHERED_WABBAJACK = ItemRegistry.registerLegendaryWeapon(
                "withered_wabbajack",
                s -> new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_withered_wabbajack,
                ConfigConstructor.is_fireproof_withered_wabbajack
        );
        WHIRLIGIG_SAWBLADE = ItemRegistry.registerLegendaryWeapon(
                "whirligig_sawblade",
                s -> new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_whirligig_sawblade,
                ConfigConstructor.is_fireproof_whirligig_sawblade
        );
        DRAGONSLAYER_SWORDSPEAR = ItemRegistry.registerLegendaryWeapon(
                "dragonslayer_swordspear",
                s -> new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_dragonslayer_swordspear,
                ConfigConstructor.is_fireproof_dragonslayer_swordspear
        );
        GUINSOOS_RAGEBLADE = ItemRegistry.registerLegendaryWeapon(
                "rageblade",
                s -> new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_rageblade,
                ConfigConstructor.is_fireproof_rageblade
        );
        GUTS_SWORD = ItemRegistry.registerLegendaryWeapon(
                "guts_sword",
                s -> new HeapOfRawIron(ModToolMaterials.IRON_BLOCK, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_heap_of_raw_iron,
                ConfigConstructor.is_fireproof_heap_of_raw_iron
        );
        NIGHTFALL = ItemRegistry.registerLegendaryWeapon(
                "nightfall",
                s -> new Nightfall(ModToolMaterials.IRON_BLOCK, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_nightfall,
                ConfigConstructor.is_fireproof_nightfall
        );
        COMET_SPEAR = ItemRegistry.registerLegendaryWeapon(
                "comet_spear",
                s -> new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_comet_spear,
                ConfigConstructor.is_fireproof_comet_spear
        );
        LICH_BANE = ItemRegistry.registerLegendaryWeapon(
                "lich_bane",
                s -> new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_lich_bane,
                ConfigConstructor.is_fireproof_lich_bane
        );

        GALEFORCE = ItemRegistry.registerLegendaryWeapon(
                "galeforce",
                s -> new Galeforce(s, () -> Ingredient.ofItems(ItemRegistry.VERGLAS, ItemRegistry.MOONSTONE)),
                st -> st.maxDamage(1300).rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_galeforce,
                ConfigConstructor.is_fireproof_galeforce
        );

        TRANSLUCENT_SWORD = ItemRegistry.registerWeaponItem(
                "translucent_sword",
                s -> new TranslucentWeapon(
                        ModToolMaterials.LOST_SOUL,
                        (int) ConfigConstructor.translucent_sword_damage,
                        ConfigConstructor.translucent_sword_attack_speed,
                        s
                ),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_translucent_sword,
                ConfigConstructor.is_fireproof_translucent_weapons
        );
        TRANSLUCENT_GLAIVE = ItemRegistry.registerWeaponItem(
                "translucent_glaive",
                s -> new TranslucentWeapon(
                        ModToolMaterials.LOST_SOUL,
                        (int) ConfigConstructor.translucent_glaive_damage,
                        ConfigConstructor.translucent_glaive_attack_speed,
                        s
                ),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_translucent_glaive,
                ConfigConstructor.is_fireproof_translucent_weapons
        );
        TRANSLUCENT_DOUBLE_GREATSWORD = ItemRegistry.registerWeaponItem(
                "translucent_double_greatsword",
                s -> new TranslucentWeapon(
                        ModToolMaterials.LOST_SOUL,
                        (int) ConfigConstructor.translucent_double_edged_greatsword_damage,
                        ConfigConstructor.translucent_double_edged_greatsword_attack_speed,
                        s
                ),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_translucent_double_edged_greatsword,
                ConfigConstructor.is_fireproof_translucent_weapons
        );

        DRAUGR = ItemRegistry.registerLegendaryWeapon(
                "draugr",
                s -> new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_draugr,
                ConfigConstructor.is_fireproof_draugr
        );
        DAWNBREAKER = ItemRegistry.registerLegendaryWeapon(
                "dawnbreaker",
                s -> new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_dawnbreaker,
                ConfigConstructor.is_fireproof_dawnbreaker
        );
        SOUL_REAPER = ItemRegistry.registerLegendaryWeapon(
                "soul_reaper",
                s -> new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_soul_reaper,
                ConfigConstructor.is_fireproof_soul_reaper
        );
        FORLORN_SCYTHE = ItemRegistry.registerLegendaryWeapon(
                "forlorn_scythe",
                s -> new ForlornScythe(ModToolMaterials.LOST_SOUL_DURABLE, s),
                st -> st.rarity(Rarity.UNCOMMON),
                ConfigConstructor.disable_recipe_forlorn_scythe,
                ConfigConstructor.is_fireproof_forlorn_scythe
        );

        LEVIATHAN_AXE = ItemRegistry.registerLegendaryItem(
                "leviathan_axe",
                s -> new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.is_fireproof_leviathan_axe
        ); //Handled in RecipeHandler

        SKOFNUNG = ItemRegistry.registerLegendaryWeapon(
                "skofnung",
                s -> new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_skofnung,
                ConfigConstructor.is_fireproof_skofnung
        );
        MJOLNIR = ItemRegistry.registerLegendaryWeapon(
                "mjolnir",
                s -> new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_mjolnir,
                ConfigConstructor.is_fireproof_mjolnir
        );
        FREYR_SWORD = ItemRegistry.registerLegendaryWeapon(
                "freyr_sword",
                s -> new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_sword_of_freyr,
                ConfigConstructor.is_fireproof_sword_of_freyr
        );
        STING = ItemRegistry.registerLegendaryWeapon(
                "sting",
                s -> new Sting(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_sting,
                ConfigConstructor.is_fireproof_sting
        );
        FEATHERLIGHT = ItemRegistry.registerLegendaryWeapon(
                "featherlight",
                s -> new Featherlight(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_featherlight,
                ConfigConstructor.is_fireproof_featherlight
        );
        CRUCIBLE_SWORD = ItemRegistry.registerLegendaryWeapon(
                "crucible_sword",
                s -> new CrucibleSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_crucible_sword,
                ConfigConstructor.is_fireproof_crucible_sword
        );

        DARKIN_SCYTHE_PRE = ItemRegistry.registerLegendaryWeapon(
                "darkin_scythe_pre",
                s -> new DarkinScythePre(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_darkin_scythe,
                ConfigConstructor.is_fireproof_darkin_scythe
        );
        DARKIN_SCYTHE_PRIME = ItemRegistry.registerLegendaryItem(
                "darkin_scythe",
                s -> new DarkinScythePrime(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.is_fireproof_darkin_scythe_prime
        ); // Gained by transforming DARKIN_SCYTHE_PRE
        SHADOW_ASSASSIN_SCYTHE = ItemRegistry.registerLegendaryItem(
                "shadow_assassin_scythe",
                s -> new ShadowAssassinScythe(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.is_fireproof_shadow_assassin_scythe
        ); // Gained by transforming DARKIN_SCYTHE_PRE

        KIRKHAMMER = ItemRegistry.registerWeaponItem(
                "kirkhammer",
                s -> new Kirkhammmer(ModToolMaterials.IRON_BLOCK, s),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_kirkhammer,
                ConfigConstructor.is_fireproof_kirkhammer
        );
        SILVER_SWORD = ItemRegistry.registerItem(
                "silver_sword",
                s -> new TrickWeapon(
                        ModToolMaterials.IRON_BLOCK,
                        (int) ConfigConstructor.kirkhammer_silver_sword_damage,
                        ConfigConstructor.kirkhammer_silver_sword_attack_speed,
                        s,
                        ConfigConstructor.disable_use_silver_sword,
                        ConfigConstructor.kirkhammer_silver_sword_righteous_base_undead_bonus_damage,
                        ConfigConstructor.kirkhammer_silver_sword_righteous_undead_bonus_damage_per_level
                ),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.is_fireproof_silver_sword
        ); // Switched to by other trick weapons

        HOLY_GREATSWORD = ItemRegistry.registerWeaponItem(
                "holy_greatsword",
                s -> new TrickWeapon(
                        ModToolMaterials.IRON_BLOCK,
                        (int) ConfigConstructor.ludwigs_holy_greatsword_damage,
                        ConfigConstructor.ludwigs_holy_greatsword_attack_speed,
                        s,
                        ConfigConstructor.disable_use_ludwigs_holy_greatsword,
                        ConfigConstructor.ludwigs_holy_greatsword_righteous_base_undead_bonus_damage,
                        ConfigConstructor.ludwigs_holy_greatsword_righteous_undead_bonus_damage_per_level
                ),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_ludwigs_holy_blade,
                ConfigConstructor.is_fireproof_ludwigs_holy_blade
        );

        DRAUPNIR_SPEAR = ItemRegistry.registerLegendaryWeapon(
                "draupnir_spear",
                s -> new DraupnirSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_draupnir_spear,
                ConfigConstructor.is_fireproof_draupnir_spear
        );

        HOLY_MOONLIGHT_GREATSWORD = ItemRegistry.registerLegendaryItem(
                "holy_moonlight_greatsword",
                s -> new HolyMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.is_fireproof_holy_moonlight_greatsword
        ); // Switched to by Holy Moonlight Sword

        HOLY_MOONLIGHT_SWORD = ItemRegistry.registerLegendaryWeapon(
                "holy_moonlight_sword",
                s -> new HolyMoonlightSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_holy_moonlight_sword,
                ConfigConstructor.is_fireproof_holy_moonlight_sword
        );

        FROSTMOURNE = ItemRegistry.registerLegendaryWeapon(
                "frostmourne",
                s -> new Frostmourne(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_frostmourne,
                ConfigConstructor.is_fireproof_frostmourne
        );

        MASTER_SWORD = ItemRegistry.registerLegendaryWeapon(
                "master_sword",
                s -> new MasterSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_master_sword,
                ConfigConstructor.is_fireproof_master_sword
        );

        NIGHTS_EDGE_ITEM = ItemRegistry.registerLegendaryWeapon(
                "nights_edge_item",
                s -> new NightsEdgeItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_nights_edge,
                ConfigConstructor.is_fireproof_nights_edge
        );

        EMPOWERED_DAWNBREAKER = ItemRegistry.registerLegendaryWeapon(
                "empowered_dawnbreaker",
                s -> new EmpoweredDawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_empowered_dawnbreaker,
                ConfigConstructor.is_fireproof_empowered_dawnbreaker
        );

        KRAKEN_SLAYER = ItemRegistry.registerWeaponItem(
                "kraken_slayer",
                s -> new KrakenSlayer(s, () -> Ingredient.ofItems(Items.GOLD_INGOT)),
                st -> st.maxDamage(1258).rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_kraken_slayer_bow,
                ConfigConstructor.is_fireproof_kraken_slayer_bow
        );
        KRAKEN_SLAYER_CROSSBOW = ItemRegistry.registerLegendaryWeapon(
                "kraken_slayer_crossbow",
                s -> new KrakenSlayerCrossbow(s, () -> Ingredient.ofItems(Items.GOLD_INGOT)),
                st -> st.maxDamage(1258).rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_kraken_slayer_crossbow,
                ConfigConstructor.is_fireproof_kraken_slayer_crossbow
        );
        DARKMOON_LONGBOW = ItemRegistry.registerLegendaryWeapon(
                "darkmoon_longbow",
                s -> new DarkmoonLongbow(s, () -> Ingredient.ofItems(Items.GOLD_INGOT)),
                st -> st.maxDamage(1400).rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_darkmoon_longbow,
                ConfigConstructor.is_fireproof_darkmoon_longbow
        );

        CHUNGUS_STAFF = ItemRegistry.registerLegendaryWeapon(
                "chungus_staff",
                s -> new ChungusStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.maxDamage(1258).rarity(Rarity.EPIC).recipeRemainder(CHUNGUS_STAFF),
                ConfigConstructor.disable_recipe_chungus_staff,
                ConfigConstructor.is_fireproof_chungus_staff
        );

        DARK_MOON_GREATSWORD = ItemRegistry.registerLegendaryWeapon(
                "dark_moon_greatsword",
                s -> new DarkMoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_dark_moon_greatsword,
                ConfigConstructor.is_fireproof_dark_moon_greatsword
        );

        GLAIVE_OF_HODIR = ItemRegistry.registerLegendaryWeapon(
                "glaive_of_hodir",
                s -> new GlaiveOfHodir(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_glaive_of_hodir,
                ConfigConstructor.is_fireproof_glaive_of_hodir
        );

        EXCALIBUR = ItemRegistry.registerLegendaryWeapon(
                "excalibur",
                s -> new Excalibur(ModToolMaterials.ECHO_SHARD, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_excalibur,
                ConfigConstructor.is_fireproof_excalibur
        );

        MOONVEIL = ItemRegistry.registerLegendaryWeapon(
                "moonveil",
                s -> new Moonveil(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_moonveil,
                ConfigConstructor.is_fireproof_moonveil
        );

        SIMONS_BOWBLADE = ItemRegistry.registerLegendaryWeapon(
                "simons_bowblade",
                s -> new SimonsBowblade(s, () -> Ingredient.ofItems(Items.IRON_BLOCK, ItemRegistry.SOUL_INGOT)),
                st -> st.maxDamage(1354).rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_simons_bowblade,
                ConfigConstructor.is_fireproof_simons_bowblade
        );

        SIMONS_BLADE = ItemRegistry.registerLegendaryItem(
                "simons_blade",
                s -> new TrickWeapon(
                        ModToolMaterials.IRON_BLOCK,
                        (int) ConfigConstructor.simons_blade_damage,
                        ConfigConstructor.simons_blade_attack_speed,
                        s,
                        ConfigConstructor.disable_use_simons_blade,
                        ConfigConstructor.simons_blade_righteous_base_undead_bonus_damage,
                        ConfigConstructor.simons_blade_righteous_undead_bonus_damage_per_level
                ),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.is_fireproof_simons_blade
        ); // Switched to by Simon's Bowblade

        DRAGONBANE = ItemRegistry.registerLegendaryWeapon(
                "dragonbane",
                s -> new Dragonbane(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_dragonbane,
                ConfigConstructor.is_fireproof_dragonbane
        );

        SUPERNOVA = ItemRegistry.registerLegendaryWeapon(
                "supernova",
                s -> new Supernova(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_supernova,
                ConfigConstructor.is_fireproof_supernova
        );

        MEHRUNES_RAZOR = ItemRegistry.registerLegendaryWeapon(
                "mehrunes_razor",
                s -> new MehrunesRazor(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_mehrunes_razor,
                ConfigConstructor.is_fireproof_mehrunes_razor
        );

        TONITRUS = ItemRegistry.registerLegendaryWeapon(
                "tonitrus",
                s -> new Tonitrus(ModToolMaterials.IRON_BLOCK, s),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_tonitrus,
                ConfigConstructor.is_fireproof_tonitrus
        );

        BLOODLUST = ItemRegistry.registerLegendaryWeapon(
                "bloodlust",
                s -> new Bloodlust(ModToolMaterials.IRON_BLOCK, s),
                st -> st.rarity(Rarity.RARE),
                ConfigConstructor.disable_recipe_bloodlust,
                ConfigConstructor.is_fireproof_bloodlust
        );

        NIGHTLORDS_SWORD = ItemRegistry.registerLegendaryWeapon(
                "nightlords_sword",
                s -> new NightlordsSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, s),
                st -> st.rarity(Rarity.EPIC),
                ConfigConstructor.disable_recipe_sword_of_the_nightlord,
                ConfigConstructor.is_fireproof_sword_of_the_nightlord
        );
    }
}