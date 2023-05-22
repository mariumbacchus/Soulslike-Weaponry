package net.soulsweaponry.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public interface IKeybindAbility {

    void useKeybindAbility(ServerWorld world, ItemStack stack, PlayerEntity player);
}