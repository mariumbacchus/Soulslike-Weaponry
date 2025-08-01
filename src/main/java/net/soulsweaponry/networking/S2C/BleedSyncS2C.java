package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.entitydata.IEntityDataSaver;

public record BleedSyncS2C(int bleedLevel) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "bleed_data_sync");
    public static final CustomPayload.Id<BleedSyncS2C> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, BleedSyncS2C> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, BleedSyncS2C::bleedLevel, BleedSyncS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(BleedSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        int lvl = payload.bleedLevel();
        ((IEntityDataSaver) client.player).getPersistentData().putInt(BleedData.BLEED_ID, lvl);
    }
}