package net.soulsweaponry.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;

import java.util.function.Supplier;

import static net.soulsweaponry.SoulsWeaponry.ITEM_GROUP_LIST;

public class ItemGroupRegistry {

    public static final DeferredRegister<ItemGroup> ITEM_GROUPS = DeferredRegister.create(Registries.ITEM_GROUP.getKey(), SoulsWeaponry.ModId);

    public static final RegistryObject<ItemGroup> MAIN_GROUP = ITEM_GROUPS.register("general", () -> ItemGroup.builder()
            .displayName(Text.translatable("itemGroup.soulsweapons.general"))
            .icon(() -> new ItemStack(ItemRegistry.MOONSTONE.get())).entries((displayContext, entries) -> {
                for (Supplier<? extends Item> item : ITEM_GROUP_LIST) {
                    entries.add(item.get());
                }
            })
            .build());

    public static void register(IEventBus bus) {
        ITEM_GROUPS.register(bus);
    }
}
