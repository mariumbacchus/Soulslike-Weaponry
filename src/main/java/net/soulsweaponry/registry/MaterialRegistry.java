package net.soulsweaponry.registry;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.ModTags;

import java.util.EnumMap;

public class MaterialRegistry {

    public static final ArmorMaterial CHAOS_ARMOR = new ArmorMaterial(
            55,
            createMapWithArray(ConfigConstructor.chaos_armor_armor_points),
            15,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            4.0f,
            0.2f,
            ModTags.Items.REPAIRS_CHAOS_ARMOR,
            Identifier.of(SoulsWeaponry.ModId, "chaos_armor")
    );

    public static final ArmorMaterial ENHANCED_CHAOS_ARMOR = new ArmorMaterial(
            70,
            createMapWithArray(ConfigConstructor.enhanced_chaos_armor_armor_points),
            15,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            4.0f,
            0.2f,
            ModTags.Items.REPAIRS_ENHANCED_CHAOS_ARMOR,
            Identifier.of(SoulsWeaponry.ModId, "enhanced_chaos_armor")
    );

    public static final ArmorMaterial WITHERED_ARMOR = new ArmorMaterial(
            55,
            createMapWithArray(ConfigConstructor.withered_armor_armor_points),
            15,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            4.0f,
            0.2f,
            ModTags.Items.REPAIRS_WITHERED_ARMOR,
            Identifier.of(SoulsWeaponry.ModId, "withered_armor")
    );

    public static final ArmorMaterial ENHANCED_WITHERED_ARMOR = new ArmorMaterial(
            70,
            createMapWithArray(ConfigConstructor.enhanced_withered_armor_armor_points),
            15,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            4.0f,
            0.2f,
            ModTags.Items.REPAIRS_ENHANCED_WITHERED_ARMOR,
            Identifier.of(SoulsWeaponry.ModId, "enhanced_withered_armor")
    );

    public static final ArmorMaterial CHAOS_SET = new ArmorMaterial(
            37,
            createMapWithArray(ConfigConstructor.chaos_set_armor_points),
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            0.0f,
            0.0f,
            ModTags.Items.REPAIRS_CHAOS_SET,
            Identifier.of(SoulsWeaponry.ModId, "chaos_set")
    );

    public static final ArmorMaterial SOUL_INGOT = new ArmorMaterial(
            34,
            createMapWithArray(ConfigConstructor.soul_ingot_armor_points),
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0f,
            0.0f,
            ModTags.Items.REPAIRS_SOUL_INGOT,
            Identifier.of(SoulsWeaponry.ModId, "soul_ingot")
    );

    public static final ArmorMaterial SOUL_ROBES = new ArmorMaterial(
            16,
            createMapWithArray(ConfigConstructor.soul_robes_armor_points),
            30,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            0.0f,
            0.0f,
            ModTags.Items.REPAIRS_SOUL_ROBES,
            Identifier.of(SoulsWeaponry.ModId, "soul_robes")
    );

    public static final ArmorMaterial FORLORN_ARMOR = new ArmorMaterial(
            30,
            createMapWithArray(ConfigConstructor.forlorn_armor_armor_points),
            8,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0f,
            0.0f,
            ModTags.Items.REPAIRS_FORLORN,
            Identifier.of(SoulsWeaponry.ModId, "forlorn")
    );

    /**
     * Used to apply armor values directly based on the array from the config
     */
    private static EnumMap<EquipmentType, Integer> createMapWithArray(float[] array) {
        if (array.length != 4) {
            throw new IllegalArgumentException("Armor points array needs to have 4 elements!");
        }
        return Util.make(new EnumMap<>(EquipmentType.class), map -> {
            map.put(EquipmentType.BOOTS, (int) array[0]);
            map.put(EquipmentType.LEGGINGS, (int) array[1]);
            map.put(EquipmentType.CHESTPLATE, (int) array[2]);
            map.put(EquipmentType.HELMET, (int) array[3]);
            map.put(EquipmentType.BODY, (int) array[2]);
            // Note: Type.BODY is for non-humanoid mobs, like horses & wolves, change this
            // hardcoded line in the future if needed.
        });
    }
}