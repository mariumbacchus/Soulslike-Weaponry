package net.soulsweaponry.items.material;

import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.soulsweaponry.registry.ItemRegistry;

public enum ModToolMaterials implements ToolMaterial {
    IRON_BLOCK(1537, 8.0f, -1.0f, BlockTags.INCORRECT_FOR_IRON_TOOL, 16, Ingredient.ofItems(Items.IRON_BLOCK)),
    LOST_SOUL(328, 7.0f, -1.0f, BlockTags.INCORRECT_FOR_IRON_TOOL, 20, Ingredient.ofItems(ItemRegistry.LOST_SOUL, ItemRegistry.SOUL_INGOT)),
    MOONSTONE_OR_VERGLAS(1756, 10.0f, -1.0f, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 10, Ingredient.ofItems(ItemRegistry.MOONSTONE, ItemRegistry.VERGLAS)),
    CRIMSON_INGOT(1984, 10.0f, -1.0f, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 8, Ingredient.ofItems(ItemRegistry.CRIMSON_INGOT)),
    MOONSTONE_TOOL(1721, 8.5f, 3.0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 12, Ingredient.ofItems(ItemRegistry.MOONSTONE, ItemRegistry.VERGLAS));

    private final int durability;
    private final float miningSpeedMultiplier;
    private final float attackDamage;
    private final TagKey<Block> inverseTag;
    private final int enchantability;
    private final Ingredient repairIngredient;

    private ModToolMaterials(int durability, float miningSpeedMultiplier, float attackDamage, TagKey<Block> inverseTag, int enchantability, Ingredient repairIngredient) {
        this.durability = durability;
        this.miningSpeedMultiplier = miningSpeedMultiplier;
        this.attackDamage = attackDamage;
        this.inverseTag = inverseTag;
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
    public TagKey<Block> getInverseTag() {
        return this.inverseTag;
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
