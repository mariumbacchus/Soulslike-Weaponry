package net.soulsweaponry.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Random;

public interface Withered {

    IntegerProperty AGE = BlockStateProperties.AGE_3;

    Block getBlock();
    Block getBlockToReturnAs();

    default void doTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if ((random.nextInt(3) == 0 || this.canTurn(world, pos, 4)) && world.getMaxLocalRawBrightness(pos) > 11 - state.getValue(AGE) - state.getLightBlock(world, pos) && this.increaseAge(state, world, pos)) {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                mutable.setWithOffset(pos, direction);
                BlockState blockState = world.getBlockState(mutable);
                if (!blockState.is(this.getBlock()) || this.increaseAge(blockState, world, mutable)) continue;
                world.scheduleTick(mutable, this.getBlock(), Mth.nextInt(random, 20, 40));
            }
            return;
        }
        world.scheduleTick(pos, this.getBlock(), Mth.nextInt(random, 20, 40));
    }

    boolean canTurn(BlockGetter world, BlockPos pos, int maxNeighbors);

    default boolean increaseAge(BlockState state, Level world, BlockPos pos) {
        int i = state.getValue(AGE);
        if (i < 3) {
            world.setBlock(pos, state.setValue(AGE, i + 1), 2);
            return false;
        }
        this.turnBack(world, pos);
        return true;
    }

    default void resetAge(BlockState state, Level world, BlockPos pos) {
        world.setBlock(pos, state.setValue(AGE, 0), 2);
    }

    default void turnBack(Level world, BlockPos pos) {
        world.setBlock(pos, this.getBlockToReturnAs().defaultBlockState(), 2);
        world.updateNeighborsAt(pos, this.getBlockToReturnAs());
    }
}
