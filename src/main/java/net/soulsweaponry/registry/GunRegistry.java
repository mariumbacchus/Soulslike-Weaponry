package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.util.Rarity;
import net.soulsweaponry.items.*;

public class GunRegistry {

    public static GunItem HUNTER_PISTOL = new HunterPistol(new FabricItemSettings().maxDamage(700).rarity(Rarity.RARE));
    public static GunItem BLUNDERBUSS = new Blunderbuss(new FabricItemSettings().maxDamage(900).rarity(Rarity.RARE));
    public static GunItem GATLING_GUN = new GatlingGun(new FabricItemSettings().maxDamage(1000).rarity(Rarity.RARE));
    public static GunItem HUNTER_CANNON = new HunterCannon(new FabricItemSettings().maxDamage(1250).rarity(Rarity.RARE));
    
    public static void init() {
        ItemRegistry.registerGunItem(HUNTER_PISTOL, "hunter_pistol");
        ItemRegistry.registerGunItem(BLUNDERBUSS, "blunderbuss");
        ItemRegistry.registerGunItem(GATLING_GUN, "gatling_gun");
        ItemRegistry.registerGunItem(HUNTER_CANNON, "hunter_cannon");
    }
}
