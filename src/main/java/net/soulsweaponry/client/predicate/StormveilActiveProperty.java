package net.soulsweaponry.client.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.abilities.ISharpened;
import net.soulsweaponry.registry.EffectRegistry;
import org.jetbrains.annotations.Nullable;

public class StormveilActiveProperty implements NumericProperty {

    public static final MapCodec<StormveilActiveProperty> CODEC = MapCodec.unit(new StormveilActiveProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        if (holder == null) {
            return 0.0f;
        }
        return holder.hasStatusEffect(EffectRegistry.STORMVEIL) ? 1.0f : 0.0f;
    }

    @Override
    public MapCodec<? extends NumericProperty> getCodec() {
        return CODEC;
    }
}
