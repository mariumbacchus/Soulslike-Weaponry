package net.soulsweaponry.client.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.registry.EffectRegistry;
import org.jetbrains.annotations.Nullable;

public class ChungusTonicProperty implements NumericProperty {

    public static final MapCodec<ChungusTonicProperty> CODEC = MapCodec.unit(new ChungusTonicProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
        return (contents != null && contents.matches(EffectRegistry.CHUNGUS_TONIC_POTION)) ? 1.0f : 0.0f;
    }

    @Override
    public MapCodec<? extends NumericProperty> getCodec() {
        return CODEC;
    }
}
