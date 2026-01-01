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
import net.soulsweaponry.items.abilities.IConfigDisable;
import net.soulsweaponry.registry.SoundRegistry;

public abstract class SpawnBossBlock extends Block implements IConfigDisable {

    public SpawnBossBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (spawnBoss(world, pos, player, itemStack)) {
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    public abstract boolean spawnBoss(World world, BlockPos pos, PlayerEntity player, ItemStack itemStack);

    /**
     * Initiate the spawning of the entity. Call this inside {@link #spawnBoss(World, BlockPos, PlayerEntity, ItemStack)}
     * at the end of the manual initialization of the custom entities to spawn to set the position, play sound,
     * remove the block and spawn the boss.
     */
    public boolean spawnEntity(World world, BlockPos pos, PlayerEntity player, LivingEntity boss, boolean disableSpawn, ItemStack consumableItem, boolean consumeItem) {
        if (disableSpawn) {
            this.notifyDisabledBossRespawning(player);
            return false;
        }
        boss.setPos(pos.getX(), pos.getY() + 0.1f, pos.getZ());
        world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        world.spawnEntity(boss);
        world.removeBlock(pos, false);
        if (!player.getAbilities().creativeMode && consumeItem) {
            consumableItem.decrement(1);
        }
        return true;
    }

    // Handled through #spawnEntity by checking config line for each boss instead of just the block
    @Override
    public boolean isDisabled(ItemStack stack) {
        return false;
    }
}
