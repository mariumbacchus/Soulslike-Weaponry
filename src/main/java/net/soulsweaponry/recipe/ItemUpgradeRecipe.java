package net.soulsweaponry.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SmithingRecipeDisplay;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.RecipeSerializerRegistry;
import net.soulsweaponry.util.UpgradeUtil;
import org.jetbrains.annotations.Nullable;

// Suppressing because SmithingTransformRecipe which this class is based off of uses optionals as input (despite being bad practice) so it should still work
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ItemUpgradeRecipe implements SmithingRecipe {

    final Optional<Ingredient> template;
    final Optional<Ingredient> base;
    final Optional<Ingredient> addition;
    final float primaryBonus;
    final float secondaryBonus;
    final boolean fallback;

    @Nullable
    private IngredientPlacement ingredientPlacement;

    public ItemUpgradeRecipe(Optional<Ingredient> template, Optional<Ingredient> base, Optional<Ingredient> addition, float primaryBonus, float secondaryBonus, boolean fallback) {
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.primaryBonus = primaryBonus;
        this.secondaryBonus = secondaryBonus;
        this.fallback = fallback;
    }

    public float primaryBonus() {
        return this.primaryBonus;
    }

    public float secondaryBonus() {
        return this.secondaryBonus;
    }

    public boolean fallback() {
        return this.fallback;
    }

    @Override
    public Optional<Ingredient> template() {
        return this.template;
    }

    @Override
    public Optional<Ingredient> base() {
        return this.base;
    }

    @Override
    public Optional<Ingredient> addition() {
        return this.addition;
    }

    @Override
    public boolean matches(SmithingRecipeInput input, World world) {
        if (!Ingredient.matches(this.template(), input.template())
                || !Ingredient.matches(this.base(), input.base())
                || !Ingredient.matches(this.addition(), input.addition())) {
            return false;
        }
        int level = input.base().getOrDefault(ComponentRegistry.ITEM_UPGRADE_LEVEL, 0);
        if (level >= (int) ConfigConstructor.item_upgrading_max_level) {
            return false;
        }
        if (!this.fallback()) {
            return true;
        }
        // If the recipe is a fallback recipe, check if other recipes exist. If one does, use that one, if not then use the fallback recipe.
        if (world.getServer() != null) {
            ServerRecipeManager manager = world.getServer().getRecipeManager();
            for (RecipeEntry<?> entry : manager.values()) {
                if (!(entry.value() instanceof SmithingRecipe recipe)) {
                    continue;
                }
                if (recipe == this) {
                    continue;
                }
                if (recipe instanceof ItemUpgradeRecipe other && !other.fallback()) {
                    // Check if that specific recipe matches the same triple of items
                    if (Ingredient.matches(other.template(), input.template())
                            && Ingredient.matches(other.base(), input.base())
                            && Ingredient.matches(other.addition(), input.addition())) {
                        // A specific, non-fallback upgrade exists for this input, this fallback recipe should NOT be used.
                        return false;
                    }
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
        int nextLevel = Math.min(prev + 1, (int) ConfigConstructor.item_upgrading_max_level);
        applyUpgrades(out, nextLevel);
        return out;
    }

    public void applyUpgrades(ItemStack out, int nextLevel) {
        out.set(ComponentRegistry.ITEM_UPGRADE_LEVEL, nextLevel);
        UpgradeUtil.rebuildUpgradeAttributesForCurrentForm(out, nextLevel, this.primaryBonus(), this.secondaryBonus());
    }

    @Override
    public RecipeSerializer<ItemUpgradeRecipe> getSerializer() {
        return RecipeSerializerRegistry.SMITHING_ITEM_UPGRADE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forMultipleSlots(List.of(this.template(), this.base(), this.addition()));
        }
        return this.ingredientPlacement;
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        ItemStack displayResult = firstPreviewStack(this.base);
        return List.of(
                new SmithingRecipeDisplay(
                        Ingredient.toDisplay(this.template()),
                        Ingredient.toDisplay(this.base()),
                        Ingredient.toDisplay(this.addition()),
                        new SlotDisplay.StackSlotDisplay(displayResult),
                        new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)
                )
        );
    }

    private ItemStack firstPreviewStack(Optional<Ingredient> ingredient) {
        if (ingredient.isEmpty()) {
            return new ItemStack(Items.BARRIER); // must be non-empty
        }
        var matches = ingredient.get().getMatchingItems();
        if (matches.isEmpty()) {
            return new ItemStack(Items.BARRIER);
        }
        ItemStack out = matches.getFirst().value().getDefaultStack().copy();
        int prev = out.getOrDefault(ComponentRegistry.ITEM_UPGRADE_LEVEL, 0);
        int next = Math.min(prev + 1, 5);
        this.applyUpgrades(out, next);
        return out;
    }

    public static class Serializer implements RecipeSerializer<ItemUpgradeRecipe> {
        private static final MapCodec<ItemUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Ingredient.CODEC.optionalFieldOf("template").forGetter(recipe -> recipe.template),
                                Ingredient.CODEC.optionalFieldOf("base").forGetter(recipe -> recipe.base),
                                Ingredient.CODEC.optionalFieldOf("addition").forGetter(recipe -> recipe.addition),
                                Codec.FLOAT.fieldOf("primaryBonus").forGetter(ItemUpgradeRecipe::primaryBonus),
                                Codec.FLOAT.fieldOf("secondaryBonus").forGetter(ItemUpgradeRecipe::secondaryBonus),
                                Codec.BOOL.optionalFieldOf("fallback", false).forGetter(ItemUpgradeRecipe::fallback)
                        )
                        .apply(instance, ItemUpgradeRecipe::new)
        );

        private static final PacketCodec<RegistryByteBuf, ItemUpgradeRecipe> PACKET_CODEC =
                PacketCodec.tuple(
                        Ingredient.OPTIONAL_PACKET_CODEC,
                        recipe -> recipe.template,
                        Ingredient.OPTIONAL_PACKET_CODEC,
                        recipe -> recipe.base,
                        Ingredient.OPTIONAL_PACKET_CODEC,
                        recipe -> recipe.addition,
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