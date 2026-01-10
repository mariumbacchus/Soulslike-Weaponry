package net.soulsweaponry.client.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.IHasEssence;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ChargedEssenceProperty implements NumericProperty {

    public static final MapCodec<ChargedEssenceProperty> CODEC = MapCodec.unit(new ChargedEssenceProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        Optional<IHasEssence> op = IHasAbilities.getAbility(stack, IHasEssence.class);
        return (op.isPresent() && op.get().hasMaxEssence(stack)) ? 1.0f : 0.0f;
    }

    @Override
    public MapCodec<? extends NumericProperty> getCodec() {
        return CODEC;
    }
}