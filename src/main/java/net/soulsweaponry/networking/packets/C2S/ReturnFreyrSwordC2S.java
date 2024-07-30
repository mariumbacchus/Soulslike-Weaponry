package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.items.FreyrSword;

import java.util.Optional;
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
        try {
            Optional<UUID> op = player.getDataTracker().get(FreyrSword.SUMMON_UUID);
            if (op.isPresent() && player.getBlockPos() != null) {
                Entity sword = player.getWorld().getEntity(op.get());
                if (sword instanceof FreyrSwordEntity) {
                    if (!((FreyrSwordEntity)sword).insertStack(player)) {
                        sword.setPos(player.getX(), player.getEyeY(), player.getZ());
                        ((FreyrSwordEntity)sword).dropStack();
                    }
                    sword.discard();
                } else if (ConfigConstructor.inform_player_about_no_bound_freyr_sword) {
                    player.sendMessage(text, true);
                }
            }
        } catch (Exception e) {
            if (ConfigConstructor.inform_player_about_no_bound_freyr_sword) {
                player.sendMessage(text, true);
            }
        }
    }
}
