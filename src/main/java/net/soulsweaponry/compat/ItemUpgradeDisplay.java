package net.soulsweaponry.compat;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;
import net.soulsweaponry.registry.ComponentRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemUpgradeDisplay extends BasicDisplay {

    // Serializer exactly like DefaultSmithingDisplay does it, just without the extra "type" field.
    public static final DisplaySerializer<ItemUpgradeDisplay> SERIALIZER =
            DisplaySerializer.of(
                    RecordCodecBuilder.mapCodec(instance ->
                            instance.group(
                                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(BasicDisplay::getInputEntries),
                                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(BasicDisplay::getOutputEntries),
                                    Identifier.CODEC.optionalFieldOf("location").forGetter(BasicDisplay::getDisplayLocation)
                            ).apply(instance, ItemUpgradeDisplay::new)
                    ),
                    PacketCodec.tuple(
                            EntryIngredient.streamCodec().collect(PacketCodecs.toList()), BasicDisplay::getInputEntries,
                            EntryIngredient.streamCodec().collect(PacketCodecs.toList()), BasicDisplay::getOutputEntries,
                            PacketCodecs.optional(Identifier.PACKET_CODEC), BasicDisplay::getDisplayLocation,
                            ItemUpgradeDisplay::new
                    )
            );

    public ItemUpgradeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    // REI will call this constructor via recipe filler
    public ItemUpgradeDisplay(net.minecraft.recipe.RecipeEntry<ItemUpgradeRecipe> recipe) {
        super(ItemUpgradeDisplayUtil.inputs(recipe.value()),
                List.of(ItemUpgradeDisplayUtil.outputPreview(recipe.value())),
                Optional.of(recipe.id().getValue()));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ItemUpgradeCategory.ITEM_UPGRADE;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    // small helper holder so the codec/packet constructors don't need recipe class
    static class ItemUpgradeDisplayUtil {
        static List<EntryIngredient> inputs(ItemUpgradeRecipe r) {
            return List.of(
                    opt(r.template()),
                    opt(r.base()),
                    opt(r.addition())
            );
        }

        static EntryIngredient outputPreview(ItemUpgradeRecipe r) {
            var baseOpt = r.base();
            if (baseOpt.isEmpty()) return EntryIngredients.of(Items.BARRIER);

            var matching = baseOpt.get().getMatchingItems();
            if (matching.isEmpty()) return EntryIngredients.of(Items.BARRIER);

            // Build multiple outputs so REI can cycle them
            List<ItemStack> outs = new ArrayList<>();

            int max = (int) ConfigConstructor.item_upgrading_max_level;

            for (var entry : matching) {
                ItemStack out = entry.value().getDefaultStack().copy();

                int prev = out.getOrDefault(ComponentRegistry.ITEM_UPGRADE_LEVEL, 0);
                int next = Math.min(prev + 1, max);

                r.applyUpgrades(out, next);
                outs.add(out);
            }

            // This makes it cyclable
            return EntryIngredients.ofItemStacks(outs);
        }
    }

    static EntryIngredient opt(Optional<Ingredient> opt) {
        return opt.map(EntryIngredients::ofIngredient).orElse(EntryIngredient.empty());
    }

    static EntryIngredient barrier() {
        return EntryIngredients.of(Items.BARRIER);
    }
}