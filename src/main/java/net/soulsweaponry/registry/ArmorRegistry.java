package net.soulsweaponry.registry;

import net.minecraft.item.Item;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.util.Rarity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.armor.*;

import java.util.function.UnaryOperator;

public class ArmorRegistry {

    private static UnaryOperator<Item.Settings> epic() {
        return s -> s.rarity(Rarity.EPIC);
    }

    public static final Item CHAOS_CROWN = ItemRegistry.registerItem(
            "chaos_crown",
            settings -> new ChaosCrown(MaterialRegistry.CHAOS_SET, EquipmentType.HELMET, settings),
            epic(),
            ConfigConstructor.is_fireproof_chaos_crown
    );

    public static final Item CHAOS_HELMET = ItemRegistry.registerArmorItem(
            "chaos_helmet",
            settings -> new ChaosHelmet(MaterialRegistry.CHAOS_ARMOR, EquipmentType.HELMET, settings),
            epic(),
            ConfigConstructor.disable_recipe_chaos_helmet,
            ConfigConstructor.is_fireproof_chaos_helmet
    );

    public static final Item ARKENPLATE = ItemRegistry.registerArmorItem(
            "arkenplate",
            settings -> new Arkenplate(MaterialRegistry.CHAOS_ARMOR, EquipmentType.CHESTPLATE, settings),
            epic(),
            ConfigConstructor.disable_recipe_arkenplate,
            ConfigConstructor.is_fireproof_arkenplate
    );

    public static final Item ENHANCED_ARKENPLATE = ItemRegistry.registerArmorItem(
            "enhanced_arkenplate",
            settings -> new EnhancedArkenplate(MaterialRegistry.ENHANCED_CHAOS_ARMOR, EquipmentType.CHESTPLATE, settings),
            epic(),
            ConfigConstructor.disable_recipe_enhanced_arkenplate,
            ConfigConstructor.is_fireproof_arkenplate
    );

    public static final Item WITHERED_CHEST = ItemRegistry.registerArmorItem(
            "withered_chest",
            settings -> new Hallowheart(MaterialRegistry.WITHERED_ARMOR, EquipmentType.CHESTPLATE, settings),
            epic(),
            ConfigConstructor.disable_recipe_withered_chest,
            ConfigConstructor.is_fireproof_hallowheart
    );

    public static final Item ENHANCED_WITHERED_CHEST = ItemRegistry.registerArmorItem(
            "enhanced_withered_chest",
            settings -> new EnhancedHallowheart(MaterialRegistry.ENHANCED_WITHERED_ARMOR, EquipmentType.CHESTPLATE, settings),
            epic(),
            ConfigConstructor.disable_recipe_enhanced_withered_chest,
            ConfigConstructor.is_fireproof_hallowheart
    );

    public static final Item CHAOS_ROBES = ItemRegistry.registerItem(
            "chaos_robes",
            settings -> new ChaosRobes(MaterialRegistry.CHAOS_SET, EquipmentType.CHESTPLATE, settings),
            epic(),
            ConfigConstructor.is_fireproof_chaos_robes
    );

    public static final Item SOUL_INGOT_HELMET = ItemRegistry.registerArmorItem(
            "soul_ingot_helmet",
            settings -> new SoulIngotArmor(MaterialRegistry.SOUL_INGOT, EquipmentType.HELMET, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_soul_ingot_helmet,
            ConfigConstructor.is_fireproof_soul_ingot_set
    );

    public static final Item SOUL_INGOT_CHESTPLATE = ItemRegistry.registerArmorItem(
            "soul_ingot_chestplate",
            settings -> new SoulIngotArmor(MaterialRegistry.SOUL_INGOT, EquipmentType.CHESTPLATE, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_soul_ingot_chestplate,
            ConfigConstructor.is_fireproof_soul_ingot_set
    );

    public static final Item SOUL_INGOT_LEGGINGS = ItemRegistry.registerArmorItem(
            "soul_ingot_leggings",
            settings -> new SoulIngotArmor(MaterialRegistry.SOUL_INGOT, EquipmentType.LEGGINGS, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_soul_ingot_leggings,
            ConfigConstructor.is_fireproof_soul_ingot_set
    );

    public static final Item SOUL_INGOT_BOOTS = ItemRegistry.registerArmorItem(
            "soul_ingot_boots",
            settings -> new SoulIngotArmor(MaterialRegistry.SOUL_INGOT, EquipmentType.BOOTS, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_soul_ingot_boots,
            ConfigConstructor.is_fireproof_soul_ingot_set
    );

    public static final Item SOUL_ROBES_HELMET = ItemRegistry.registerArmorItem(
            "soul_robes_helmet",
            settings -> new SoulRobesArmor(MaterialRegistry.SOUL_ROBES, EquipmentType.HELMET, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_soul_robes_helmet,
            ConfigConstructor.is_fireproof_soul_robes_set
    );

    public static final Item SOUL_ROBES_CHESTPLATE = ItemRegistry.registerArmorItem(
            "soul_robes_chestplate",
            settings -> new SoulRobesArmor(MaterialRegistry.SOUL_ROBES, EquipmentType.CHESTPLATE, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_soul_robes_chestplate,
            ConfigConstructor.is_fireproof_soul_robes_set
    );

    public static final Item SOUL_ROBES_LEGGINGS = ItemRegistry.registerArmorItem(
            "soul_robes_leggings",
            settings -> new SoulRobesArmor(MaterialRegistry.SOUL_ROBES, EquipmentType.LEGGINGS, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_soul_robes_leggings,
            ConfigConstructor.is_fireproof_soul_robes_set
    );

    public static final Item SOUL_ROBES_BOOTS = ItemRegistry.registerArmorItem(
            "soul_robes_boots",
            settings -> new SoulRobesArmor(MaterialRegistry.SOUL_ROBES, EquipmentType.BOOTS, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_soul_robes_boots,
            ConfigConstructor.is_fireproof_soul_robes_set
    );

    public static final Item FORLORN_HELMET = ItemRegistry.registerArmorItem(
            "forlorn_helmet",
            settings -> new ForlornArmor(MaterialRegistry.FORLORN_ARMOR, EquipmentType.HELMET, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_forlorn_helmet,
            ConfigConstructor.is_fireproof_forlorn_set
    );

    public static final Item FORLORN_CHESTPLATE = ItemRegistry.registerArmorItem(
            "forlorn_chestplate",
            settings -> new ForlornArmor(MaterialRegistry.FORLORN_ARMOR, EquipmentType.CHESTPLATE, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_forlorn_chestplate,
            ConfigConstructor.is_fireproof_forlorn_set
    );

    public static final Item FORLORN_LEGGINGS = ItemRegistry.registerArmorItem(
            "forlorn_leggings",
            settings -> new ForlornArmor(MaterialRegistry.FORLORN_ARMOR, EquipmentType.LEGGINGS, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_forlorn_leggings,
            ConfigConstructor.is_fireproof_forlorn_set
    );

    public static final Item FORLORN_BOOTS = ItemRegistry.registerArmorItem(
            "forlorn_boots",
            settings -> new ForlornArmor(MaterialRegistry.FORLORN_ARMOR, EquipmentType.BOOTS, settings),
            UnaryOperator.identity(),
            ConfigConstructor.disable_recipe_forlorn_boots,
            ConfigConstructor.is_fireproof_forlorn_set
    );

    public static void init() {}
}