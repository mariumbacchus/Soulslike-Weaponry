package net.soulsweaponry.registry;

import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Rarity;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.material.ModToolMaterials;

public class WeaponRegistry {
    
    public static final ItemGroup MAIN_GROUP = SoulsWeaponry.MAIN_GROUP;

    public static ToolItem BLUEMOON_SHORTSWORD = new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem BLUEMOON_GREATSWORD = new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem MOONLIGHT_SHORTSWORD = new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem MOONLIGHT_GREATSWORD = new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem PURE_MOONLIGHT_GREATSWORD = new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem BLOODTHIRSTER = new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, -2.4F, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC));
    public static ToolItem DARKIN_BLADE = new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, -3F, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC));
    public static ToolItem DRAGON_STAFF = new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem WITHERED_WABBAJACK = new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());
    public static ToolItem WHIRLIGIG_SAWBLADE = new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem DRAGONSLAYER_SWORDSPEAR = new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.6f, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC));
    public static ToolItem GUINSOOS_RAGEBLADE = new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem GUTS_SWORD = new DragonslayerSwordBerserk(ModToolMaterials.IRON_BLOCK, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem NIGHTFALL = new Nightfall(ModToolMaterials.IRON_BLOCK, -3, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC));
    public static ToolItem COMET_SPEAR = new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.6f, new Item.Settings().group(MAIN_GROUP).fireproof().rarity(Rarity.EPIC));
    public static ToolItem LICH_BANE = new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static BowItem GALEFORCE = new Galeforce(new Item.Settings().group(MAIN_GROUP).maxDamage(1300).rarity(Rarity.EPIC));
    public static ToolItem TRANSLUCENT_SWORD = new SwordItem(ModToolMaterials.LOST_SOUL, 6, -2.4F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem TRANSLUCENT_GLAIVE = new SwordItem(ModToolMaterials.LOST_SOUL, 7, -2.6F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem TRANSLUCENT_DOUBLE_GREATSWORD = new SwordItem(ModToolMaterials.LOST_SOUL, 8, -2.8F, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem DRAUGR = new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem DAWNBREAKER = new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem SOUL_REAPER = new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem FORLORN_SCYTHE = new ForlornScythe(ModToolMaterials.LOST_SOUL, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.UNCOMMON));
    public static ToolItem LEVIATHAN_AXE = new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());
    public static ToolItem SKOFNUNG = new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());
    public static ToolItem MJOLNIR = new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());
    public static ToolItem FREYR_SWORD = new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());
    public static ToolItem STING = new Sting(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem FEATHERLIGHT = new Featherlight(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static ToolItem CRUCIBLE_SWORD = new CrucibleSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());
    public static ToolItem DARKIN_SCYTHE_PRE = new DarkinScythePre(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem DARKIN_SCYTHE_PRIME = new DarkinScythePrime(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static ToolItem SHADOW_ASSASSIN_SCYTHE = new ShadowAssassinScythe(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC));
    public static TrickWeapon KIRKHAMMER = new TrickWeapon(ModToolMaterials.IRON_BLOCK, 0, -3f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC), 1, true);
    public static TrickWeapon SILVER_SWORD = new TrickWeapon(ModToolMaterials.IRON_BLOCK, 1, -2.4f, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC), 0, false);

    public static void init() {
        ItemRegistry.registerItem(BLUEMOON_SHORTSWORD, "bluemoon_shortsword");
        ItemRegistry.registerItem(BLUEMOON_GREATSWORD, "bluemoon_greatsword");
        ItemRegistry.registerItem(MOONLIGHT_SHORTSWORD, "moonlight_shortsword");
        ItemRegistry.registerItem(MOONLIGHT_GREATSWORD, "moonlight_greatsword");
        ItemRegistry.registerItem(PURE_MOONLIGHT_GREATSWORD, "pure_moonlight_greatsword");
        ItemRegistry.registerItem(BLOODTHIRSTER, "bloodthirster");
        ItemRegistry.registerItem(DARKIN_BLADE, "darkin_blade");
        ItemRegistry.registerItem(DRAGON_STAFF, "dragon_staff");
        ItemRegistry.registerItem(WITHERED_WABBAJACK, "withered_wabbajack");
        ItemRegistry.registerItem(WHIRLIGIG_SAWBLADE, "whirligig_sawblade");
        ItemRegistry.registerItem(DRAGONSLAYER_SWORDSPEAR, "dragonslayer_swordspear");
        ItemRegistry.registerItem(GUINSOOS_RAGEBLADE, "rageblade");
        ItemRegistry.registerItem(GUTS_SWORD, "guts_sword");
        ItemRegistry.registerItem(NIGHTFALL, "nightfall");
        ItemRegistry.registerItem(COMET_SPEAR, "comet_spear");
        ItemRegistry.registerItem(LICH_BANE, "lich_bane");
        ItemRegistry.registerItem(GALEFORCE, "galeforce");
        ItemRegistry.registerItem(TRANSLUCENT_SWORD, "translucent_sword");
        ItemRegistry.registerItem(TRANSLUCENT_GLAIVE, "translucent_glaive");
        ItemRegistry.registerItem(TRANSLUCENT_DOUBLE_GREATSWORD, "translucent_double_greatsword");
        ItemRegistry.registerItem(DRAUGR, "draugr");
        ItemRegistry.registerItem(DAWNBREAKER, "dawnbreaker");
        ItemRegistry.registerItem(SOUL_REAPER, "soul_reaper");
        ItemRegistry.registerItem(FORLORN_SCYTHE, "forlorn_scythe");
        ItemRegistry.registerItem(LEVIATHAN_AXE, "leviathan_axe");
        ItemRegistry.registerItem(SKOFNUNG, "skofnung");
        ItemRegistry.registerItem(MJOLNIR, "mjolnir");
        ItemRegistry.registerItem(FREYR_SWORD, "freyr_sword");
        ItemRegistry.registerItem(STING, "sting");
        ItemRegistry.registerItem(FEATHERLIGHT, "featherlight");
        ItemRegistry.registerItem(CRUCIBLE_SWORD, "crucible_sword");
        ItemRegistry.registerItem(DARKIN_SCYTHE_PRE, "darkin_scythe_pre");
        ItemRegistry.registerItem(DARKIN_SCYTHE_PRIME, "darkin_scythe");
        ItemRegistry.registerItem(SHADOW_ASSASSIN_SCYTHE, "shadow_assassin_scythe");
        ItemRegistry.registerItem(KIRKHAMMER, "kirkhammer");
        ItemRegistry.registerItem(SILVER_SWORD, "silver_sword");
    }
}
