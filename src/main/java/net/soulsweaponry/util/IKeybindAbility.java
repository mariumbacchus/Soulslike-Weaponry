package net.soulsweaponry.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public interface IKeybindAbility {

    /**
     * Server side effects.
     * Remember to add the check for whether {@code ModdedSword.isDisabled()} is true or not and then return early.
     * @param world server world
     * @param stack item stack
     * @param player (server) player
     */
    void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player);

    /**
     * Client side effects.
     * Remember to add the check for whether {@code ModdedSword.isDisabled()} is true or not and then return early.
     * @param world client world
     * @param stack item stack
     * @param player client player
     */
    void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player);
}