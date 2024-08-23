package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.ReturningProjectile;
import net.soulsweaponry.entitydata.ReturningProjectileData;

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
        UUID uuid = ReturningProjectileData.getReturningProjectileUuid(player);
        Text text = Text.translatable("soulsweapons.weapon.no_soulbound_weapon");
        if (uuid != null) {
            Entity entity = ((ServerWorld)player.getWorld()).getEntity(uuid);
            if (entity instanceof ReturningProjectile projectile) {
                projectile.setShouldReturn(true);
            } else if (ConfigConstructor.inform_player_about_no_soulbound_thrown_weapon) {
                player.sendMessage(text, true);
            }
        } else if (ConfigConstructor.inform_player_about_no_soulbound_thrown_weapon) {
            player.sendMessage(text, true);
        }
    }
}
