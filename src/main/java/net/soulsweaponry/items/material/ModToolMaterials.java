package net.soulsweaponry.items.material;

import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.BlockTags;
import net.minecraftforge.common.ForgeTier;
import net.soulsweaponry.registry.ItemRegistry;

public class ModToolMaterials {

    public static final ForgeTier IRON_BLOCK = new ForgeTier(2, 1537, 8f, -1f, 16, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.ofItems(Items.IRON_BLOCK));
    public static final ForgeTier LOST_SOUL = new ForgeTier(2, 328, 7f, -1f, 20, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.ofItems(ItemRegistry.LOST_SOUL.get(), ItemRegistry.SOUL_INGOT.get()));
    public static final ForgeTier MOONSTONE_OR_VERGLAS = new ForgeTier(3, 1756, 10f, -1f, 10, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.ofItems(ItemRegistry.MOONSTONE.get(), ItemRegistry.VERGLAS.get()));
    public static final ForgeTier CRIMSON_INGOT = new ForgeTier(3, 1984, 10f, -1f, 8, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.ofItems(ItemRegistry.CRIMSON_INGOT.get()));
    public static final ForgeTier MOONSTONE_TOOL = new ForgeTier(4, 1721, 8.5f, -1f, 12, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.ofItems(ItemRegistry.MOONSTONE.get(), ItemRegistry.VERGLAS.get()));
}
