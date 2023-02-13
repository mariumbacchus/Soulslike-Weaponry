package net.soulsweaponry.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Rarity;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.Blunderbuss;
import net.soulsweaponry.items.GatlingGun;
import net.soulsweaponry.items.GunItem;
import net.soulsweaponry.items.HunterCannon;
import net.soulsweaponry.items.HunterPistol;

public class GunRegistry {
    
    public static final ItemGroup MAIN_GROUP = SoulsWeaponry.MAIN_GROUP;

    public static GunItem HUNTER_PISTOL = new HunterPistol(new Item.Settings().group(MAIN_GROUP).maxDamage(700).rarity(Rarity.RARE));
    public static GunItem BLUNDERBUSS = new Blunderbuss(new Item.Settings().group(MAIN_GROUP).maxDamage(900).rarity(Rarity.RARE));
    public static GunItem GATLING_GUN = new GatlingGun(new Item.Settings().group(MAIN_GROUP).maxDamage(1000).rarity(Rarity.RARE));
    public static GunItem HUNTER_CANNON = new HunterCannon(new Item.Settings().group(MAIN_GROUP).maxDamage(1250).rarity(Rarity.RARE));
    
    public static void init() {
        ItemRegistry.registerItem(HUNTER_PISTOL, "hunter_pistol");
        ItemRegistry.registerItem(BLUNDERBUSS, "blunderbuss");
        ItemRegistry.registerItem(GATLING_GUN, "gatling_gun");
        ItemRegistry.registerItem(HUNTER_CANNON, "hunter_cannon");
    }
}
