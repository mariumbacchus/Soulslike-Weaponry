package net.soulsweaponry.registry;

import net.minecraft.util.Rarity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.gun.*;

public class GunRegistry {

    public static GunItem HUNTER_PISTOL;
    public static GunItem BLUNDERBUSS;
    public static GunItem GATLING_GUN;
    public static GunItem HUNTER_CANNON;

    public static void init() {
        HUNTER_PISTOL = ItemRegistry.registerGunItem(
                "hunter_pistol",
                HunterPistol::new,
                s -> s.maxDamage(700).rarity(Rarity.RARE).enchantable(7),
                ConfigConstructor.is_fireproof_hunter_pistol
        );

        BLUNDERBUSS = ItemRegistry.registerGunItem(
                "blunderbuss",
                Blunderbuss::new,
                s -> s.maxDamage(900).rarity(Rarity.RARE).enchantable(7),
                ConfigConstructor.is_fireproof_blunderbuss
        );

        GATLING_GUN = ItemRegistry.registerGunItem(
                "gatling_gun",
                GatlingGun::new,
                s -> s.maxDamage(1000).rarity(Rarity.RARE).enchantable(7),
                ConfigConstructor.is_fireproof_gatling_gun
        );

        HUNTER_CANNON = ItemRegistry.registerGunItem(
                "hunter_cannon",
                HunterCannon::new,
                s -> s.maxDamage(1250).rarity(Rarity.RARE).enchantable(7),
                ConfigConstructor.is_fireproof_hunter_cannon
        );
    }
}