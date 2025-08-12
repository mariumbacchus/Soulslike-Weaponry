package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.networking.C2S.packets.ParryC2S;

public class ParryC2SReceiver {

    public static void receive(ParryC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        server.execute(() -> {
            ItemStack stack = player.getStackInHand(Hand.OFF_HAND);
            if (ConfigConstructor.enable_shield_parry && stack.isIn(ConventionalItemTags.SHIELD_TOOLS) && !player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                ParryData.setParryFrames((IEntityDataSaver) player, 1);
                player.getItemCooldownManager().set(stack.getItem(), player.isCreative() ? 10 : (int) ConfigConstructor.shield_parry_cooldown);
            }
        });
    }
}
