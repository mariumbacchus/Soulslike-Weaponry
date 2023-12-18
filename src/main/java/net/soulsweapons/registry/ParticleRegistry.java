package net.soulsweapons.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweapons.SoulsWeaponry;

public class ParticleRegistry {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SoulsWeaponry.MOD_ID);

    public static final RegistryObject<SimpleParticleType> NIGHTFALL_PARTICLE = PARTICLES.register("nightfall_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> DAZZLING_PARTICLE = PARTICLES.register("dazzling_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> PURPLE_FLAME = PARTICLES.register("purple_flame", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> DARK_STAR = PARTICLES.register("dark_star", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLACK_FLAME = PARTICLES.register("black_flame", () -> new SimpleParticleType(true));

    public static void register(IEventBus bus) {
        PARTICLES.register(bus);
    }
}
