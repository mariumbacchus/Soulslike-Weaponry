package net.soulsweaponry.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.material.ModToolMaterials;

import static net.soulsweaponry.SoulsWeaponry.MAIN_GROUP;

public class WeaponRegistry {

    //TODO legg til resten av våpen
    public static final DeferredRegister<Item> WEAPONS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulsWeaponry.MOD_ID);

    public static final RegistryObject<TieredItem> BLUEMOON_SHORTSWORD = WEAPONS.register("bluemoon_shortsword", () -> new BluemoonShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4F, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.RARE)));
    public static final RegistryObject<TieredItem> BLUEMOON_GREATSWORD = WEAPONS.register("bluemoon_greatsword", () -> new BluemoonGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.RARE)));
//    public static final RegistryObject<TieredItem> MOONLIGHT_SHORTSWORD = WEAPONS.register("", () -> new MoonlightShortsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4F, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC));
//    public static final RegistryObject<TieredItem> MOONLIGHT_GREATSWORD = WEAPONS.register("", () -> new MoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC));
//    public static final RegistryObject<TieredItem> PURE_MOONLIGHT_GREATSWORD = WEAPONS.register("", () -> new PureMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8F, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC));
    public static final RegistryObject<TieredItem> BLOODTHIRSTER = WEAPONS.register("bloodthirster", () -> new Bloodthirster(ModToolMaterials.CRIMSON_INGOT, -2.4F, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<TieredItem> DARKIN_BLADE = WEAPONS.register("darkin_blade", () -> new DarkinBlade(ModToolMaterials.CRIMSON_INGOT, -3F, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC)));
//    public static final RegistryObject<TieredItem> DRAGON_STAFF = WEAPONS.register("", () -> new DragonStaff(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC));
//    public static final RegistryObject<TieredItem> WITHERED_WABBAJACK = WEAPONS.register("", () -> new WitheredWabbajack(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC).fireResistant());
//    public static final RegistryObject<TieredItem> WHIRLIGIG_SAWBLADE = WEAPONS.register("", () -> new WhirligigSawblade(ModToolMaterials.IRON_BLOCK, -2.4f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC));
//    public static final RegistryObject<TieredItem> DRAGONSLAYER_SWORDSPEAR = WEAPONS.register("", () -> new DragonslayerSwordspear(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.6f, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC));
//    public static final RegistryObject<TieredItem> GUINSOOS_RAGEBLADE = WEAPONS.register("", () -> new GuinsoosRageblade(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC));
//    public static final RegistryObject<TieredItem> GUTS_SWORD = WEAPONS.register("", () -> new DragonslayerSwordBerserk(ModToolMaterials.IRON_BLOCK, -3f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC));
    public static final RegistryObject<TieredItem> NIGHTFALL = WEAPONS.register("nightfall", () -> new Nightfall(ModToolMaterials.IRON_BLOCK, -3, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<TieredItem> COMET_SPEAR = WEAPONS.register("comet_spear", () -> new CometSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.6f, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC)));
//    public static final RegistryObject<TieredItem> LICH_BANE = WEAPONS.register("", () -> new LichBane(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC));
//    public static final RegistryObject<BowItem> GALEFORCE = WEAPONS.register("", () -> new Galeforce(new Item.Properties().tab(MAIN_GROUP).fireResistant().maxDamage(1300).rarity(Rarity.EPIC));
//    public static final RegistryObject<TieredItem> TRANSLUCENT_SWORD = WEAPONS.register("", () -> new SwordItem(ModToolMaterials.LOST_SOUL, 6, -2.4F, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.RARE));
//    public static final RegistryObject<TieredItem> TRANSLUCENT_GLAIVE = WEAPONS.register("", () -> new SwordItem(ModToolMaterials.LOST_SOUL, 7, -2.6F, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.RARE));
//    public static final RegistryObject<TieredItem> TRANSLUCENT_DOUBLE_GREATSWORD = WEAPONS.register("", () -> new SwordItem(ModToolMaterials.LOST_SOUL, 8, -2.8F, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.RARE));
//    public static final RegistryObject<TieredItem> DRAUGR = WEAPONS.register("", () -> new Draugr(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC));
    public static final RegistryObject<TieredItem> DAWNBREAKER = WEAPONS.register("dawnbreaker", () -> new Dawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<TieredItem> SOUL_REAPER = WEAPONS.register("soul_reaper", () -> new SoulReaper(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<TieredItem> FORLORN_SCYTHE = WEAPONS.register("forlorn_scythe", () -> new ForlornScythe(ModToolMaterials.LOST_SOUL, -3f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<TieredItem> LEVIATHAN_AXE = WEAPONS.register("leviathan_axe", () -> new LeviathanAxe(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
//    public static final RegistryObject<TieredItem> SKOFNUNG = WEAPONS.register("", () -> new Skofnung(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<TieredItem> MJOLNIR = WEAPONS.register("mjolnir", () -> new Mjolnir(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<TieredItem> FREYR_SWORD = WEAPONS.register("freyr_sword", () -> new FreyrSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<TieredItem> STING = WEAPONS.register("sting", () -> new Sting(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.RARE)));
//    public static final RegistryObject<TieredItem> FEATHERLIGHT = WEAPONS.register("", () -> new Featherlight(ModToolMaterials.MOONSTONE_OR_VERGLAS, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.RARE));
    public static final RegistryObject<TieredItem> CRUCIBLE_SWORD = WEAPONS.register("crucible_sword", () -> new CrucibleSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<TieredItem> DARKIN_SCYTHE_PRE = WEAPONS.register("darkin_scythe_pre", () -> new DarkinScythePre(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<TieredItem> DARKIN_SCYTHE_PRIME = WEAPONS.register("darkin_scythe", () -> new DarkinScythePrime(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<TieredItem> SHADOW_ASSASSIN_SCYTHE = WEAPONS.register("shadow_assassin_scythe", () -> new ShadowAssassinScythe(ModToolMaterials.MOONSTONE_OR_VERGLAS, -3f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
//    public static TrickWeapon KIRKHAMMER = WEAPONS.register("", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, 0, -3f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.RARE), 1, 0, true, false);
//    public static TrickWeapon SILVER_SWORD = WEAPONS.register("", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, 1, -2.4f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.RARE), 0, 1, false, true);
//    public static TrickWeapon HOLY_GREATSWORD = WEAPONS.register("", () -> new TrickWeapon(ModToolMaterials.IRON_BLOCK, 2, -2.8f, new Item.Properties().tab(MAIN_GROUP).rarity(Rarity.RARE), 1, 2, false, true);
    public static final RegistryObject<TieredItem> DRAUPNIR_SPEAR = WEAPONS.register("draupnir_spear", () -> new DraupnirSpear(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.6f, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC)));
//    public static TrickWeapon HOLY_MOONLIGHT_GREATSWORD = WEAPONS.register("", () -> new HolyMoonlightGreatsword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f,  WEAPONS.register("", () -> Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC), 4);
//    public static TrickWeapon HOLY_MOONLIGHT_SWORD = WEAPONS.register("", () -> new TrickWeapon(ModToolMaterials.MOONSTONE_OR_VERGLAS, 4, -2.4f, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC), 3, 4, false, true);
//    public static final RegistryObject<TieredItem> FROSTMOURNE = WEAPONS.register("", () -> new Frostmourne(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC));
//    public static final RegistryObject<TieredItem> MASTER_SWORD = WEAPONS.register("", () -> new MasterSword(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.4f, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC));
//    public static final RegistryObject<TieredItem> NIGHTS_EDGE_ITEM = WEAPONS.register("", () -> new NightsEdgeItem(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC));
    public static final RegistryObject<TieredItem> EMPOWERED_DAWNBREAKER = WEAPONS.register("empowered_dawnbreaker", () -> new EmpoweredDawnbreaker(ModToolMaterials.MOONSTONE_OR_VERGLAS, -2.8f, new Item.Properties().tab(MAIN_GROUP).fireResistant().rarity(Rarity.EPIC)));
//    public static final RegistryObject<final RegistryObject<BowItem>> KRAKEN_SLAYER = WEAPONS.register("", () -> new KrakenSlayer(new Item.Properties().tab(MAIN_GROUP).maxDamage(1258).fireResistant().rarity(Rarity.EPIC));
//    public static final RegistryObject<BowItem> DARKMOON_LONGBOW = WEAPONS.register("", () -> new DarkmoonLongbow(new Item.Properties().tab(MAIN_GROUP).fireResistant().maxDamage(1400).rarity(Rarity.EPIC));


    public static void register(IEventBus eventBus) {
        WEAPONS.register(eventBus);
    }
}
