package net.soulsweaponry.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

/**
 * If this replaced tall flowers, it will turn into a random tall flower 
 * block instead of it's previous form, just like {@link WitheredFlower}
 */
public class WitheredTallFlower extends WitheredTallGrass {

    private final StatusEffect effect;
    public static final BooleanProperty CANNOT_TURN = BooleanProperty.of("can_turn");

    public WitheredTallFlower(Settings settings, Block replacedBlock, StatusEffect effect) {
        super(settings, replacedBlock);
        this.effect = effect;
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CANNOT_TURN);
    }

    @Override
    public void turnBack(BlockState state, World world, BlockPos pos) {
        this.replacedBlock = this.getRandomTallFlower();
        super.turnBack(state, world, pos);
    }

    private TallPlantBlock getRandomTallFlower() {
        Block[] tallFlowers = {Blocks.SUNFLOWER, Blocks.LILAC, Blocks.PEONY, Blocks.ROSE_BUSH};
        return (TallPlantBlock)tallFlowers[new Random().nextInt(tallFlowers.length)];
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
    protected boolean canTurn(BlockView world, BlockPos pos, int maxNeighbors) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set((Vec3i)pos, Direction.DOWN);
        if (!(world.getBlockState(mutable).getBlock() instanceof WitheredBlock) && !world.getBlockState(pos).get(CANNOT_TURN)) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return super.canPlantOnTop(floor, world, pos) || floor.isOf(Blocks.NETHERRACK) || floor.isOf(Blocks.SOUL_SAND) || floor.isOf(Blocks.SOUL_SOIL) || floor.isOf(Blocks.END_STONE);
    }
}
