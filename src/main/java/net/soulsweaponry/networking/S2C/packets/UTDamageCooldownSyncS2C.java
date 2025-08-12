package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

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
}