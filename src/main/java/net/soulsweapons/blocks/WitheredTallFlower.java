package net.soulsweapons.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.registries.ForgeRegistries;

public class WitheredTallFlower extends WitheredTallGrass {

    public static final BooleanProperty CANNOT_TURN = BooleanProperty.create("can_turn");
    private final MobEffect effect;

    public WitheredTallFlower(Properties pProperties, Block replacedBlock, MobEffect effect) {
        super(pProperties, replacedBlock);
        this.effect = effect;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(CANNOT_TURN);
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public Block getBlockToReturnAs() {
        return this.getRandomFlower();
    }

    private Block getRandomFlower() {
        System.out.println(ForgeRegistries.BLOCKS.tags().getTag(BlockTags.SMALL_FLOWERS));
        return Blocks.SUNFLOWER; //TODO implement
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (world.isClientSide || world.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addEffect(new MobEffectInstance(this.effect, 40));
        }
    }

    @Override
    public boolean canTurn(BlockGetter world, BlockPos pos, int maxNeighbors) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.setWithOffset(pos, Direction.DOWN);
        return !(world.getBlockState(mutable).getBlock() instanceof WitheredBlock) && !world.getBlockState(pos).getValue(CANNOT_TURN);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return super.mayPlaceOn(pState, pLevel, pPos) || pState.is(Blocks.NETHERRACK) || pState.is(Blocks.END_STONE) || pState.is(Blocks.SOUL_SAND) || pState.is(Blocks.SOUL_SOIL);
    }
}
