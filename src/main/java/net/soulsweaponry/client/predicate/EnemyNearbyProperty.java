package net.soulsweaponry.client.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.inventorytick.Luminate;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnemyNearbyProperty implements NumericProperty {

    public static final MapCodec<EnemyNearbyProperty> CODEC = MapCodec.unit(new EnemyNearbyProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        return Luminate.isActive(stack) ? 1f : 0f;
    }

    @Override
    public MapCodec<? extends NumericProperty> getCodec() {
        return CODEC;
    }
}
