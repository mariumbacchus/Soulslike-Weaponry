package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WitheredTallGrass extends TallPlantBlock {

    public static final int MAX_AGE = 3;
    public static final IntProperty AGE = Properties.AGE_3;
    protected Block replacedBlock;

    public WitheredTallGrass(Settings settings, Block replacedBlock) {
        super(settings);
        this.replacedBlock = replacedBlock;
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(AGE, 0));
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(HALF, DoubleBlockHalf.LOWER));
    }

    /* public void placeThis(WorldAccess world, BlockState state, BlockPos pos, int flags, boolean canTurn) {
        BlockPos blockPos = pos.up();
        this.canTurn = canTurn;
        world.setBlockState(pos, WitheredTallGrass.withWaterloggedState(world, pos, this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER)), flags);
        world.setBlockState(blockPos, WitheredTallGrass.withWaterloggedState(world, blockPos, this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER)), flags);
    } */

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.scheduledTick(state, world, pos, random);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.scheduleBlockTick(pos, this, MathHelper.nextInt(random, 20, 40));
    }

    protected boolean increaseAge(BlockState state, World world, BlockPos pos) {
        int i = state.get(AGE);
        if (i < 3) {
            world.setBlockState(pos, (BlockState)state.with(AGE, i + 1), Block.NOTIFY_LISTENERS);
            return false;
        }
        this.turnBack(state, world, pos);
        return true;
    }

    public void resetAge(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, (BlockState)state.with(AGE, 0), Block.NOTIFY_LISTENERS);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (this.canTurn(world, pos, 2)) {
            this.turnBack(state, world, pos);
        }
        DebugInfoSender.sendNeighborUpdate(world, pos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        builder.add(HALF);
    }

    protected boolean canTurn(BlockView world, BlockPos pos, int maxNeighbors) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set((Vec3i)pos, Direction.DOWN);
        return !(world.getBlockState(mutable).getBlock() instanceof WitheredBlock);
    }

    public void turnBack(BlockState state, World world, BlockPos pos) {
        TallPlantBlock.placeAt(world, this.replacedBlock.getDefaultState(), pos, 2);
    }
}
