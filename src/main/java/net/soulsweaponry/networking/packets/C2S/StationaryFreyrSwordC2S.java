package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static net.soulsweaponry.events.PlayerDataHandlerEvents.SUMMON_UUID;

public class StationaryFreyrSwordC2S {

    public StationaryFreyrSwordC2S() {

    }

    //Same as decode
    public StationaryFreyrSwordC2S(PacketByteBuf buf) {

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

    private void handlePacket(ServerPlayerEntity player, StationaryFreyrSwordC2S packet) {
        Text text = new TranslatableText("soulsweapons.weapon.no_freyr_sword");
        try {
            Optional<UUID> op = player.getDataTracker().get(SUMMON_UUID);
            if (op.isPresent() && player.getBlockPos() != null) {
                Entity entity = player.getWorld().getEntity(op.get());
                if (entity instanceof FreyrSwordEntity sword) {
                    sword.setStationaryPos(player.getBlockPos());
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
