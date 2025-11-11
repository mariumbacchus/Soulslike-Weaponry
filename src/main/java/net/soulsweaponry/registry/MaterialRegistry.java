package net.soulsweaponry.registry;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class MaterialRegistry {

    public static final RegistryEntry<ArmorMaterial> CHAOS_ARMOR = registerArmorMaterial("chaos_armor", () -> new ArmorMaterial(
            createMapWithArray(ConfigConstructor.chaos_armor_armor_points),
            15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, () -> Ingredient.ofItems(ItemRegistry.MOONSTONE, Items.NETHERITE_INGOT),
            List.of(new ArmorMaterial.Layer(Identifier.of(SoulsWeaponry.ModId, "chaos_armor"))), 4.0f, 0.2f));
    public static final RegistryEntry<ArmorMaterial> ENHANCED_CHAOS_ARMOR = registerArmorMaterial("enhanced_chaos_armor", () -> new ArmorMaterial(
            createMapWithArray(ConfigConstructor.enhanced_chaos_armor_armor_points),
            15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, () -> Ingredient.ofItems(ItemRegistry.MOONSTONE, Items.NETHERITE_INGOT),
            List.of(new ArmorMaterial.Layer(Identifier.of(SoulsWeaponry.ModId, "enhanced_chaos_armor"))), 4.0f, 0.2f));

    public static final RegistryEntry<ArmorMaterial> WITHERED_ARMOR = registerArmorMaterial("withered_armor", () -> new ArmorMaterial(
            createMapWithArray(ConfigConstructor.withered_armor_armor_points),
            15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, () -> Ingredient.ofItems(ItemRegistry.CRIMSON_INGOT, Items.NETHERITE_INGOT),
            List.of(new ArmorMaterial.Layer(Identifier.of(SoulsWeaponry.ModId, "withered_armor"))), 4.0f, 0.2f));
    public static final RegistryEntry<ArmorMaterial> ENHANCED_WITHERED_ARMOR = registerArmorMaterial("enhanced_withered_armor", () -> new ArmorMaterial(
            createMapWithArray(ConfigConstructor.enhanced_withered_armor_armor_points),
            15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, () -> Ingredient.ofItems(ItemRegistry.CRIMSON_INGOT, Items.NETHERITE_INGOT),
            List.of(new ArmorMaterial.Layer(Identifier.of(SoulsWeaponry.ModId, "enhanced_withered_armor"))), 4.0f, 0.2f));

    public static final RegistryEntry<ArmorMaterial> CHAOS_SET = registerArmorMaterial("chaos_set", () -> new ArmorMaterial(
            createMapWithArray(ConfigConstructor.chaos_set_armor_points),
            10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, () -> Ingredient.ofItems(ItemRegistry.MOONSTONE),
            List.of(new ArmorMaterial.Layer(Identifier.of(SoulsWeaponry.ModId, "chaos_set"))), 0, 0));
    public static final RegistryEntry<ArmorMaterial> SOUL_INGOT = registerArmorMaterial("soul_ingot", () -> new ArmorMaterial(
            createMapWithArray(ConfigConstructor.soul_ingot_armor_points),
            10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> Ingredient.ofItems(ItemRegistry.SOUL_INGOT),
            List.of(new ArmorMaterial.Layer(Identifier.of(SoulsWeaponry.ModId, "soul_ingot"))), 0, 0));
    public static final RegistryEntry<ArmorMaterial> SOUL_ROBES = registerArmorMaterial("soul_robes", () -> new ArmorMaterial(
            createMapWithArray(ConfigConstructor.soul_robes_armor_points),
            30, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, () -> Ingredient.ofItems(ItemRegistry.SOUL_INGOT),
            List.of(new ArmorMaterial.Layer(Identifier.of(SoulsWeaponry.ModId, "soul_robes"))), 0, 0));
    public static final RegistryEntry<ArmorMaterial> FORLORN_ARMOR = registerArmorMaterial("forlorn", () -> new ArmorMaterial(
            createMapWithArray(ConfigConstructor.forlorn_armor_armor_points),
            8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> Ingredient.ofItems(ItemRegistry.SOUL_INGOT),
            List.of(new ArmorMaterial.Layer(Identifier.of(SoulsWeaponry.ModId, "forlorn"))), 0, 0));

    public static RegistryEntry<ArmorMaterial> registerArmorMaterial(String name, Supplier<ArmorMaterial> material) {
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(SoulsWeaponry.ModId, name), material.get());
    }

    /**
     * Used to apply armor values directly based on the array from the config
     */
    private static EnumMap<ArmorItem.Type, Integer> createMapWithArray(float[] array) {
        if (array.length != 4) {
            throw new IllegalArgumentException("Armor points array needs to have 4 elements!");
        }
        return Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, (int) array[0]);
            map.put(ArmorItem.Type.LEGGINGS, (int) array[1]);
            map.put(ArmorItem.Type.CHESTPLATE, (int) array[2]);
            map.put(ArmorItem.Type.HELMET, (int) array[3]);
            map.put(ArmorItem.Type.BODY, (int) array[2]);
            // Note: Type.BODY is for non-humanoid mobs, like horses & wolves, change this
            // hardcoded line in the future if needed.
        });
    }
}
