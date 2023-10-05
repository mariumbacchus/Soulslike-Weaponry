package net.soulsweaponry.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public interface IKeybindAbility {

    void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player);
    void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player);
}
