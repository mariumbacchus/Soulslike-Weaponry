package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Example {

    private final BlockPos blockPos;

    public Example(BlockPos pos) { //Putt ting som skal lagres til buf i parameteret f.eks BlockPos
        this.blockPos = pos;
    }

    //Same as decode
    public Example(PacketByteBuf buf) {
        this(buf.readBlockPos());
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //on server
            ServerPlayerEntity player = context.getSender();
            player.sendMessage(new LiteralText("hello world!" + this.blockPos), true);
        });
        context.setPacketHandled(true);
    }
}
