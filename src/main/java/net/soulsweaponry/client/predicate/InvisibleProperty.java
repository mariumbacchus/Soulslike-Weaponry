package net.soulsweaponry.client.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.abilities.use.InvisibleItem;
import org.jetbrains.annotations.Nullable;

public class InvisibleProperty implements NumericProperty {

    public static final MapCodec<InvisibleProperty> CODEC = MapCodec.unit(new InvisibleProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        return InvisibleItem.isInvisible(stack) ? 1.0f : 0.0f;
    }

    @Override
    public MapCodec<? extends NumericProperty> getCodec() {
        return CODEC;
    }
}
