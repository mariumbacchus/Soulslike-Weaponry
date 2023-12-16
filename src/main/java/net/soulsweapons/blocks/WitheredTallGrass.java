package net.soulsweapons.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Random;

public class WitheredTallGrass extends DoublePlantBlock implements Withered {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    protected final Block replacedBlock;

    public WitheredTallGrass(Properties pProperties, Block replacedBlock) {
        super(pProperties);
        this.replacedBlock = replacedBlock;
        this.registerDefaultState(this.getStateDefinition().any().setValue(AGE, 0));
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        this.doTick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public void doTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        world.scheduleTick(pos, this, Mth.nextInt(random, 20, 40));
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(AGE);
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
        world.removeBlock(pos, false);
        DoublePlantBlock.placeAt(world, this.getBlockToReturnAs().defaultBlockState(), pos, 2);
    }
}
