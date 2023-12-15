package net.soulsweapons.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweapons.SoulsWeaponry;
import net.soulsweapons.items.LoreItem;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulsWeaponry.MOD_ID);

    public static final RegistryObject<Item> LORD_SOUL_RED = ITEMS.register("lord_soul_red", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC), "lord_soul_red", 4));
    public static final RegistryObject<Item> LORD_SOUL_DARK = ITEMS.register("lord_soul_dark", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC), "lord_soul_red", 3));
    public static final RegistryObject<Item> LORD_SOUL_VOID = ITEMS.register("lord_soul_void", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC), "lord_soul_red", 3));
    public static final RegistryObject<Item> LORD_SOUL_ROSE = ITEMS.register("lord_soul_rose", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC), "lord_soul_rose", 3));
    public static final RegistryObject<Item> LORD_SOUL_PURPLE = ITEMS.register("lord_soul_purple", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC), "lord_soul_purple", 3));
    public static final RegistryObject<Item> LORD_SOUL_WHITE = ITEMS.register("lord_soul_white", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC), "lord_soul_white", 3));
    public static final RegistryObject<Item> LORD_SOUL_DAY_STALKER = ITEMS.register("lord_soul_day_stalker", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC), "lord_soul_day_stalker", 2));
    public static final RegistryObject<Item> LORD_SOUL_NIGHT_PROWLER = ITEMS.register("lord_soul_night_prowler", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC), "lord_soul_night_prowler", 3));
    public static final RegistryObject<Item> LOST_SOUL = ITEMS.register("lost_soul", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.RARE), "lost_soul", 3));
    public static final RegistryObject<Item> MOONSTONE = ITEMS.register("moonstone", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> CHUNGUS_EMERALD = ITEMS.register("chungus_emerald", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> DEMON_HEART = ITEMS.register("demon_heart", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)
            .food(new FoodProperties.Builder().alwaysEat().meat().nutrition(4).saturationMod(6f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 150, 0), 1)
                    .effect(new MobEffectInstance(MobEffects.JUMP, 150, 0), 10) //TODO: Replace with bloodthirsty
                    .effect(new MobEffectInstance(MobEffects.CONFUSION, 400, 0), 1)
                    .build()), "demon_heart", 3));
    public static final RegistryObject<Item> MOLTEN_DEMON_HEART = ITEMS.register("molten_demon_heart", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> DEMON_CHUNK = ITEMS.register("demon_chunk", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> SOUL_INGOT = ITEMS.register("soul_ingot", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> SILVER_BULLET = ITEMS.register("silver_bullet", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));

    public static final RegistryObject<Item> SHARD_OF_UNCERTAINTY = ITEMS.register("shard_of_uncertainty", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.RARE).fireResistant(), "shard_of_uncertainty", 1));

    public static final RegistryObject<Item> VERGLAS = ITEMS.register("verglas", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));

    public static final RegistryObject<Item> IRON_SKULL = ITEMS.register("iron_skull", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
