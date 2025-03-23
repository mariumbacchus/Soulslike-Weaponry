package net.soulsweaponry.items.material;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.EnumMap;
import java.util.List;

public class ModArmorMaterials {
    //Boots are on the left
    public static final RegistryEntry<ArmorMaterial> CHAOS_ARMOR = register("chaos_armor", /*new int[]{500, 600, 700, 500},*/ ConfigConstructor.chaos_armor_armor_points, 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 4.0f, 0.2f, Ingredient.ofItems(ItemRegistry.MOONSTONE, Items.NETHERITE_INGOT));
    public static final RegistryEntry<ArmorMaterial> CHAOS_SET = register("chaos_set", /*new int[]{400, 500, 600, 400},*/ ConfigConstructor.chaos_set_armor_points, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0f, 0f, Ingredient.ofItems(ItemRegistry.MOONSTONE));
    public static final RegistryEntry<ArmorMaterial> SOUL_INGOT = register("soul_ingot", /*new int[]{350, 410, 480, 380},*/ ConfigConstructor.soul_ingot_armor_points, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0f, 0f, Ingredient.ofItems(ItemRegistry.SOUL_INGOT));
    public static final RegistryEntry<ArmorMaterial> SOUL_ROBES = register("soul_robes", /*new int[]{150, 210, 280, 180},*/ ConfigConstructor.soul_robes_armor_points, 30, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0f, 0f, Ingredient.ofItems(ItemRegistry.SOUL_INGOT));
    public static final RegistryEntry<ArmorMaterial> FORLORN_ARMOR = register("forlorn", /*new int[]{380, 440, 510, 320},*/ ConfigConstructor.forlorn_armor_armor_points, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0f, 0f, Ingredient.ofItems(ItemRegistry.SOUL_INGOT));
    public static final RegistryEntry<ArmorMaterial> WITHERED_ARMOR = register("withered_armor", /*new int[]{500, 600, 700, 500},*/ ConfigConstructor.withered_armor_armor_points, 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 4.0f, 0.2f, Ingredient.ofItems(ItemRegistry.CRIMSON_INGOT, Items.NETHERITE_INGOT));

    private static RegistryEntry.Reference<ArmorMaterial> register(String name, /*int[] baseDurability,*/ int[] protectionAmounts, int enchantability, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, Ingredient repairIngredientSupplier) {
        EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);

        // "Boots are on the left" :(
        enumMap.put(ArmorItem.Type.values()[0], protectionAmounts[3]);
        enumMap.put(ArmorItem.Type.values()[1], protectionAmounts[2]);
        enumMap.put(ArmorItem.Type.values()[2], protectionAmounts[1]);
        enumMap.put(ArmorItem.Type.values()[3], protectionAmounts[0]);

        Identifier id = Identifier.of(SoulsWeaponry.ModId, name);

        return Registry.registerReference(
                Registries.ARMOR_MATERIAL,
                id,
                new ArmorMaterial(enumMap, enchantability, equipSound, () -> repairIngredientSupplier, List.of(new ArmorMaterial.Layer(id)), toughness, knockbackResistance)
        );
    }
}
