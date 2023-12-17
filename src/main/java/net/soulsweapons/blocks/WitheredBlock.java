package net.soulsweapons.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.soulsweapons.registry.EffectRegistry;

import java.util.Random;

public class WitheredBlock extends Block {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private final Block replacedBlock;

    public WitheredBlock(Properties pProperties, Block replacedBlock) {
        super(pProperties);
        this.replacedBlock = replacedBlock;
        this.registerDefaultState(this.getStateDefinition().any().setValue(AGE, 0));
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity entity) {
        if (entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(EffectRegistry.DECAY.get(), 40, 0));
        }
        super.stepOn(pLevel, pPos, pState, entity);
    }

    @Override
    public void animateTick(BlockState pState, Level world, BlockPos pos, Random random) {
        double x = random.nextDouble();
        double z = random.nextDouble();
        world.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + x, (double)pos.getY() + 1.1f, (double)pos.getZ() + z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        this.tick(state, world, pos, random);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if ((random.nextInt(3) == 0 || this.canTurn(world, pos, 4)) && world.getMaxLocalRawBrightness(pos) > 11 - state.getValue(AGE) - state.getLightBlock(world, pos) && this.increaseAge(state, world, pos)) {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                mutable.setWithOffset(pos, direction);
                BlockState blockState = world.getBlockState(mutable);
                if (!blockState.is(this) || this.increaseAge(blockState, world, mutable)) continue;
                world.scheduleTick(mutable, this, Mth.nextInt(random, 20, 40));
            }
            return;
        }
        world.scheduleTick(pos, this, Mth.nextInt(random, 20, 40));
    }

    protected boolean increaseAge(BlockState state, Level world, BlockPos pos) {
        int i = state.getValue(AGE);
        if (i < 3) {
            world.setBlock(pos, state.setValue(AGE, i + 1), 2);
            return false;
        }
        this.turnBack(world, pos);
        return true;
    }

    public void resetAge(BlockState state, Level world, BlockPos pos) {
        world.setBlock(pos, state.setValue(AGE, 0), 2);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (sourceBlock.defaultBlockState().is(this) && this.canTurn(world, pos, 2)) {
            this.turnBack(world, pos);
        }
        super.neighborChanged(state, world, pos, sourceBlock, sourcePos, notify);
    }

    protected boolean canTurn(BlockGetter world, BlockPos pos, int maxNeighbors) {
        int i = 0;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            mutable.setWithOffset(pos, direction);
            if (!world.getBlockState(mutable).is(this) || ++i < maxNeighbors) continue;
            return false;
        }
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    protected void turnBack(Level world, BlockPos pos) {
        world.setBlock(pos, this.replacedBlock.defaultBlockState(), 2);
        world.updateNeighborsAt(pos, this.replacedBlock);
    }
}
