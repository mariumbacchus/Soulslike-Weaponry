package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public class ParticleRegistry {

    public static final DefaultParticleType NIGHTFALL_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType DAZZLING_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType PURPLE_FLAME = FabricParticleTypes.simple();
    public static final DefaultParticleType DARK_STAR = FabricParticleTypes.simple();
    public static final DefaultParticleType BLACK_FLAME = FabricParticleTypes.simple();

    public static void init() {
        registerParticle(NIGHTFALL_PARTICLE, "nightfall_particle");
        registerParticle(DAZZLING_PARTICLE, "dazzling_particle");
        registerParticle(PURPLE_FLAME, "purple_flame");
        registerParticle(DARK_STAR, "dark_star");
        registerParticle(BLACK_FLAME, "black_flame");
    }

    public static void registerParticle(DefaultParticleType particle, String name) {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(SoulsWeaponry.ModId, name), particle);
    }
}
