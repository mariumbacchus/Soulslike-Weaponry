package net.soulsweaponry.registry;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.gun.*;

public class GunRegistry {

    public static GunItem HUNTER_PISTOL = new HunterPistol(new Item.Settings().maxDamage(700).rarity(Rarity.RARE));
    public static GunItem BLUNDERBUSS = new Blunderbuss(new Item.Settings().maxDamage(900).rarity(Rarity.RARE));
    public static GunItem GATLING_GUN = new GatlingGun(new Item.Settings().maxDamage(1000).rarity(Rarity.RARE));
    public static GunItem HUNTER_CANNON = new HunterCannon(new Item.Settings().maxDamage(1250).rarity(Rarity.RARE));

    public static void init() {
        ItemRegistry.registerGunItem(HUNTER_PISTOL, "hunter_pistol", ConfigConstructor.is_fireproof_hunter_pistol);
        ItemRegistry.registerGunItem(BLUNDERBUSS, "blunderbuss", ConfigConstructor.is_fireproof_blunderbuss);
        ItemRegistry.registerGunItem(GATLING_GUN, "gatling_gun", ConfigConstructor.is_fireproof_gatling_gun);
        ItemRegistry.registerGunItem(HUNTER_CANNON, "hunter_cannon", ConfigConstructor.is_fireproof_hunter_cannon);
    }
}