package net.soulsweaponry.registry;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;

public class ParticleRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SoulsWeaponry.ModId);

    public static final RegistryObject<DefaultParticleType> NIGHTFALL_PARTICLE = PARTICLES.register("nightfall_particle", () -> new DefaultParticleType(true));
    public static final RegistryObject<DefaultParticleType> DAZZLING_PARTICLE = PARTICLES.register("dazzling_particle", () -> new DefaultParticleType(true));
    public static final RegistryObject<DefaultParticleType> PURPLE_FLAME = PARTICLES.register("purple_flame", () -> new DefaultParticleType(true));
    public static final RegistryObject<DefaultParticleType> DARK_STAR = PARTICLES.register("dark_star", () -> new DefaultParticleType(true));
    public static final RegistryObject<DefaultParticleType> BLACK_FLAME = PARTICLES.register("black_flame", () -> new DefaultParticleType(true));
    public static final RegistryObject<DefaultParticleType> SUN_PARTICLE = PARTICLES.register("sun_particle", () -> new DefaultParticleType(true));
    public static final RegistryObject<DefaultParticleType> MOONVEIL_PARTICLE = PARTICLES.register("moonveil_particle", () -> new DefaultParticleType(true));
    public static final RegistryObject<DefaultParticleType> BLUE_FLAME = PARTICLES.register("blue_flame", () -> new DefaultParticleType(true));

    public static void register(IEventBus bus) {
        PARTICLES.register(bus);
    }
}
