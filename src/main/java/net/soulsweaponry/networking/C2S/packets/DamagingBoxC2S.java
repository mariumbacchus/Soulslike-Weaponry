package net.soulsweaponry.networking.C2S.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.soulsweaponry.SoulsWeaponry;

import java.util.UUID;

public record DamagingBoxC2S(
        BlockPos blockPos,
        double expansion,
        float damage,
        float knockbackStrength,
        float knockbackX,
        float knockbackZ,
        Identifier soundId,
        BlockPos soundPos,
        UUID attackerUuid
) implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "damaging_box");
    public static final CustomPayload.Id<DamagingBoxC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, DamagingBoxC2S> CODEC = PacketCodec.of(
                    (pkt, buf) -> {
                        buf.writeBlockPos(pkt.blockPos());
                        buf.writeDouble(pkt.expansion());
                        buf.writeFloat(pkt.damage());
                        buf.writeFloat(pkt.knockbackStrength());
                        buf.writeFloat(pkt.knockbackX());
                        buf.writeFloat(pkt.knockbackZ());
                        buf.writeIdentifier(pkt.soundId());
                        buf.writeBlockPos(pkt.soundPos());
                        buf.writeUuid(pkt.attackerUuid());
                    },
                    buf -> new DamagingBoxC2S(
                            buf.readBlockPos(),
                            buf.readDouble(),
                            buf.readFloat(),
                            buf.readFloat(),
                            buf.readFloat(),
                            buf.readFloat(),
                            buf.readIdentifier(),
                            buf.readBlockPos(),
                            buf.readUuid()
                    )
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
