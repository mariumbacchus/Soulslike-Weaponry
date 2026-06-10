package net.soulsweaponry.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.RecipeSerializerRegistry;
import net.soulsweaponry.util.UpgradeUtil;

public record ItemUpgradeRecipe(Ingredient template, Ingredient base, Ingredient addition, float primaryBonus, float secondaryBonus, boolean fallback) implements SmithingRecipe {

    @Override
    public boolean testTemplate(ItemStack stack) {
        return this.template.test(stack);
    }

    @Override
    public boolean testBase(ItemStack stack) {
        return this.base.test(stack);
    }

    @Override
    public boolean testAddition(ItemStack stack) {
        return this.addition.test(stack);
    }

    @Override
    public boolean matches(SmithingRecipeInput input, World world) {
        if (!this.template.test(input.template()) || !this.base.test(input.base()) || !this.addition.test(input.addition())) {
            return false;
        }
        int level = input.base().getOrDefault(ComponentRegistry.ITEM_UPGRADE_LEVEL, 0);
        if (level >= (int) WeaponConfig.item_upgrading_max_level) {
            return false;
        }
        if (!this.fallback()) {
            return true;
        }
        // If the recipe is a fallback recipe, check if other recipes exist. If one does, use that one, if not then use the fallback recipe.
        RecipeManager manager = world.getRecipeManager();
        for (RecipeEntry<SmithingRecipe> entry : manager.listAllOfType(RecipeType.SMITHING)) {
            SmithingRecipe recipe = entry.value();
            if (recipe == this) {
                continue;
            }
            if (recipe instanceof ItemUpgradeRecipe other && !other.fallback()) {
                // Check if that specific recipe matches the same triple of items
                if (other.template().test(input.template())
                        && other.base().test(input.base())
                        && other.addition().test(input.addition())) {
                    // A specific, non-fallback upgrade exists for this input, this fallback recipe should NOT be used.
                    return false;
                }
            }
        }
        // No specific recipe found, fallback recipe is allowed
        return true;
    }

    @Override
    public ItemStack craft(SmithingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack out = input.base().copy();
        int prev = out.getOrDefault(ComponentRegistry.ITEM_UPGRADE_LEVEL, 0);
        int nextLevel = Math.min(prev + 1, (int) WeaponConfig.item_upgrading_max_level);
        applyUpgrades(out, nextLevel);
        return out;
    }

    public void applyUpgrades(ItemStack out, int nextLevel) {
        out.set(ComponentRegistry.ITEM_UPGRADE_LEVEL, nextLevel);
        UpgradeUtil.rebuildUpgradeAttributesForCurrentForm(out, nextLevel, this.primaryBonus(), this.secondaryBonus());
    }

    /**
     * Used for recipe book preview, smithing results depend on inputs,
     * so returning EMPTY is fine.
     */
    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        ItemStack[] matches = this.base.getMatchingStacks();
        return matches.length > 0 ? matches[0].copy() : ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.SMITHING_ITEM_UPGRADE;
    }

    public static class Serializer implements RecipeSerializer<ItemUpgradeRecipe> {
        private static final MapCodec<ItemUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("template").forGetter(recipe -> recipe.template),
                                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("base").forGetter(recipe -> recipe.base),
                                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("addition").forGetter(recipe -> recipe.addition),
                                Codec.FLOAT.fieldOf("primaryBonus").forGetter(ItemUpgradeRecipe::primaryBonus),
                                Codec.FLOAT.fieldOf("secondaryBonus").forGetter(ItemUpgradeRecipe::secondaryBonus),
                                Codec.BOOL.optionalFieldOf("fallback", false).forGetter(ItemUpgradeRecipe::fallback)
                        )
                        .apply(instance, ItemUpgradeRecipe::new)
        );
        private static final PacketCodec<RegistryByteBuf, ItemUpgradeRecipe> PACKET_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, ItemUpgradeRecipe::template,
                        Ingredient.PACKET_CODEC, ItemUpgradeRecipe::base,
                        Ingredient.PACKET_CODEC, ItemUpgradeRecipe::addition,
                        PacketCodecs.FLOAT, ItemUpgradeRecipe::primaryBonus,
                        PacketCodecs.FLOAT, ItemUpgradeRecipe::secondaryBonus,
                        PacketCodecs.BOOL, ItemUpgradeRecipe::fallback,
                        ItemUpgradeRecipe::new
                );

        @Override
        public MapCodec<ItemUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ItemUpgradeRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
