package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.soulsweaponry.items.armor.ForlornArmor;
import net.soulsweaponry.items.armor.SoulIngotArmor;
import net.soulsweaponry.items.armor.SoulRobesArmor;
import net.soulsweaponry.items.material.ModArmorMaterials;

public class ArmorRegistry {

    public static final Item SOUL_INGOT_HELMET = new SoulIngotArmor(ModArmorMaterials.SOUL_INGOT, ArmorItem.Type.HELMET, new FabricItemSettings());
    public static final Item SOUL_INGOT_CHESTPLATE = new SoulIngotArmor(ModArmorMaterials.SOUL_INGOT, ArmorItem.Type.CHESTPLATE, new FabricItemSettings());
    public static final Item SOUL_INGOT_LEGGINGS = new SoulIngotArmor(ModArmorMaterials.SOUL_INGOT, ArmorItem.Type.LEGGINGS, new FabricItemSettings());
    public static final Item SOUL_INGOT_BOOTS = new SoulIngotArmor(ModArmorMaterials.SOUL_INGOT, ArmorItem.Type.BOOTS, new FabricItemSettings());
    public static final Item SOUL_ROBES_HELMET = new SoulRobesArmor(ModArmorMaterials.SOUL_ROBES, ArmorItem.Type.HELMET, new FabricItemSettings());
    public static final Item SOUL_ROBES_CHESTPLATE = new SoulRobesArmor(ModArmorMaterials.SOUL_ROBES, ArmorItem.Type.CHESTPLATE, new FabricItemSettings());
    public static final Item SOUL_ROBES_LEGGINGS = new SoulRobesArmor(ModArmorMaterials.SOUL_ROBES, ArmorItem.Type.LEGGINGS, new FabricItemSettings());
    public static final Item SOUL_ROBES_BOOTS = new SoulRobesArmor(ModArmorMaterials.SOUL_ROBES, ArmorItem.Type.BOOTS, new FabricItemSettings());
    public static final Item FORLORN_HELMET = new ForlornArmor(ModArmorMaterials.FORLORN_ARMOR, ArmorItem.Type.HELMET, new FabricItemSettings());
    public static final Item FORLORN_CHESTPLATE = new ForlornArmor(ModArmorMaterials.FORLORN_ARMOR, ArmorItem.Type.CHESTPLATE, new FabricItemSettings());
    public static final Item FORLORN_LEGGINGS = new ForlornArmor(ModArmorMaterials.FORLORN_ARMOR, ArmorItem.Type.LEGGINGS, new FabricItemSettings());
    public static final Item FORLORN_BOOTS = new ForlornArmor(ModArmorMaterials.FORLORN_ARMOR, ArmorItem.Type.BOOTS, new FabricItemSettings());

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
