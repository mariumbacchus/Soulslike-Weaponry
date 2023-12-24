package net.soulsweaponry.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.Blunderbuss;
import net.soulsweaponry.items.GunItem;

import static net.soulsweaponry.SoulsWeaponry.MAIN_GROUP;

public class GunRegistry {

    public static final DeferredRegister<Item> GUNS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulsWeaponry.MOD_ID);
    //TODO add all guns

    //public static final RegistryObject<GunItem> BLUNDERBUSS = GUNS.register("blunderbuss", () -> new Blunderbuss(new Item.Properties().tab(MAIN_GROUP).durability(900).rarity(Rarity.RARE)));

    public static void register(IEventBus eventBus) {
        GUNS.register(eventBus);
    }
}
