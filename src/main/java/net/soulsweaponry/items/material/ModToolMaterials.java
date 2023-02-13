package net.soulsweaponry.items.material;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.soulsweaponry.registry.ItemRegistry;

public enum ModToolMaterials implements ToolMaterial {
    
    IRON_BLOCK(1537, 8.0f, -1.0f, MiningLevels.IRON, 16, Ingredient.ofItems(Items.IRON_BLOCK)),
    LOST_SOUL(328, 7.0f, -1.0f, MiningLevels.IRON, 20, Ingredient.ofItems(ItemRegistry.LOST_SOUL, ItemRegistry.SOUL_INGOT)),
    MOONSTONE_OR_VERGLAS(1756, 10.0f, -1.0f, MiningLevels.DIAMOND, 10, Ingredient.ofItems(ItemRegistry.MOONSTONE, ItemRegistry.VERGLAS)),
    CRIMSON_INGOT(1984, 10.0f, -1.0f, MiningLevels.DIAMOND, 8, Ingredient.ofItems(ItemRegistry.CRIMSON_INGOT)),
    MOONSTONE_TOOL(1721, 8.5f, 3.0f, MiningLevels.NETHERITE, 12, Ingredient.ofItems(ItemRegistry.MOONSTONE, ItemRegistry.VERGLAS));

    private final int durability;
    private final float miningSpeedMultiplier;
    private final float attackDamage;
    private final int miningLevel;
    private final int enchantability;
    private final Ingredient repairIngredient;

    private ModToolMaterials(int durability, float miningSpeedMultiplier, float attackDamage, int miningLevel, int enchantability, Ingredient repairIngredient) {
        this.durability = durability;
        this.miningSpeedMultiplier = miningSpeedMultiplier;
        this.attackDamage = attackDamage;
        this.miningLevel = miningLevel;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeedMultiplier;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }
}
