package net.soulsweaponry.registry;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.armor.*;
import net.soulsweaponry.items.material.ModArmorMaterials;
import net.soulsweaponry.items.material.ModToolMaterials;

import java.util.function.Supplier;

public class ItemRegistry {

    public static final ItemGroup MAIN_GROUP = SoulsWeaponry.MAIN_GROUP;
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulsWeaponry.ModId);

    public static final RegistryObject<LoreItem> LORD_SOUL_RED = ITEMS.register("lord_soul_red", () -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_red", 4));
    public static final RegistryObject<LoreItem> LORD_SOUL_DARK = registerItem("lord_soul_dark", () -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_dark", 3));
    public static final RegistryObject<LoreItem> LORD_SOUL_VOID = registerItem("lord_soul_void",() -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_void", 3));
    public static final RegistryObject<LoreItem> LORD_SOUL_ROSE = registerItem("lord_soul_rose",() -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_rose", 3));
    public static final RegistryObject<LoreItem> LORD_SOUL_PURPLE = registerItem("lord_soul_purple", () -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_purple", 3));
    public static final RegistryObject<LoreItem> LORD_SOUL_WHITE = registerItem("lord_soul_white", () -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_white", 3));
    public static final RegistryObject<LoreItem> LORD_SOUL_DAY_STALKER = registerItem("lord_soul_day_stalker", () ->new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_day_stalker", 2));
    public static final RegistryObject<LoreItem> LORD_SOUL_NIGHT_PROWLER = registerItem("lord_soul_night_prowler",() -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_night_prowler", 3));
    public static final RegistryObject<Item> LOST_SOUL = registerItem("lost_soul", () -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE), "lost_soul", 3));
    public static final RegistryObject<Item> MOONSTONE = registerItem("moonstone", () -> new Item(new Item.Settings().group(MAIN_GROUP)));
    public static final RegistryObject<Item> CHUNGUS_EMERALD = registerItem("chungus_emerald", () -> new Item(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DEMON_HEART = registerItem("demon_heart", () -> new LoreItem(new Item.Settings().group(MAIN_GROUP).food(new FoodComponent.Builder()
            .hunger(4).saturationModifier(6f).meat().alwaysEdible()
            .effect(() -> new StatusEffectInstance(StatusEffects.STRENGTH, 150, 0), 1)
            .effect(() -> new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY.get(), 150, 0), 10)
            .effect(() -> new StatusEffectInstance(StatusEffects.NAUSEA, 400, 0), 1).build()),
            "demon_heart", 3));
    public static final RegistryObject<Item> MOLTEN_DEMON_HEART= registerItem("molten_demon_heart", () -> new Item(new Item.Settings().group(MAIN_GROUP)));
    public static final RegistryObject<Item> DEMON_CHUNK = registerItem("demon_chunk",() -> new Item(new Item.Settings().group(MAIN_GROUP)));
    public static final RegistryObject<Item> CRIMSON_INGOT = registerItem("crimson_ingot",() -> new Item(new Item.Settings().group(MAIN_GROUP)));
    public static final RegistryObject<Item> SOUL_INGOT = registerItem("soul_ingot", () -> new Item(new Item.Settings().group(MAIN_GROUP)));
    public static final RegistryObject<Item> SILVER_BULLET = registerItem("silver_bullet", () -> new Item(new Item.Settings().group(MAIN_GROUP).maxCount(20)));
    public static final RegistryObject<Item> BOSS_COMPASS = registerItem("boss_compass", () -> new BossCompass(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MOONSTONE_RING = registerItem("moonstone_ring", () -> new MoonstoneRing(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).maxDamage(25)));
    public static final RegistryObject<Item> SHARD_OF_UNCERTAINTY = registerItem("shard_of_uncertainty", () -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE).fireproof(), "shard_of_uncertainty", 1));
    public static final RegistryObject<Item> VERGLAS = registerItem("verglas", () -> new Item(new Item.Settings().group(MAIN_GROUP)));
    public static final RegistryObject<Item> SKOFNUNG_STONE = registerItem("skofnung_stone", () -> new SkofnungStone(new Item.Settings().maxDamage(20).group(MAIN_GROUP)));
    public static final RegistryObject<Item> IRON_SKULL = registerItem("iron_skull", () -> new Item(new Item.Settings().group(MAIN_GROUP)));

    public static final RegistryObject<Item> MOONSTONE_SHOVEL = registerItem("moonstone_shovel",() -> new ShovelItem(ModToolMaterials.MOONSTONE_TOOL, 1.5f, -3.0f, new Item.Settings().group(MAIN_GROUP)));
    public static final RegistryObject<Item> MOONSTONE_PICKAXE = registerItem("moonstone_pickaxe", () -> new ModTools.ModPickaxe(ModToolMaterials.MOONSTONE_TOOL, 1, -2.8f, new Item.Settings().group(MAIN_GROUP)));
    public static final RegistryObject<Item> MOONSTONE_AXE = registerItem("moonstone_axe",() -> new ModTools.ModAxe(ModToolMaterials.MOONSTONE_TOOL, 5, -3.0f, new Item.Settings().group(MAIN_GROUP)));
    public static final RegistryObject<Item> MOONSTONE_HOE = registerItem("moonstone_hoe",() -> new ModTools.ModHoe(ModToolMaterials.MOONSTONE_TOOL, -3, 0.0f, new Item.Settings().group(MAIN_GROUP)));

    public static final RegistryObject<LoreItem> WITHERED_DEMON_HEART = registerItem("withered_demon_heart", () -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE).fireproof(), "withered_demon_heart", 4));
    public static final RegistryObject<LoreItem> ARKENSTONE = registerItem("arkenstone", () -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE).fireproof(), "arkenstone", 4));
    public static final RegistryObject<LoreItem> ESSENCE_OF_EVENTIDE = registerItem("essence_of_eventide",() -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE).fireproof(), "essence_of_eventide", 2));
    public static final RegistryObject<LoreItem> ESSENCE_OF_LUMINESCENCE = registerItem("essence_of_luminescence", () -> new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE).fireproof(), "essence_of_luminescence", 3));
    public static final RegistryObject<Item> CHAOS_CROWN = registerItem("chaos_crown", () -> new ChaosSet(ModArmorMaterials.CHAOS_SET, EquipmentSlot.HEAD, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static final RegistryObject<Item> CHAOS_HELMET = registerItem("chaos_helmet", () -> new ChaosArmor(ModArmorMaterials.CHAOS_ARMOR, EquipmentSlot.HEAD, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static final RegistryObject<Item> ARKENPLATE = registerItem("arkenplate", () -> new ChaosArmor(ModArmorMaterials.CHAOS_ARMOR, EquipmentSlot.CHEST, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static final RegistryObject<Item> ENHANCED_ARKENPLATE = registerItem("enhanced_arkenplate", () -> new EChaosArmor(ModArmorMaterials.CHAOS_ARMOR, EquipmentSlot.CHEST, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static final RegistryObject<Item> WITHERED_CHEST = registerItem("withered_chest", () -> new WitheredArmor(ModArmorMaterials.CHAOS_ARMOR, EquipmentSlot.CHEST, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static final RegistryObject<Item> ENHANCED_WITHERED_CHEST = registerItem("enhanced_withered_chest", () -> new WitheredArmor(ModArmorMaterials.WITHERED_ARMOR, EquipmentSlot.CHEST, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static final RegistryObject<Item> CHAOS_ROBES = registerItem("chaos_robes",() -> new ChaosSet(ModArmorMaterials.CHAOS_SET, EquipmentSlot.CHEST, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));
    public static final RegistryObject<Item> CHAOS_ORB = registerItem("chaos_orb", () -> new ChaosOrb(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof()));

    public static final RegistryObject<MusicDiscItem> CHUNGUS_DISC = registerItem("chungus_disc", () -> new MusicDiscItem(7, SoundRegistry.BIG_CHUNGUS_SONG_EVENT, new Item.Settings().group(MAIN_GROUP).maxCount(1)));

    public static <I extends Item> RegistryObject<I> registerItem(String id, Supplier<I> item) {
        return ITEMS.register(id, item);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
