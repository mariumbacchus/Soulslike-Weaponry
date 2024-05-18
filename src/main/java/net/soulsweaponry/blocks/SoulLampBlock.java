package net.soulsweaponry.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ItemRegistry;

public class SoulLampBlock extends Block {

    public static final BooleanProperty LIT = Properties.LIT;

    public SoulLampBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(LIT, false));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(LIT).booleanValue() && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(LIT), Block.NOTIFY_LISTENERS);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (state.get(LIT)) {
            world.setBlockState(pos, state.with(LIT, false), Block.NOTIFY_LISTENERS);
            if (!player.getAbilities().creativeMode) {
                player.dropItem(ItemRegistry.LOST_SOUL.get());
            }
            return ActionResult.SUCCESS;
        } else if (itemStack.isOf(ItemRegistry.LOST_SOUL.get())) {
            world.setBlockState(pos, state.with(LIT, true), Block.NOTIFY_LISTENERS);
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}