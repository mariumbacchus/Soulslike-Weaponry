package net.soulsweaponry.registry;

import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.material.ModToolMaterials;

import static net.soulsweaponry.SoulsWeaponry.MAIN_GROUP;

public class WeaponRegistry {

    public static RegistryObject<ToolItem> BLUEMOON_SHORTSWORD = ItemRegistry.registerItem("bluemoon_shortsword", new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
    public static RegistryObject<ToolItem> BLUEMOON_GREATSWORD = ItemRegistry.registerItem("bluemoon_greatsword", new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
    public static RegistryObject<ToolItem> MOONLIGHT_SHORTSWORD = ItemRegistry.registerItem("moonlight_shortsword", new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> MOONLIGHT_GREATSWORD = ItemRegistry.registerItem("moonlight_greatsword", new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> PURE_MOONLIGHT_GREATSWORD = ItemRegistry.registerItem("pure_moonlight_greatsword", new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> BLOODTHIRSTER = ItemRegistry.registerItem("bloodthirster", new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, -2.4F, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<DetonateGroundItem> DARKIN_BLADE = ItemRegistry.registerItem("darkin_blade", new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, -3F, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> DRAGON_STAFF = ItemRegistry.registerItem("dragon_staff", new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> WITHERED_WABBAJACK = ItemRegistry.registerItem("withered_wabbajack", new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static RegistryObject<ToolItem> WHIRLIGIG_SAWBLADE = ItemRegistry.registerItem("whirligig_sawblade", new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> DRAGONSLAYER_SWORDSPEAR = ItemRegistry.registerItem("dragonslayer_swordspear", new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.6f, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> GUINSOOS_RAGEBLADE = ItemRegistry.registerItem("rageblade", new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> GUTS_SWORD = ItemRegistry.registerItem("guts_sword", new DragonslayerSwordBerserk(ModToolMaterials.IRON_BLOCK, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> NIGHTFALL = ItemRegistry.registerItem("nightfall", new Nightfall(ModToolMaterials.IRON_BLOCK, -3, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<DetonateGroundItem> COMET_SPEAR = ItemRegistry.registerItem("comet_spear", new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.6f, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> LICH_BANE = ItemRegistry.registerItem("lich_bane", new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)));
    public static RegistryObject<BowItem> GALEFORCE = ItemRegistry.registerItem("galeforce", new Galeforce(new Item.Settings().group(MAIN_GROUP).fireproof().maxDamage(1300).rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> TRANSLUCENT_SWORD = ItemRegistry.registerItem("translucent_sword", new SwordItem(ModToolMaterials.LOST_SOUL, 6, -2.4F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
    public static RegistryObject<ToolItem> TRANSLUCENT_GLAIVE = ItemRegistry.registerItem("translucent_glaive", new SwordItem(ModToolMaterials.LOST_SOUL, 7, -2.6F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
    public static RegistryObject<ToolItem> TRANSLUCENT_DOUBLE_GREATSWORD = ItemRegistry.registerItem("translucent_double_greatsword", new SwordItem(ModToolMaterials.LOST_SOUL, 8, -2.8F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
    public static RegistryObject<ToolItem> DRAUGR = ItemRegistry.registerItem("draugr", new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> DAWNBREAKER = ItemRegistry.registerItem("dawnbreaker", new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> SOUL_REAPER = ItemRegistry.registerItem("soul_reaper", new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> FORLORN_SCYTHE = ItemRegistry.registerItem("forlorn_scythe", new ForlornScythe(ModToolMaterials.LOST_SOUL, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.UNCOMMON)));
    public static RegistryObject<ToolItem> LEVIATHAN_AXE = ItemRegistry.registerItem("leviathan_axe", new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static RegistryObject<ToolItem> SKOFNUNG = ItemRegistry.registerItem("skofnung", new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static RegistryObject<ToolItem> MJOLNIR = ItemRegistry.registerItem("mjolnir", new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static RegistryObject<ToolItem> FREYR_SWORD = ItemRegistry.registerItem("freyr_sword", new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static RegistryObject<ToolItem> STING = ItemRegistry.registerItem("sting", new Sting(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
    public static RegistryObject<ToolItem> FEATHERLIGHT = ItemRegistry.registerItem("featherlight", new Featherlight(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
    public static RegistryObject<ToolItem> CRUCIBLE_SWORD = ItemRegistry.registerItem("crucible_sword", new CrucibleSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static RegistryObject<ToolItem> DARKIN_SCYTHE_PRE = ItemRegistry.registerItem("darkin_scythe_pre", new DarkinScythePre(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static RegistryObject<ToolItem> DARKIN_SCYTHE_PRIME = ItemRegistry.registerItem("darkin_scythe", new DarkinScythePrime(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static RegistryObject<ToolItem> SHADOW_ASSASSIN_SCYTHE = ItemRegistry.registerItem("shadow_assassin_scythe", new ShadowAssassinScythe(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static RegistryObject<TrickWeapon> KIRKHAMMER = ItemRegistry.registerItem("kirkhammer", new TrickWeapon(ModToolMaterials.IRON_BLOCK, 0, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE), 1, 0, true, false));
    public static RegistryObject<TrickWeapon> SILVER_SWORD = ItemRegistry.registerItem("silver_sword", new TrickWeapon(ModToolMaterials.IRON_BLOCK, 1, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE), 0, 1, false, true));
    public static RegistryObject<TrickWeapon> HOLY_GREATSWORD = ItemRegistry.registerItem("holy_greatsword", new TrickWeapon(ModToolMaterials.IRON_BLOCK, 2, -2.8f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE), 1, 2, false, true));
    public static RegistryObject<ToolItem> DRAUPNIR_SPEAR = ItemRegistry.registerItem("draupnir_spear", new DraupnirSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.6f,  new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<HolyMoonlightGreatsword> HOLY_MOONLIGHT_GREATSWORD = ItemRegistry.registerItem("holy_moonlight_greatsword", new HolyMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f,  new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC), 4));
    public static RegistryObject<TrickWeapon> HOLY_MOONLIGHT_SWORD = ItemRegistry.registerItem("holy_moonlight_sword", new HolyMoonlightSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f,  new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<Frostmourne> FROSTMOURNE = ItemRegistry.registerItem("frostmourne", new Frostmourne(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f,  new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> MASTER_SWORD = ItemRegistry.registerItem("master_sword", new MasterSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> NIGHTS_EDGE_ITEM = ItemRegistry.registerItem("nights_edge_item", new NightsEdgeItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<ToolItem> EMPOWERED_DAWNBREAKER = ItemRegistry.registerItem("empowered_dawnbreaker", new EmpoweredDawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<BowItem> KRAKEN_SLAYER = ItemRegistry.registerItem("kraken_slayer", new KrakenSlayer(new Item.Settings().group(MAIN_GROUP).maxDamage(1258).fireproof().rarity(Rarity.EPIC)));
    public static RegistryObject<CrossbowItem> KRAKEN_SLAYER_CROSSBOW = ItemRegistry.registerItem("kraken_slayer_crossbow", new KrakenSlayerCrossbow(new Item.Settings().group(MAIN_GROUP).fireproof().maxDamage(1258).rarity(Rarity.EPIC)));
    public static RegistryObject<BowItem> DARKMOON_LONGBOW = ItemRegistry.registerItem("darkmoon_longbow", new DarkmoonLongbow(new Item.Settings().group(MAIN_GROUP).fireproof().maxDamage(1400).rarity(Rarity.EPIC)));
}
