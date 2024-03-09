package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShortPlantBlock;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WitheredGrass extends ShortPlantBlock implements Withered {

    private final Block replacedBlock;

    public WitheredGrass(Settings settings, Block replacedBlock) {
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
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (this.canTurn(world, pos, 0)) {
            this.turnBack(world, pos);
        }
        DebugInfoSender.sendNeighborUpdate(world, pos);
    }
}