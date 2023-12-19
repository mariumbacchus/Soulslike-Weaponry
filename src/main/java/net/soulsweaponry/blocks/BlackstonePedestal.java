package net.soulsweaponry.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlackstonePedestal extends Block {

    public BlackstonePedestal(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
//        if (itemStack.isOf(ItemRegistry.SHARD_OF_UNCERTAINTY)) { //TODO add functionality when bosses are made
//            if (!player.getAbilities().creativeMode) {
//                itemStack.decrement(1);
//            }
//            ChaosMonarch boss = new ChaosMonarch(EntityRegistry.CHAOS_MONARCH, world);
//            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
//            boss.setAttack(1);
//            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
//            world.spawnEntity(boss);
//            world.removeBlock(pos, false);
//            return ActionResult.SUCCESS;
//        } else if (itemStack.isOf(ItemRegistry.WITHERED_DEMON_HEART)) {
//            if (!player.getAbilities().creativeMode) {
//                itemStack.decrement(1);
//            }
//            AccursedLordBoss boss = new AccursedLordBoss(EntityRegistry.ACCURSED_LORD_BOSS, world);
//            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
//            boss.setAttackAnimation(AccursedLordAnimations.SPAWN);
//            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
//            world.spawnEntity(boss);
//            world.removeBlock(pos, false);
//            return ActionResult.SUCCESS;
//        }
        return InteractionResult.FAIL;
    }
}
