package net.soulsweaponry.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.function.Predicate;

public class PurifiedBloodCauldronBlock extends LeveledCauldronBlock {

    public PurifiedBloodCauldronBlock(Settings settings, Predicate<Biome.Precipitation> precipitationPredicate, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, precipitationPredicate, behaviorMap);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (this.isEntityTouchingFluid(state, pos, entity) && entity instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20, 0));
        }
    }

    public void incrementFluidLevel(World world, BlockPos pos, BlockState state) {
        int level = state.get(LEVEL);
        if (level < 3) {
            world.setBlockState(pos, state.with(LEVEL, level + 1), 3);
        }
    }
}