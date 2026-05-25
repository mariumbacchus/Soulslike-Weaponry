package net.soulsweaponry.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.config.BossConfig;
import net.soulsweaponry.entity.mobs.*;
import net.soulsweaponry.entity.mobs.AccursedLordBoss.AccursedLordAnimations;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;

public class BlackstonePedestal extends SpawnBossBlock {

    public BlackstonePedestal(Settings settings) {
        super(settings);
    }

    @Override
    public boolean spawnBoss(World world, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        if (itemStack.isOf(ItemRegistry.SHARD_OF_UNCERTAINTY.get())) {
            ChaosMonarch entity = new ChaosMonarch(EntityRegistry.CHAOS_MONARCH.get(), world);
            entity.setAttack(1);
            return spawnEntity(world, pos, player, entity, BossConfig.chaos_monarch_disable_respawn, itemStack, BossConfig.chaos_monarch_consume_item_on_summoning);
        } else if (itemStack.isOf(ItemRegistry.DEMON_CHUNK.get()) || itemStack.isOf(ItemRegistry.WITHERED_DEMON_HEART.get())) {
            AccursedLordBoss entity = new AccursedLordBoss(EntityRegistry.ACCURSED_LORD_BOSS.get(), world);
            entity.setAttackAnimation(AccursedLordAnimations.SPAWN);
            return spawnEntity(world, pos, player, entity, BossConfig.decaying_king_disable_respawn, itemStack, BossConfig.decaying_king_consume_item_on_summoning);
        }
        return false;
    }
}