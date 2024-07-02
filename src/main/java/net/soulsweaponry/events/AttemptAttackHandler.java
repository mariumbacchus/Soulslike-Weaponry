package net.soulsweaponry.events;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.networking.PacketIds;
import net.soulsweaponry.registry.WeaponRegistry;

public class AttemptAttackHandler implements AttemptAttackCallback {
    @Override
    public ActionResult useViaAttack(PlayerEntity player, World world) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            boolean moonlight = stack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD) && !ConfigConstructor.disable_use_moonlight_shortsword;
            boolean bluemoon = stack.isOf(WeaponRegistry.BLUEMOON_SHORTSWORD) && !ConfigConstructor.disable_use_bluemoon_shortsword;
            // Sending message each left click with the item is a bit much so don't do that
            if (moonlight || bluemoon) {
                if (world.isClient) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(PacketIds.MOONLIGHT, buf);
                }
            }
        }
        return ActionResult.PASS;
    }
}
