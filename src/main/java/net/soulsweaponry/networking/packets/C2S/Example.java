package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Example {

    private final BlockPos blockPos;

    public Example(BlockPos pos) { //Putt ting som skal lagres til buf i parameteret f.eks BlockPos
        this.blockPos = pos;
    }

    //Same as decode
    public Example(FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    // Same as encode
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //on server
            ServerPlayer player = context.getSender();
            player.sendMessage(new TextComponent("hello world!" + this.blockPos), player.getUUID());
        });
        context.setPacketHandled(true);
    }
}
