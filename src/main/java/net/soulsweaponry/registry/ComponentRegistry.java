package net.soulsweaponry.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.soulsweaponry.SoulsWeaponry;

import java.util.function.UnaryOperator;

public class ComponentRegistry {

	public static final ComponentType<Integer> KILLS = register("kills", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));

	private static <T> ComponentType<T> register(String path, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, SoulsWeaponry.id(path), builderOperator.apply(ComponentType.builder()).build());
	}
}
