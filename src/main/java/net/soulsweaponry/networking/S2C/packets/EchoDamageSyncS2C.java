package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public record EchoDamageSyncS2C(float echoDamage, float savedDamageMod) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "echo_damage_sync");
    public static final Id<EchoDamageSyncS2C> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, EchoDamageSyncS2C> CODEC =
            PacketCodec.tuple(PacketCodecs.FLOAT, EchoDamageSyncS2C::echoDamage,
                    PacketCodecs.FLOAT, EchoDamageSyncS2C::savedDamageMod,
                    EchoDamageSyncS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}