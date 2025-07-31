package net.soulsweaponry.registry;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.armor.*;

public class ArmorRegistry {

    public static final Item CHAOS_CROWN = new ChaosCrown(MaterialRegistry.CHAOS_SET, ArmorItem.Type.HELMET, new Item.Settings().rarity(Rarity.EPIC).maxDamage(ArmorItem.Type.HELMET.getMaxDamage(37)));
    public static final Item CHAOS_HELMET = new ChaosHelmet(MaterialRegistry.CHAOS_ARMOR, ArmorItem.Type.HELMET, new Item.Settings().rarity(Rarity.EPIC).maxDamage(ArmorItem.Type.HELMET.getMaxDamage(55)));
    public static final Item ARKENPLATE = new Arkenplate(MaterialRegistry.CHAOS_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC).maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(55)));
    public static final Item ENHANCED_ARKENPLATE = new EnhancedArkenplate(MaterialRegistry.CHAOS_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC).maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(70)));
    public static final Item WITHERED_CHEST = new Hallowheart(MaterialRegistry.WITHERED_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC).maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(55)));
    public static final Item ENHANCED_WITHERED_CHEST = new EnhancedHallowheart(MaterialRegistry.WITHERED_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC).maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(70)));
    public static final Item CHAOS_ROBES = new ChaosRobes(MaterialRegistry.CHAOS_SET, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC).maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(37)));

    public static final Item SOUL_INGOT_HELMET = new SoulIngotArmor(MaterialRegistry.SOUL_INGOT, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(34)));
    public static final Item SOUL_INGOT_CHESTPLATE = new SoulIngotArmor(MaterialRegistry.SOUL_INGOT, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(34)));
    public static final Item SOUL_INGOT_LEGGINGS = new SoulIngotArmor(MaterialRegistry.SOUL_INGOT, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(34)));
    public static final Item SOUL_INGOT_BOOTS = new SoulIngotArmor(MaterialRegistry.SOUL_INGOT, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(34)));
    public static final Item SOUL_ROBES_HELMET = new SoulRobesArmor(MaterialRegistry.SOUL_ROBES, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(16)));
    public static final Item SOUL_ROBES_CHESTPLATE = new SoulRobesArmor(MaterialRegistry.SOUL_ROBES, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(16)));
    public static final Item SOUL_ROBES_LEGGINGS = new SoulRobesArmor(MaterialRegistry.SOUL_ROBES, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(16)));
    public static final Item SOUL_ROBES_BOOTS = new SoulRobesArmor(MaterialRegistry.SOUL_ROBES, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(16)));
    public static final Item FORLORN_HELMET = new ForlornArmor(MaterialRegistry.FORLORN_ARMOR, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(30)));
    public static final Item FORLORN_CHESTPLATE = new ForlornArmor(MaterialRegistry.FORLORN_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(30)));
    public static final Item FORLORN_LEGGINGS = new ForlornArmor(MaterialRegistry.FORLORN_ARMOR, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(30)));
    public static final Item FORLORN_BOOTS = new ForlornArmor(MaterialRegistry.FORLORN_ARMOR, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(30)));

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
