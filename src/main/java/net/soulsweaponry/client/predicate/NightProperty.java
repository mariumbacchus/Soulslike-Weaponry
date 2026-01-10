package net.soulsweaponry.client.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NightProperty implements NumericProperty {

    public static final MapCodec<NightProperty> CODEC = MapCodec.unit(new NightProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        if (holder == null) return 0.0f;

        var w = holder.getWorld();
        if (!w.getDimension().hasSkyLight()) return 0.0f;

        long dayTime = w.getTimeOfDay() % 24000L;
        boolean night = dayTime > 13000L && dayTime < 23000L;
        if (!night) return 0.0f;

        return stack.getEnchantments().isEmpty() ? 1.0f : 0.5f;
    }

    @Override
    public MapCodec<? extends NumericProperty> getCodec() {
        return CODEC;
    }
}
