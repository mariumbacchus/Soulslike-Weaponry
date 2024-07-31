package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.entitydata.FreyrSwordSummonData;

import java.util.UUID;
import java.util.function.Supplier;

public class ReturnFreyrSwordC2S {

    public ReturnFreyrSwordC2S() {

    }

    //Same as decode
    public ReturnFreyrSwordC2S(PacketByteBuf buf) {

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

    private void handlePacket(ServerPlayerEntity player, ReturnFreyrSwordC2S packet) {
        Text text = new TranslatableText("soulsweapons.weapon.no_freyr_sword");
        UUID uuid = FreyrSwordSummonData.getSummonUuid(player);
        if (uuid != null && player.getBlockPos() != null) {
            Entity sword = player.getWorld().getEntity(uuid);
            if (sword instanceof FreyrSwordEntity freyrSword) {
                if (!freyrSword.insertStack(player)) {
                    freyrSword.setPos(player.getX(), player.getEyeY(), player.getZ());
                    freyrSword.dropStack();
                }
                freyrSword.discard();
            } else if (ConfigConstructor.inform_player_about_no_bound_freyr_sword) {
                player.sendMessage(text, true);
            }
        }
    }
}
