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
import net.soulsweaponry.entitydata.PostureData;

public record PostureSyncS2C(int posture) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "posture_data_sync");
    public static final Id<PostureSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, PostureSyncS2C> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, PostureSyncS2C::posture, PostureSyncS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(PostureSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        int lvl = payload.posture();
        ((IEntityDataSaver) client.player).getPersistentData().putInt(PostureData.POSTURE_ID, lvl);
    }
}