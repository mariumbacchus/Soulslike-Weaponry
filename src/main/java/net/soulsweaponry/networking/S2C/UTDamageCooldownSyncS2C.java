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

/**
 * Packet for determining the effects upon exiting Umbral Trespass ability used in {@link net.soulsweaponry.items.UmbralTrespassItem}.
 */
public record UTDamageCooldownSyncS2C(float damage, int cooldown, boolean heal) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "umbral_trespass_damage_cooldown_sync");
    public static final Id<UTDamageCooldownSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, UTDamageCooldownSyncS2C> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.FLOAT, UTDamageCooldownSyncS2C::damage,
                    PacketCodecs.INTEGER, UTDamageCooldownSyncS2C::cooldown,
                    PacketCodecs.BOOL, UTDamageCooldownSyncS2C::heal,
                    UTDamageCooldownSyncS2C::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(UTDamageCooldownSyncS2C payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();
        if (client == null || client.player == null) {
            return;
        }
        ((IEntityDataSaver)client.player).getPersistentData().putFloat(UmbralTrespassData.UMBRAL_DAMAGE_ID, payload.damage());
        ((IEntityDataSaver)client.player).getPersistentData().putInt(UmbralTrespassData.COOLDOWN_ID, payload.cooldown());
        ((IEntityDataSaver)client.player).getPersistentData().putBoolean(UmbralTrespassData.HEAL_ID, payload.heal());
    }
}