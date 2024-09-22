package net.soulsweaponry.networking.C2S;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.util.ModTags;

public class ParryC2S {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        server.execute(() -> {
            ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
            if (serverWorld != null) {
                ItemStack stack = player.getStackInHand(Hand.OFF_HAND);
                if (ConfigConstructor.enable_shield_parry && stack.isIn(ModTags.Items.SHIELDS) && !player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                    ParryData.setParryFrames((IEntityDataSaver) player, 1);
                    player.getItemCooldownManager().set(stack.getItem(), player.isCreative() ? 10 : ConfigConstructor.shield_parry_cooldown);
                }
            }
        });
    }
}
