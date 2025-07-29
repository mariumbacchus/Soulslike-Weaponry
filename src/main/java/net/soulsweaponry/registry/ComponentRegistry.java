package net.soulsweaponry.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

import java.util.function.UnaryOperator;

public class ComponentRegistry {

    public static final ComponentType<Integer> KILLS = register("kills", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));
    public static final ComponentType<Integer> FIRED_SHOTS = register("firedShots", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));

    public static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(SoulsWeaponry.ModId, name),
                builder.apply(ComponentType.builder()).build());
    }
}
