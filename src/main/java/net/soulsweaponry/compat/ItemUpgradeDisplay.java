package net.soulsweaponry.compat;

import com.mojang.serialization.Codec;
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
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ItemUpgradeDisplay extends BasicDisplay {

    private final float primaryBonus;
    private final float secondaryBonus;

    public static final DisplaySerializer<ItemUpgradeDisplay> SERIALIZER =
            DisplaySerializer.of(
                    RecordCodecBuilder.mapCodec(instance ->
                            instance.group(
                                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(BasicDisplay::getInputEntries),
                                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(BasicDisplay::getOutputEntries),
                                    Identifier.CODEC.optionalFieldOf("location").forGetter(BasicDisplay::getDisplayLocation),
                                    Codec.FLOAT.fieldOf("primaryBonus").forGetter(ItemUpgradeDisplay::primaryBonus),
                                    Codec.FLOAT.fieldOf("secondaryBonus").forGetter(ItemUpgradeDisplay::secondaryBonus)
                            ).apply(instance, ItemUpgradeDisplay::new)
                    ),
                    PacketCodec.tuple(
                            EntryIngredient.streamCodec().collect(PacketCodecs.toList()), BasicDisplay::getInputEntries,
                            EntryIngredient.streamCodec().collect(PacketCodecs.toList()), BasicDisplay::getOutputEntries,
                            PacketCodecs.optional(Identifier.PACKET_CODEC), BasicDisplay::getDisplayLocation,
                            PacketCodecs.FLOAT, ItemUpgradeDisplay::primaryBonus,
                            PacketCodecs.FLOAT, ItemUpgradeDisplay::secondaryBonus,
                            ItemUpgradeDisplay::new
                    )
            );

    public ItemUpgradeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs,
                              Optional<Identifier> location, float primaryBonus, float secondaryBonus) {
        super(inputs, outputs, location);
        this.primaryBonus = primaryBonus;
        this.secondaryBonus = secondaryBonus;
    }

    public ItemUpgradeDisplay(RecipeEntry<ItemUpgradeRecipe> recipe) {
        this(
                ItemUpgradeDisplayUtil.inputs(recipe.value()),
                List.of(ItemUpgradeDisplayUtil.outputPreview(recipe.value())),
                Optional.of(recipe.id().getValue()),
                recipe.value().primaryBonus(),
                recipe.value().secondaryBonus()
        );
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ItemUpgradeREIIds.ITEM_UPGRADE;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    public float primaryBonus() {
        return primaryBonus;
    }

    public float secondaryBonus() {
        return secondaryBonus;
    }

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

            EntryIngredient.Builder b = EntryIngredient.builder(matching.size());
            for (var entry : matching) {
                ItemStack s = entry.value().getDefaultStack().copy();
                b.add(me.shedaniel.rei.api.common.util.EntryStacks.of(s));
            }
            return b.build();
        }
    }

    static EntryIngredient opt(Optional<Ingredient> opt) {
        return opt.map(EntryIngredients::ofIngredient).orElse(EntryIngredient.empty());
    }
}