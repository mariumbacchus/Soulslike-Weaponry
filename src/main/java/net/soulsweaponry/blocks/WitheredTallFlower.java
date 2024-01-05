package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * If this replaced tall flowers, it will turn into a random tall flower
 * block instead of its previous form, just like {@link net.soulsweaponry.blocks.WitheredFlower}
 */
public class WitheredTallFlower extends WitheredTallGrass {

    private final StatusEffect effect;
    public static final BooleanProperty CANNOT_TURN = BooleanProperty.of("can_turn");
    private static final Supplier<List<Block>> TALL_FLOWERS = () -> Registries.BLOCK.stream().filter((block -> block.getDefaultState().isIn(BlockTags.TALL_FLOWERS))).toList(); //NOTE: unsure if this works on servers (it should tho, right?)

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
    public Block getBlock() {
        return this;
    }

    @Override
    public Block getBlockToReturnAs() {
        return this.getRandomTallFlower();
    }

    private Block getRandomTallFlower() {
        return TALL_FLOWERS.get().get(new Random().nextInt(TALL_FLOWERS.get().size()));
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
    public boolean canTurn(BlockView world, BlockPos pos, int maxNeighbors) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(pos, Direction.DOWN);
        return !(world.getBlockState(mutable).getBlock() instanceof WitheredBlock) && !world.getBlockState(pos).get(CANNOT_TURN);
    }
}