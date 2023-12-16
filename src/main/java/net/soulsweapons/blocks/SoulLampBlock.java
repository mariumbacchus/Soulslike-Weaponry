package net.soulsweapons.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.soulsweapons.registry.ItemRegistry;

import java.util.Random;

public class SoulLampBlock extends Block {

    private static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SoulLampBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT, false));
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (state.getValue(LIT) && !world.hasSignal(pos, Direction.DOWN)) {
            world.setBlock(pos, state.cycle(LIT), 3);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (state.getValue(LIT)) {
            world.setBlock(pos, state.setValue(LIT, false), 3);
            if (!player.isCreative()) {
                player.addItem(ItemRegistry.LOST_SOUL.get().getDefaultInstance());
            }
            return InteractionResult.SUCCESS;
        } else if (itemStack.is(ItemRegistry.LOST_SOUL.get())) {
            world.setBlock(pos, state.setValue(LIT, true), 3);
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
