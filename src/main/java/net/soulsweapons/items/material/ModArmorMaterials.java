package net.soulsweapons.items.material;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.soulsweapons.registry.ItemRegistry;

public enum ModArmorMaterials implements ArmorMaterial {
    //Boots are on the left, TODO: armor values are fillers
    CHAOS_ARMOR("chaos_armor", new int[]{500, 600, 700, 500}, new int[]{1, 1, 1, 1} /*ConfigConstructor.chaos_armor_armor_points*/, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, 0.1f, Ingredient.of(ItemRegistry.MOONSTONE.get(), Items.NETHERITE_INGOT)),
    CHAOS_SET("chaos_set", new int[]{400, 500, 600, 400}, new int[]{1, 1, 1, 1}/*ConfigConstructor.chaos_set_armor_points*/, 10, SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f, Ingredient.of(ItemRegistry.MOONSTONE.get())),
    SOUL_INGOT("soul_ingot", new int[]{350, 410, 480, 380}, new int[]{1, 1, 1, 1}/*ConfigConstructor.soul_ingot_armor_points*/, 10, SoundEvents.ARMOR_EQUIP_IRON, 0f, 0f, Ingredient.of(ItemRegistry.SOUL_INGOT.get())),
    SOUL_ROBES("soul_robes", new int[]{150, 210, 280, 180}, new int[]{1, 1, 1, 1}/*ConfigConstructor.soul_robes_armor_points*/, 30, SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f, Ingredient.of(ItemRegistry.SOUL_INGOT.get())),
    FORLORN_ARMOR("forlorn", new int[]{380, 440, 510, 320}, new int[]{1, 1, 1, 1}/*ConfigConstructor.forlorn_armor_armor_points*/, 8, SoundEvents.ARMOR_EQUIP_IRON, 0f, 0f, Ingredient.of(ItemRegistry.SOUL_INGOT.get()));

    private final String name;
    private final int[] baseDurability;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Ingredient repairIngredientSupplier;

    ModArmorMaterials(String name, int[] baseDurability, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Ingredient repairIngredientSupplier) {
        this.name = name;
        this.baseDurability = baseDurability;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = repairIngredientSupplier;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slot) {
        return this.baseDurability[slot.getIndex()];
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slot) {
        return this.protectionAmounts[slot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredientSupplier;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
