package net.soulsweaponry.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WitheredTallGrass extends DoublePlantBlock implements Withered {

    protected final Block replacedBlock;

    public WitheredTallGrass(Properties pProperties, Block replacedBlock) {
        super(pProperties);
        this.replacedBlock = replacedBlock;
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public Block getBlockToReturnAs() {
        return this.replacedBlock;
    }

    @Override
    public boolean canTurn(BlockGetter world, BlockPos pos, int maxNeighbors) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.setWithOffset(pos, Direction.DOWN);
        return !(world.getBlockState(mutable).getBlock() instanceof WitheredBlock);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (this.canTurn(world, pos, 2)) {
            this.turnBack(world, pos);
        }
        super.neighborChanged(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @Override
    public void turnBack(Level world, BlockPos pos) {
        world.removeBlock(pos, false); // This can (maybe) be removed in future updates, at least in fabric it can.
        DoublePlantBlock.placeAt(world, this.getBlockToReturnAs().defaultBlockState(), pos, 2);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return super.mayPlaceOn(pState, pLevel, pPos) || pState.is(Blocks.NETHERRACK) || pState.is(Blocks.END_STONE) || pState.is(Blocks.SOUL_SAND) || pState.is(Blocks.SOUL_SOIL);
    }
}
