package net.soulsweaponry.items.material;

import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial {
    
    IRON_BLOCK(ModTags.Blocks.INCORRECT_FOR_IRON_BLOCK_TOOL, 1537, 8.0f, -1.0f, 16, () -> Ingredient.ofItems(Items.IRON_BLOCK)),
    LOST_SOUL(ModTags.Blocks.INCORRECT_FOR_LOST_SOUL_TOOL, 328, 7.0f, -1.0f, 20, () -> Ingredient.fromTag(ModTags.Items.LOST_SOUL_REPAIR)),
    LOST_SOUL_DURABLE(ModTags.Blocks.INCORRECT_FOR_LOST_SOUL_DURABLE_TOOL, 1537, 7.0f, -1.0f, 14, () -> Ingredient.fromTag(ModTags.Items.LOST_SOUL_REPAIR)),
    MOONSTONE_OR_VERGLAS(ModTags.Blocks.INCORRECT_FOR_MOONSTONE_OR_VERGLAS_TOOL, 1756, 10.0f, -1.0f, 10, () -> Ingredient.ofItems(ItemRegistry.MOONSTONE, ItemRegistry.VERGLAS)),
    CRIMSON_INGOT(ModTags.Blocks.INCORRECT_CRIMSON_INGOT_TOOL, 1984, 10.0f, -1.0f, 8, () -> Ingredient.ofItems(ItemRegistry.CRIMSON_INGOT)),
    MOONSTONE_TOOL(ModTags.Blocks.INCORRECT_FOR_MOONSTONE_TOOL, 1721, 8.5f, 3.0f, 12, () -> Ingredient.ofItems(ItemRegistry.MOONSTONE, ItemRegistry.VERGLAS)),
    ECHO_SHARD(ModTags.Blocks.INCORRECT_FOR_ECHO_SHARD_TOOL, 2548, 10.0f, -1.0f, 10, () -> Ingredient.ofItems(Items.ECHO_SHARD));

    private final TagKey<Block> inverseTag;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterials(final TagKey<Block> inverseTag, final int itemDurability, final float miningSpeed,
                     final float attackDamage, final int enchantability, final Supplier<Ingredient> repairIngredient) {
        this.inverseTag = inverseTag;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
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
        return this.repairIngredient.get();
    }
}
