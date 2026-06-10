package net.soulsweaponry.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.config.EntityConfig;
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
        if (itemStack.isOf(ItemRegistry.SHARD_OF_UNCERTAINTY)) {
            ChaosMonarch entity = new ChaosMonarch(EntityRegistry.CHAOS_MONARCH, world);
            entity.setAttack(1);
            return spawnEntity(world, pos, player, entity, EntityConfig.chaos_monarch_disable_respawn, itemStack, EntityConfig.chaos_monarch_consume_item_on_summoning);
        } else if (itemStack.isOf(ItemRegistry.DEMON_CHUNK) || itemStack.isOf(ItemRegistry.WITHERED_DEMON_HEART)) {
            AccursedLordBoss entity = new AccursedLordBoss(EntityRegistry.ACCURSED_LORD_BOSS, world);
            entity.setAttackAnimation(AccursedLordAnimations.SPAWN);
            return spawnEntity(world, pos, player, entity, EntityConfig.decaying_king_disable_respawn, itemStack, EntityConfig.decaying_king_consume_item_on_summoning);
        }
        return false;
    }
}
