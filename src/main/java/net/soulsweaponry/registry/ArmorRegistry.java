package net.soulsweaponry.registry;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.armor.ForlornArmor;
import net.soulsweaponry.items.armor.SoulIngotArmor;
import net.soulsweaponry.items.armor.SoulRobesArmor;
import net.soulsweaponry.items.material.ModArmorMaterials;

import static net.soulsweaponry.SoulsWeaponry.MAIN_GROUP;

public class ArmorRegistry {

    public static RegistryObject<Item> SOUL_INGOT_HELMET;
    public static RegistryObject<Item> SOUL_INGOT_CHESTPLATE;
    public static RegistryObject<Item> SOUL_INGOT_LEGGINGS;
    public static RegistryObject<Item> SOUL_INGOT_BOOTS;
    public static RegistryObject<Item> SOUL_ROBES_HELMET;
    public static RegistryObject<Item> SOUL_ROBES_CHESTPLATE;
    public static RegistryObject<Item> SOUL_ROBES_LEGGINGS;
    public static RegistryObject<Item> SOUL_ROBES_BOOTS;
    public static RegistryObject<Item> FORLORN_HELMET;
    public static RegistryObject<Item> FORLORN_CHESTPLATE;
    public static RegistryObject<Item> FORLORN_LEGGINGS;
    public static RegistryObject<Item> FORLORN_BOOTS;

    public static void register() {
        SOUL_INGOT_HELMET = ItemRegistry.registerArmorItem("soul_ingot_helmet", () -> new SoulIngotArmor(ModArmorMaterials.SOUL_INGOT, EquipmentSlot.HEAD, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_soul_ingot_helmet);
        SOUL_INGOT_CHESTPLATE = ItemRegistry.registerArmorItem("soul_ingot_chestplate", () -> new SoulIngotArmor(ModArmorMaterials.SOUL_INGOT, EquipmentSlot.CHEST, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_soul_ingot_chestplate);
        SOUL_INGOT_LEGGINGS = ItemRegistry.registerArmorItem("soul_ingot_leggings", () -> new SoulIngotArmor(ModArmorMaterials.SOUL_INGOT, EquipmentSlot.LEGS, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_soul_ingot_leggings);
        SOUL_INGOT_BOOTS = ItemRegistry.registerArmorItem("soul_ingot_boots", () -> new SoulIngotArmor(ModArmorMaterials.SOUL_INGOT, EquipmentSlot.FEET, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_soul_ingot_boots);
        SOUL_ROBES_HELMET = ItemRegistry.registerArmorItem("soul_robes_helmet", () -> new SoulRobesArmor(ModArmorMaterials.SOUL_ROBES, EquipmentSlot.HEAD, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_soul_robes_helmet);
        SOUL_ROBES_CHESTPLATE = ItemRegistry.registerArmorItem("soul_robes_chestplate", () -> new SoulRobesArmor(ModArmorMaterials.SOUL_ROBES, EquipmentSlot.CHEST, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_soul_robes_chestplate);
        SOUL_ROBES_LEGGINGS = ItemRegistry.registerArmorItem("soul_robes_leggings", () -> new SoulRobesArmor(ModArmorMaterials.SOUL_ROBES, EquipmentSlot.LEGS, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_soul_robes_leggings);
        SOUL_ROBES_BOOTS = ItemRegistry.registerArmorItem("soul_robes_boots", () -> new SoulRobesArmor(ModArmorMaterials.SOUL_ROBES, EquipmentSlot.FEET, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_soul_robes_boots);
        FORLORN_HELMET = ItemRegistry.registerArmorItem("forlorn_helmet", () -> new ForlornArmor(ModArmorMaterials.FORLORN_ARMOR, EquipmentSlot.HEAD, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_forlorn_helmet);
        FORLORN_CHESTPLATE = ItemRegistry.registerArmorItem("forlorn_chestplate", () -> new ForlornArmor(ModArmorMaterials.FORLORN_ARMOR, EquipmentSlot.CHEST, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_forlorn_chestplate);
        FORLORN_LEGGINGS = ItemRegistry.registerArmorItem("forlorn_leggings", () -> new ForlornArmor(ModArmorMaterials.FORLORN_ARMOR, EquipmentSlot.LEGS, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_forlorn_leggings);
        FORLORN_BOOTS = ItemRegistry.registerArmorItem("forlorn_boots", () -> new ForlornArmor(ModArmorMaterials.FORLORN_ARMOR, EquipmentSlot.FEET, new Item.Settings().group(MAIN_GROUP)), ConfigConstructor.disable_recipe_forlorn_boots);
    }
}
