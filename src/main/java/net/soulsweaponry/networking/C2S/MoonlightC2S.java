package net.soulsweaponry.networking.C2S;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.misc.MoonstoneRing;
import net.soulsweaponry.items.sword.MoonlightShortsword;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

public class MoonlightC2S {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        server.execute(() -> {
            ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.getWorld()).orNull();
            if (serverWorld != null) {
                if (!player.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING)) {
                    boolean hasEffect = player.hasStatusEffect(EffectRegistry.MOON_HERALD);
                    int amp = hasEffect ? player.getStatusEffect(EffectRegistry.MOON_HERALD).getAmplifier() + 1 : 0;
                    MoonstoneRing.MOONSTONE_RING_SHOOT_MOONLIGHT.shootMoonlight(serverWorld, WeaponRegistry.MOONLIGHT_SHORTSWORD.getDefaultStack(), player, amp, 0);
                    player.getItemCooldownManager().set(ItemRegistry.MOONSTONE_RING, MoonstoneRing.MOONSTONE_RING_SHOOT_MOONLIGHT.cooldownWithEffect());
                    player.swingHand(Hand.MAIN_HAND, true);
                    serverWorld.playSound(null, player.getBlockPos(), SoundRegistry.MOONLIGHT_SMALL_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                }
            }
        });
    }
}