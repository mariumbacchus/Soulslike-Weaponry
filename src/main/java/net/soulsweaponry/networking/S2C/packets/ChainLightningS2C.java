package net.soulsweaponry.networking.S2C.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import org.joml.Vector3f;

public record ChainLightningS2C(Vector3f from, Vector3f to) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "chain_lightning");
    public static final CustomPayload.Id<ChainLightningS2C> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ChainLightningS2C> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VECTOR3F.cast(),
                    ChainLightningS2C::from,
                    PacketCodecs.VECTOR3F.cast(),
                    ChainLightningS2C::to,
                    ChainLightningS2C::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
