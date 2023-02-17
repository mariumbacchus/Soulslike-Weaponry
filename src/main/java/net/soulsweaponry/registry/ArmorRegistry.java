package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.SoulRobesItem;
import net.soulsweaponry.items.material.ModArmorMaterials;

public class ArmorRegistry {

    public static final ItemGroup MAIN_GROUP = SoulsWeaponry.MAIN_GROUP;

    public static final Item SOUL_INGOT_HELMET = new ArmorItem(ModArmorMaterials.SOUL_INGOT, EquipmentSlot.HEAD, new FabricItemSettings());
    public static final Item SOUL_INGOT_CHESTPLATE = new ArmorItem(ModArmorMaterials.SOUL_INGOT, EquipmentSlot.CHEST, new FabricItemSettings());
    public static final Item SOUL_INGOT_LEGGINGS = new ArmorItem(ModArmorMaterials.SOUL_INGOT, EquipmentSlot.LEGS, new FabricItemSettings());
    public static final Item SOUL_INGOT_BOOTS = new ArmorItem(ModArmorMaterials.SOUL_INGOT, EquipmentSlot.FEET, new FabricItemSettings());
    public static final Item SOUL_ROBES_HELMET = new SoulRobesItem(ModArmorMaterials.SOUL_ROBES, EquipmentSlot.HEAD, new FabricItemSettings());
    public static final Item SOUL_ROBES_CHESTPLATE = new SoulRobesItem(ModArmorMaterials.SOUL_ROBES, EquipmentSlot.CHEST, new FabricItemSettings());
    public static final Item SOUL_ROBES_LEGGINGS = new SoulRobesItem(ModArmorMaterials.SOUL_ROBES, EquipmentSlot.LEGS, new FabricItemSettings());
    public static final Item SOUL_ROBES_BOOTS = new SoulRobesItem(ModArmorMaterials.SOUL_ROBES, EquipmentSlot.FEET, new FabricItemSettings());
    public static final Item FORLORN_HELMET = new ArmorItem(ModArmorMaterials.FORLORN_ARMOR, EquipmentSlot.HEAD, new FabricItemSettings());
    public static final Item FORLORN_CHESTPLATE = new ArmorItem(ModArmorMaterials.FORLORN_ARMOR, EquipmentSlot.CHEST, new FabricItemSettings());
    public static final Item FORLORN_LEGGINGS = new ArmorItem(ModArmorMaterials.FORLORN_ARMOR, EquipmentSlot.LEGS, new FabricItemSettings());
    public static final Item FORLORN_BOOTS = new ArmorItem(ModArmorMaterials.FORLORN_ARMOR, EquipmentSlot.FEET, new FabricItemSettings());

    public static void init() {
        ItemRegistry.registerItem(SOUL_INGOT_HELMET, "soul_ingot_helmet");
        ItemRegistry.registerItem(SOUL_INGOT_CHESTPLATE, "soul_ingot_chestplate");
        ItemRegistry.registerItem(SOUL_INGOT_LEGGINGS, "soul_ingot_leggings");
        ItemRegistry.registerItem(SOUL_INGOT_BOOTS, "soul_ingot_boots");
        ItemRegistry.registerItem(SOUL_ROBES_HELMET, "soul_robes_helmet");
        ItemRegistry.registerItem(SOUL_ROBES_CHESTPLATE, "soul_robes_chestplate");
        ItemRegistry.registerItem(SOUL_ROBES_LEGGINGS, "soul_robes_leggings");
        ItemRegistry.registerItem(SOUL_ROBES_BOOTS, "soul_robes_boots");
        ItemRegistry.registerItem(FORLORN_HELMET, "forlorn_helmet");
        ItemRegistry.registerItem(FORLORN_CHESTPLATE, "forlorn_chestplate");
        ItemRegistry.registerItem(FORLORN_LEGGINGS, "forlorn_leggings");
        ItemRegistry.registerItem(FORLORN_BOOTS, "forlorn_boots");
    }
}
