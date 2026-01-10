package net.soulsweaponry.client.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

public class BetterCombatProperty implements NumericProperty {

    public static final MapCodec<BetterCombatProperty> CODEC = MapCodec.unit(new BetterCombatProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        return WeaponUtil.isModLoaded("bettercombat") ? 1f : 0f;
    }

    @Override
    public MapCodec<? extends NumericProperty> getCodec() {
        return CODEC;
    }
}
