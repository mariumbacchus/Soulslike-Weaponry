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
import net.soulsweaponry.entitydata.UmbralTrespassData;

public record ShouldDamageRidingSyncS2C(boolean damageRiding) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "should_damage_riding_sync");
    public static final Id<ShouldDamageRidingSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ShouldDamageRidingSyncS2C> CODEC =
            PacketCodec.tuple(PacketCodecs.BOOL, ShouldDamageRidingSyncS2C::damageRiding, ShouldDamageRidingSyncS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(ShouldDamageRidingSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver)client.player).getPersistentData().putBoolean(UmbralTrespassData.DAMAGE_RIDING_ID, payload.damageRiding());
    }
}