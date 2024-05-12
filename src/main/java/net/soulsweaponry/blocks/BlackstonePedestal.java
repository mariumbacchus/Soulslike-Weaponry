package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlackstonePedestal extends Block {

    public BlackstonePedestal(Settings settings) {
        super(settings);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        /*ItemStack itemStack = player.getStackInHand(hand); TODO
        if (itemStack.isOf(ItemRegistry.SHARD_OF_UNCERTAINTY)) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            ChaosMonarch boss = new ChaosMonarch(EntityRegistry.CHAOS_MONARCH, world);
            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
            boss.setAttack(1);
            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            world.spawnEntity(boss);
            world.removeBlock(pos, false);
            return ActionResult.SUCCESS;
        } else if (itemStack.isOf(ItemRegistry.DEMON_CHUNK) || itemStack.isOf(ItemRegistry.WITHERED_DEMON_HEART)) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            AccursedLordBoss boss = new AccursedLordBoss(EntityRegistry.ACCURSED_LORD_BOSS, world);
            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
            boss.setAttackAnimation(AccursedLordAnimations.SPAWN);
            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            world.spawnEntity(boss);
            world.removeBlock(pos, false);
            return ActionResult.SUCCESS;
        }*/
        return ActionResult.FAIL;
    }
}