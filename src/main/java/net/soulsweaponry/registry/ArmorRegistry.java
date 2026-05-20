package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.armor.*;
import net.soulsweaponry.items.material.ModArmorMaterials;

public class ArmorRegistry {

    public static final Item CHAOS_CROWN = new ChaosCrown(ModArmorMaterials.CHAOS_SET, ArmorItem.Type.HELMET, new FabricItemSettings().rarity(Rarity.EPIC));
    public static final Item CHAOS_HELMET = new ChaosHelmet(ModArmorMaterials.CHAOS_ARMOR, ArmorItem.Type.HELMET, new FabricItemSettings().rarity(Rarity.EPIC));
    public static final Item ARKENPLATE = new Arkenplate(ModArmorMaterials.CHAOS_ARMOR, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().rarity(Rarity.EPIC));
    public static final Item ENHANCED_ARKENPLATE = new EnhancedArkenplate(ModArmorMaterials.ENHANCED_CHAOS_ARMOR, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().rarity(Rarity.EPIC));
    public static final Item WITHERED_CHEST = new Hallowheart(ModArmorMaterials.WITHERED_ARMOR, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().rarity(Rarity.EPIC));
    public static final Item ENHANCED_WITHERED_CHEST = new EnhancedHallowheart(ModArmorMaterials.ENHANCED_WITHERED_ARMOR, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().rarity(Rarity.EPIC));
    public static final Item CHAOS_ROBES = new ChaosRobes(ModArmorMaterials.CHAOS_SET, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().rarity(Rarity.EPIC));

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
        ItemRegistry.registerItem(CHAOS_CROWN, "chaos_crown", ConfigConstructor.is_fireproof_chaos_crown);
        ItemRegistry.registerArmorItem(CHAOS_HELMET, "chaos_helmet", ConfigConstructor.disable_recipe_chaos_helmet, ConfigConstructor.is_fireproof_chaos_helmet);
        ItemRegistry.registerArmorItem(ARKENPLATE, "arkenplate", ConfigConstructor.disable_recipe_arkenplate, ConfigConstructor.is_fireproof_arkenplate);
        ItemRegistry.registerArmorItem(ENHANCED_ARKENPLATE, "enhanced_arkenplate", ConfigConstructor.disable_recipe_enhanced_arkenplate, ConfigConstructor.is_fireproof_arkenplate);
        ItemRegistry.registerArmorItem(WITHERED_CHEST, "withered_chest", ConfigConstructor.disable_recipe_withered_chest, ConfigConstructor.is_fireproof_hallowheart);
        ItemRegistry.registerArmorItem(ENHANCED_WITHERED_CHEST, "enhanced_withered_chest", ConfigConstructor.disable_recipe_enhanced_withered_chest, ConfigConstructor.is_fireproof_hallowheart);
        ItemRegistry.registerItem(CHAOS_ROBES, "chaos_robes", ConfigConstructor.is_fireproof_chaos_robes);

        ItemRegistry.registerArmorItem(SOUL_INGOT_HELMET, "soul_ingot_helmet", ConfigConstructor.disable_recipe_soul_ingot_helmet, ConfigConstructor.is_fireproof_soul_ingot_set);
        ItemRegistry.registerArmorItem(SOUL_INGOT_CHESTPLATE, "soul_ingot_chestplate", ConfigConstructor.disable_recipe_soul_ingot_chestplate, ConfigConstructor.is_fireproof_soul_ingot_set);
        ItemRegistry.registerArmorItem(SOUL_INGOT_LEGGINGS, "soul_ingot_leggings", ConfigConstructor.disable_recipe_soul_ingot_leggings, ConfigConstructor.is_fireproof_soul_ingot_set);
        ItemRegistry.registerArmorItem(SOUL_INGOT_BOOTS, "soul_ingot_boots", ConfigConstructor.disable_recipe_soul_ingot_boots, ConfigConstructor.is_fireproof_soul_ingot_set);
        ItemRegistry.registerArmorItem(SOUL_ROBES_HELMET, "soul_robes_helmet", ConfigConstructor.disable_recipe_soul_robes_helmet, ConfigConstructor.is_fireproof_soul_robes_set);
        ItemRegistry.registerArmorItem(SOUL_ROBES_CHESTPLATE, "soul_robes_chestplate", ConfigConstructor.disable_recipe_soul_robes_chestplate, ConfigConstructor.is_fireproof_soul_robes_set);
        ItemRegistry.registerArmorItem(SOUL_ROBES_LEGGINGS, "soul_robes_leggings", ConfigConstructor.disable_recipe_soul_robes_leggings, ConfigConstructor.is_fireproof_soul_robes_set);
        ItemRegistry.registerArmorItem(SOUL_ROBES_BOOTS, "soul_robes_boots", ConfigConstructor.disable_recipe_soul_robes_boots, ConfigConstructor.is_fireproof_soul_robes_set);
        ItemRegistry.registerArmorItem(FORLORN_HELMET, "forlorn_helmet", ConfigConstructor.disable_recipe_forlorn_helmet, ConfigConstructor.is_fireproof_forlorn_set);
        ItemRegistry.registerArmorItem(FORLORN_CHESTPLATE, "forlorn_chestplate", ConfigConstructor.disable_recipe_forlorn_chestplate, ConfigConstructor.is_fireproof_forlorn_set);
        ItemRegistry.registerArmorItem(FORLORN_LEGGINGS, "forlorn_leggings", ConfigConstructor.disable_recipe_forlorn_leggings, ConfigConstructor.is_fireproof_forlorn_set);
        ItemRegistry.registerArmorItem(FORLORN_BOOTS, "forlorn_boots", ConfigConstructor.disable_recipe_forlorn_boots, ConfigConstructor.is_fireproof_forlorn_set);
    }
}