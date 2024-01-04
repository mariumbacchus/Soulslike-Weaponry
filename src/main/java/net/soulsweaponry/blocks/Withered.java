package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public interface Withered {

    IntProperty AGE = Properties.AGE_3;

    Block getBlock();
    Block getBlockToReturnAs();

    default void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((random.nextInt(3) == 0 || this.canTurn(world, pos, 4)) && world.getLightLevel(pos) > 11 - state.get(AGE) - state.getOpacity(world, pos) && this.increaseAge(state, world, pos)) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (Direction direction : Direction.values()) {
                mutable.set(pos, direction);
                BlockState blockState = world.getBlockState(mutable);
                if (!blockState.isOf(this.getBlock()) || this.increaseAge(blockState, world, mutable)) continue;
                world.createAndScheduleBlockTick(mutable, this.getBlock(), MathHelper.nextInt(random, 20, 40));
            }
            return;
        }
        world.createAndScheduleBlockTick(pos, this.getBlock(), MathHelper.nextInt(random, 20, 40));
    }

    boolean canTurn(BlockView world, BlockPos pos, int maxNeighbors);

    default boolean increaseAge(BlockState state, World world, BlockPos pos) {
        int i = state.get(AGE);
        if (i < 3) {
            world.setBlockState(pos, state.with(AGE, i + 1), Block.NOTIFY_LISTENERS);
            return false;
        }
        this.turnBack(world, pos);
        return true;
    }

    default void resetAge(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, state.with(AGE, 0), Block.NOTIFY_LISTENERS);
    }

    default void turnBack(World world, BlockPos pos) {
        world.setBlockState(pos, this.getBlockToReturnAs().getDefaultState());
        world.updateNeighbor(pos, this.getBlockToReturnAs(), pos);
    }
}
