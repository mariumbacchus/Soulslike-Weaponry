package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.entity.projectile.ReturningProjectile;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class ReturnThrownWeaponC2S {

    public ReturnThrownWeaponC2S() {

    }

    //Same as decode
    public ReturnThrownWeaponC2S(PacketByteBuf buf) {

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

    private void handlePacket(ServerPlayerEntity player, ReturnThrownWeaponC2S packet) {
        try {
            Optional<UUID> op = player.getDataTracker().get(ReturningProjectile.THROWN_WEAPON_OPT);
            if (op.isPresent()) {
                Entity entity = player.getWorld().getEntity(op.get());
                if (entity instanceof ReturningProjectile projectile) {
                    projectile.setShouldReturn(true);
                } else {
                    player.sendMessage(new LiteralText("There is no 'Soulbound' weapon bound to you!"), true);
                }
            }
        } catch (Exception e) {
            player.sendMessage(new LiteralText("There is no 'Soulbound' weapon bound to you!"), true);
        }
    }
}
