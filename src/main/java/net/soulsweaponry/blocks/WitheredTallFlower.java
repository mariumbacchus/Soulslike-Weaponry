package net.soulsweaponry.blocks;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class WitheredTallFlower extends WitheredTallGrass {

    public static final BooleanProperty CANNOT_TURN = BooleanProperty.create("can_turn");
    private final Supplier<MobEffect> effect;

    public WitheredTallFlower(Supplier<MobEffect> effect, Properties pProperties, Block replacedBlock) {
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
        List<Block> list = ForgeRegistries.BLOCKS.tags().getTag(BlockTags.TALL_FLOWERS).stream().toList();
        return list.get(new Random().nextInt(list.size()));
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (world.isClientSide || world.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addEffect(new MobEffectInstance(this.effect.get(), 40));
        }
    }

    @Override
    public boolean canTurn(BlockGetter world, BlockPos pos, int maxNeighbors) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.setWithOffset(pos, Direction.DOWN);
        return !(world.getBlockState(mutable).getBlock() instanceof WitheredBlock) && !world.getBlockState(pos).getValue(CANNOT_TURN);
    }
}
