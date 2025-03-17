package net.soulsweaponry.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class PurifiedBloodBlock extends FluidBlock {

    public PurifiedBloodBlock(Supplier<FlowableFluid> fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity living && entity.age % 60 == 0) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 80 ,0));
        }
    }
}