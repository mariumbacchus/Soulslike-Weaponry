package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.ShapeContext;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WitheredGrass extends FernBlock {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
    public static final int MAX_AGE = 3;
    public static final IntProperty AGE = Properties.AGE_3;
    private Block replacedBlock;

    public WitheredGrass(Settings settings, Block replacedBlock) {
        super(settings);
        this.replacedBlock = replacedBlock;
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(AGE, 0));
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.scheduledTick(state, world, pos, random);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((random.nextInt(3) == 0 || this.canTurn(world, pos, 0)) && world.getLightLevel(pos) > 11 - state.get(AGE) - state.getOpacity(world, pos) && this.increaseAge(state, world, pos)) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            mutable.set((Vec3i)pos, Direction.DOWN);
            if (!(world.getBlockState(mutable).getBlock() instanceof WitheredBlock)) {
                world.createAndScheduleBlockTick(mutable, this, MathHelper.nextInt(random, 20, 40));
            }
            return;
        }
        world.createAndScheduleBlockTick(pos, this, MathHelper.nextInt(random, 20, 40));
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
        if (this.canTurn(world, pos, 0)) {
            this.turnBack(state, world, pos);
        }
        DebugInfoSender.sendNeighborUpdate(world, pos);
    }

    protected boolean canTurn(BlockView world, BlockPos pos, int maxNeighbors) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set((Vec3i)pos, Direction.DOWN);
        if (!(world.getBlockState(mutable).getBlock() instanceof WitheredBlock)) {
            return true;
        }
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    protected void turnBack(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, this.replacedBlock.getDefaultState());
        //world.updateNeighbor(pos, this.replacedBlock, pos);
    }
}
