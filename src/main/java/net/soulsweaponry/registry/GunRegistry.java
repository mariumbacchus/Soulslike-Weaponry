package net.soulsweaponry.registry;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.items.*;

import static net.soulsweaponry.SoulsWeaponry.MAIN_GROUP;

public class GunRegistry {

    public static RegistryObject<Item> HUNTER_PISTOL;
    public static RegistryObject<Item> BLUNDERBUSS;
    public static RegistryObject<Item> GATLING_GUN;
    public static RegistryObject<Item> HUNTER_CANNON;

    public static void register() {
        HUNTER_PISTOL = ItemRegistry.registerGunItem("hunter_pistol", () -> new HunterPistol(new Item.Settings().group(MAIN_GROUP).maxDamage(700).rarity(Rarity.RARE)));
        BLUNDERBUSS = ItemRegistry.registerGunItem("blunderbuss", () -> new Blunderbuss(new Item.Settings().group(MAIN_GROUP).maxDamage(900).rarity(Rarity.RARE)));
        GATLING_GUN = ItemRegistry.registerGunItem("gatling_gun", () -> new GatlingGun(new Item.Settings().group(MAIN_GROUP).maxDamage(1000).rarity(Rarity.RARE)));
        HUNTER_CANNON = ItemRegistry.registerGunItem("hunter_cannon", () -> new HunterCannon(new Item.Settings().group(MAIN_GROUP).maxDamage(1250).rarity(Rarity.RARE)));
    }
}
