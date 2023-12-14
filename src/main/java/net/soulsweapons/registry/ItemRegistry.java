package net.soulsweapons.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweapons.SoulsWeaponry;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulsWeaponry.MOD_ID);

    public static final RegistryObject<Item> MOONSTONE = ITEMS.register("moonstone", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
