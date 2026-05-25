package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.items.misc.MoonstoneRing;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

import java.util.function.Supplier;

public class MoonlightC2S {

    public MoonlightC2S() {

    }

    //Same as decode
    public MoonlightC2S(PacketByteBuf buf) {

    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            this.handlePacket(player, this);
        });
        context.setPacketHandled(true);
    }

    private void handlePacket(ServerPlayerEntity player, MoonlightC2S packet) {
        if (!player.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING.get())) {
            ServerWorld serverWorld = player.getServerWorld();
            boolean hasEffect = player.hasStatusEffect(EffectRegistry.MOON_HERALD.get());
            int amp = hasEffect ? player.getStatusEffect(EffectRegistry.MOON_HERALD.get()).getAmplifier() + 1 : 0;
            MoonstoneRing.MOONSTONE_RING_SHOOT_MOONLIGHT.shootMoonlight(serverWorld, WeaponRegistry.MOONLIGHT_SHORTSWORD.get().getDefaultStack(), player, amp, 0);
            player.getItemCooldownManager().set(ItemRegistry.MOONSTONE_RING.get(), MoonstoneRing.MOONSTONE_RING_SHOOT_MOONLIGHT.cooldownWithEffect());
            player.swingHand(Hand.MAIN_HAND, true);
            serverWorld.playSound(null, player.getBlockPos(), SoundRegistry.MOONLIGHT_SMALL_EVENT.get(), SoundCategory.PLAYERS, 1f, 1f);
        }
    }
}
