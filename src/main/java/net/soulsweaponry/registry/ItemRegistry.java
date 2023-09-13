package net.soulsweaponry.registry;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.material.ModArmorMaterials;
import net.soulsweaponry.items.material.ModToolMaterials;

public class ItemRegistry {
  
    public static final ItemGroup MAIN_GROUP = SoulsWeaponry.MAIN_GROUP;

    public static final LoreItem LORD_SOUL_RED = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_red", 3);
    public static final LoreItem LORD_SOUL_DARK = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_dark", 3);
    public static final LoreItem LORD_SOUL_VOID = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_void", 2);
    public static final LoreItem LORD_SOUL_ROSE = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_rose", 2);
    public static final LoreItem LORD_SOUL_PURPLE = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_purple", 3);
    public static final LoreItem LORD_SOUL_WHITE = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_white", 3);
    public static final LoreItem LORD_SOUL_DAY_STALKER = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof(), "lord_soul_day_stalker", 2);
    public static final Item LOST_SOUL = new Item(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static final Item MOONSTONE = new Item(new Item.Settings().group(MAIN_GROUP));
    public static final Item CHUNGUS_EMERALD = new Item(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.UNCOMMON));
    public static final Item DEMON_HEART = new Item(new Item.Settings().group(MAIN_GROUP).food(new FoodComponent.Builder().hunger(4).saturationModifier(6f).meat().alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 150, 0), 1).statusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, 150, 0), 1).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 0), 1).build()));
    public static final Item MOLTEN_DEMON_HEART= new Item(new Item.Settings().group(MAIN_GROUP));
    public static final Item DEMON_CHUNK = new Item(new Item.Settings().group(MAIN_GROUP));
    public static final Item CRIMSON_INGOT = new Item(new Item.Settings().group(MAIN_GROUP));
    public static final Item SOUL_INGOT = new Item(new Item.Settings().group(MAIN_GROUP));
    public static final Item SILVER_BULLET = new Item(new Item.Settings().group(MAIN_GROUP).maxCount(20));
    public static final Item BOSS_COMPASS = new BossCompass(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE));
    public static final Item MOONSTONE_RING = new MoonstoneRing(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).maxDamage(25));
    public static final Item SHARD_OF_UNCERTAINTY = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE).fireproof(), "shard_of_uncertainty", 1);
    public static final Item VERGLAS = new Item(new Item.Settings().group(MAIN_GROUP));
    public static final Item SKOFNUNG_STONE = new SkofnungStone(new Item.Settings().maxDamage(20).group(MAIN_GROUP));
    public static final Item IRON_SKULL = new Item(new Item.Settings().group(MAIN_GROUP));

    public static final Item MOONSTONE_SHOVEL = new ShovelItem(ModToolMaterials.MOONSTONE_TOOL, 1.5f, -3.0f, new Item.Settings().group(MAIN_GROUP));
    public static final Item MOONSTONE_PICKAXE = new ModTools.ModPickaxe(ModToolMaterials.MOONSTONE_TOOL, 1, -2.8f, new Item.Settings().group(MAIN_GROUP));
    public static final Item MOONSTONE_AXE = new ModTools.ModAxe(ModToolMaterials.MOONSTONE_TOOL, 5, -3.0f, new Item.Settings().group(MAIN_GROUP));
    public static final Item MOONSTONE_HOE = new ModTools.ModHoe(ModToolMaterials.MOONSTONE_TOOL, -3, 0.0f, new Item.Settings().group(MAIN_GROUP));

    public static final LoreItem WITHERED_DEMON_HEART = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE).fireproof(), "withered_demon_heart", 3);
    public static final LoreItem ARKENSTONE = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE).fireproof(), "arkenstone", 4);
    public static final LoreItem ESSENCE_OF_EVENTIDE = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE).fireproof(), "essence_of_eventide", 2);
    public static final LoreItem ESSENCE_OF_LUMINESCENCE = new LoreItem(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.RARE).fireproof(), "essence_of_luminescence", 3);
    public static final Item CHAOS_CROWN = new ChaosSet(ModArmorMaterials.CHAOS_SET, EquipmentSlot.HEAD, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());
    public static final Item CHAOS_HELMET = new ChaosSet(ModArmorMaterials.CHAOS_ARMOR, EquipmentSlot.HEAD, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());
    public static final Item ARKENPLATE = new ChaosSet(ModArmorMaterials.CHAOS_ARMOR, EquipmentSlot.CHEST, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());
    public static final Item CHAOS_ROBES = new ChaosSet(ModArmorMaterials.CHAOS_SET, EquipmentSlot.CHEST, new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());
    public static final Item CHAOS_ORB = new ChaosOrb(new Item.Settings().group(MAIN_GROUP).rarity(Rarity.EPIC).fireproof());

    public static final ModMusicDiscs CHUNGUS_DISC = new ModMusicDiscs(7, SoundRegistry.BIG_CHUNGUS_SONG_EVENT, new Item.Settings().group(MAIN_GROUP).maxCount(1));

    public static void init() {
        registerLoreItem(LORD_SOUL_RED);
        registerLoreItem(LORD_SOUL_DARK);
        registerLoreItem(LORD_SOUL_VOID);
        registerLoreItem(LORD_SOUL_ROSE);
        registerLoreItem(LORD_SOUL_PURPLE);
        registerLoreItem(LORD_SOUL_WHITE);
        registerLoreItem(LORD_SOUL_DAY_STALKER);
        registerItem(LOST_SOUL, "lost_soul");
        registerItem(MOONSTONE, "moonstone");
        registerItem(CHUNGUS_EMERALD, "chungus_emerald");
        registerItem(DEMON_HEART, "demon_heart");
        registerItem(MOLTEN_DEMON_HEART, "molten_demon_heart");
        registerItem(DEMON_CHUNK, "demon_chunk");
        registerItem(CRIMSON_INGOT, "crimson_ingot");
        registerItem(SOUL_INGOT, "soul_ingot");
        registerItem(SILVER_BULLET, "silver_bullet");
        registerItem(BOSS_COMPASS, "boss_compass");
        registerItem(MOONSTONE_RING, "moonstone_ring");
        registerItem(SHARD_OF_UNCERTAINTY, "shard_of_uncertainty");
        registerItem(VERGLAS, "verglas");
        registerItem(SKOFNUNG_STONE, "skofnung_stone");
        registerItem(IRON_SKULL, "iron_skull");

        registerItem(MOONSTONE_SHOVEL, "moonstone_shovel");
        registerItem(MOONSTONE_PICKAXE, "moonstone_pickaxe");
        registerItem(MOONSTONE_AXE, "moonstone_axe");
        registerItem(MOONSTONE_HOE, "moonstone_hoe");

        registerLoreItem(WITHERED_DEMON_HEART);
        registerLoreItem(ARKENSTONE);
        registerLoreItem(ESSENCE_OF_EVENTIDE);
        registerLoreItem(ESSENCE_OF_LUMINESCENCE);
        registerItem(CHAOS_CROWN, "chaos_crown");
        registerItem(CHAOS_HELMET, "chaos_helmet");
        registerItem(ARKENPLATE, "arkenplate");
        registerItem(CHAOS_ROBES, "chaos_robes");
        registerItem(CHAOS_ORB, "chaos_orb");

        registerItem(CHUNGUS_DISC, "chungus_disc");
    }

  public static <I extends Item> I registerItem(I item, String name) {
		return Registry.register(Registry.ITEM, new Identifier(SoulsWeaponry.ModId, name), item);
	}

  public static <I extends LoreItem> I registerLoreItem(I item) {
		return Registry.register(Registry.ITEM, new Identifier(SoulsWeaponry.ModId, item.getIdName()), item);
	}
}
