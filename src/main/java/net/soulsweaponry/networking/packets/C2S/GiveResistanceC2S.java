package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GiveResistanceC2S {

    public GiveResistanceC2S() {

    }

    //Same as decode
    public GiveResistanceC2S(PacketByteBuf buf) {

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

    private void handlePacket(ServerPlayerEntity player, GiveResistanceC2S packet) {
        if (player.hasStatusEffect(StatusEffects.RESISTANCE)) {
            player.removeStatusEffect(StatusEffects.RESISTANCE);
        } else {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400000, 30, false, true));
        }
    }
}
