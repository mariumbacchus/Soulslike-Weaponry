package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public interface Withered {
    // NOTE: Blocks that implement this won't turn back on their own, unless given an AGE property in the builder.
    // Usually, the block only checks if the block under is withered or not (which has AGE property) and turns based on that.
    // What to do is determined in the "canTurn" method.
    Block getBlock();
    Block getBlockToReturnAs();

    boolean canTurn(BlockView world, BlockPos pos, int maxNeighbors);

    default void turnBack(World world, BlockPos pos, @Nullable WireOrientation wireOrientation) {
        world.setBlockState(pos, this.getBlockToReturnAs().getDefaultState());
        world.updateNeighbor(pos, this.getBlockToReturnAs(), wireOrientation);
    }
}