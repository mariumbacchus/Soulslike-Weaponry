package net.soulsweaponry.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.WitherRoseBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

/**
 * Will turn back into a random flower instead of it's previous form when ticked.
 * Now there's a way to find the proper flowers without needing to look
 * for them, just use this to re-roll the same ones.
 */
public class WitheredFlower extends WitherRoseBlock {

    private Block flowerToReplace;
    private StatusEffect effect;
    public static final int MAX_AGE = 3;
    public static final IntProperty AGE = Properties.AGE_3;
    public static final BooleanProperty CANNOT_TURN = BooleanProperty.of("can_turn");

    public WitheredFlower(StatusEffect effect, Settings settings, Block defaultReplaced) {
        super(effect, settings);
        this.flowerToReplace = defaultReplaced;
        this.effect = effect;
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
        if (!(world.getBlockState(mutable).getBlock() instanceof WitheredBlock) && !world.getBlockState(pos).get(CANNOT_TURN)) {
            return true;
        }
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        builder.add(CANNOT_TURN);
    }

    protected void turnBack(BlockState state, World world, BlockPos pos) {
        this.flowerToReplace = this.getRandomFlower();
        world.setBlockState(pos, this.flowerToReplace.getDefaultState());
    }

    private FlowerBlock getRandomFlower() {
        Block[] smallFlowers = {Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, 
            Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP, 
            Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, 
            Blocks.LILY_OF_THE_VALLEY, Blocks.WITHER_ROSE};
        return (FlowerBlock) smallFlowers[new Random().nextInt(smallFlowers.length)];
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient || world.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(this.effect, 40));
        }
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return super.canPlantOnTop(floor, world, pos) || floor.isOf(Blocks.NETHERRACK) || floor.isOf(Blocks.SOUL_SAND) || floor.isOf(Blocks.SOUL_SOIL) || floor.isOf(Blocks.END_STONE);
    }
}
