package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StopBossMusicS2C {

    private final Identifier songId;

    public StopBossMusicS2C(Identifier songId) {
        this.songId = songId;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeIdentifier(this.songId);
    }

    //Same as decode/fromBytes
    public StopBossMusicS2C(PacketByteBuf buf) {
        this.songId = buf.readIdentifier();
    }

    public Identifier getSongId() {
        return this.songId;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, StopBossMusicS2C packet) {
        MinecraftClient.getInstance().getSoundManager().stopSounds(packet.getSongId(), SoundCategory.MUSIC);
    }
}
