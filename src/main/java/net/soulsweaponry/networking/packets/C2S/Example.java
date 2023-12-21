package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Example {

    public Example() {

    }

    public Example(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf byf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //on server
            ServerPlayer player = context.getSender();
        });
        return true;
    }
}
