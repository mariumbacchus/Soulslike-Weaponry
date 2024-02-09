package net.soulsweaponry.events;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

public class AttemptAttackHandler implements AttemptAttackCallback {
    @Override
    public ActionResult useViaAttack(PlayerEntity player, World world) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD) || stack.isOf(WeaponRegistry.BLUEMOON_SHORTSWORD)) {
                if (world.isClient) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(PacketRegistry.MOONLIGHT, buf);
                }
            }
        }
        return ActionResult.PASS;
    }
}
