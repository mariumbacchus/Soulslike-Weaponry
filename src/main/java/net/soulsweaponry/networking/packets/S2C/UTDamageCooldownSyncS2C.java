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
    private final float healMod;
    private final double maxHealthDamage;

    public UTDamageCooldownSyncS2C(float damage, int cooldown, float healMod, double maxHealthDamage) {
        this.damage = damage;
        this.cooldown = cooldown;
        this.healMod = healMod;
        this.maxHealthDamage = maxHealthDamage;
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeFloat(this.damage);
        buf.writeInt(this.cooldown);
        buf.writeFloat(this.healMod);
        buf.writeDouble(this.maxHealthDamage);
    }

    //Same as decode/fromBytes
    public UTDamageCooldownSyncS2C(PacketByteBuf buf) {
        this.damage = buf.readFloat();
        this.cooldown = buf.readInt();
        this.healMod = buf.readFloat();
        this.maxHealthDamage = buf.readDouble();
    }

    public float getDamage() {
        return this.damage;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public float getHealMod() {
        return healMod;
    }

    public double getMaxHealthDamage() {
        return maxHealthDamage;
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
        ClientUmbralTrespassData.setHealMod(packet.getHealMod());
        ClientUmbralTrespassData.setMaxHealthDamage(packet.getMaxHealthDamage());
    }
}
