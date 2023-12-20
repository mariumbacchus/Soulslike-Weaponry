package net.soulsweaponry.util;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IKeybindAbility {

    void useKeybindAbilityServer(ServerLevel world, ItemStack stack, Player player);
    void useKeybindAbilityClient(ClientLevel world, ItemStack stack, LocalPlayer player);
}
