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
    public static final DefaultParticleType SUN_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType MOONVEIL_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType BLUE_FLAME = FabricParticleTypes.simple();
    public static final DefaultParticleType SOUL_SPARK = FabricParticleTypes.simple();
    public static final DefaultParticleType ECHO_SMOKE = FabricParticleTypes.simple();
    public static final DefaultParticleType ECHO_SWEEP_ATTACK = FabricParticleTypes.simple();

    public static void init() {
        registerParticle(NIGHTFALL_PARTICLE, "nightfall_particle");
        registerParticle(DAZZLING_PARTICLE, "dazzling_particle");
        registerParticle(PURPLE_FLAME, "purple_flame");
        registerParticle(DARK_STAR, "dark_star");
        registerParticle(BLACK_FLAME, "black_flame");
        registerParticle(SUN_PARTICLE, "sun_particle");
        registerParticle(MOONVEIL_PARTICLE, "moonveil_particle");
        registerParticle(BLUE_FLAME, "blue_flame");
        registerParticle(SOUL_SPARK, "soul_spark");
        registerParticle(ECHO_SMOKE, "echo_smoke");
        registerParticle(ECHO_SWEEP_ATTACK, "echo_sweep_attack");
    }

    public static void registerParticle(DefaultParticleType particle, String name) {
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(SoulsWeaponry.ModId, name), particle);
	}
}
