package net.soulsweaponry.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public interface Withered {
    // NOTE: Blocks that implement this won't turn back on their own, unless given an AGE property in the builder.
    // Usually, the block only checks if the block under is withered or not (which has AGE property) and turns based on that.
    // What to do is determined in the "canTurn" method.
    Block getBlock();
    Block getBlockToReturnAs();

    boolean canTurn(BlockGetter world, BlockPos pos, int maxNeighbors);

    default void turnBack(Level world, BlockPos pos) {
        world.setBlock(pos, this.getBlockToReturnAs().defaultBlockState(), 2);
        world.updateNeighborsAt(pos, this.getBlockToReturnAs());
    }
}
