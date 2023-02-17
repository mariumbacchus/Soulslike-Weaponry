package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Rarity;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.material.ModToolMaterials;

public class WeaponRegistry {
    
    public static final ItemGroup MAIN_GROUP = SoulsWeaponry.MAIN_GROUP;

    public static ToolItem BLUEMOON_SHORTSWORD = new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4F, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem BLUEMOON_GREATSWORD = new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem MOONLIGHT_SHORTSWORD = new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4F, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem MOONLIGHT_GREATSWORD = new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem PURE_MOONLIGHT_GREATSWORD = new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem BLOODTHIRSTER = new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, -2.4F, new FabricItemSettings().fireproof().rarity(Rarity.EPIC));
    public static ToolItem DARKIN_BLADE = new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, -3F, new FabricItemSettings().fireproof().rarity(Rarity.EPIC));
    public static ToolItem DRAGON_STAFF = new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem WITHERED_WABBAJACK = new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new FabricItemSettings().rarity(Rarity.EPIC).fireproof());
    public static ToolItem WHIRLIGIG_SAWBLADE = new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, -2.4f, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem DRAGONSLAYER_SWORDSPEAR = new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.6f, new FabricItemSettings().fireproof().rarity(Rarity.EPIC));
    public static ToolItem GUINSOOS_RAGEBLADE = new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem GUTS_SWORD = new DragonslayerSwordBerserk(ModToolMaterials.IRON_BLOCK, -3f, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem NIGHTFALL = new Nightfall(ModToolMaterials.IRON_BLOCK, -3, new FabricItemSettings().fireproof().rarity(Rarity.EPIC));
    public static ToolItem COMET_SPEAR = new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.6f, new FabricItemSettings().fireproof().rarity(Rarity.EPIC));
    public static ToolItem LICH_BANE = new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new FabricItemSettings().rarity(Rarity.EPIC));
    public static BowItem GALEFORCE = new Galeforce(new FabricItemSettings().maxDamage(1300).rarity(Rarity.EPIC));
    public static ToolItem TRANSLUCENT_SWORD = new SwordItem(ModToolMaterials.LOST_SOUL, 6, -2.4F, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem TRANSLUCENT_GLAIVE = new SwordItem(ModToolMaterials.LOST_SOUL, 7, -2.6F, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem TRANSLUCENT_DOUBLE_GREATSWORD = new SwordItem(ModToolMaterials.LOST_SOUL, 8, -2.8F, new FabricItemSettings().rarity(Rarity.RARE));
    public static ToolItem DRAUGR = new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem DAWNBREAKER = new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem SOUL_REAPER = new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new FabricItemSettings().rarity(Rarity.EPIC));
    public static ToolItem FORLORN_SCYTHE = new ForlornScythe(ModToolMaterials.LOST_SOUL, -3f, new FabricItemSettings().rarity(Rarity.UNCOMMON));
    public static ToolItem LEVIATHAN_AXE = new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new FabricItemSettings().rarity(Rarity.EPIC).fireproof());
    public static ToolItem SKOFNUNG = new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new FabricItemSettings().rarity(Rarity.EPIC).fireproof());
    public static ToolItem MJOLNIR = new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new FabricItemSettings().rarity(Rarity.EPIC).fireproof());
    public static ToolItem FREYR_SWORD = new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new FabricItemSettings().rarity(Rarity.EPIC).fireproof());

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
    }
    
    public String[] weaponNames = {
        "bloodthirster", "bluemoon_greatsword", "bluemoon_shortsword", "comet_spear", 
        "darkin_blade", "dawnbreaker", "heap_of_raw_iron", "dragonslayer_swordspear",
        "dragon_staff", "draugr_night", "forlorn_scythe", "rageblade", "lich_bane",
        "moonlight_greatsword", "moonlight_shortsword", "nightfall", "soulreaper",
        "whirligig_sawblade", "withered_wabbajack",
    };
}
