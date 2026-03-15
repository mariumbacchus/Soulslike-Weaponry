package net.soulsweaponry.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.RecipeSerializerRegistry;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;
import net.soulsweaponry.util.WeaponUtil;

import java.util.stream.Stream;

public record ItemUpgradeRecipe(Identifier id, Ingredient template, Ingredient base, Ingredient addition, float primaryBonus, float secondaryBonus, boolean fallback) implements SmithingRecipe {

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
    public boolean matches(Inventory inventory, World world) {
        ItemStack templateStack = inventory.getStack(0);
        ItemStack baseStack = inventory.getStack(1);
        ItemStack additionStack = inventory.getStack(2);

        if (!this.template.test(templateStack) || !this.base.test(baseStack) || !this.addition.test(additionStack)) {
            return false;
        }

        int level = WeaponUtil.getUpgradeLevel(baseStack);
        if (level >= (int) ConfigConstructor.item_upgrading_max_level) {
            return false;
        }

        if (!this.fallback) {
            return true;
        }

        // Fallback logic: only allow this recipe if no non-fallback ItemUpgradeRecipe matches same triple.
        RecipeManager manager = world.getRecipeManager();
        for (SmithingRecipe recipe : manager.listAllOfType(RecipeType.SMITHING)) {
            if (recipe == this) continue;

            if (recipe instanceof ItemUpgradeRecipe other && !other.fallback()) {
                if (other.template().test(templateStack)
                        && other.base().test(baseStack)
                        && other.addition().test(additionStack)) {
                    return false; // a specific non-fallback upgrade exists, so don't use fallback
                }
            }
        }
        return true;
    }


    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        ItemStack out = inventory.getStack(1).copy();
        int prev = WeaponUtil.getUpgradeLevel(out);
        int nextLevel = Math.min(prev + 1, (int) ConfigConstructor.item_upgrading_max_level);
        applyUpgrades(out, nextLevel);
        return out;
    }

    public void applyUpgrades(ItemStack stack, int level) {
        WeaponUtil.setUpgradeLevel(stack, level);
        NbtHelper.putFloat(stack, NbtIds.UPGRADE_PRIMARY, this.primaryBonus);
        NbtHelper.putFloat(stack, NbtIds.UPGRADE_SECONDARY, this.secondaryBonus);
    }

    /**
     * Used for recipe book preview, smithing results depend on inputs,
     * so returning EMPTY is fine.
     */
    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        ItemStack[] matches = this.base.getMatchingStacks();
        return matches.length > 0 ? matches[0].copy() : ItemStack.EMPTY;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.SMITHING_ITEM_UPGRADE;
    }

    @Override
    public boolean isEmpty() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::isEmpty);
    }

    public static class Serializer implements RecipeSerializer<ItemUpgradeRecipe> {

        @Override
        public ItemUpgradeRecipe read(Identifier id, JsonObject json) {
            Ingredient template = Ingredient.fromJson(JsonHelper.getElement(json, "template"));
            Ingredient base = Ingredient.fromJson(JsonHelper.getElement(json, "base"));
            Ingredient addition = Ingredient.fromJson(JsonHelper.getElement(json, "addition"));

            float primaryBonus = JsonHelper.getFloat(json, "primaryBonus");
            float secondaryBonus = JsonHelper.getFloat(json, "secondaryBonus");
            boolean fallback = JsonHelper.getBoolean(json, "fallback", false);

            return new ItemUpgradeRecipe(id, template, base, addition, primaryBonus, secondaryBonus, fallback);
        }

        @Override
        public ItemUpgradeRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient template = Ingredient.fromPacket(buf);
            Ingredient base = Ingredient.fromPacket(buf);
            Ingredient addition = Ingredient.fromPacket(buf);

            float primaryBonus = buf.readFloat();
            float secondaryBonus = buf.readFloat();
            boolean fallback = buf.readBoolean();

            return new ItemUpgradeRecipe(id, template, base, addition, primaryBonus, secondaryBonus, fallback);
        }

        @Override
        public void write(PacketByteBuf buf, ItemUpgradeRecipe recipe) {
            recipe.template.write(buf);
            recipe.base.write(buf);
            recipe.addition.write(buf);

            buf.writeFloat(recipe.primaryBonus);
            buf.writeFloat(recipe.secondaryBonus);
            buf.writeBoolean(recipe.fallback);
        }
    }
}