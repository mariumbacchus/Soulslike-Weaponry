package net.soulsweapons.registry;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweapons.SoulsWeaponry;
import net.soulsweapons.items.*;
import net.soulsweapons.items.material.ModArmorMaterials;
import net.soulsweapons.items.material.ModToolMaterials;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulsWeaponry.MOD_ID);

    public static final RegistryObject<Item> LORD_SOUL_RED = ITEMS.register("lord_soul_red", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant(), "lord_soul_red", 4));
    public static final RegistryObject<Item> LORD_SOUL_DARK = ITEMS.register("lord_soul_dark", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant(), "lord_soul_red", 3));
    public static final RegistryObject<Item> LORD_SOUL_VOID = ITEMS.register("lord_soul_void", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant(), "lord_soul_red", 3));
    public static final RegistryObject<Item> LORD_SOUL_ROSE = ITEMS.register("lord_soul_rose", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant(), "lord_soul_rose", 3));
    public static final RegistryObject<Item> LORD_SOUL_PURPLE = ITEMS.register("lord_soul_purple", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant(), "lord_soul_purple", 3));
    public static final RegistryObject<Item> LORD_SOUL_WHITE = ITEMS.register("lord_soul_white", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant(), "lord_soul_white", 3));
    public static final RegistryObject<Item> LORD_SOUL_DAY_STALKER = ITEMS.register("lord_soul_day_stalker", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant(), "lord_soul_day_stalker", 2));
    public static final RegistryObject<Item> LORD_SOUL_NIGHT_PROWLER = ITEMS.register("lord_soul_night_prowler", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant(), "lord_soul_night_prowler", 3));
    public static final RegistryObject<Item> LOST_SOUL = ITEMS.register("lost_soul", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.RARE), "lost_soul", 3));
    public static final RegistryObject<Item> MOONSTONE = ITEMS.register("moonstone", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> CHUNGUS_EMERALD = ITEMS.register("chungus_emerald", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> DEMON_HEART = ITEMS.register("demon_heart", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)
            .food(new FoodProperties.Builder().alwaysEat().meat().nutrition(4).saturationMod(6f)
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 150, 0), 1)
                    .effect(() -> new MobEffectInstance(EffectRegistry.BLOODTHIRSTY.get(), 150, 0), 10)
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 400, 0), 1)
                    .build()), "demon_heart", 3));
    public static final RegistryObject<Item> MOLTEN_DEMON_HEART = ITEMS.register("molten_demon_heart", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> DEMON_CHUNK = ITEMS.register("demon_chunk", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> CRIMSON_INGOT = ITEMS.register("crimson_ingot", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> SOUL_INGOT = ITEMS.register("soul_ingot", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> SILVER_BULLET = ITEMS.register("silver_bullet", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> BOSS_COMPASS = ITEMS.register("boss_compass", () -> new BossCompass(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.RARE)));//TODO husk å lage predicates, at den spinner og sånt, at buer har animasjon osv.
    public static final RegistryObject<Item> MOONSTONE_RING = ITEMS.register("moonstone_ring", () -> new MoonstoneRing(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).durability(25)));
    public static final RegistryObject<Item> SHARD_OF_UNCERTAINTY = ITEMS.register("shard_of_uncertainty", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.RARE).fireResistant(), "shard_of_uncertainty", 1));
    public static final RegistryObject<Item> VERGLAS = ITEMS.register("verglas", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> SKOFNUNG_STONE = ITEMS.register("skofnung_stone", () -> new SkofnungStone(new Item.Properties().durability(20).tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> IRON_SKULL = ITEMS.register("iron_skull", () -> new Item(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));

    public static final RegistryObject<Item> MOONSTONE_SHOVEL = ITEMS.register("moonstone_shovel", () -> new ShovelItem(ModToolMaterials.MOONSTONE_TOOL, 1.5f, -3f, new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> MOONSTONE_PICKAXE = ITEMS.register("moonstone_pickaxe", () -> new ShovelItem(ModToolMaterials.MOONSTONE_TOOL, 1f, -2.8f, new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> MOONSTONE_AXE = ITEMS.register("moonstone_axe", () -> new ShovelItem(ModToolMaterials.MOONSTONE_TOOL, 5f, -3f, new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    public static final RegistryObject<Item> MOONSTONE_HOE = ITEMS.register("moonstone_hoe", () -> new ShovelItem(ModToolMaterials.MOONSTONE_TOOL, 0f, -3f, new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));

    public static final RegistryObject<Item> WITHERED_DEMON_HEART = ITEMS.register("withered_demon_heart", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.RARE).fireResistant(), "withered_demon_heart", 4));
    public static final RegistryObject<Item> ARKENSTONE = ITEMS.register("arkenstone", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.RARE).fireResistant(), "arkenstone", 4));
    public static final RegistryObject<Item> ESSENCE_OF_EVENTIDE = ITEMS.register("essence_of_eventide", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.RARE).fireResistant(), "essence_of_eventide", 2));
    public static final RegistryObject<Item> ESSENCE_OF_LUMINESCENCE = ITEMS.register("essence_of_luminescence", () -> new LoreItem(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.RARE).fireResistant(), "essence_of_luminescence", 3));
    public static final RegistryObject<Item> CHAOS_CROWN = ITEMS.register("chaos_crown", () -> new ChaosSet(ModArmorMaterials.CHAOS_SET, EquipmentSlot.HEAD, new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> CHAOS_HELMET = ITEMS.register("chaos_helmet", () -> new ChaosSet(ModArmorMaterials.CHAOS_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> ARKENPLATE = ITEMS.register("arkenplate", () -> new ChaosSet(ModArmorMaterials.CHAOS_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> CHAOS_ROBES = ITEMS.register("chaos_robes", () -> new ChaosSet(ModArmorMaterials.CHAOS_SET, EquipmentSlot.CHEST, new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> CHAOS_ORB = ITEMS.register("chaos_orb", () -> new ChaosOrb(new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> CHUNGUS_DISC = ITEMS.register("chungus_disc", () -> new RecordItem(5, SoundRegistry.BIG_CHUNGUS_SONG_EVENT, new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
