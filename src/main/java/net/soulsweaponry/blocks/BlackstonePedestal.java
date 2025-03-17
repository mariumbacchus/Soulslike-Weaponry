package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.*;
import net.soulsweaponry.entity.mobs.AccursedLordBoss.AccursedLordAnimations;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;

public class BlackstonePedestal extends Block implements IConfigDisable {

    public BlackstonePedestal(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (spawnBoss(world, pos, player, itemStack)) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    private boolean spawnBoss(World world, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        if (itemStack.isOf(ItemRegistry.SHARD_OF_UNCERTAINTY.get())) {
            ChaosMonarch entity = new ChaosMonarch(EntityRegistry.CHAOS_MONARCH.get(), world);
            entity.setAttack(1);
            return spawnEntity(world, pos, player, entity, ConfigConstructor.chaos_monarch_disable_respawn);
        } else if (itemStack.isOf(ItemRegistry.DEMON_CHUNK.get()) || itemStack.isOf(ItemRegistry.WITHERED_DEMON_HEART.get())) {
            AccursedLordBoss entity = new AccursedLordBoss(EntityRegistry.ACCURSED_LORD_BOSS.get(), world);
            entity.setAttackAnimation(AccursedLordAnimations.SPAWN);
            return spawnEntity(world, pos, player, entity, ConfigConstructor.decaying_king_disable_respawn);
        }
        return false;
    }

    private boolean spawnEntity(World world, BlockPos pos, PlayerEntity player, LivingEntity boss, boolean disableSpawn) {
        if (disableSpawn) {
            this.notifyDisabledBossRespawning(player);
            return false;
        }
        boss.setPos(pos.getX(), pos.getY() + 0.1f, pos.getZ());
        world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT.get(), SoundCategory.HOSTILE, 1f, 1f);
        world.spawnEntity(boss);
        world.removeBlock(pos, false);
        return true;
    }

    // Ignore this since it isn't an item
    @Override
    public boolean isDisabled(ItemStack stack) {
        return false;
    }
}