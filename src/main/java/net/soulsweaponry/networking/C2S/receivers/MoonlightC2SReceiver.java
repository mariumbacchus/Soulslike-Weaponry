package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.misc.MoonstoneRing;
import net.soulsweaponry.networking.C2S.packets.MoonlightC2S;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

public class MoonlightC2SReceiver {

    public static void receive(MoonlightC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            if (!player.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING)) {
                boolean hasEffect = player.hasStatusEffect(EffectRegistry.MOON_HERALD);
                int amp = hasEffect ? player.getStatusEffect(EffectRegistry.MOON_HERALD).getAmplifier() + 1 : 0;
                MoonstoneRing.MOONSTONE_RING_SHOOT_MOONLIGHT.shootMoonlight(serverWorld, WeaponRegistry.MOONLIGHT_SHORTSWORD.getDefaultStack(), player, amp, 0);
                player.getItemCooldownManager().set(ItemRegistry.MOONSTONE_RING, MoonstoneRing.MOONSTONE_RING_SHOOT_MOONLIGHT.cooldownWithEffect());
                player.swingHand(Hand.MAIN_HAND, true);
                serverWorld.playSound(null, player.getBlockPos(), SoundRegistry.MOONLIGHT_SMALL_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            }
        });
    }
}
