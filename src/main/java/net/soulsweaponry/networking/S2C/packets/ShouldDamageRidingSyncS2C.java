package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record ShouldDamageRidingSyncS2C(boolean damageRiding) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "should_damage_riding_sync");
    public static final Id<ShouldDamageRidingSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ShouldDamageRidingSyncS2C> CODEC =
            PacketCodec.tuple(PacketCodecs.BOOL, ShouldDamageRidingSyncS2C::damageRiding, ShouldDamageRidingSyncS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}