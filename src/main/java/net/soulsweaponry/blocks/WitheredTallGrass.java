package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WitheredTallGrass extends TallPlantBlock implements Withered {

    protected Block replacedBlock;

    public WitheredTallGrass(Settings settings, Block replacedBlock) {
        super(settings);
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
    public boolean canTurn(BlockView world, BlockPos pos, int maxNeighbors) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(pos, Direction.DOWN);
        return !(world.getBlockState(mutable).getBlock() instanceof WitheredBlock);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos,
                               Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient && state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER && this.canTurn(world, pos, 2)) {
            this.turnBack(world, pos);
        }
    }

    @Override
    public void turnBack(World world, BlockPos pos) {
        BlockState state = this.getBlockToReturnAs().getDefaultState();
        if (!state.contains(Properties.DOUBLE_BLOCK_HALF)) {
            state = Blocks.TALL_GRASS.getDefaultState();
        }
        int flags = Block.NOTIFY_LISTENERS | Block.FORCE_STATE | Block.SKIP_DROPS;
        TallPlantBlock.placeAt(world, state, pos, flags);
    }

    @Override
    protected boolean canPlantOnTop(BlockState state, BlockView world, BlockPos pos) {
        return super.canPlantOnTop(state, world, pos) || state.isOf(Blocks.NETHERRACK) || state.isOf(Blocks.END_STONE) || state.isOf(Blocks.SOUL_SAND) || state.isOf(Blocks.SOUL_SOIL);
    }
}