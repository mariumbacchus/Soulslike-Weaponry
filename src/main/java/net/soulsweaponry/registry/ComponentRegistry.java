package net.soulsweaponry.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.soulsweaponry.SoulsWeaponry;

import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class ComponentRegistry {
    //TODO when making it possible to add whatever ability to any weapon, make sure to no longer use the same components for the same abilities! (unless that's the point)
    public static final ComponentType<Integer> KILLS = register("kills", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));
    public static final ComponentType<Integer> RED_SOULS = register("red_souls", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));
    public static final ComponentType<Integer> BLUE_SOULS = register("blue_souls", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));
    public static final ComponentType<Integer> AMOUNT_USED = register("amount_used", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));
    public static final ComponentType<Boolean> EMPOWERED = register("empowered", builder -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL));
    public static final ComponentType<Integer> CHARGE = register("charge", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));
    // UUID is saved and processed as a string
    public static final ComponentType<UUID> SAVED_UUID = register("saved_uuid", builder ->
            builder.codec(Codec.STRING.xmap(UUID::fromString, UUID::toString))
                    .packetCodec(PacketCodecs.STRING.xmap(UUID::fromString, UUID::toString).mapBuf(buf -> buf)));
    public static final ComponentType<List<Integer>> INT_LIST = register("int_list", builder ->
                    builder.codec(Codec.list(Codec.INT)).packetCodec(PacketCodecs.registryCodec(Codec.list(Codec.INT))));
    public static final ComponentType<Float> BLADE_DANCE_BONUS_DAMAGE = register("blade_dance_bonus_damage", builder -> builder.codec(Codec.FLOAT).packetCodec(PacketCodecs.FLOAT));
    public static final ComponentType<Float> BLADE_DANCE_BONUS_ATTACK_SPEED = register("blade_dance_bonus_attack_speed", builder -> builder.codec(Codec.FLOAT).packetCodec(PacketCodecs.FLOAT));
    public static final ComponentType<BlockPos> SAVED_BLOCK_POS = register("saved_block_pos", builder -> builder.codec(BlockPos.CODEC).packetCodec(BlockPos.PACKET_CODEC));
    public static final ComponentType<Boolean> INVISIBLE = register("invisible", builder -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL));

    public static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(SoulsWeaponry.ModId, name),
                builder.apply(ComponentType.builder()).build());
    }
}
