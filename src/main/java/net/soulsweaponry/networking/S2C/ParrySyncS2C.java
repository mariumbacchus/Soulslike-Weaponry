package net.soulsweaponry.networking.S2C;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.ParryData;

public record ParrySyncS2C(int parryFrame) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "parry_data_sync");
    public static final CustomPayload.Id<ParrySyncS2C> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ParrySyncS2C> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, ParrySyncS2C::parryFrame, ParrySyncS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(ParrySyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        int lvl = payload.parryFrame();
        ((IEntityDataSaver) client.player).getPersistentData().putInt(ParryData.PARRY_FRAMES_ID, lvl);
    }
}