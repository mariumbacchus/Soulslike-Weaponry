package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.util.NbtHelper;

import java.util.UUID;
import java.util.function.Supplier;

public class SummonUUIDsSyncS2C {

    private final int length;
    private final String listId;
    private final UUID[] summons;

    public SummonUUIDsSyncS2C(int listLength, String listId, UUID[] summons) {
        this.length = listLength;
        this.listId = listId;
        this.summons = summons;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(this.length);
        buf.writeString(this.listId);
        for (UUID uuid : this.summons) {
            buf.writeUuid(uuid);
        }
    }

    //Same as decode/fromBytes
    public SummonUUIDsSyncS2C(PacketByteBuf buf) {
        this.length = buf.readInt();
        this.listId = buf.readString();
        this.summons = new UUID[this.length];
        for (int i = 0; i < length; i++) {
            this.summons[i] = buf.readUuid();
        }
    }

    public int getLength() {
        return this.length;
    }

    public String getListId() {
        return this.listId;
    }

    public UUID[] getSummons() {
        return this.summons;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            this.handlePacket(client.player, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientPlayerEntity player, SummonUUIDsSyncS2C packet) {
        String listId = packet.getListId();
        NbtCompound nbt = player.getPersistentData();
        NbtHelper.saveUUIDArr(nbt, packet.getSummons(), listId);
    }
}
