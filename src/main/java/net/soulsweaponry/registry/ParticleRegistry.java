package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public class ParticleRegistry {

    public static final DefaultParticleType NIGHTFALL_PARTICLE = registerParticle(FabricParticleTypes.simple(), "nightfall_particle");
    public static final DefaultParticleType DAZZLING_PARTICLE = registerParticle(FabricParticleTypes.simple(), "dazzling_particle");
    public static final DefaultParticleType PURPLE_FLAME = registerParticle(FabricParticleTypes.simple(), "purple_flame");
    public static final DefaultParticleType DARK_STAR = registerParticle(FabricParticleTypes.simple(), "dark_star");
    public static final DefaultParticleType BLACK_FLAME = registerParticle(FabricParticleTypes.simple(), "black_flame");

    public static <I extends DefaultParticleType> I registerParticle(I particle, String name) {
		return Registry.register(Registries.PARTICLE_TYPE, new Identifier(SoulsWeaponry.ModId, name), particle);
	}
}
