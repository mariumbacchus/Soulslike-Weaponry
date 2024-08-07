package net.soulsweaponry.networking.packets.S2C;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.client.entitydata.ClientUmbralTrespassData;

import java.util.function.Supplier;

public class UTDamageCooldownSyncS2C {

    private final float damage;
    private final int cooldown;
    private final boolean shouldHeal;

    public UTDamageCooldownSyncS2C(float damage, int cooldown, boolean shouldHeal) {
        this.damage = damage;
        this.cooldown = cooldown;
        this.shouldHeal = shouldHeal;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeFloat(this.damage);
        buf.writeInt(this.cooldown);
        buf.writeBoolean(this.shouldHeal);
    }

    //Same as decode/fromBytes
    public UTDamageCooldownSyncS2C(PacketByteBuf buf) {
        this.damage = buf.readFloat();
        this.cooldown = buf.readInt();
        this.shouldHeal = buf.readBoolean();
    }

    public float getDamage() {
        return this.damage;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public boolean shouldHeal() {
        return this.shouldHeal;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            this.handlePacket(world, this);
        }));
        context.setPacketHandled(true);
    }

    private void handlePacket(ClientWorld world, UTDamageCooldownSyncS2C packet) {
        ClientUmbralTrespassData.setDamage(packet.getDamage());
        ClientUmbralTrespassData.setCooldown(packet.getCooldown());
        ClientUmbralTrespassData.setShouldHeal(packet.shouldHeal());
    }
}
